/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A concrete implementation of <code>Graphics</code> using an SWT
 * <code>GC</code>. There are 2 states contained in this graphics class -- the
 * applied state which is the actual state of the GC and the current state which
 * is the current state of this graphics object.  Certain properties can be
 * changed multiple times and the GC won't be updated until it's actually used.
 * <P>
 * WARNING: This class is not intended to be subclassed.
 */
public class SWTGraphics
	extends Graphics
{

/**
 * An internal type used to represent and update the GC clip area.
 * @since 3.1
 */
interface Clipping {
	Rectangle getBoundingBox(Rectangle rect);
	Clipping getCopy();
	void intersect(Rectangle rect);
	void scale(double amount);
	void setOn(GC gc);
	void translate(int dx, int dy);
}

/**
 * Any state stored in this class is only applied when it is needed by a
 * specific graphics call.
 * @since 3.1
 */
static class LazyState {
	public Color bgColor;
	Clipping clipping;
	public Color fgColor;
	public Font font;
	public int lineStyle;
	public int lineWidth;
}

class RectangleClipping implements Clipping {
	private double top, left, botm, rt;
	RectangleClipping() { }
	RectangleClipping(org.eclipse.swt.graphics.Rectangle rect) {
		left = rect.x;
		top = rect.y;
		rt = rect.x + rect.width;
		botm = rect.y + rect.height;
	}
	RectangleClipping(Rectangle rect) {
		left = rect.x;
		top = rect.y;
		rt = rect.right();
		botm = rect.bottom();
	}
	public Rectangle getBoundingBox(Rectangle rect) {
		rect.x = (int)left;
		rect.y = (int)top;
		rect.width = (int)Math.ceil(rt) - rect.x;
		rect.height = (int)Math.ceil(botm) - rect.y;
		return rect;
	}
	public Clipping getCopy() {
		RectangleClipping result = new RectangleClipping();
		result.left = left;
		result.rt = rt;
		result.top = top;
		result.botm = botm;
		return result;
	}
	public void intersect(Rectangle rect) {
		left = Math.max(left, rect.x);
		rt = Math.min(rt, rect.right());
		top = Math.max(top, rect.y);
		botm = Math.min(botm, rect.bottom());
		if (rt < left || botm < top) {
			rt = left - 1;//Use negative size to ensure ceiling function doesn't add a pixel
			botm = top - 1;
		}
	}
	public void scale(double s) {
		top /= s;
		left /= s;
		botm /= s;
		rt /= s;
	}
	public void setOn(GC gc) {
		int xInt = (int)Math.floor(left);
		int yInt = (int)Math.floor(top);
		gc.setClipping(xInt, yInt,
				(int)Math.ceil(rt) - xInt,
				(int)Math.ceil(botm) - yInt);
	}
	public void translate(int dx, int dy) {
		left += dx;
		rt += dx;
		top += dy;
		botm += dy;
	}
}

/**
 * Contains the entire state of the Graphics.
 */
static class State
	extends LazyState
	implements Cloneable
{
	float affineMatrix[];
	int alpha;
	int lineAttributes;
	int lineDash[];
	boolean xor;
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * Copies all state information from the given State to this State
	 * @param state The State to copy from
	 */
	public void copyFrom(State state) {
		bgColor = state.bgColor;
		fgColor = state.fgColor;
		lineStyle = state.lineStyle;
		lineWidth = state.lineWidth;
		font = state.font;
		xor = state.xor;
		lineDash = state.lineDash;
		lineAttributes = state.lineAttributes;
		affineMatrix = state.affineMatrix;
		clipping = state.clipping;
		alpha = state.alpha;
	}
}

/**
 * debug flag.
 */
public static boolean debug = false;
private final LazyState appliedState = new LazyState();
private final State currentState = new State();
private GC gc;
private boolean sharedClip;

private List stack = new ArrayList();
private int stackPointer = 0;

Transform transform;
private boolean transformDirty;

/**
 * Constructs a new SWTGraphics that draws to the Canvas using the given GC.
 * @param gc the GC
 */
public SWTGraphics(GC gc) {
	this.gc = gc;
	transform = new Transform(null);
	//No translation necessary because translation is <0,0> at construction.
	init();
}

/**
 * If the background color has changed, this change will be pushed to the GC.  Also calls
 * {@link #checkGC()}.
 */
protected final void checkFill() {
	if (!appliedState.bgColor.equals(currentState.bgColor))
		gc.setBackground(appliedState.bgColor = currentState.bgColor);
	checkGC();
}

/**
 * If the XOR or the clip region has changed, these changes will be pushed to the GC.
 */
protected final void checkGC() {
//	if (appliedState.clipping != currentState.clipping)
//		clipping.setOn(GC);
}

/**
 * If the line width, line style, foreground or background colors have changed, these
 * changes will be pushed to the GC.  Also calls {@link #checkGC()}.
 */
protected final void checkPaint() {
	checkGC();
	if (!appliedState.fgColor.equals(currentState.fgColor))
		gc.setForeground(appliedState.fgColor = currentState.fgColor);
	if (appliedState.lineStyle != currentState.lineStyle)
		gc.setLineStyle(appliedState.lineStyle = currentState.lineStyle);
	if (appliedState.lineWidth != currentState.lineWidth)
		gc.setLineWidth(appliedState.lineWidth = currentState.lineWidth);
	if (!appliedState.bgColor.equals(currentState.bgColor))
		gc.setBackground(appliedState.bgColor = currentState.bgColor);
}

/**
 * If the font has changed, this change will be pushed to the GC.  Also calls
 * {@link #checkPaint()} and {@link #checkFill()}.
 */
protected final void checkText() {
	checkPaint();
	checkFill();
	if (!appliedState.font.equals(currentState.font))
		gc.setFont(appliedState.font = currentState.font);
}

/**
 * @see Graphics#clipRect(Rectangle)
 */
public void clipRect(Rectangle rect) {
	if (sharedClip) {
		currentState.clipping = currentState.clipping.getCopy();
		sharedClip = false;
	}
	currentState.clipping.intersect(rect);
	currentState.clipping.setOn(gc);
}

/**
 * @see Graphics#dispose()
 */
public void dispose() {
	while (stackPointer > 0) {
		popState();
	}
	transform.dispose();
}

/**
 * @see Graphics#drawArc(int, int, int, int, int, int)
 */
public void drawArc(int x, int y, int width, int height, int offset, int length) {
	checkPaint();
	gc.drawArc(x, y, width, height, offset, length);
}

/**
 * @see Graphics#drawFocus(int, int, int, int)
 */
public void drawFocus(int x, int y, int w, int h) {
	checkPaint();
	checkFill();
	gc.drawFocus(x, y, w + 1, h + 1);
}

/**
 * @see Graphics#drawImage(Image, int, int)
 */
public void drawImage(Image srcImage, int x, int y) {
	checkGC();
	gc.drawImage(srcImage, x, y);
}

/**
 * @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int)
 */
public void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, 
						int w2, int h2) {
	checkGC();
	gc.drawImage(srcImage, x1, y1, w1, h1, x2, y2, w2, h2);
}

