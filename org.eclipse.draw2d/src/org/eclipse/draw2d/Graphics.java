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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The Graphics class allows you to draw to a surface.  The drawXxx() methods that pertain 
 * to shapes draw an outline of the shape, whereas the fillXxx() methods fill in the shape.
 * Also provides for drawing text, lines and images.
 */
public abstract class Graphics {

/** @see SWT#LINE_SOLID */
public static final int LINE_SOLID = SWT.LINE_SOLID;
/** @see SWT#LINE_DASH */
public static final int LINE_DASH = SWT.LINE_DASH;
/** @see SWT#LINE_DASHDOT */
public static final int LINE_DASHDOT = SWT.LINE_DASHDOT;
/** @see SWT#LINE_DASHDOTDOT */
public static final int LINE_DASHDOTDOT = SWT.LINE_DASHDOTDOT;
/** @see SWT#LINE_DOT */
public static final int LINE_DOT = SWT.LINE_DOT;

/**
 * Sets the clip region to the given rectangle.  Anything outside this rectangle will not
 * be drawn.
 * @param r the clip rectangle
 */
public abstract void clipRect(Rectangle r);

/**
 * Disposes this Graphics object.
 */
public abstract void dispose();

/**
 * @see #drawArc(int, int, int, int, int, int)
 */
public final void drawArc(Rectangle r, int offset, int length) {
	drawArc(r.x, r.y, r.width, r.height, offset, length);
}

/**
 * Draws the outline of an arc located at (x,y) with width <i>w</i> and height <i>h</i>. 
 * The starting angle of the arc (specified in degrees) is <i>offset</i> and <i>length</i>
 * is the arc's angle (specified in degrees).
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 * @param offset the start angle
 * @param length the length of the arc
 */
public abstract void drawArc(int x, int y, int w, int h, int offset, int length);

/**
 * Fills the interior of an arc located at (<i>x</i>,<i>y</i>) with width <i>w</i> and 
 * height <i>h</i>. The starting angle of the arc (specified in degrees) is <i>offset</i>
 * and <i>length</i> is the arc's angle (specified in degrees).
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 * @param offset the start angle
 * @param length the length of the arc
 */
public abstract void fillArc(int x, int y, int w, int h, int offset, int length);

/**
 * @see #fillArc(int, int, int, int, int, int)
 */
public final void fillArc(Rectangle r, int offset, int length) {
	fillArc(r.x, r.y, r.width, r.height, offset, length);
}

/**
 * @see #fillGradient(int, int, int, int, boolean)
 */
public final void fillGradient(Rectangle r, boolean vertical) {
	fillGradient(r.x, r.y, r.width, r.height, vertical);
}

/**
 * Fills the the given rectangle with a gradient from the foreground color to the 
 * background color. If <i>vertical</i> is <code>true</code>, the gradient will go from 
 * top to bottom.  Otherwise, it will go from left to right.
 * background color.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 * @param vertical whether the gradient should be vertical
 */
public abstract void fillGradient(int x, int y, int w, int h, boolean vertical);

/**
 * @see #drawFocus(int, int, int, int)
 */
public final void drawFocus(Rectangle r) {
	drawFocus(r.x, r.y, r.width, r.height);
}

/**
 * Draws a focus rectangle.
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 */
public abstract void drawFocus(int x, int y, int w, int h);

/**
 * @see #drawImage(Image, int, int)
 */
public final void drawImage(Image srcImage, Point p) {
	drawImage(srcImage, p.x, p.y);
}

/**
 * Draws the given Image at the location (x,y).
 * @param srcImage the Image
 * @param x the x coordinate
 * @param y the y coordinate
 */
public abstract void drawImage(Image srcImage, int x, int y);

/**
 * @see #drawImage(Image, int, int, int, int, int, int, int, int)
 */
public final void drawImage(Image srcImage, Rectangle src, Rectangle dest) {
	drawImage(srcImage, src.x, src.y, src.width, src.height, 
						dest.x, dest.y, dest.width, dest.height);
}

/**
 * Draws a rectangular section of the given Image to the specified rectangular reagion on
 * the canvas.  The section of the image bounded by the rectangle (x1,y1,w1,h1) is copied
 * to the section of the canvas bounded by the rectangle (x2,y2,w2,h2).  If these two 
 * sizes are different, scaling will occur.
 * 
 * @param srcImage the image
 * @param x1 the x coordinate of the source
 * @param y1 the y coordinate of the source
 * @param w1 the width of the source
 * @param h1 the height of the source
 * @param x2 the x coordinate of the destination
 * @param y2 the y coordinate of the destination
 * @param w2 the width of the destination
 * @param h2 the height of the destination
 */
public abstract void drawImage(Image srcImage, int x1, int y1, int w1, int h1, 
													int x2, int y2, int w2, int h2);

/**
 * @see #drawLine(int, int, int, int)
 */
public final void drawLine(Point p1, Point p2) {
	drawLine(p1.x, p1.y, p2.x, p2.y);
}

/**
 * Draws a line between the points <code>(x1,y1)</code> and <code>(x2,y2)</code> using the
 * foreground color.
 * @param x1 the x coordinate for the first point
 * @param y1 the y coordinate for the first point
 * @param x2 the x coordinate for the second point
 * @param y2 the y coordinate for the second point
 */
public abstract void drawLine(int x1, int y1, int x2, int y2);

/**
 * @see #drawOval(int, int, int, int)
 */
public final void drawOval(Rectangle r) {
	drawOval(r.x, r.y, r.width, r.height);
}

/**
 * Draws the outline of an ellipse that fits inside the rectangle with the given
 * properties using the foreground color.
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 */
public abstract void drawOval(int x, int y, int w, int h);

/** 
 * Draws a pixel, using the foreground color, at the specified
 * point (<code>x</code>, <code>y</code>).
 * <p>
 * Note that the receiver's line attributes do not affect this
 * operation.
 * </p>
 *
 * @param x the point's x coordinate
 * @param y the point's y coordinate 
 * 
 */
public void drawPoint(int x, int y) {
	drawLine(x, y, x, y);
}

/**
 * @see #fillOval(int, int, int, int)
 */
public final void fillOval(Rectangle r) {
	fillOval(r.x, r.y, r.width, r.height);
}

/**
 * Fills an ellipse that fits inside the rectangle with the given properties using the 
 * background color.
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param w the width
 * @param h the height
 */
public abstract void fillOval(int x, int y, int w, int h);

/**
 * Draws a closed polygon defined by the given Integer array containing the vertices in
 * x,y order.  The first and last points in the list will be connected.
 * @param points the vertices
 */
public void drawPolygon(int[] points) {
	drawPolygon(getPointList(points));
}

/**
 * Draws a closed polygon defined by the given <code>PointList</code> containing the 
 * vertices.  The first and last points in the list will be connected.
 * @param points the vertices
 */
public abstract void drawPolygon(PointList points);

/**
 * Fills a closed polygon defined by the given Integer array containing the 
 * vertices in x,y order.  The first and last points in the list will be connected.
 * @param points the vertices
 */
public void fillPolygon(int[] points) {
	fillPolygon(getPointList(points));
}

/**
 * Fills a closed polygon defined by the given <code>PointList</code> containing the 
 * vertices.  The first and last points in the list will be connected.
 * @param points the vertices
 */
public abstract void fillPolygon(PointList points);

/**
 * Draws a polyline defined by the given Integer array containing the vertices in x,y
 * order. The first and last points in the list will <b>not</b> be connected.
 * @param points the vertices
 */
public void drawPolyline(int[] points) {
	drawPolyline(getPointList(points));
}

/**
 * Draws a polyline defined by the given <code>PointList</code> containing the vertices.
 * The first and last points in the list will <b>not</b> be connected.
 * @param points the vertices
 */
public abstract void drawPolyline(PointList points);

/**
 * @see #drawRectangle(int, int, int, int)
 */
public final void drawRectangle(Rectangle r) {
	drawRectangle(r.x, r.y, r.width, r.height);
}

/**
 * Draws a rectangle whose top-left corner is located at the point (x,y) with the given 
 * width and height.
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param width the width
 * @param height the height
 */
public abstract void drawRectangle(int x, int y, int width, int height);

/**
 * @see #fillRectangle(int, int, int, int)
 */
public final void fillRectangle(Rectangle r) {
	fillRectangle(r.x, r.y, r.width, r.height);
}

/**
 * Fills a rectangle whose top-left corner is located at the point (x,y) with the given
 * width and height.
 * 
 * @param x the x coordinate
 * @param y the y coordinate
 * @param width the width
 * @param height the height
 */
public abstract void fillRectangle(int x, int y, int width, int height);

/**
 * Draws a rectangle with rounded corners using the foreground color.  <i>arcWidth</i> and
 * <i>arcHeight</i> represent the horizontal and vertical diameter of the corners.
 * 
 * @param r the rectangle
 * @param arcWidth the arc width
 * @param arcHeight the arc height
 */
public abstract void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight);

