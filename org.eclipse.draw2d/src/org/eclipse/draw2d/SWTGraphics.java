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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.*;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Implementation of providing the drawing capabilities of SWT's GC class in Draw2d.
 * There are 2 states contained in this graphics class -- the applied state which is the
 * actual state of the GC and the current state which is the current state of this
 * graphics object.  Certain properties can be changed multiple times and the GC won't be
 * updated until it's actually used.
 */
public class SWTGraphics
	extends Graphics
{

/** Contains the state variables of this SWTGraphics object **/
protected static class State
	implements Cloneable
{
	/** Background and foreground colors **/
	public Color
		bgColor,
		fgColor;
	/** Clip values **/
	public int clipX, clipY, clipW, clipH; //X and Y are absolute here.
	/** Font value **/
	public Font font;  //Fonts are immutable, shared references are safe
	/** Line values**/
	public int
		lineWidth,
		lineStyle,
		dx, dy;
	/** XOR value **/
	public boolean xor;

	/** @see Object#clone() **/
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
		dx = state.dx;
		dy = state.dy;
		font = state.font;
		clipX = state.clipX;
		clipY = state.clipY;
		clipW = state.clipW;
		clipH = state.clipH;
		xor = state.xor;
	}
}

public static boolean debug = false;
private final State appliedState = new State();
private final State currentState = new State();

private GC gc;

private final Rectangle relativeClip;

private List stack = new ArrayList();
private int stackPointer = 0;

private int translateX = 0;
private int translateY = 0;

/**
 * Constructs a new SWTGraphics that draws to the Canvas using the given GC.
 * @param gc the GC
 */
public SWTGraphics(GC gc) {
	gc.setLineWidth(1);
	this.gc = gc;
	//No translation necessary because translation is <0,0> at construction.
	relativeClip = new Rectangle(gc.getClipping());
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
 * If the XOR or the clip region has change, these changes will be pushed to the GC.
 */
protected final void checkGC() {
	if (appliedState.xor != currentState.xor)
		gc.setXORMode(appliedState.xor = currentState.xor);
	if (appliedState.clipX != currentState.clipX 
		|| appliedState.clipY != currentState.clipY 
		|| appliedState.clipW != currentState.clipW 
		|| appliedState.clipH != currentState.clipH) {
		gc.setClipping(appliedState.clipX = currentState.clipX,
						appliedState.clipY = currentState.clipY,
						appliedState.clipW = currentState.clipW,
						appliedState.clipH = currentState.clipH);
	}
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
	relativeClip.intersect(rect);
	setClipAbsolute(relativeClip.x + translateX,
			    relativeClip.y + translateY,
			    relativeClip.width,
			    relativeClip.height);
}

/**
 * @see Graphics#dispose()
 */
public void dispose() {
	while (stackPointer > 0) {
		popState();
	}
}

/**
 * @see Graphics#drawArc(int, int, int, int, int, int)
 */
public void drawArc(int x, int y, int width, int height, int offset, int length) {
	checkPaint();
	gc.drawArc(x + translateX, y + translateY, width, height, offset, length);
}

/**
 * @see Graphics#drawFocus(int, int, int, int)
 */
public void drawFocus(int x, int y, int w, int h) {
	checkPaint();
	checkFill();
	gc.drawFocus(x + translateX, y + translateY, w + 1, h + 1);
}

/**
 * @see Graphics#drawImage(Image, int, int)
 */
public void drawImage(Image srcImage, int x, int y) {
	checkGC();
	gc.drawImage(srcImage, x + translateX, y + translateY);
}

/**
 * @see Graphics#drawImage(Image, int, int, int, int, int, int, int, int)
 */
public void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, 
						int w2, int h2) {
	checkGC();
	gc.drawImage(srcImage, x1, y1, w1, h1, x2 + translateX, y2 + translateY, w2, h2);
}

/**
 * @see Graphics#drawLine(int, int, int, int)
 */
