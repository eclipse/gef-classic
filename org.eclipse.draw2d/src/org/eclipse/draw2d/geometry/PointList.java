package org.eclipse.draw2d.geometry;
/* 
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/** 
 * Represents a List of Points.  This class is used for building an <code>int[]</code>.
 * The array is internal, and is constructed and queried by the client using {@link Point Points}.
 * SWT uses integer arrays when painting polylines and polygons.
 */
public class PointList
	implements java.io.Serializable, Translatable
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
public PointList() { }

/** 
 * Constructs a PointList with capacity <i>size</i>.
 * 
 * @param size  Number of points to hold.
 * @since 2.0
 */
public PointList(int size) {
	points = new int[size * 2];
}

	
/** 
 * Adds Point <i>p</i> to this PointList.
 * @see  #removePoint(int)
 * @since 2.0
 */
public void addPoint(Point p) {
	addPoint(p.x, p.y);
}

/** 
 * Adds the input point values to this PointList.
 * @param x  X value of a point to add
 * @param y  Y value of a point to add
 * @since 2.0
 */
public void addPoint(int x, int y) {
	bounds = null;
	int arrayLength = points.length;
	int usedLength = size * 2;
	if (arrayLength == usedLength) {
		int old[] = points;
		points = new int[arrayLength + 2];
		System.arraycopy(old, 0, points, 0, arrayLength);
	}
	points[usedLength]   = x;
	points[usedLength + 1] = y;
	size++;
}

/** 
 * Returns the smallest Rectangle which contains all Points.
 * @return The smallest Rectangle which contains all Points.
 * @since 2.0
 */
public Rectangle getBounds() {
	if (bounds != null)
		return bounds;
	bounds = new Rectangle();
	if (size > 0) {
		bounds.setLocation(getPoint(0));
		for (int i = 0; i < size; i++)
			bounds.union(getPoint(i));
	}
	return bounds;
}

/**
 * Creates a copy
 * @return PointList A copy of this PointList */
public PointList getCopy() {
	PointList result = new PointList(size);
	System.arraycopy(points, 0, result.points, 0, points.length);
	result.size = size;
	result.bounds = null;
	return result;
}

/** 
 * Returns the first Point in the list.
 * @return  The first point in the list.
 * @throws IndexOutOfBoundsException if the list is empty
 * @since 2.0
 */
public Point getFirstPoint() {
	return getPoint(0);
}

/** 
 * Returns the last point in the list.
 * @throws IndexOutOfBoundsException if the list is empty
 * @return  The last Point in the list
 * @since 2.0
 */
public Point getLastPoint() {
	return getPoint(size - 1);
}

/** 
 * Returns the midpoint of the list of Points.  The midpoint is the median of the List, unless
 * there are 2 medians (size is even), then the middle of the medians is returned.
 * @return The midpoint
 * @throws IndexOutOfBoundsException if the list is empty
 */
public Point getMidpoint() {
	if (size() % 2 == 0)
		return getPoint(size() / 2 - 1).
			getTranslated(getPoint(size() / 2)).
			scale(0.5f);
	return getPoint(size() / 2);
}

/** 
 * Returns the Point in the list at the specified index.
 * @param index Index of the desired Point
 * @return  The requested Point
 * @throws IndexOutOfBoundsException If the specified index is out of range
 * @since 2.0
 */
public Point getPoint(int index) {
	if (index < 0 || index >= size)
	    throw new IndexOutOfBoundsException(
	    	"Index: " + index + //$NON-NLS-1$
	    	", Size: " + size); //$NON-NLS-1$
	index  *= 2;
	return new Point(points[index], points[index + 1]);
}

/** 
 * Copies the x and y values at given index into a specified Point.
 * This method exists to avoid the creation of a new <code>Point</code>.
 * @see #getPoint(int)
 * @param p The Point which will be set with the &lt;x, y&gt; values
 * @param index The index being requested
 * @return The parameter <code>p</code> is returned for convenience
 * @since 2.0
 */
