/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.draw2d.geometry.*;

/**
 * A Graphics object able to scale all operations based on the current scale factor.
 */
public class ScaledGraphics
	extends Graphics
{

/**
 * The internal state of the scaled graphics.
 */
protected static class State {
	private double zoom;
	private double appliedX;
	private double appliedY;
	private Font font;
	private int lineWidth; 

	/**
	 * Constructs a new, uninitialized State object.
	 */
	protected State() { }
	
	/**
	 * Constructs a new State object and initializes the properties based on the given 
	 * values.
	 * 
	 * @param zoom the zoom factor
	 * @param x the x offset
	 * @param y the y offset
	 * @param font the font
	 * @param lineWidth the line width
	 */
	protected State(double zoom, double x, double y, Font font, int lineWidth) {
		this.zoom = zoom;
		this.appliedX = x;
		this.appliedY = y;
		this.font = font;
		this.lineWidth = lineWidth;
	}
	
	/**
	 * Sets all the properties of the state object.
	 * @param zoom the zoom factor
	 * @param x the x offset
	 * @param y the y offset
	 * @param font the font
	 * @param lineWidth the line width
	 */
	protected void setValues(double zoom, double x, double y, 
								Font font, int lineWidth) {
		this.zoom = zoom;
		this.appliedX = x;
		this.appliedY = y;
		this.font = font;
		this.lineWidth = lineWidth;
	}
}

static class FontKey {
	Font font;
	int height;
	protected FontKey() { }
	protected FontKey(Font font, int height) {
		this.font = font;
		this.height = height;
	}
	
	public boolean equals(Object obj) {
		return (((FontKey)obj).font.equals(font) 
				&& ((FontKey)obj).height == height);
	}

	public int hashCode() {
		return font.hashCode() ^ height;
	}

	protected void setValues(Font font, int height) {
		this.font = font;
		this.height = height;
	}
}

private static class FontHeightCache {
	Font font;
	int height;
}

private static final Rectangle TEMP = new Rectangle();
//private static final Point PT = new Point();
private Map fontCache = new HashMap();
private Map fontDataCache = new HashMap();

private FontHeightCache localCache = new FontHeightCache();
private FontHeightCache targetCache = new FontHeightCache();
private Graphics graphics;
private Font localFont;
private int localLineWidth;
private double fractionalX;
private double fractionalY;
double zoom = 1.0;
private List stack = new ArrayList();
private int stackPointer = 0;
private FontKey fontKey = new FontKey();
private boolean allowText = true;

/**
 * Constructs a new ScaledGraphics based on the given Graphics object.
 * @param g the base graphics object
 */
public ScaledGraphics(Graphics g) {
	graphics = g;
	localFont = g.getFont();
	localLineWidth = g.getLineWidth();
}

/** @see Graphics#clipRect(Rectangle) */
public void clipRect(Rectangle r) {
	graphics.clipRect(zoomClipRect(r));
}

/** @see Graphics#dispose() */
public void dispose() {
	//Remove all states from the stack
	while (stackPointer > 0) {
		popState();
	}
	
	//Dispose fonts
	Iterator iter = fontCache.values().iterator();
	while (iter.hasNext()) {
		Font font = ((Font)iter.next());
 		font.dispose();
 	}

}

/** @see Graphics#drawArc(int, int, int, int, int, int) */
public void drawArc(int x, int y, int w, int h, int offset, int sweep) {
	if (offset == 0 || sweep == 0)
		return;
	Rectangle z = zoomRect(x, y, w, h);
	if (!z.isEmpty())
		graphics.drawArc(z, offset, sweep);
}

/** @see Graphics#fillArc(int, int, int, int, int, int) */
public void fillArc(int x, int y, int w, int h, int offset, int sweep) {
	Rectangle z = zoomFillRect(x, y, w, h);
	if (offset == 0 || sweep == 0 || z.isEmpty())
		return;
	graphics.fillArc(z, offset, sweep);
}

/** @see Graphics#fillGradient(int, int, int, int, boolean) */
public void fillGradient(int x, int y, int w, int h, boolean vertical) {
	graphics.fillGradient(zoomFillRect(x, y, w, h), vertical);
}

/** @see Graphics#drawFocus(int, int, int, int) */
public void drawFocus(int x, int y, int w, int h) {
	graphics.drawFocus(zoomRect(x, y, w, h));
}

/** @see Graphics#drawImage(Image, int, int) */
public void drawImage(Image srcImage, int x, int y) {
	org.eclipse.swt.graphics.Rectangle size = srcImage.getBounds();
	graphics.drawImage(srcImage, 0, 0, size.width, size.height,
		(int)(Math.floor((x * zoom + fractionalX))), 
		(int)(Math.floor((y * zoom + fractionalY))),
		(int)(Math.floor((size.width * zoom + fractionalX))), 
		(int)(Math.floor((size.height * zoom + fractionalY))));
}

/** @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int) */
public void drawImage(Image srcImage, int sx, int sy, int sw, int sh,
										int tx, int ty, int tw, int th) {
	//"t" == target rectangle, "s" = source
			 
	Rectangle t = zoomRect(tx, ty, tw, th);
	if (!t.isEmpty())
		graphics.drawImage(srcImage, sx, sy, sw, sh, t.x, t.y, t.width, t.height);
}

/** @see Graphics#drawLine(int, int, int, int) */
public void drawLine(int x1, int y1, int x2, int y2) {
	graphics.drawLine(
		(int)(Math.floor((x1 * zoom + fractionalX))),
		(int)(Math.floor((y1 * zoom + fractionalY))),
		(int)(Math.floor((x2 * zoom + fractionalX))),
		(int)(Math.floor((y2 * zoom + fractionalY))));
}

/** @see Graphics#drawOval(int, int, int, int) */
public void drawOval(int x, int y, int w, int h) {
	graphics.drawOval(zoomRect(x, y, w, h));
}

/** @see Graphics#fillOval(int, int, int, int) */
public void fillOval(int x, int y, int w, int h) {
	graphics.fillOval(zoomFillRect(x, y, w, h));
}

/**
 * @see Graphics#drawPolygon(int[])
 */
public void drawPolygon(int[] points) {
	graphics.drawPolygon(zoomPointList(points));
}

/** @see Graphics#drawPolygon(PointList) */
public void drawPolygon(PointList points) {
	graphics.drawPolygon(zoomPointList(points.toIntArray()));
}

/** @see Graphics#drawPoint(int, int) */
public void drawPoint(int x, int y) {
	graphics.drawPoint((int)Math.floor(x * zoom + fractionalX),(int)Math.floor(y * zoom + fractionalY));
}

/**
 * @see Graphics#fillPolygon(int[])
 */
public void fillPolygon(int[] points) {
	graphics.fillPolygon(zoomPointList(points));
}

/** @see Graphics#fillPolygon(PointList) */
public void fillPolygon(PointList points) {
	graphics.fillPolygon(zoomPointList(points.toIntArray()));
}

/**
 * @see Graphics#drawPolyline(int[])
 */
public void drawPolyline(int[] points) {
	graphics.drawPolyline(zoomPointList(points));
}

/** @see Graphics#drawPolyline(PointList) */
public void drawPolyline(PointList points) {
	graphics.drawPolyline(zoomPointList(points.toIntArray()));
}

/** @see Graphics#drawRectangle(int, int, int, int) */
public void drawRectangle(int x, int y, int w, int h) {
	graphics.drawRectangle(zoomRect(x, y, w, h));
}

/** @see Graphics#fillRectangle(int, int, int, int) */
public void fillRectangle(int x, int y, int w, int h) {
	graphics.fillRectangle(zoomFillRect(x, y, w, h));
}

/** @see Graphics#drawRoundRectangle(Rectangle, int, int) */
public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.drawRoundRectangle(zoomRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

/** @see Graphics#fillRoundRectangle(Rectangle, int, int) */
public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.fillRoundRectangle(zoomFillRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

/** @see Graphics#drawString(String, int, int) */
public void drawString(String s, int x, int y) {
	if (allowText)
		graphics.drawString(s, zoomTextPoint(x, y));
}

/** @see Graphics#fillString(String, int, int) */
public void fillString(String s, int x, int y) {
	if (allowText)
		graphics.fillString(s, zoomTextPoint(x, y));
}

/** @see Graphics#drawText(String, int, int) */
public void drawText(String s, int x, int y) {
	if (allowText)
		graphics.drawText(s, zoomTextPoint(x, y));
}

/** @see Graphics#fillText(String, int, int) */
public void fillText(String s, int x, int y) {
	if (allowText)
		graphics.fillText(s, zoomTextPoint(x, y));
}

/** @see Graphics#getBackgroundColor() */
public Color getBackgroundColor() {
	return graphics.getBackgroundColor();
}

/** @see Graphics#getClip(Rectangle) */
public Rectangle getClip(Rectangle rect) {
	graphics.getClip(rect);
	int x = (int)(rect.x / zoom);
	int y = (int)(rect.y / zoom);
	/*
	 * If the clip rectangle is queried, perform an inverse zoom, and take the ceiling of
	 * the resulting double. This is necessary because forward scaling essentially performs
	 * a floor() function. Without this, figures will think that they don't need to paint
	 * when actually they do.
	 */
	rect.width = (int)Math.ceil(rect.right() / zoom) - x;
	rect.height = (int)Math.ceil(rect.bottom() / zoom) - y;
	rect.x = x;
	rect.y = y;
	return rect;
}

/** @see Graphics#getFont() */
public Font getFont() {
	return getLocalFont();
}

/** @see Graphics#getFontMetrics() */
public FontMetrics getFontMetrics() {
	return FigureUtilities.getFontMetrics(localFont);
}

/** @see Graphics#getForegroundColor() */
public Color getForegroundColor() {
	return graphics.getForegroundColor();
}

/** @see Graphics#getLineStyle() */
public int getLineStyle() {
	return graphics.getLineStyle();
}

/** @see Graphics#getLineWidth() */
public int getLineWidth() {
	return getLocalLineWidth();
}

private Font getLocalFont() {
	return localFont;
}

private int getLocalLineWidth() {
	return localLineWidth;
}	

/** @see Graphics#getXORMode() */
public boolean getXORMode() {
	return graphics.getXORMode();
}

/** @see Graphics#popState() */
public void popState() {
	graphics.popState();
	stackPointer--;
	restoreLocalState((State)stack.get(stackPointer));
}

/** @see Graphics#pushState() */
public void pushState() {
	State s;
	if (stack.size() > stackPointer) {
		s = (State)stack.get(stackPointer);
		s.setValues(zoom, fractionalX, fractionalY, getLocalFont(), localLineWidth);
	} else {
		stack.add(new State(zoom, fractionalX, fractionalY, getLocalFont(), 
								localLineWidth));
	}
	stackPointer++;

	graphics.pushState();
}

private void restoreLocalState(State state) {
	this.fractionalX = state.appliedX;
	this.fractionalY = state.appliedY;
	setScale(state.zoom);
	setLocalFont(state.font);
	setLocalLineWidth(state.lineWidth);
}

/** @see Graphics#restoreState() */
public void restoreState() {
	graphics.restoreState();
	restoreLocalState((State)stack.get(stackPointer - 1));
}

/** @see Graphics#scale(double) */
public void scale(double amount) {
	setScale(zoom * amount);
}

void setScale(double value) {
	if (zoom == value)
		return;
	this.zoom = value;
	graphics.setFont(zoomFont(getLocalFont()));
	graphics.setLineWidth(zoomLineWidth(localLineWidth));
}

Font getCachedFont(FontKey key) {
	Font font = (Font)fontCache.get(key);
	if (font != null)
		return font;
	key = new FontKey(key.font, key.height);
	FontData data = key.font.getFontData()[0];
	data.setHeight(key.height);
	Font zoomedFont = createFont(data);
	fontCache.put(key, zoomedFont);
	return zoomedFont;
}

FontData getCachedFontData(Font f) {
	FontData data = (FontData)fontDataCache.get(f);
	if (data != null)
		return data;
	data = getLocalFont().getFontData()[0];
	fontDataCache.put(f, data);
	return data;
}

Font createFont(FontData data) {
	return new Font(Display.getCurrent(), data);
}

/** @see Graphics#setBackgroundColor(Color) */
public void setBackgroundColor(Color rgb) {
	graphics.setBackgroundColor(rgb);
}

/** @see Graphics#setClip(Rectangle) */
public void setClip(Rectangle r) {
	graphics.setClip(zoomClipRect(r));
}

/** @see Graphics#setFont(Font) */
public void setFont(Font f) {
	setLocalFont(f);
}

/** @see Graphics#setForegroundColor(Color) */
public void setForegroundColor(Color rgb) {
	graphics.setForegroundColor(rgb);
}

/** @see Graphics#setLineStyle(int) */
public void setLineStyle(int style) {
	graphics.setLineStyle(style);
}

/** @see Graphics#setLineWidth(int) */
public void setLineWidth(int width) {
	setLocalLineWidth(width);
}

private void setLocalFont(Font f) {
	localFont = f;
	graphics.setFont(zoomFont(f));
}

private void setLocalLineWidth(int width) {
	localLineWidth = width;
	graphics.setLineWidth(zoomLineWidth(width));
}	

/** @see Graphics#setXORMode(boolean) */
public void setXORMode(boolean b) {
	graphics.setXORMode(b);
}

/** @see Graphics#translate(int, int) */
public void translate(int dx, int dy) {
	// fractionalX/Y is the fractional part left over from previous 
	// translates that gets lost in the integer approximation.
	double dxFloat = dx * zoom + fractionalX;
	double dyFloat = dy * zoom + fractionalY;
	fractionalX = dxFloat - Math.floor(dxFloat);
	fractionalY = dyFloat - Math.floor(dyFloat);
	graphics.translate((int)Math.floor(dxFloat), (int)Math.floor(dyFloat));
}

private Point zoomTextPoint(int x, int y) {
	if (localCache.font != localFont) {
		//Font is different, re-calculate its height
		FontMetrics metric = FigureUtilities.getFontMetrics(localFont); 
		localCache.height = metric.getHeight() - metric.getDescent();
		localCache.font = localFont;
	}
	if (targetCache.font != graphics.getFont()) {
		FontMetrics metric = graphics.getFontMetrics();
		targetCache.font = graphics.getFont();
		targetCache.height = metric.getHeight() - metric.getDescent();
	}
	return new Point(((int)(Math.floor((x * zoom) + fractionalX))),
						(int)(Math.floor((y + localCache.height - 1) * zoom 
											- targetCache.height + 1 + fractionalY)));
}

private int[] zoomPointList(int[] points) {
	for (int i = 0; (i + 1) < points.length; i+= 2) {
		points[i] = (int)(Math.floor((points[i] * zoom + fractionalX)));
		points[i + 1] = (int)(Math.floor((points[i + 1] * zoom + fractionalY)));
	}
	return points;
}

private Rectangle zoomFillRect(int x, int y, int w, int h) {
	TEMP.x = (int)(Math.floor((x * zoom + fractionalX)));
	TEMP.y = (int)(Math.floor((y * zoom + fractionalY)));
	TEMP.width = (int)(Math.floor(((x + w - 1) * zoom + fractionalX))) - TEMP.x + 1;
	TEMP.height = (int)(Math.floor(((y + h - 1) * zoom + fractionalY))) - TEMP.y + 1;
	return TEMP;
}

Font zoomFont(Font f) {
	FontData data = getCachedFontData(f);
	int zoomedFontHeight = zoomFontHeight(data.getHeight());
	allowText = zoomedFontHeight > 0;
	fontKey.setValues(f, zoomedFontHeight);
	return getCachedFont(fontKey);
}

int zoomFontHeight(int height) {
	return (int)(zoom * height);
}	

private Rectangle zoomClipRect(Rectangle r) {
	TEMP.x = (int)(Math.floor(r.x * zoom + fractionalX));
	TEMP.y = (int)(Math.floor(r.y * zoom + fractionalY));
	TEMP.width = (int)(Math.ceil(((r.x + r.width) * zoom + fractionalX))) - TEMP.x;
	TEMP.height = (int)(Math.ceil(((r.y + r.height) * zoom + fractionalY))) - TEMP.y;
	return TEMP;
}

private Rectangle zoomRect(int x, int y, int w, int h) {
	TEMP.x = (int)(Math.floor(x * zoom + fractionalX));
	TEMP.y = (int)(Math.floor(y * zoom + fractionalY));
	TEMP.width = (int)(Math.floor(((x + w) * zoom + fractionalX))) - TEMP.x;
	TEMP.height = (int)(Math.floor(((y + h) * zoom + fractionalY))) - TEMP.y;
	return TEMP;
}

//private Rectangle zoomRect(Rectangle r) {
//	TEMP.x = (int)(Math.floor((r.x * zoom + fractionalX)));
//	TEMP.y = (int)(Math.floor((r.y * zoom + fractionalY)));
//	TEMP.width = (int)(Math.floor((r.right() * zoom + fractionalX))) - TEMP.x;
//	TEMP.height = (int)(Math.floor((r.bottom() * zoom + fractionalY))) - TEMP.y;
//	return TEMP;
//}

int zoomLineWidth(int w) {
	return w;
}

}