public void drawLine(int x1, int y1, int x2, int y2) {
	checkPaint();
	gc.drawLine(x1 + translateX, y1 + translateY, x2 + translateX, y2 + translateY);
}

/**
 * @see Graphics#drawOval(int, int, int, int)
 */
public void drawOval(int x, int y, int width, int height) {
	checkPaint();
	gc.drawOval(x + translateX, y + translateY, width, height);
}

/**
 * @see Graphics#drawPolygon(PointList)
 */
public void drawPolygon(PointList points) {
	checkPaint();
	try {
		points.translate(translateX, translateY);
		gc.drawPolygon(points.toIntArray());
	} finally {
		points.translate(-translateX, -translateY);
	}
}

/**
 * @see Graphics#drawPolyline(PointList)
 */
public void drawPolyline(PointList points) {
	checkPaint();
	try {
		points.translate(translateX, translateY);
		int array[] = points.toIntArray();
		gc.drawPolyline(array);
		if (getLineWidth() == 1 && array.length >= 2) {
			int x = array[array.length - 2];
			int y = array[array.length - 1];
			gc.drawLine(x, y, x, y);
		}
	} finally {
		points.translate(-translateX, -translateY);
	}
}

/**
 * @see Graphics#drawRectangle(int, int, int, int)
 */
public void drawRectangle(int x, int y, int width, int height) {
	checkPaint();
	gc.drawRectangle(x + translateX, y + translateY, width, height);
}

/**
 * @see Graphics#drawRoundRectangle(Rectangle, int, int)
 */
public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	checkPaint();
	gc.drawRoundRectangle(r.x + translateX, r.y + translateY, r.width, r.height, 
							arcWidth, arcHeight);
}

/**
 * @see Graphics#drawString(String, int, int)
 */
public void drawString(String s, int x, int y) {
	checkText();
	gc.drawString(s, x + translateX, y + translateY, true);
}

/**
 * @see Graphics#drawText(String, int, int)
 */
public void drawText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x + translateX, y + translateY, true);
}

/**
 * @see Graphics#fillArc(int, int, int, int, int, int)
 */
public void fillArc(int x, int y, int width, int height, int offset, int length) {
	checkFill();
	gc.fillArc(x + translateX, y + translateY, width, height, offset, length);
}

/**
 * @see Graphics#fillGradient(int, int, int, int, boolean)
 */
public void fillGradient(int x, int y, int w, int h, boolean vertical) {
	checkFill();
	checkPaint();
	gc.fillGradientRectangle(x + translateX, y + translateY, w, h, vertical);
}

/**
 * @see Graphics#fillOval(int, int, int, int)
 */
public void fillOval(int x, int y, int width, int height) {
	checkFill();
	gc.fillOval(x + translateX, y + translateY, width, height);
}

/**
 * @see Graphics#fillPolygon(PointList)
 */
public void fillPolygon(PointList points) {
	checkFill();
	try {
		points.translate(translateX, translateY);
		gc.fillPolygon(points.toIntArray());
	} finally {
		points.translate(-translateX, -translateY);
	}
}

/**
 * @see Graphics#fillRectangle(int, int, int, int)
 */
public void fillRectangle(int x, int y, int width, int height) {
	checkFill();
	gc.fillRectangle(x + translateX, y + translateY, width, height);
}

/**
 * @see Graphics#fillRoundRectangle(Rectangle, int, int)
 */
public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	checkFill();
	gc.fillRoundRectangle(r.x + translateX, r.y + translateY, r.width, r.height, 
							arcWidth, arcHeight);
}

/**
 * @see Graphics#fillString(String, int, int)
 */
public void fillString(String s, int x, int y) {
	checkText();
	gc.drawString(s, x + translateX, y + translateY, false);
}

/**
 * @see Graphics#fillText(String, int, int)
 */