/**
 * Fills a rectangle with rounded corners using the background color.  <i>arcWidth</i> and
 * <i>arcHeight</i> represent the horizontal and vertical diameter of the corners.
 * 
 * @param r the rectangle
 * @param arcWidth the arc width
 * @param arcHeight the arc height
 */
public abstract void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight);

/**
 * Draws the given string using the current font and foreground color. Tab expansion and 
 * carriage return processing are performed. The background of the text will be
 * transparent.
 * 
 * @param s the text
 * @param x the x coordinate
 * @param y the y coordinate
 */
public abstract void drawText(String s, int x, int y);

/**
 * Renders the specified TextLayout to this Graphics.
 * @since 3.0
 * @param layout the TextLayout
 * @param x the x coordinate
 * @param y the y coordinate
 */
public void drawTextLayout(TextLayout layout, int x, int y) {
	throw new RuntimeException("The method has not been implemented"); //$NON-NLS-1$
}

/**
 * Draws the given string using the current font and foreground color. No tab expansion or 
 * carriage return processing will be performed. The background of the string will be
 * transparent.
 * 
 * @param s the string
 * @param x the x coordinate
 * @param y the y coordinate
 */
public abstract void drawString(String s, int x, int y);

/**
 * @see #drawString(String, int, int)
 */
