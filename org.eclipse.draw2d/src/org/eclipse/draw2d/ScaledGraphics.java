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

public class ScaledGraphics
	extends Graphics
{

protected static class State {
	private double zoom;
	private double appliedX;
	private double appliedY;
	private Font font;
	private int lineWidth; 

	protected State(){}
	protected State(double zoom, double x, double y, Font font, int lineWidth) {
		this.zoom = zoom;
		this.appliedX = x;
		this.appliedY = y;
		this.font = font;
		this.lineWidth = lineWidth;
	}
	
	protected void setValues(double zoom, double x, double y, Font font, int lineWidth) {
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
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (((FontKey)obj).font.equals(font) 
				&& ((FontKey)obj).height == height);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
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
private static final Point PT = new Point();
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
private static PointList[] pointListCache = new PointList[8];
private boolean allowText = true;

static {
	for (int i = 0; i < pointListCache.length; i++)
		pointListCache[i] = new PointList(i+1);
}

public ScaledGraphics(Graphics g){
	graphics = g;
	localFont = g.getFont();
	localLineWidth = g.getLineWidth();
}

public void clipRect(Rectangle r) {
	graphics.clipRect(zoomClipRect(r));
}

public void dispose(){
	//Remove all states from the stack
	while(stackPointer > 0) {
		popState();
	}
	
	//Dispose fonts
	Iterator iter = fontCache.values().iterator();
	while (iter.hasNext()) {
		Font font = ((Font)iter.next());
 		font.dispose();
 	}

}

public void drawArc(int x, int y, int w, int h, int offset, int sweep) {
	if (offset == 0 || sweep == 0)
		return;
	Rectangle z = zoomRect(x, y, w, h);
	if (!z.isEmpty())
		graphics.drawArc(z, offset, sweep);
}

public void fillArc(int x, int y, int w, int h, int offset, int sweep) {
	Rectangle z = zoomFillRect(x, y, w, h);
	if (offset == 0 || sweep == 0 || z.isEmpty())
		return;
	graphics.fillArc(z, offset, sweep);
}

public void fillGradient(int x, int y, int w, int h, boolean vertical) {
	graphics.fillGradient(zoomFillRect(x, y, w, h), vertical);
}

public void drawFocus(int x, int y, int w, int h) {
	graphics.drawFocus(zoomRect(x,y,w,h));
}

public void drawImage(Image srcImage, int x, int y) {
	org.eclipse.swt.graphics.Rectangle size = srcImage.getBounds();
	graphics.drawImage(srcImage, 0, 0, size.width, size.height,
		(int)(Math.floor((x*zoom +fractionalX))), (int)(Math.floor((y*zoom +fractionalY))),
		(int)(Math.floor((size.width*zoom +fractionalX))), (int)(Math.floor((size.height*zoom +fractionalY))));
}

public void drawImage(Image srcImage,
	int sx, int sy, int sw, int sh,
	int tx, int ty, int tw, int th)
{
	//"t" == target rectangle, "s" = source
			 
	Rectangle t = zoomRect(tx, ty, tw, th);
	if (!t.isEmpty())
		graphics.drawImage(srcImage, sx,sy,sw,sh, t.x, t.y, t.width, t.height);
}

public void drawLine(int x1, int y1, int x2, int y2) {
	graphics.drawLine(
		(int)(Math.floor((x1 * zoom +fractionalX))),
		(int)(Math.floor((y1 * zoom +fractionalY))),
		(int)(Math.floor((x2 * zoom +fractionalX))),
		(int)(Math.floor((y2 * zoom +fractionalY))));
}

public void drawOval(int x, int y, int w, int h) {
	graphics.drawOval(zoomRect(x,y,w,h));
}

public void fillOval(int x, int y, int w, int h) {
	graphics.fillOval(zoomFillRect(x,y,w,h));
}

public void drawPolygon(PointList points) {
	graphics.drawPolygon(zoomPointList(points));
}

public void fillPolygon(PointList points) {
	graphics.fillPolygon(zoomPointList(points));
}

public void drawPolyline(PointList points) {
	graphics.drawPolyline(zoomPointList(points));
}

public void drawRectangle(int x, int y, int w, int h) {
	graphics.drawRectangle(zoomRect(x,y,w,h));
}

public void fillRectangle(int x, int y, int w, int h) {
	graphics.fillRectangle(zoomFillRect(x,y,w,h));
}

public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.drawRoundRectangle(zoomRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.fillRoundRectangle(zoomFillRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

public void drawString(String s, int x, int y) {
	if (allowText)
		graphics.drawString(s, zoomTextPoint(x, y));
}

public void fillString(String s, int x, int y) {
	if (allowText)
		graphics.fillString(s, zoomTextPoint(x, y));
}

public void drawText(String s, int x, int y) {
	if (allowText)
		graphics.drawText(s, zoomTextPoint(x, y));
}

public void fillText(String s, int x, int y) {
	if (allowText)
		graphics.fillText(s, zoomTextPoint(x, y));
}

public Color getBackgroundColor() {
	return graphics.getBackgroundColor();
}

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
	rect.width = (int)Math.ceil(rect.right()/zoom) - x;
	rect.height = (int)Math.ceil(rect.bottom()/zoom) - y;
	rect.x = x;
	rect.y = y;
	return rect;
}

public Font getFont() {
	return getLocalFont();
}

public FontMetrics getFontMetrics() {
	return FigureUtilities.getFontMetrics(localFont);
}

public Color getForegroundColor() {
	return graphics.getForegroundColor();
}

public int getLineStyle() {
	return graphics.getLineStyle();
}

public int getLineWidth() {
	return getLocalLineWidth();
}

private Font getLocalFont(){
	return localFont;
}

private int getLocalLineWidth() {
	return localLineWidth;
}	

public boolean getXORMode() {
	return graphics.getXORMode();
}

public void popState() {
	graphics.popState();
	stackPointer--;
	restoreLocalState((State)stack.get(stackPointer));
}

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

public void restoreState() {
	graphics.restoreState();
	restoreLocalState((State)stack.get(stackPointer - 1));
}

public void scale(double amount) {
	setScale(zoom * amount);
}

void setScale(double value){
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
	fontCache.put(key,zoomedFont);
	return zoomedFont;
}

FontData getCachedFontData(Font f) {
	FontData data = (FontData)fontDataCache.get(f);
	if (data != null)
		return data;
	data = getLocalFont().getFontData()[0];
	fontDataCache.put(f,data);
	return data;
}

Font createFont(FontData data) {
	return new Font(Display.getCurrent(), data);
}

public void setBackgroundColor(Color rgb) {
	graphics.setBackgroundColor(rgb);
}

public void setClip(Rectangle r) {
	graphics.setClip(zoomClipRect(r));
}

public void setFont(Font f) {
	setLocalFont(f);
}

public void setForegroundColor(Color rgb) {
	graphics.setForegroundColor(rgb);
}

public void setLineStyle(int style) {
	graphics.setLineStyle(style);
}

public void setLineWidth(int width) {
	setLocalLineWidth(width);
}

private void setLocalFont(Font f){
	localFont = f;
	graphics.setFont(zoomFont(f));
}

private void setLocalLineWidth(int width) {
	localLineWidth = width;
	graphics.setLineWidth(zoomLineWidth(width));
}	

public void setXORMode(boolean b) {
	graphics.setXORMode(b);
}

public void translate(int dx, int dy){
	//fractionalX/Y is the fractional part left over from previous translates that gets lost
	//in the integer approximation.
	double dxFloat = dx * zoom + fractionalX;
	double dyFloat = dy * zoom + fractionalY;
	fractionalX = dxFloat - Math.floor(dxFloat);
	fractionalY = dyFloat - Math.floor(dyFloat);
	graphics.translate((int)Math.floor(dxFloat), (int)Math.floor(dyFloat));
}

private Point zoomTextPoint(int x, int y){
	if (localCache.font != localFont){
		//Font is different, re-calculate its height
		FontMetrics metric = FigureUtilities.getFontMetrics(localFont); 
		localCache.height = metric.getHeight() - metric.getDescent();
		localCache.font = localFont;
	}
	if (targetCache.font != graphics.getFont()){
		FontMetrics metric = graphics.getFontMetrics();
		targetCache.font = graphics.getFont();
		targetCache.height = metric.getHeight() - metric.getDescent();
	}
	return new Point(((int)(Math.floor((x*zoom)+fractionalX))),
		(int)(Math.floor((y + localCache.height - 1)*zoom - targetCache.height + 1 +fractionalY)));
}

private PointList zoomPointList(PointList points) {
	PointList scaled = null;
	
	// Look in cache for a PointList with the same length as 'points'
	for (int i = 0; i < pointListCache.length; i++) {
		if (pointListCache[i].size() == points.size()) {
			scaled = pointListCache[i];
			
			// Move this PointList up one notch in the array
			if (i != 0) {
				PointList temp = pointListCache[i - 1];
				pointListCache[i - 1] = scaled;
				pointListCache[i] = temp;	
			}
		}
	}
	
	// If no PointList is found, take the one that is last and resize it.
	if (scaled == null) {
		scaled = pointListCache[pointListCache.length - 1];
		scaled.setSize(points.size());
	}
	
	// Scale the points and set em
	for (int i = 0; i < scaled.size(); i++) {
		Point p = points.getPoint(Point.SINGLETON, i);
		p.x = (int)(Math.floor((p.x * zoom + fractionalX)));
		p.y = (int)(Math.floor((p.y * zoom + fractionalY)));
		scaled.setPoint(p, i);
	}
	return scaled;
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

private Rectangle zoomRect(Rectangle r){
	TEMP.x = (int)(Math.floor((r.x * zoom + fractionalX)));
	TEMP.y = (int)(Math.floor((r.y * zoom + fractionalY)));
	TEMP.width = (int)(Math.floor((r.right() * zoom + fractionalX))) - TEMP.x;
	TEMP.height = (int)(Math.floor((r.bottom() * zoom + fractionalY))) - TEMP.y;
	return TEMP;
}

int zoomLineWidth(int w) {
	return w;
}

}