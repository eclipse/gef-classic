/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

import org.eclipse.draw2d.PositionConstants;

/**
 * Represents a point (x, y) in 2-dimensional space.  This class provides various methods
 * for manipulating this Point or creating new derived geometrical Objects.
 */
public class Point
	implements Cloneable, java.io.Serializable, Translatable
{

static final long serialVersionUID = 1;
/**A singleton for use in short calculations*/
public static final Point SINGLETON = new Point();

/**x value*/
public int x;
/**y value*/
public int y;

/**
 * Constructs a Point at location (0,0).
 * @since 2.0
 */
public Point() { }

/**
 * Constructs a Point at the same location as the given Point.
 * @param copy  Point from which the initial values are taken.
 * @since 2.0
 */
public Point(Point copy) {
	x = copy.x;
	y = copy.y;
}

/**
 * Constructs a Point at the same location as the given SWT Point.
 * @param copy  Point from which the initial values are taken.
 * @since 2.0
 */
public Point(org.eclipse.swt.graphics.Point copy) {
	x = copy.x;
	y = copy.y;
}


/**
 * Constructs a Point at the specified x and y locations.
 *
 * @param x x value
 * @param y y value
 * @since 2.0
 */
public Point(int x, int y) {
	this.x = x;
	this.y = y;
}

/**
 * Constructs a Point at the specified x and y locations.
 * @param x  x value
 * @param y  y value
 * @since 2.0
 */
public Point(double x, double y) {
	this.x = (int)x;
	this.y = (int)y;
}

/**
 * Test for equality.
 * @param o Object being tested for equality
 * @return  true if both x and y values are equal
 * @since 2.0
 */
public boolean equals(Object o) {
	if (o instanceof Point) {
		Point p = (Point)o;
		return p.x == x && p.y == y;
	}
	return false;
}

/**
 * @return a copy of this Point
 * @since 2.0
 */
public Point getCopy() {
	return new Point(x, y);
}

/**
 * Calculates the difference in between this Point and the one specified.
 * @param pt The Point being subtracted from this Point
 * @return A new Dimension representing the difference
 * @since 2.0
 */
public Dimension getDifference(Point pt) {
	return new Dimension(this.x - pt.x, this.y - pt.y);
}

/**
 * Calculates the distance from this Point to the one specified.
 * @param pt The Point being compared to this
 * @return The distance
 * @since 2.0
 */
public double getDistance(Point pt) {
	return Math.sqrt(getPreciseDistance2(pt));
}

/**
 * Calculates the distance squared between this Point and the one specified. If
 * the distance squared is larger than the maximum integer value, then
 * <code>Integer.MAX_VALUE</code> will be returned.
 * @param pt The reference Point
 * @return distance<sup>2</sup>
 * @since 2.0
 */
public int getDistance2(Point pt) {
	long i = pt.x - x;
	long j = pt.y - y;
	long result = i * i + j * j;
	if (result > Integer.MAX_VALUE)
		return Integer.MAX_VALUE;
	return (int)result;
}

private double getPreciseDistance2(Point pt) {
	double i = pt.preciseX() - preciseX();
	double j = pt.preciseY() - preciseY();
	return i * i + j * j;
}

/**
 * Calculates the orthogonal distance to the specified point.  The orthogonal distance is
 * the sum of the horizontal and vertical differences.
 * @param pt The reference Point
 * @return the orthoganal distance
 */
public int getDistanceOrthogonal(Point pt) {
	return Math.abs(y - pt.y) + Math.abs(x - pt.x);
}

/**
 * Creates a Point with negated x and y values.
 * @return A new Point
 * @since 2.0
 */
public Point getNegated() {
	return getCopy().negate();
}

/**
 * Calculates the relative position of the specified Point to this Point.
 * @param p The reference Point
 * @return NORTH, SOUTH, EAST, or WEST, as defined in {@link PositionConstants}
 */
public int getPosition(Point p) {
	int dx = p.x - x;
	int dy = p.y - y;
	if (Math.abs(dx) > Math.abs(dy)) {
		if (dx < 0)
			return PositionConstants.WEST;
		return PositionConstants.EAST;
	}
	if (dy < 0)
		return PositionConstants.NORTH;
	return PositionConstants.SOUTH;
}

/**
 * Creates a new Point from this Point by scaling by the specified amount.
 * @param amount scale factor
 * @return A new Point
 * @since 2.0
 */
public Point getScaled(double amount) {
	return getCopy().scale(amount);
}

/**
 * Creates a new SWT {@link org.eclipse.swt.graphics.Point Point} from this Point.
 * @return	A new SWT Point
 * @since 2.0
 */
public org.eclipse.swt.graphics.Point getSWTPoint() { 
	return new org.eclipse.swt.graphics.Point(x, y);
}

/**
 * Creates a new Point which is translated by the values of the input Dimension.
 * @param delta Dimension which provides the translation amounts.
 * @return  A new Point
 * @since 2.0
 */
public Point getTranslated(Dimension delta) {
	return getCopy().translate(delta);
}

/**
 * Creates a new Point which is translated by the specified x and y values
 * @param x horizontal component
 * @param y vertical component
 * @return  A new Point
 * @since 2.0
 */
public Point getTranslated(int x, int y) {
	return getCopy().translate(x, y);
}

/**
 * Creates a new Point which is translated by the values of the provided Point.
 * @param pt Point which provides the translation amounts.
 * @return  A new Point
 * @since 2.0
 */
public Point getTranslated(Point pt) {
	return getCopy().translate(pt);
}

/**
 * Creates a new Point with the transposed values of this Point.
 * Can be useful in orientation change calculations.
 * @return  A new Point
 * @since 2.0
 */
public Point getTransposed() {
	return getCopy().transpose();
}

/**
 * @see java.lang.Object#hashCode()
 */
public int hashCode() {
	return (x * y) ^ (x + y);
}

/**
 * Creates a new Point representing the MAX of two provided Points.
 * @param p1 first point
 * @param p2 second point
 * @return A new Point representing the Max()
 */
public static Point max(Point p1, Point p2) {
	return new Rectangle(p1, p2)
		.getBottomRight()
		.translate(-1, -1);
}

/**
 * Creates a new Point representing the MIN of two provided Points.
 * @param p1 first point
 * @param p2 second point
 * @return A new Point representing the Min()
 */
public static Point min(Point p1, Point p2) {
	return new Rectangle(p1, p2).getTopLeft();
}

/**
 * Negates the x and y values of this Point.
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point negate() {
	x = -x;
	y = -y;
	return this;
}

/** @see Translatable#performScale(double) */
public void performScale(double factor) {
	scale(factor);
}

/** @see Translatable#performTranslate(int, int) */
public void performTranslate(int dx, int dy) {
	translate(dx, dy);
}

/**
 * Scales this Point by the specified amount.
 * @return  <code>this</code> for convenience
 * @param amount scale factor
 * @since 2.0
 */
public Point scale(double amount) {
	x = (int)Math.floor(x * amount);
	y = (int)Math.floor(y * amount);
	return this;
}

/**
 * Scales this Point by the specified values.
 * @param xAmount horizontal scale factor
 * @param yAmount  vertical scale factor
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point scale(double xAmount, double yAmount) {
	x = (int)Math.floor(x * xAmount);
	y = (int)Math.floor(y * yAmount);
	return this;
}

/**
 * Sets the location of this Point to the provided x and y locations.
 * @return  <code>this</code> for convenience
 * @param x the x location
 * @param y the y location
 * @since 2.0
 */
public Point setLocation(int x, int y) {
	this.x = x;
	this.y = y;
	return this;
}

/**
 * Sets the location of this Point to the specified Point.
 * @return  <code>this</code> for convenience
 * @param pt the Location
 * @since 2.0
 */
public Point setLocation(Point pt) {
	x = pt.x;
	y = pt.y;
	return this;
}

/**
 * @return String representation.
 * @since 2.0
 */
public String toString() {
	return "Point(" + x + ", " + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Shifts the location of this Point by the location of the
 * input Point along each of the axes, and returns this for
 * convenience.
 *
 * @param p  Point to which the origin is being shifted.
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point translate(Point p) {
	return translate(p.x, p.y);
}

/**
 * Shifts this Point by the values of the Dimension along
 * each axis, and returns this for convenience.
 *
 * @param d  Dimension by which the origin is being shifted.
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point translate(Dimension d) {
	return translate(d.width, d.height);
}

/**
 * Shifts this Point by the values supplied along each axes, and
 * returns this for convenience.
 *
 * @param dx  Amount by which point is shifted along X axis.
 * @param dy  Amount by which point is shifted along Y axis.
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point translate(int dx, int dy) {
	x += dx;
	y += dy;
	return this;
}

/**
 * Transposes this object.  X and Y values are exchanged.
 * @return  <code>this</code> for convenience
 * @since 2.0
 */
public Point transpose() {
	int temp = x;
	x = y;
	y = temp;
	return this;
}

/**
 * Returns <code>double</code> x coordinate
 * 
 * @return <code>double</code> x coordinate
 * @since 3.4
 */
public double preciseX() {
	return x;
}

/**
 * Returns <code>double</code> y coordinate
 * 
 * @return <code>double</code> y coordinate
 * @since 3.4
 */
public double preciseY() {
	return y;
}

}
