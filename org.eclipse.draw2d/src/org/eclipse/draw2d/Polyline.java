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

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Renders a {@link PointList} as a series of line segments.  A Polyline figure should be
 * positioned by manipulating its points, <EM>NOT</EM> by calling
 * {@link Figure#setBounds(Rectangle)}.
 * <P>
 * A polyline's bounds will be calculated automatically based on its PointList.  The
 * bounds will be the smallest Rectangle large enough to render the line properly.
 * Children should not be added to a Polyline and will not affect the bounds calculation.
 */
public class Polyline
	extends Shape
{

PointList points = new PointList();
private static final int TOLERANCE = 2;
private static final Rectangle LINEBOUNDS = Rectangle.SINGLETON;

{
	bounds  = null;
}

/**
 * Adds the passed point to the Polyline.
 * 
 * @param pt the Point to be added to the Polyline
 * @since 2.0
 */
public void addPoint(Point pt) {
	points.addPoint(pt);
	bounds = null;
	repaint();
}

/**
 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
 */
public boolean containsPoint(int x, int y) {

	int tolerance = lineWidth / 2 + TOLERANCE;
	LINEBOUNDS.setBounds(getBounds());
	LINEBOUNDS.expand(tolerance, tolerance);
	if (!LINEBOUNDS.contains(x, y))
		return false;
	int ints[] = points.toIntArray();
	for (int index = 0; index < ints.length - 3; index  += 2) {
		if (lineContainsPoint(ints[index], ints[index + 1],
			ints[index + 2], ints[index + 3], x, y, tolerance))
			return true;
	}
	List children = getChildren();
	for (int i = 0; i < children.size(); i++) {
		if (((IFigure)children.get(i)).containsPoint(x, y))
			return true;
	}
	return false;
}

private boolean lineContainsPoint(
	int x1, int y1,
	int x2, int y2,
	int px, int py,
	int tolerance) {
	LINEBOUNDS.setSize(0, 0);
	LINEBOUNDS.setLocation(x1, y1);
	LINEBOUNDS.union(x2, y2);
	LINEBOUNDS.expand(tolerance, tolerance);
	if (!LINEBOUNDS.contains(px, py))
		return false;

	int v1x, v1y, v2x, v2y;
	int numerator, denominator;
	int result = 0;

	/**
	 * calculates the length squared of the cross product of two vectors, v1 & v2.
	 */
	if (x1 != x2 && y1 != y2) {
		v1x = x2 - x1;
		v1y = y2 - y1;
		v2x = px - x1;
		v2y = py - y1;
		
		numerator = v2x * v1y - v1x * v2y;
		
		denominator = v1x * v1x + v1y * v1y;

		result = (int)((long)numerator * numerator / denominator);
	}
	
	// if it is the same point, and it passes the bounding box test,
	// the result is always true.
	return result <= tolerance * tolerance;
							
}

/**
 * Null implementation for a line.
 * @see org.eclipse.draw2d.Shape#fillShape(Graphics)
 */
protected void fillShape(Graphics g) { }

/**
 * @see org.eclipse.draw2d.IFigure#getBounds()
 */
public Rectangle getBounds() {
	if (bounds == null) {
		bounds = getPoints()
			.getBounds()
			.getExpanded(lineWidth / 2, lineWidth / 2);
	}
	return bounds;
}

/**
 * Returns the last point in the Polyline.
 * @since 2.0
 * @return the last point
 */
public Point getEnd() {
	return points.getLastPoint();
}

/**
 * Returns the points in this Polyline <B>by reference</B>. If the returned list is 
 * modified, this Polyline must be informed by calling {@link #setPoints(PointList)}.  
 * Failure to do so will result in layout and paint problems.
 * 
 * @return this Polyline's points
 * @since 2.0
 */
public PointList getPoints() {
	return points;
}

/**
 * @return the first point in the Polyline
 * @since 2.0
 */
public Point getStart() {
	return points.getFirstPoint();
}

/**
 * Inserts a given point at a specified index in the Polyline.
 * 
 * @param pt the point to be added
 * @param index the position in the Polyline where the point is to be added
 * 
 * @since 2.0
 */
public void insertPoint(Point pt, int index) {
	bounds = null;
	points.insertPoint(pt, index);
	repaint();
}

/**
 * @return <code>false</code> because Polyline's aren't filled
 */
public boolean isOpaque() {
	return false;
}

/**
 * @see Shape#outlineShape(Graphics)
 */
protected void outlineShape(Graphics g) {
	g.drawPolyline(points);
}

/**
 * @see Figure#primTranslate(int, int)
 */
public void primTranslate(int x, int y) { }

/** 
 * Erases the Polyline and removes all of its {@link Point Points}.
 * 
 * @since 2.0
 */
public void removeAllPoints() {
	erase();
	bounds = null;
	points.removeAllPoints();
}

/**
 * Removes a point from the Polyline.
 * 
 * @param index the position of the point to be removed
 * @since 2.0
 */
public void removePoint(int index) {
	erase();
	bounds = null;
	points.removePoint(index);
}

/**
 * Sets the end point of the Polyline
 * 
 * @param end the point that will become the last point in the Polyline
 * @since 2.0
 */
public void setEnd(Point end) {
	if (points.size() < 2)
		addPoint(end);
	else
		setPoint(end, points.size() - 1);
}

/**
 * Sets the points at both extremes of the Polyline
 * 
 * @param start the point to become the first point in the Polyline
 * @param end the point to become the last point in the Polyline
 * @since 2.0
 */
public void setEndpoints(Point start, Point end) {
	setStart(start);
	setEnd(end);
}

/**
 * @see org.eclipse.draw2d.Shape#setLineWidth(int)
 */
public void setLineWidth(int w) {
	if (lineWidth == w)
		return;
	if (w < lineWidth) //The bounds will become smaller, so erase must occur first.
		erase();
	bounds = null;
	super.setLineWidth(w);
}

/**
 * Sets the point at <code>index</code> to the Point <code>pt</code>.  Calling this method
 * results in a recalculation of the polyline's bounding box.  If you're going to set
 * multiple Points, use {@link #setPoints(PointList)}.
 * @param pt the point
 * @param index the index
 */
public void setPoint(Point pt, int index) {
	erase();
	points.setPoint(pt, index);
	bounds = null;
	repaint();
}

/**
 * Sets the list of points to be used by this polyline connection. Removes any previously 
 * existing points. The polyline will hold onto the given list by reference.
 *
 * @param points new set of points
 * @since 2.0
 */
public void setPoints(PointList points) {
	erase();
	this.points = points;
	bounds = null;
	firePropertyChange(Connection.PROPERTY_POINTS, null, points); //$NON-NLS-1$
	repaint();
}

/**
 * Sets the start point of the Polyline
 * 
 * @param start the point that will become the first point in the Polyline
 * @since 2.0
 */
public void setStart(Point start) {
	if (points.size() == 0)
		addPoint(start);
	else
		setPoint(start, 0);
}

/**
 * @see Figure#useLocalCoordinates()
 */
protected boolean useLocalCoordinates() {
	return false;
}

}
