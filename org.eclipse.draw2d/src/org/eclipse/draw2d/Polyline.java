package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.draw2d.geometry.*;

/**
 * Provides for graphical lines comprised of {@link Point Points}.
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
 * @param pt The Point to be added to the Polyline.
 * @since 2.0
 */
public void addPoint(Point pt) {
	points.addPoint(pt);
	bounds = null;
	repaint();
}

/** * @see org.eclipse.draw2d.IFigure#containsPoint(int, int) */
public boolean containsPoint(int x, int y) {

	int tolerance = lineWidth / 2;
	LINEBOUNDS.setBounds(getBounds());
	LINEBOUNDS.expand(tolerance, tolerance);
	if (!LINEBOUNDS.contains(x, y))
		return false;
	tolerance += TOLERANCE;
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

	if (x1 != x2 && y1 != y2) {
		
		v1x = x2 - x1;
		v1y = y2 - y1;
		v2x = px - x1;
		v2y = py - y1;
		
		numerator = v2x * v1y - v1x * v2y;
		
		denominator = v1x * v1x + v1y * v1y;

		result = ((numerator << 10) / denominator * numerator) >> 10;
	}
	
	// if it is the same point, and it passes the bounding box test,
	// the result is always true.
	return result <= tolerance * tolerance;
							
}

/**
 * Null implementation for a line
 * @see org.eclipse.draw2d.Shape#fillShape(Graphics) */
protected void fillShape(Graphics g) { }

/** * @see org.eclipse.draw2d.IFigure#getBounds() */
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
 * Returns the points in this Polyline <B>by reference</B>.
 * If the returned list is modified, this Polyline must
 * be informed by calling {@link #setPoints(PointList)}.  Failure to do so will result in
 * layout and paint problems.
 * 
 * @since 2.0
 */
public PointList getPoints() {
	return points;
}

/**
 * Returns the first point in the Polyline.
 * 
 * @since 2.0
 */
public Point getStart() {
	return points.getFirstPoint();
}

/**
 * Inserts a given point at a specified index in the Polyline.
 * 
 * @param pt The point to be added
 * @param index The position in the Polyline where the 
 *               point is to be added.
 * 
 * @since 2.0
 */
public void insertPoint(Point pt, int index) {
	bounds = null;
	points.insertPoint(pt, index);
	repaint();
}
public boolean isOpaque() {
	return false;
}

protected void outlineShape(Graphics g) {
	g.drawPolyline(points);
}

public void primTranslate(int x, int y) { }

/** 
 * Erases the Polyline and removes all of its
 * {@link Point Points}.
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
 * @param index The position of the point to be removed.
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
 * @param end The point that will become the last point
 *             in the Polyline. 
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
 * @param start The point to become the first point in the Polyline.
 * @param end The point to become the last point in the Polyline.
 * @since 2.0
 */
public void setEndpoints(Point start, Point end) {
	setStart(start);
	setEnd(end);
}

public void setLineWidth(int w) {
	bounds = null;
	super.setLineWidth(w);
}

/**
 * Sets the point at <code>index</code> to the Point
 * <code>pt</code>.  Calling this method results in a 
 * repaint.  If you're going to set multiple Points, 
 * use {@link #setPoints(PointList)}.
 */
public void setPoint(Point pt, int index) {
	erase();
	points.setPoint(pt, index);
	bounds = null;
	repaint();
}

/**
 * Sets the list of points to be used by this polyline connection.
 * Removes any previously existing points. 
 *
 * @param points  New set of points.
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
 * @param start The point that will become the first point
 *               in the Polyline.
 * @since 2.0
 */
public void setStart(Point start) {
	if (points.size() == 0)
		addPoint(start);
	else
		setPoint(start, 0);
}

protected boolean useLocalCoordinates() {
	return false;
}

}