public final void drawString(String s, Point p) {
	drawString(s, p.x, p.y);
}

/**
 * @see #drawText(String, int, int)
 */
public final void drawText(String s, Point p) {
	drawText(s, p.x, p.y);
}

/**
 * Draws a string using the specified styles. The styles are defined by {@link
 * GC#drawText(String, int, int, int)}.
 * @param s the String to draw
 * @param x the x location
 * @param y the y location
 * @param style the styles used to render the string
 * @since 3.0
 */
public void drawText(String s, int x, int y, int style) {
	throw new RuntimeException("Graphics#drawText(String, int, int, int)" + //$NON-NLS-1$
			"is not implemented properly on " + getClass().getName()); //$NON-NLS-1$
}

/**
 * Draws a string using the specified styles. The styles are defined by {@link
 * GC#drawText(String, int, int, int)}.
 * @param s the String to draw
 * @param p the point at which to draw the string
 * @param style the styles used to render the string
 * @since 3.0
 */
public final void drawText(String s, Point p, int style) {
	drawText(s, p.x, p.y, style);
}

/**
 * @see #fillString(String, int, int)
 */
public final void fillString(String s, Point p) {
	fillString(s, p.x, p.y);
}

/**
 * Draws the given string using the current font and foreground color. No tab expansion or 
 * carriage return processing will be performed. The background of the string will be
 * filled with the current background color.
 * 
 * @param s the string
 * @param x the x coordinate
 * @param y the y coordinate
 */
