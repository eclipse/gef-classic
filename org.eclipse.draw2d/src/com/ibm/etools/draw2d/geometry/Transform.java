package com.ibm.etools.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Provides support for transformations of scaling,
 * translation and rotation.
 */
public class Transform {

private double
	scaleX = 1.0,
	scaleY = 1.0,
	dx, dy,
	cos = 1.0,
	sin;

/**
 * Sets the value for the amount of scaling to be done
 * along all axes.
 *
 * @param scale  Value by which scaling has to be done.
 * @see  #setScale(double,double)
 * @since 2.0
 */
public void setScale(double scale){
	scaleX = scaleY = scale;
}

/**
 * Sets the amount of scaling to be done along X and Y
 * axes individually.
 *
 * @param x  Amount of scaling on X axis.
 * @param y  Amount of scaling on Y axis.
 * @see  #setScale(double)
 * @since 2.0
 */
public void setScale(double x, double y){
	scaleX=x;
	scaleY=y;
}

/**
 * Sets the angle by which rotation is to be done.
 * 
 * @param angle  Angle of rotation.
 * @since 2.0
 */
public void setRotation(double angle){
	cos = Math.cos(angle);
	sin = Math.sin(angle);
}

/**
 * Sets the translation amounts for both axes.
 *
 * @param x  Amount of shift on X axis.
 * @param y  Amount of shift on Y axis.
 * @since 2.0
 */
public void setTranslation(double x, double y){
	dx = x;
	dy = y;
}

/**
 * Returns a new transformed Point of the input Point
 * based on the transformation values set.
 *
 * @param p  Point being transformed.
 * @return  The transformed Point.
 * @since 2.0
 */
public Point getTransformed(Point p){
	double x = p.x;
	double y = p.y;
	double temp;
	x *= scaleX;
	y *= scaleY;
	
	temp = x*cos - y*sin;
	y    = x*sin + y*cos;
	x = temp;
	return new Point(Math.round(x+dx), Math.round(y+dy));
}

}