package com.ibm.etools.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.PositionConstants;

/**
 * Provides support for a point in two dimensional space.
 */
public class Point
	implements Cloneable, java.io.Serializable, Translatable
{
public int x,y;

static final long serialVersionUID = 1;
public static final Point SINGLETON = new Point();

/**
 * Constructs a Point at location (0,0).
 * 
 * @since 2.0
 */
public Point(){}

/**
 * Constructs a Point which is at the same location
 * as the input Point.
 *
 * @param copy  Point from which the initial values are
 *               taken.
 * @since 2.0
 */
public Point(Point copy){x = copy.x; y = copy.y;}

/**
 * Constructs a Point at the locations along axes
 * suplied as input.
 *
 * @param x  Position of point along X axis.
 * @param y  Position of point along Y axis.
 * @since 2.0
 */
public Point(int x, int y){this.x = x; this.y = y;}

/**
 * Constructs a Point at the locations along axes
 * suplied as input in <code>double</code> format.
 *
 * @param x  Position of point along X axis.
 * @param y  Position of point along Y axis.
 * @since 2.0
 */
public Point(double x, double y){
	this.x = (int)x;
	this.y = (int)y;
}

/**
 * Returns whether this Point is equal to the Object
 * input. 
 * Points are deemed equal if their x and y values 
 * are equivalent.
 * 
 * @param o  Object being tested for equality.
 * @return  Result of the equality test.
 * @since 2.0
 */
public boolean equals(Object o){
	if (o instanceof Point){
		Point p = (Point)o;
		return p.x == x && p.y == y;
	}
	return false;
}

/**
 * Returns a new Point with the negated values of this Point.
 *
 * @return A new point with the negated values.
 * @since 2.0
 */
public Point getNegated(){ return getCopy().negate();}

public int getPosition(Point p){
	int dx = p.x - x;
	int dy = p.y - y;
	if (Math.abs(dx) > Math.abs(dy)){
		if (dx < 0)
			return PositionConstants.WEST;
		return PositionConstants.EAST;
	}
	if (dy < 0)
		return PositionConstants.NORTH;
	return PositionConstants.SOUTH;
}

/**
 * Returns a new Point, which is scaled by the input value.
 *
 * @param amount  Value indicating the amount of scaling to 
 *                 be done.
 * @return        A new point with the scaled values of this.
 * @since 2.0
 */
public Point getScaled(float amount){ return getCopy().scale(amount);}

/**
 * Returns a new org.eclipse.swt.graphics.Point, that is 
 * equivalent to this Point.
 *
 * @return	A new org.eclipse.swt.graphics.Point
 * @since 2.0
 */
public org.eclipse.swt.graphics.Point getSWTPoint(){ 
	return new org.eclipse.swt.graphics.Point( x, y );
}


/**
 * Returns a new Point which is translated by the values of the input Dimension.
 *
 * @param delta  Dimension which provides the translation amounts.
 * @return  A new point containing the translated values.
 * @since 2.0
 */
public Point getTranslated(Dimension delta){ return getCopy().translate(delta);}

/**
 * Returns a new Point which is translated by the values of the input Point.
 *
 * @param pt  Point which provides the translation amounts.
 * @return  A new point containing the translated values.
 * @since 2.0
 */
public Point getTranslated(Point pt){ return getCopy().translate(pt);}

/**
 * Returns a new Point which is translated by the values of the inputs.
 *
 * @param x  Amount by which the Point should be translated along X axis.
 * @param y  Amount by which the Point should be translated along Y axis.
 * @return  A new point containing the translated values.
 * @since 2.0
 */
public Point getTranslated(int x, int y){
	return getCopy().translate(x,y);
}

/**
 * Returns a new Point with the transposed values of this Point.
 * Can be useful in orientation change calculations.
 *
 * @return  A new point with the transposed values of this.
 * @since 2.0
 */
public Point getTransposed(){return getCopy().transpose();}

public static Point max(Point p1, Point p2){
	return new Rectangle(p1, p2).getBottomRight().translate(-1, -1);
}

public static Point min(Point p1, Point p2){
	return new Rectangle(p1, p2).getTopLeft();
}

/**
 * Returns a copy of this Point.
 * 
 * @since 2.0
 */
public Point getCopy(){
	return new Point(x, y);
}

/**
 * Returns the difference in location between this Point 
 * and the Point <i>pt</i> as a Dimension.
 *
 * @param pt  Point to which the difference is required.
 * @return  Dimension of the difference between the two points.
 * @since 2.0
 */
public Dimension getDifference(Point pt){
	return new Dimension(this.x - pt.x, this.y - pt.y);
}

/**
 * Returns the distance of this Point from the Point <i>pt</i>.
 * 
 * @param pt  Point from which the distance is required.
 * @return  Distance as a <code>double</code>
 * @since 2.0
 */
public double getDistance(Point pt){return Math.sqrt(getDistance2(pt));}

/**
 * Returns the orthogonal distance to the specified point.  The orthogonal distance is
 * the sum of the horizontal and vertical differences.
 */
public int getDistanceOrthogonal(Point pt){
	return Math.abs(y - pt.y) + Math.abs(x - pt.x);
}

/**
 * Returns the sum of the squares of the length between this Point 
 * and the Point <i>pt</i>.
 *
 * @param pt  Point against which the difference calculations are made.
 * @return  The value of the square of the distance.
 * @since 2.0
 */
public int getDistance2(Point pt){
	int i = pt.x - x;
	int j = pt.y - y;
	return i*i + j*j;
}

/**
 * Mirrors this Point about the origin, and returns this
 * for conveinience.
 * 
 * @return  This point with the inverted values.
 * @since 2.0
 */
public Point negate(){
	x = -x;
	y = -y;
	return this;
}
/**
 * Scales the location of this Point along both X and Y 
 * axes by <i>factor</i>.
 * 
 * @since 2.0
 */
public void performScale(float factor){
	scale(factor);
}

/**
 * Translates this Point by (dx,dy).
 * 
 * @since 2.0
 */
public void performTranslate(int dx, int dy){
	translate(dx, dy);
}

/**
 * Scales the location of this Point along both X and Y 
 * axes by the value input.
 *
 * @return  This point with the scaled values.
 * @since 2.0
 */
public Point scale(float amount){
	x *= amount;
	y *= amount;
	return this;
}

/**
 * Scales the location of this Point along X and Y axes
 * by the respective values given as input, and returns 
 * this for convenience.
 *
 * @param xAmount  Amounts scaling along X axis.
 * @param yAMount  Amounts scaling along Y axis.
 * @return  This point with the scaled values.
 * @since 2.0
 */
public Point scale(float xAmount, float yAmount){
	x = Math.round(xAmount * x);
	y = Math.round(yAmount * y);
	return this;
}

/**
 * Sets the location of this Point by setting its x
 * and y coordinates to pt.x and pt.y, respectively.
 * 
 * @since 2.0
 */
public Point setLocation(Point pt){
	x = pt.x;
	y = pt.y;
	return this;
}

/**
 * Sets the location of this Point by setting its x
 * and y coordinates to the x and y values passed in.
 * 
 * @since 2.0
 */
public Point setLocation(int x, int y){
	this.x = x;
	this.y = y;
	return this;
}

/**
 * Returns the description of this Point as a String.
 *
 * @return  String containing the details of this Point.
 * @since 2.0
 */
public String toString(){
	return "Point("+x+", "+y+")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Shifts the location of this Point by the location of the
 * input Point along each of the axes, and returns this for
 * convenience.
 *
 * @param p  Point to which the origin is being shifted.
 * @return  This point with its new shifted value.
 * @see  #translate(int,int)
 * @see  #translate(Dimension)
 * @since 2.0
 */
public Point translate(Point p){return translate(p.x,p.y);}

/**
 * Shifts this Point by the values of the Dimension along
 * each axis, and returns this for convenience.
 *
 * @param d  Dimension by which the origin is being shifted.
 * @return  This point with its new shifted value.
 * @see  #translate(Point)
 * @see  #translate(int,int)
 * @since 2.0
 */
public Point translate(Dimension d){return translate(d.width,d.height);}

/**
 * Shifts this Point by the values supplied along each axes, and
 * returns this for convenience.
 *
 * @param dx  Amount by which point is shifted along X axis.
 * @param dy  Amount by which point is shifted along Y axis.
 * @return  This point with its new shifted value.
 * @see  #translate(Point)
 * @see  #translate(Dimension)
 * @since 2.0
 */
public Point translate(int dx, int dy){x+=dx;y+=dy; return this;}

/**
 * Interchanges the location of this Point along 
 * each of the axes and returns this for convenience.
 *
 * @return  This point with the new values.
 * @since 2.0
 */
public Point transpose(){
	int temp = x;
	x = y;
	y = temp;
	return this;
}

}