public abstract void fillString(String s, int x, int y);

/**
 * @see #fillText(String, int, int)
 */
public final void fillText(String s, Point p) {
	fillText(s, p.x, p.y);
}

/**
 * Draws the given string using the current font and foreground color. Tab expansion and 
 * carriage return processing are performed. The background of the text will be filled
 * with the current background color.
 * 
 * @param s the text
 * @param x the x coordinate
 * @param y the y coordinate
 */
public abstract void fillText(String s, int x, int y);

/**
 * Returns the background color used for filling.
 * @return the background color
 */
public abstract Color getBackgroundColor();

/**
 * Modifies the given rectangle to match the clip region and returns that rectangle.
 * @param rect the rectangle to hold the clip region
 * @return the clip rectangle
 */
public abstract Rectangle getClip(Rectangle rect);

/**
 * Returns the font used to draw and fill text.
 * @return the font
 */
public abstract Font getFont();

/**
 * Returns the font metrics for the current font.
 * @return the font metrics
 */
public abstract FontMetrics getFontMetrics();

/**
 * Returns the foreground color used to draw lines and text.
 * @return the foreground color
 */
public abstract Color getForegroundColor();

/**
 * Returns the line style.
 * @return the line style
 */
public abstract int getLineStyle();

/**
 * Returns the current line width.
 * @return the line width
 */
public abstract int getLineWidth();

/**
 * Returns a pointlist containing all the points from the integer array.
 * @param points an integer array of x,y points
 * @return the corresponding pointlist
 */
private PointList getPointList(int[] points) {
	PointList pointList = new PointList(points.length / 2);
	for (int i = 0; (i + 1) < points.length; i += 2)
		pointList.addPoint(points[i], points[i + 1]);
	return pointList;
}

/**
 * Returns the current absolute scaling which will be applied to the underlying Device
 * when painting to this Graphics.  The default value is 1.0.
 * @since 3.0
 * @return the effective absolute scaling factor
 */
public double getAbsoluteScale() {
	return 1.0;
}

/**
 * Returns <code>true</code> if this graphics object should use XOR mode with painting.
 * @return whether XOR mode is turned on
 */
public abstract boolean getXORMode();

/**
 * Pops the previous state of this graphics object off the stack (if {@link #pushState()} 
 * has previously been called) and restores the current state to that popped state.
 */
public abstract void popState();

/**
 * Pushes the current state of this graphics object onto a stack.
 */
public abstract void pushState();

/**
 * Restores the previous state of this graphics object.
 */
public abstract void restoreState();

/**
 * Scales this graphics object by the given amount.  
 * @param amount the scale factor
 */
public abstract void scale(double amount);

/**
 * Sets the background color.
 * @param rgb the new background color
 */
public abstract void setBackgroundColor(Color rgb);

/**
 * Sets the clip rectangle. Painting will <b>not</b> occur outside this area.
 * @param r the new clip rectangle
 */
public abstract void setClip(Rectangle r);

/**
 * Sets the font.
 * @param f the new font
 */
public abstract void setFont(Font f);

/**
 * Sets the foreground color.
 * @param rgb the new foreground color
 */
public abstract void setForegroundColor(Color rgb);

/**
 * Sets the line style.
 * @param style the new style
 */
public abstract void setLineStyle(int style);

/**
 * Sets the line width.
 * @param width the new width
 */
public abstract void setLineWidth(int width);

/**
 * Sets the XOR mode.
 * @param b the new XOR mode
 */
public abstract void setXORMode(boolean b);

/**
 * @see #translate(int, int)
 */
public final void translate(Point pt) {
	translate(pt.x, pt.y);
}

/**
 * Translates this graphics object so that its origin is offset horizontally by <i>dx</i> 
 * and vertically by <i>dy</i>.
 * @param dx the horizontal offset
 * @param dy the vertical offset
 */
public abstract void translate(int dx, int dy);

}