/**
 * @see Graphics#drawLine(int, int, int, int)
 */
public void drawLine(int x1, int y1, int x2, int y2) {
	checkPaint();
	gc.drawLine(x1, y1, x2, y2);
}

/**
 * @see Graphics#drawOval(int, int, int, int)
 */
public void drawOval(int x, int y, int width, int height) {
	checkPaint();
	gc.drawOval(x, y, width, height);
}

/**
 * @see Graphics#drawPoint(int, int)
 */
public void drawPoint(int x, int y) {
	checkPaint();
	gc.drawPoint(x, y);
}

/**
 * @see Graphics#drawPolygon(int[])
 */
public void drawPolygon(int[] points) {
	checkPaint();
	gc.drawPolygon(points);
}

/**
 * @see Graphics#drawPolygon(PointList)
 */
public void drawPolygon(PointList points) {
	drawPolygon(points.toIntArray());
}

/**
 * @see org.eclipse.draw2d.Graphics#drawPolyline(int[])
 */
public void drawPolyline(int[] points) {
	checkPaint();
	gc.drawPolyline(points);
}

/**
 * @see Graphics#drawPolyline(PointList)
 */
public void drawPolyline(PointList points) {
	drawPolyline(points.toIntArray());
}

/**
 * @see Graphics#drawRectangle(int, int, int, int)
 */
public void drawRectangle(int x, int y, int width, int height) {
	checkPaint();
	gc.drawRectangle(x, y, width, height);
}

/**
 * @see Graphics#drawRoundRectangle(Rectangle, int, int)
 */
public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	checkPaint();
	gc.drawRoundRectangle(r.x, r.y, r.width, r.height, 
							arcWidth, arcHeight);
}

/**
 * @see Graphics#drawString(String, int, int)
 */
public void drawString(String s, int x, int y) {
	checkText();
	gc.drawString(s, x, y, true);
}

/**
 * @see Graphics#drawText(String, int, int)
 */
public void drawText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x, y, true);
}

/**
 * @see Graphics#drawTextLayout(TextLayout, int, int, int, int, Color, Color)
 */
