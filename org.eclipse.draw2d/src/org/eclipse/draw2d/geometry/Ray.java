package org.eclipse.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * Provides support for vector type calculations.
 */
final public class Ray {

public int x, y;

/**
 * Constructs a Ray of no direction and magnitude.
 * 
 * @since 2.0
 */
public Ray() {}

/**
 * Constructs a Ray with direction from the
 * origin(0,0) to the coordinates given.
 * 
 * @param _x  Location along X Axis.
 * @param _y  Location along Y axis.
 * @since 2.0
 */
public Ray(int _x, int _y) {
	x = _x;
	y = _y;
}

/**
 * Constructs a Ray with direction from the
 * origin(0,0) to the Point given.
 *
 * @param p  Point to which the ray will point from 
 *            the origin.
 * @since 2.0
 */
public Ray(Point p) {
	x = p.x; y = p.y;
}

/**
 * Constructs a Ray which points from the first input
 * Point to the second input Point.
 *
 * @param start  Strarting point of the ray.
 * @param end  Ending point of the ray.
 * @since 2.0
 */
public Ray(Point start, Point end) {
	x = end.x - start.x;
	y = end.y - start.y;
}

/**
 * Constructs a Ray which connects both the Rays given
 * as input. The direction of the Ray is from the first
 * Ray to the second one.
 *
 * @param start  Direction of the start of this Ray.
 * @param end   Direction of the end of this Ray.
 * @since 2.0
 */
public Ray(Ray start, Ray end) {
	x = end.x - start.x;
	y = end.y - start.y;
}

/**
 * Returns the cross product of this Ray and <i>r</i>.
 * Represents the amount by which two Rays
 * are directionally different.
 *
 * @param r  Ray being compared.
 * @return  Amount by which two rays are directionally
 * different. Parallel Rays result in a
 * distance of zero. Perpendicular rays result in a
 * distance of one.
 * @see #similarity(Ray)
 * @since 2.0
 */
public int assimilarity(Ray r){
	return Math.abs(x * r.y + y * r.x);
}

/**
 * Returns the dot product of this Ray and <i>p</i>.
 *
 * @return dot product of the two Rays.
 * @since 2.0
 */
public int dotProduct(Ray p){
	return x * p.x + y * p.y;
}

/**
 * Returns a new Ray which is the sum of 
 * this Ray and the input Ray <i>r</i>.
 *
 * @param r  Ray to be added to this Ray.
 * @return  New Ray which is the sum of two Rays.
 * @since 2.0
 */
public Ray getAdded(Ray r) {
	return new Ray(r.x + x, r.y + y);
}

/**
 * Returns a new Ray which points, along all axes, to the middle
 * region of this Ray and the input Ray <i>r</i>.
 *  
 * @param r  Ray to calculate the average.
 * @return  New Ray which is the average of the two Rays.
 * @since 2.0
 */
public Ray getAveraged(Ray r){
	return new Ray ((x+r.x)/2, (y+r.y)/2);
}

/**
 * Returns a new Ray which has this Ray's magnitude scaled
 * by the value provided as input.
 *
 * @param s  Value providing the amount to scale.
 * @return  New Ray containging the scaled values.
 * @since 2.0
 */
public Ray getScaled(int s){
	return new Ray(x*s, y*s);
}

/**
 * Returns whether this Ray points horizontally or not.
 *
 * @return  Result of the horizontal test.
 * @since 2.0
 */
public boolean isHorizontal(){
	return x != 0;
}

/**
 * Returns the length of this Ray. Generally this is 
 * the distance from the point to which the Ray points
 * to the origin.
 *
 * @return  Length of this Ray as a double.
 * @since 2.0
 */
public double length() {
	return Math.sqrt( dotProduct( this ) );
}

/**
 * Returns the similarity of direction between this Ray
 * and the Ray given. Parallel Rays return a
 * value of one. Perpendicular Rays return a value of
 * zero.
 *
 * @param r  Ray being tested for similarity.
 * @return  Similarity result.Parallel Rays return a
 * value of one, and perpendicular Rays give a value as
 * zero.
 * @see #assimilarity(Ray)
 * @since 2.0
 */
public int similarity(Ray r){
	return Math.abs(dotProduct(r));
}

/**
 * Returns the String description of this Ray.
 *
 * @return  The String description of this Ray.
 * @since 2.0
 */
public String toString(){
	return "(" + x + "," + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}