public void fillText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x + translateX, y + translateY, false);
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
	rect.setBounds(relativeClip);
	return rect;
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
//Current translation is assumed to be 0,0.
	currentState.bgColor = appliedState.bgColor = gc.getBackground();
	currentState.fgColor = appliedState.fgColor = gc.getForeground();
	currentState.font = appliedState.font = gc.getFont();
	currentState.lineWidth = appliedState.lineWidth = gc.getLineWidth();
	currentState.lineStyle = appliedState.lineStyle = gc.getLineStyle();
	currentState.clipX = appliedState.clipX = relativeClip.x;
	currentState.clipY = appliedState.clipY = relativeClip.y;
	currentState.clipW = appliedState.clipW = relativeClip.width;
	currentState.clipH = appliedState.clipH = relativeClip.height;
	currentState.xor = appliedState.xor = gc.getXORMode();
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
	try {
		State s;
		if (stack.size() > stackPointer) {
			s = (State)stack.get(stackPointer);
			s.copyFrom(currentState);
		} else {
			stack.add(currentState.clone());
		}
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
	setBackgroundColor(s.bgColor);
	setForegroundColor(s.fgColor);
	setLineStyle(s.lineStyle);
	setLineWidth(s.lineWidth);
	setFont(s.font);
	setXORMode(s.xor);
	setClipAbsolute(s.clipX, s.clipY, s.clipW, s.clipH);

	translateX = currentState.dx = s.dx;
	translateY = currentState.dy = s.dy;

	relativeClip.x = s.clipX - translateX;
	relativeClip.y = s.clipY - translateY;
	relativeClip.width = s.clipW;
	relativeClip.height = s.clipH;
}

/**
 * @see Graphics#scale(double)
 */
public void scale(double factor) { }

/**
 * @see Graphics#setBackgroundColor(Color)
 */
public void setBackgroundColor(Color color) {
	if (currentState.bgColor.equals(color))
		return;
	currentState.bgColor = color;
}

/**
 * @see Graphics#setClip(Rectangle)
 */
public void setClip(Rectangle rect) {
	relativeClip.x = rect.x;
	relativeClip.y = rect.y;
	relativeClip.width = rect.width;
	relativeClip.height = rect.height;

	setClipAbsolute(rect.x + translateX,
			    rect.y + translateY,
			    rect.width,
			    rect.height);
}

/**
 * Sets clip values to the given values.
 * @param x the X value
 * @param y the Y value
 * @param w the width value
 * @param h the height value
 */
protected void setClipAbsolute(int x, int y, int w, int h) {
	if (currentState.clipW == w 
		&& currentState.clipH == h 
		&& currentState.clipX == x 
		&& currentState.clipY == y) 
			return;

	currentState.clipX = x;
	currentState.clipY = y;
	currentState.clipW = w;
	currentState.clipH = h;
}

/**
 * @see Graphics#setFont(Font)
 */
public void setFont(Font f) {
	if (currentState.font == f) 
		return;
	currentState.font = f;
}

/**
 * @see Graphics#setForegroundColor(Color)
 */
public void setForegroundColor(Color color) {
	if (currentState.fgColor.equals(color))
		return;
	currentState.fgColor = color;
}

/**
 * @see Graphics#setLineStyle(int)
 */
public void setLineStyle(int style) {
	if (currentState.lineStyle == style) 
		return;
	currentState.lineStyle = style;
}

/**
 * @see Graphics#setLineWidth(int)
 */
public void setLineWidth(int width) {
	if (currentState.lineWidth == width) 
		return;
	currentState.lineWidth = width;
}

/**
 * Sets the translation values of this to the given values
 * @param x The x value
 * @param y The y value
 */
protected void setTranslation(int x, int y) {
	translateX = currentState.dx = x;
	translateY = currentState.dy = y;
}

/**
 * @see Graphics#setXORMode(boolean)
 */
public void setXORMode(boolean b) {
	if (currentState.xor == b) 
		return;
	currentState.xor = b;
}

/**
 * @see Graphics#translate(int, int)
 */
public void translate(int x, int y) {
	setTranslation(translateX + x, translateY + y);
	relativeClip.x -= x;
	relativeClip.y -= y;
}

}