public void drawTextLayout(TextLayout layout, int x, int y, int selectionStart,
		int selectionEnd, Color selectionForeground, Color selectionBackground) {
	//$TODO probably just call checkPaint since Font and BG color don't apply
	checkText();
	layout.draw(gc, x, y, selectionStart, selectionEnd,
			selectionForeground, selectionBackground);
}

/**
 * @see Graphics#fillArc(int, int, int, int, int, int)
 */
public void fillArc(int x, int y, int width, int height, int offset, int length) {
	checkFill();
	gc.fillArc(x, y, width, height, offset, length);
}

/**
 * @see Graphics#fillGradient(int, int, int, int, boolean)
 */
public void fillGradient(int x, int y, int w, int h, boolean vertical) {
	checkFill();
	checkPaint();
	gc.fillGradientRectangle(x, y, w, h, vertical);
}

/**
 * @see Graphics#fillOval(int, int, int, int)
 */
public void fillOval(int x, int y, int width, int height) {
	checkFill();
	gc.fillOval(x, y, width, height);
}

/**
 * @see Graphics#fillPolygon(int[])
 */
public void fillPolygon(int[] points) {
	checkFill();
	gc.fillPolygon(points);
}

/**
 * @see Graphics#fillPolygon(PointList)
 */
public void fillPolygon(PointList points) {
	fillPolygon(points.toIntArray());
}

/**
 * @see Graphics#fillRectangle(int, int, int, int)
 */
public void fillRectangle(int x, int y, int width, int height) {
	checkFill();
	gc.fillRectangle(x, y, width, height);
}

/**
 * @see Graphics#fillRoundRectangle(Rectangle, int, int)
 */
public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	checkFill();
	gc.fillRoundRectangle(r.x, r.y, r.width, r.height, 
							arcWidth, arcHeight);
}

/**
 * @see Graphics#fillString(String, int, int)
 */
public void fillString(String s, int x, int y) {
	checkText();
	gc.drawString(s, x, y, false);
}

/**
 * @see Graphics#fillText(String, int, int)
 */
public void fillText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x, y, false);
}

/**
 * @see Graphics#getBackgroundColor()
 */
public Color getBackgroundColor() {
	return currentState.bgColor;
}

/**
 * @see Graphics#getClip(Rectangle)
 */
public Rectangle getClip(Rectangle rect) {
	if (currentState.clipping != null)
		return currentState.clipping.getBoundingBox(rect);
	throw new IllegalStateException("Clipping cannot be queried at this point");
}

/**
 * @see Graphics#getFont()
 */
public Font getFont() {
	return currentState.font;
}

/**
 * @see Graphics#getFontMetrics()
 */
public FontMetrics getFontMetrics() {
	checkText();
	return gc.getFontMetrics();
}

/**
 * @see Graphics#getForegroundColor()
 */
public Color getForegroundColor() {
	return currentState.fgColor;
}

/**
 * @see Graphics#getLineStyle()
 */
public int getLineStyle() {
	return currentState.lineStyle;
}

/**
 * @see Graphics#getLineWidth()
 */
public int getLineWidth() {
	return currentState.lineWidth;
}

/**
 * @see Graphics#getXORMode()
 */
public boolean getXORMode() {
	return currentState.xor;
}

/**
 * Called by constructor, initializes all State information for currentState
 */
protected void init() {
	currentState.bgColor = appliedState.bgColor = gc.getBackground();
	currentState.fgColor = appliedState.fgColor = gc.getForeground();
	currentState.font = appliedState.font = gc.getFont();
	currentState.lineWidth = appliedState.lineWidth = gc.getLineWidth();
	currentState.lineStyle = appliedState.lineStyle = gc.getLineStyle();
	currentState.xor = /*appliedState.xor = */ gc.getXORMode();
	currentState.lineAttributes = gc.getLineCap() << 2 | gc.getLineJoin();
	currentState.lineDash = gc.getLineDash();
	currentState.clipping = new RectangleClipping(gc.getClipping());
	currentState.alpha = gc.getAlpha();
}

/**
 * @see Graphics#popState()
 */
public void popState() {
	stackPointer--;
	restoreState((State)stack.get(stackPointer));
}

/**
 * @see Graphics#pushState()
 */
public void pushState() {
	if (currentState.clipping == null)
		throw new IllegalStateException(
				"The clipping has been modified in a way that cannot be saved and restored.");
	try {
		State s;
		if (transformDirty) {
			transformDirty = false;
			transform.getElements(currentState.affineMatrix = new float[6]);
		}
		if (stack.size() > stackPointer) {
			s = (State)stack.get(stackPointer);
			s.copyFrom(currentState);
		} else {
			stack.add(currentState.clone());
		}
		sharedClip = true;
		stackPointer++;
	} catch (CloneNotSupportedException e) {
		throw new RuntimeException(e.getMessage());
	}
}

