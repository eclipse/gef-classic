package com.ibm.etools.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.internal.Draw2dMessages;

/**
 * Provides support for a list of points.
 */
public class PointList
	implements java.io.Serializable
{

private int[] points = new int[0];
private Rectangle bounds;
private int size = 0;

static final long serialVersionUID = 1;

/**
 * Constructs an empty PointList.
 * 
 * @since 2.0
 */
public PointList(){}

/**
 * Constructs a PointList with capacity <i>size</i>.
 *
 * @param size  Number of points to hold.
 * @since 2.0
 */
public PointList(int size){
	points = new int[size*2];
}

/**
 * Adds Point <i>p</i> to this PointList.
 *
 * @see  #removePoint(int)
 * @since 2.0
 */
public void addPoint(Point p){
	addPoint(p.x,p.y);
}

/**
 * Adds the input point values to this PointList.
 *
 * @param x  X value of a point to add.
 * @param y  Y value of a point to add.
 * @since 2.0
 */
public void addPoint(int x, int y){
	bounds = null;
	int arrayLength = points.length;
	int usedLength = size*2;
	if (arrayLength == usedLength) {
		int old[] = points;
		points = new int[arrayLength+2];
		System.arraycopy(old, 0, points, 0, arrayLength);
	}
	points[usedLength]   = x;
	points[usedLength+1] = y;
	size++;
}

/**
 * Returns rectangular area in which all the points in
 * this list would fit.
 *
 * @return  Bounding region as a rectangle.
 * @since 2.0
 */
public Rectangle getBounds() {
	if (bounds != null)
		return bounds;
	bounds = new Rectangle();
	if (size > 0) {
		bounds.setLocation(getPoint(0));
		for (int i=0; i<size; i++)
			bounds.union(getPoint(i));
	}
	return bounds;
}

/**
 * Returns the first point in the list.
 *
 * @return  The first point in the list.
 * @since 2.0
 */
public Point getFirstPoint() {
	if (size < 1) 
		return null;
	return getPoint(0);
}

/**
 * Returns the last point in the list.
 *
 * @return  The last point in the list or <code>null</code>
 *           if there are no elements.
 * @since 2.0
 */
public Point getLastPoint() {
	if(size < 1 ) 
		return null;
	return getPoint(size-1);
}

public Point getMidpoint(){
	if (size() % 2 == 0)
		return getPoint(size()/2-1).
			getTranslated(getPoint(size()/2)).
			scale(0.5f);
	return getPoint(size()/2);
}

/**
 * Returns the point in the list pointed to by the index.
 * The index starts at 0.
 *
 * @param index  Position of the point in the list.
 * @return  The point at <i>index</i>.
 * @since 2.0
 */
public Point getPoint(int index){
	index *=2;
	return new Point(points[index], points[index+1]);
}

/**
 * Copies the x and y values of the Point at <i>index</i>
 * into the x and y fields of <i>p</i> and returns 
 * <i>p</i>.
 * 
 * @since 2.0
 */
public Point getPoint(Point p, int index) {
	index *= 2;
	p.x = points[index];
	p.y = points[index+1];
	return p;
}

/**
 * Inserts a given point at a specified index in the list.
 *
 * @param p  Point to be inserted.
 * @param index  Position where the point is to be inserted.
 * @exception IndexOutOfBoundsException  if the insert index is
 * is beyond the list capacity or if it is negative.
 * @see  #setPoint(Point,int)
 * @since 2.0
 */
public void insertPoint(Point p, int index){
	bounds = null;
	index *= 2;
	if (index > points.length || index < 0)
		throw new IndexOutOfBoundsException(Draw2dMessages.ERR_PointList_InsertPoint_Exception_IndexOutOfBounds);

	int length = points.length;
	int old[] = points;
	points = new int[length+2];
	System.arraycopy(old, 0, points, 0, index);
	System.arraycopy(old, index, points, index+2, length-index);
	
	points[index] = p.x;
	points[index+1]=p.y;
	size++;
}

/**
 * Removes all the points stored by this list. Resets all
 * the properties based on the point information.
 * 
 * @since 2.0
 */
public void removeAllPoints() {
	bounds = null;
	size = 0;
}

/**
 * Removes the point at the specified index from the PointList, and 
 * returns it.
 *
 * @param index   Index of the point to be removed.
 * @return  The point which has been removed.
 * @exception IndexOutOfBoundsException  if the removal index is
 * is beyond the list capacity.
 * @see  #addPoint(Point)
 * @since 2.0
 */
public Point removePoint(int index) {
	bounds = null;
	if (index >= size)
		throw new IndexOutOfBoundsException(Draw2dMessages.ERR_PointList_RemovePoint_Exception_IndexOutOfBounds);
		
	index *= 2;
	Point pt = new Point(points[index], points[index+1]);
	if (index != size*2-2)
		System.arraycopy(points, index+2, points, index, size*2-index-2);
	size--;
	return pt;
}

/**
 * Overwrites a point at an index in the list with the 
 * input point.
 *
 * @param pt  Point which is to be stored at the index.
 * @param index  Index where the given point is to be stored.
 * @since 2.0
 */
public void setPoint(Point pt, int index){
	removePoint(index);
	insertPoint(pt, index);
}

/**
 * Returns the number of points in this PointList.
 *
 * @return  The number of points.
 * @since 2.0
 */
public int size(){return size;}

/**
 * Returns the contents of this PointList as an integer array.
 * 
 * @return  The points in the list as an array of integers.
 * @since 2.0
 */
public int[] toIntArray() {
	if (points.length != size*2) {
		int[] old = points;
		points = new int[size*2];
		System.arraycopy(old, 0, points, 0, size*2);
	}
	return points;
}

/**
 * Moves the origin (0,0) of the coordinate system of all 
 * the points to the Point <i>pt</i>. This updates the position 
 * of all the points in this PointList.
 *
 * @param pt  Position by which all the points will be shifted.
 * @see #translate(int,int)
 * @since 2.0
 */
public void translate(Point pt){
	translate(pt.x, pt.y);
}

/**
 * Moves the origin (0,0) of the coordinate system of all 
 * the points to the Point (x,y). This updates the position 
 * of all the points in this PointList.
 *
 * @param x  Amount by which all the points will be shifted on the X axis.
 * @param y  Amount by which all the points will be shifted on the Y axis.
 * @see  #translate(Point)
 * @since 2.0
 */
public void translate(int x, int y){
	if (bounds != null)
		bounds.translate(x,y);
	for (int i=0; i<size*2; i+=2){
		points[i]   += x;
		points[i+1] += y;
	}
}

}
