package org.eclipse.draw2d.geometry;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Stores an integer width and height.  This class provides various methods for manipulating
 * this Dimension or creating new derived Objects.
 */
public class Dimension
	implements Cloneable, java.io.Serializable
{

/**The width.*/
public int width;
/**The height. */
public int height;

static final long serialVersionUID = 1;

/**
 * Constructs a Dimension of zero width and height.
 * 
 * @since 2.0
 */
public Dimension() { }

/**
 * Constructs a Dimension with the width and height of the 
 * passed Dimension. 
 *
 * @param d  Dimension supplying the initial values.
 * @since 2.0
 */
public Dimension(Dimension d) { width = d.width; height = d.height;}

/**
 * Constructs a Dimension where the width and height are the 
 * x and y distances of the input point from the origin.
 *
 * @param pt  Point supplying the initial values.
 * @since 2.0
 */
public Dimension(org.eclipse.swt.graphics.Point pt) { width = pt.x; height = pt.y;}

/**
 * Constructs a Dimension with the supplied width and 
 * height values.
 *
 * @param w  Width of the Dimension.
 * @param h  Height of the Dimension.
 * @since 2.0
 */
public Dimension(int w, int h) { width = w; height = h;}

/**
 * Constructs a Dimension with the width and height of the Image
 * supplied as input.
 *
 * @param image  {@link org.eclipse.swt.graphics.Image SWT Image} 
 *                supplying the dimensions.
 * @see  org.eclipse.swt.graphics.Image
 * @since 2.0
 */
public Dimension(org.eclipse.swt.graphics.Image image) {
	org.eclipse.swt.graphics.Rectangle r = image.getBounds();
	width = r.width;
	height = r.height;
}

/**
 * Returns whether the input Dimension fits into this Dimension.
 * A Dimension of the same size is considered to "fit". 
 *
 * @param d  Dimension being tested.
 * @return  <code>boolean</code> specifying the result of the fit test.
 * @since 2.0
 */
public boolean contains(Dimension d) {
	return width >= d.width && height >= d.height;
}

/**
 * Returns true if this Dimension properly contains the one specified.  Proper containment
 * is defined as containment using "<", instead of "<=".
 * @param d  Dimension being tested.
 * @return <code>true</code> if this Dimension properly contains the one specified
 * @since 2.0
 */
public boolean containsProper(Dimension d) {
	return width > d.width && height > d.height;
}

/**
 * Copies the width and height values of the input Dimension to this Dimension.
 * 
 * @param d  Dimension supplying the values.
 * @since 2.0
 */
public void copyFrom(Dimension d) {
	width = d.width;
	height = d.height;
}

/**
 * Returns the area of this Dimension.
 * @return  The area of this Dimension.
 * @since 2.0
 */
public int getArea() {
	return width * height;
}

/**
 * Creates a copy.
 * @return A copy of this Dimension
 * @since 2.0
 */
public Dimension getCopy() {
	return new Dimension(this);
}

/**
 * Creates a new Dimension representing the difference between this Dimension
 * and the one specified.
 * @param d  Dimension being compared
 * @return  A new Dimension
 * @since 2.0
 */
public Dimension getDifference(Dimension d) {
	return new Dimension(width - d.width, height - d.height);
}

/**
 * Creates a Dimension representing the sum of this Dimension and the one specified.
 * @param d  Dimension providing the expansion width and height.
 * @return A new Dimension
 * @see #getDifference(Dimension)
 * @since 2.0
 */
public Dimension getExpanded(Dimension d) {
	return new Dimension(width + d.width, height + d.height);
}

/**
 * Creates a new Dimension representing the sum of this Dimension and the one specified.
 * @param w  Value by which the width of this is to be expanded.
 * @param h  Value by which the height of this is to be expanded.
 * @return  A new Dimension
 * @see  #getDifference(Dimension)
 * @since 2.0
 */
public Dimension getExpanded(int w, int h) {
	return new Dimension(width + w, height + h);
}

/**
 * Creates a new Dimension representing the intersection of
 * this Dimension and the one specified.
 * @return A new Dimension
 * @see #intersect(Dimension)
 * @param d The Dimension to intersect with
 * @since 2.0
 */
public Dimension getIntersected(Dimension d) {
	return new Dimension(this).intersect(d);
}

/**
 * Creates a new Dimension with negated values. 
 * @return	A new Dimension
 * @since 2.0
 */
public Dimension getNegated() {
	return new Dimension(0 - width, 0 - height);
}

/**
 * Returns whether the input Object is equivalent to this Dimension.
 * True if the Object is a Dimension and its height and width are equal, false
 * otherwise.
 * @param o  Object being tested for equality.
 * @return  Result of the size test.
 * @since 2.0
 */
public boolean equals(Object o) {
	if (o instanceof Dimension) {
		Dimension d = (Dimension)o;
		return (d.width == width && d.height == height);
	}
	return false;
}

/**
 * Returns whether this Dimension's width and height are
 * equal to the given width and height.
 * @param w width
 * @param h height
 * @return true if both width and height are equal.
 * @since 2.0
 */
public boolean equals(int w, int h) {
	return width == w && height == h;
}

/**
 * Expands the size of this Dimension by the specified amount.
 * @param d  Dimension providing the expansion width and height.
 * @return <code>this</code> for convenience
 * @see #shrink(int, int)
 * @since 2.0
 */
public Dimension expand(Dimension d) {
	width  += d.width;
	height += d.height;
	return this;
}

/**
 * Expands the size of this Dimension by the specified amound.
 * @since 2.0
 * @param pt  Point supplying the dimensional values.
 * @return <code>this</code> for convenience
 */
public Dimension expand(Point pt) {
	width  += pt.x;
	height += pt.y;
	return this;
}

/**
 * Expands the size of this Dimension by the specified width and height.
 * @since 2.0
 * @param w  Value by which the width should be increased.
 * @param h  Value by which the height should be increased.
 * @return <code>this</code> for convenience
 */
public Dimension expand(int w, int h) {
	width  += w;
	height += h;
	return this;
}

/**
 * Creates a new Dimension with its width and height scaled by the
 * specified value.
 * @param amount Value by which the width and height are scaled.
 * @return  A new Dimension
 * @since 2.0
 */
public Dimension getScaled(float amount) {
	return new Dimension(this)
		.scale(amount);
}

/**
 * Creates a new Dimension with its height and width swapped.
 * Useful in orientation change calculations.
 * @return  A new Dimension
 * @since 2.0
 */
public Dimension getTransposed() {
	return new Dimension(this)
		.transpose();
}

/**
 * Creates a new Dimension representing the union of this Dimension with the one specified.
 * Union is defined as the Max() of the values from each Dimension.
 * @param d  Dimension to be unioned.
 * @return  A new Dimension
 * @since 2.0
 */
public Dimension getUnioned(Dimension d) { return new Dimension(this).union(d);}

/**
 * This Dimension is intersected with the one specified.  Intersection is performed by taking the
 * Min() of the values from each dimension.
 * @param d The Dimension used to perform the Min().
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension intersect(Dimension d) {
	width = Math.min(d.width, width);
	height = Math.min(d.height, height);
	return this;
}

/**
 * Returns whether the Dimension has width or height greater than 0. 
 *
 * @return  <code>boolean<code> containing the emptiness test.
 * @since 2.0
 */
public boolean isEmpty() {
	return (width <= 0) || (height <= 0);
}

/**
 * Negates this Dimension
 *
 * @return	Returns this Dimension for convenience.
 * @since 2.0
 */
public Dimension negate() {
	width = 0 - width;
	height = 0 - height;
	return this;
}

/**
 * Scales the width and height of this Dimension by the amount supplied, and
 * returns this for convenience. 
 *
 * @param amount  Value by which this Dimension's width and 
 *                 height are to be scaled.
 * @return  Returns this Dimension with the scaled values.
 * @since 2.0
 */
public Dimension scale(float amount) {
	return scale(amount, amount);
}

/**
 * Scales the width of this Dimension by <i>w</i> and
 * scales the height of this Dimension by <i>h</i>.
 * Returns this for convenience. 
 *
 * @param w  Value by which the width is to be scaled.
 * @param h  Value by which the height is to be scaled.
 * @return  Returns this Dimension with the scaled values.
 * @since 2.0
 */
public Dimension scale(float w, float h) {
	width  = (int)(Math.floor(width * w));
	height = (int)(Math.floor(height * h));
	return this;
}

/**
 * Reduces the width of this Dimension by <i>w</i>, and 
 * reduces the height of this Dimension by <i>h</i>.
 * Returns this for convenience.
 *
 * @param w  Value by which the width is to be reduced.
 * @param h  Value by which the height is to be reduced.
 * @return  Returns this Dimension with the reduced width and height values.
 * @since 2.0
 */
public Dimension shrink(int w, int h) {
	return expand(-w, -h);
}

/**
 * @return String representation.
 * @since 2.0
 */

public String toString() {
	return "Dimension(" +  //$NON-NLS-1$
		width + ", " +  //$NON-NLS-1$
		height + ")"; //$NON-NLS-1$
}

/**
 * Swaps the width and height of this Dimension, and
 * returns this for convenience. Can be useful in 
 * orientation changes.
 *
 * @return  This Dimension with the switched values.
 * @since 2.0
 */
public Dimension transpose() {
	int temp = width;
	width = height;
	height = temp;
	return this;
}

/**
 * Sets the width of this Dimension to the greater of
 * this Dimension's width and <i>d</i>.width.
 * Likewise for this Dimension's height.
 * 
 * @param d Dimension to union with this Dimension.
 * @return  Returns this Dimension with the unioned width and height.
 * @since 2.0
 */
public Dimension union (Dimension d) {
	width = Math.max(width, d.width);
	height = Math.max(height, d.height);
	return this;
}

}