public Point getPoint(Point p, int index) {
	if (index < 0 || index >= size)
	    throw new IndexOutOfBoundsException(
	    	"Index: " + index + //$NON-NLS-1$
	    	", Size: " + size); //$NON-NLS-1$
	index *= 2;
	p.x = points[index];
	p.y = points[index + 1];
	return p;
}

/** 
 * Inserts a given point at a specified index.
 * @param p  Point to be inserted.
 * @param index  Position where the point is to be inserted.
 * @exception IndexOutOfBoundsException  if the index is invalid
 * @see  #setPoint(Point, int)
 * @since 2.0
 */
public void insertPoint(Point p, int index) {
	bounds = null;
	if (index > size || index < 0)
	    throw new IndexOutOfBoundsException(
	    	"Index: " + index + //$NON-NLS-1$
	    	", Size: " + size); //$NON-NLS-1$
	index *= 2;

	int length = points.length;
	int old[] = points;
	points = new int[length + 2];
	System.arraycopy(old, 0, points, 0, index);
	System.arraycopy(old, index, points, index + 2, length - index);
	
	points[index] = p.x;
	points[index + 1] = p.y;
	size++;
}

/**
 * @see org.eclipse.draw2d.geometry.Translatable#performScale(double)
 */
public void performScale(double factor) {
	for (int i = 0; i < points.length; i++)
		points[i] = (int)Math.floor(points[i] * factor);
	bounds = null;
}

/**
 * @see org.eclipse.draw2d.geometry.Translatable#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) {
	for (int i = 0; i < size * 2; i += 2) {
		points[i] += dx;
		points[i + 1] += dy;
	}
	if (bounds != null)
		bounds.translate(dx, dy);
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
 * @since 2.0
 * @see  #addPoint(Point)
 * @param index   Index of the point to be removed.
 * @return  The point which has been removed
 * @throws IndexOutOfBoundsException if the removal index is beyond the list capacity
 */
public Point removePoint(int index) {
	bounds = null;
	if (index < 0 || index >= size)
	    throw new IndexOutOfBoundsException(
	    	"Index: " + index + //$NON-NLS-1$
	    	", Size: " + size); //$NON-NLS-1$
		
	index *= 2;
	Point pt = new Point(points[index], points[index + 1]);
	if (index != size * 2 - 2)
		System.arraycopy(points, index + 2, points, index, size * 2 - index - 2);
	size--;
	return pt;
}

/** 
 * Overwrites a point at a given index in the list with the specified Point.
 * @param pt  Point which is to be stored at the index.
 * @param index  Index where the given point is to be stored.
 * @since 2.0
 */
public void setPoint(Point pt, int index) {
	bounds = null;
	points[index * 2] = pt.x;
	points[index * 2 + 1] = pt.y;
}

public void setSize(int newSize) {
	if (points.length > newSize*2) {
		size = newSize;
		return;
	}
	int[] newArray = new int[newSize * 2];
	System.arraycopy(points,0,newArray,0,points.length);
	points = newArray;
	size = newSize;
}

/** 
 * Returns the number of points in this PointList.
 * @return  The number of points
 * @since 2.0
 */
public int size() {
	return size;
}

/** 
 * Returns the contents of this PointList as an integer array.
 * 
 * @return  The points in the list as an array of integers.
 * @since 2.0
 */
public int[] toIntArray() {
	if (points.length != size * 2) {
		int[] old = points;
		points = new int[size * 2];
		System.arraycopy(old, 0, points, 0, size * 2);
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
public final void translate(Point pt) {
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
public void translate(int x, int y) {
	if (x == 0 && y == 0)
		return;
	if (bounds != null)
		bounds.translate(x, y);
	for (int i = 0; i < size * 2; i += 2) {
		points[i] += x;
		points[i + 1] += y;
	}
}

}