/**
 * @see Graphics#restoreState()
 */
public void restoreState() {
	restoreState((State)stack.get(stackPointer - 1));
}

/**
 * Sets all State information to that of the given State, called by restoreState()
 * @param s the State
 */
protected void restoreState(State s) {
	//Must set the transformation matrix first since it affects font, clipping, and linewidth.
	setAffineMatrix(s.affineMatrix);
	currentState.clipping = s.clipping;
	s.clipping.setOn(gc);
	sharedClip = true;
	setBackgroundColor(s.bgColor);
	setForegroundColor(s.fgColor);
	setLineStyle(s.lineStyle);
	setLineWidth(s.lineWidth);
	setFont(s.font);
	setXORMode(s.xor);
	setAlpha(s.alpha);
}

/**
 * @see Graphics#rotate(float)
 */
public final void rotate(float degrees) {
	transform.rotate(degrees);
	transformDirty = true;
	currentState.clipping = null;
	gc.setTransform(transform);
}

/**
 * @see Graphics#scale(double)
 */
public void scale(double factor) {
	//Flush any line widths, font settings, and clipping
	checkText();
	transform.scale((float)factor, (float)factor);
	gc.setTransform(transform);
	transformDirty = true;
	if (currentState.clipping != null)
		currentState.clipping.scale(factor);
}

private void setAffineMatrix(float[] m) {
	if (!transformDirty && currentState.affineMatrix == m)
		return;
	currentState.affineMatrix = m;
	if (m == null)
		transform.setElements(1, 0, 0, 1, 0, 0);
	else
		transform.setElements(m[0], m[1], m[2], m[3], m[4], m[5]);
	gc.setTransform(transform);
}

public void setAlpha(int alpha) {
	if (currentState.alpha != alpha)
		gc.setAlpha(this.currentState.alpha = alpha);
}

/**
 * @see Graphics#setBackgroundColor(Color)
 */
public void setBackgroundColor(Color color) {
	currentState.bgColor = color;
}

/**
 * @see Graphics#setClip(Rectangle)
 */
public void setClip(Rectangle rect) {
	currentState.clipping = new RectangleClipping(rect);
	currentState.clipping.setOn(gc);
}

/**
 * Sets clip values to the given values.
 * @param x the X value
 * @param y the Y value
 * @param w the width value
 * @param h the height value
 */
protected void setClipAbsolute(int x, int y, int w, int h) {
	//MUSTFIX - this method was being called from setClip
}

/**
 * @see Graphics#setFont(Font)
 */
public void setFont(Font f) {
	currentState.font = f;
}

/**
 * @see Graphics#setForegroundColor(Color)
 */
public void setForegroundColor(Color color) {
	currentState.fgColor = color;
}

/**
 * @see org.eclipse.draw2d.Graphics#setLineCap(int)
 */
public void setLineCap(int cap) {
	if ((currentState.lineAttributes >> 2 | 3) == cap)
		return;
	currentState.lineAttributes &= ~12;
	gc.setLineCap(cap);
	currentState.lineAttributes |= (cap << 2); 
}

/**
 * @see org.eclipse.draw2d.Graphics#setLineDash(int[])
 */
public void setLineDash(int[] dash) {
	gc.setLineDash(currentState.lineDash = dash);
}

/**
 * @see org.eclipse.draw2d.Graphics#setLineJoin(int)
 */
public void setLineJoin(int join) {
	if ((currentState.lineAttributes & 3) == join)
		return;
	currentState.lineAttributes &= ~3;
	gc.setLineJoin(currentState.lineAttributes = join);
}

/**
 * @see Graphics#setLineStyle(int)
 */
public void setLineStyle(int style) {
	currentState.lineStyle = style;
}

/**
 * @see Graphics#setLineWidth(int)
 */
public void setLineWidth(int width) {
	currentState.lineWidth = width;
}

/**
 * @see Graphics#setXORMode(boolean)
 */
public void setXORMode(boolean b) {
	gc.setXORMode(currentState.xor = b);
}

/**
 * @see Graphics#translate(int, int)
 */
public void translate(int dx, int dy) {
	if (sharedClip) {
		currentState.clipping = currentState.clipping.getCopy();
		sharedClip = false;
	}
	transform.translate(dx, dy);
	if (currentState.clipping != null)
		currentState.clipping.translate(-dx, -dy);
	transformDirty = true;
	gc.setTransform(transform);
}

}
