/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.geometry;

/**
 * Stores an integer width and height. This class provides various methods for 
 * manipulating this Dimension or creating new derived Objects.
 */
public class Dimension
	implements Cloneable, java.io.Serializable, Translatable
{

/**A singleton for use in short calculations.  Use to avoid newing unnecessary objects.*/
public static final Dimension SINGLETON = new Dimension();

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
 * Constructs a Dimension with the width and height of the passed Dimension. 
 *
 * @param d the Dimension supplying the initial values
 * @since 2.0
 */
public Dimension(Dimension d) { 
	width = d.width; 
	height = d.height;
}

/**
 * Constructs a Dimension where the width and height are the x and y distances of the 
 * input point from the origin.
 *
 * @param pt the Point supplying the initial values
 * @since 2.0
 */
public Dimension(org.eclipse.swt.graphics.Point pt) { 
	width = pt.x; 
	height = pt.y;
}

/**
 * Constructs a Dimension with the supplied width and height values.
 *
 * @param w the width
 * @param h the height
 * @since 2.0
 */
public Dimension(int w, int h) { 
	width = w; 
	height = h;
}

/**
 * Constructs a Dimension with the width and height of the Image supplied as input.
 *
 * @param image the image supplying the dimensions
 * @since 2.0
 */
public Dimension(org.eclipse.swt.graphics.Image image) {
	org.eclipse.swt.graphics.Rectangle r = image.getBounds();
	width = r.width;
	height = r.height;
}

/**
 * Returns <code>true</code> if the input Dimension fits into this Dimension. A Dimension 
 * of the same size is considered to "fit". 
 *
 * @param d the dimension being tested
 * @return  <code>true</code> if this Dimension contains <i>d</i>
 * @since 2.0
 */
public boolean contains(Dimension d) {
	return width >= d.width && height >= d.height;
}

/**
 * Returns <code>true</code> if this Dimension properly contains the one specified.  
 * Proper containment is defined as containment using "<", instead of "<=".
 * 
 * @param d the dimension being tested
 * @return <code>true</code> if this Dimension properly contains the one specified
 * @since 2.0
 */
public boolean containsProper(Dimension d) {
	return width > d.width && height > d.height;
}

/**
 * Copies the width and height values of the input Dimension to this Dimension.
 * 
 * @param d the dimension supplying the values
 * @since 2.0
 */
public void setSize(Dimension d) {
	width = d.width;
	height = d.height;
}

/**
 * Returns the area of this Dimension.
 * 
 * @return the area
 * @since 2.0
 */
public int getArea() {
	return width * height;
}

/**
 * Creates and returns a copy of this Dimension.
 * @return a copy of this Dimension
 * @since 2.0
 */
public Dimension getCopy() {
	return new Dimension(this);
}

/**
 * Creates and returns a new Dimension representing the difference between this Dimension 
 * and the one specified.
 * 
 * @param d the dimension being compared
 * @return a new dimension representing the difference
 * @since 2.0
 */
public Dimension getDifference(Dimension d) {
	return new Dimension(width - d.width, height - d.height);
}

/**
 * Creates and returns a Dimension representing the sum of this Dimension and the one 
 * specified.
 * 
 * @param d the dimension providing the expansion width and height
 * @return a new dimension expanded by <i>d</i>
 * @since 2.0
 */
public Dimension getExpanded(Dimension d) {
	return new Dimension(width + d.width, height + d.height);
}

/**
 * Creates and returns a new Dimension representing the sum of this Dimension and the one 
 * specified.
 * 
 * @param w value by which the width of this is to be expanded
 * @param h value by which the height of this is to be expanded
 * @return a new Dimension expanded by the given values
 * @since 2.0
 */
public Dimension getExpanded(int w, int h) {
	return new Dimension(width + w, height + h);
}

/**
 * Creates and returns a new Dimension representing the intersection of this Dimension and
 * the one specified.
 * 
 * @param d the Dimension to intersect with
 * @return A new Dimension representing the intersection
 * @since 2.0
 */
public Dimension getIntersected(Dimension d) {
	return new Dimension(this).intersect(d);
}

/**
 * Creates and returns a new Dimension with negated values. 
 * 
 * @return	a new Dimension with negated values
 * @since 2.0
 */
public Dimension getNegated() {
	return new Dimension(0 - width, 0 - height);
}

/**
 * Returns whether the input Object is equivalent to this Dimension. <code>true</code> if 
 * the Object is a Dimension and its width and height are equal to this Dimension's width
 * and height, <code>false</code> otherwise.
 * 
 * @param o the Object being tested for equality
 * @return <code>true</code> if the given object is equal to this dimension
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
 * Returns <code>true</code> if this Dimension's width and height are equal to the given 
 * width and height.
 * 
 * @param w the width
 * @param h the height
 * @return <code>true</code> if this dimension's width and height are equal to those given.
 * @since 2.0
 */
public boolean equals(int w, int h) {
	return width == w && height == h;
}

/**
 * Expands the size of this Dimension by the specified amount.
 * 
 * @param d the Dimension providing the expansion width and height
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension expand(Dimension d) {
	width  += d.width;
	height += d.height;
	return this;
}

/**
 * Expands the size of this Dimension by the specified amound.
 * 
 * @param pt the Point supplying the dimensional values
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension expand(Point pt) {
	width  += pt.x;
	height += pt.y;
	return this;
}

/**
 * Expands the size of this Dimension by the specified width and height.
 * 
 * @param w  Value by which the width should be increased
 * @param h  Value by which the height should be increased
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension expand(int w, int h) {
	width  += w;
	height += h;
	return this;
}

/**
 * Creates a new Dimension with its width and height scaled by the specified value.
 * 
 * @param amount Value by which the width and height are scaled
 * @return a new dimension with the scale applied
 * @since 2.0
 */
public Dimension getScaled(double amount) {
	return new Dimension(this)
		.scale(amount);
}

/**
 * Creates a new Dimension with its height and width swapped. Useful in orientation change 
 * calculations.
 * 
 * @return a new Dimension with its height and width swapped
 * @since 2.0
 */
public Dimension getTransposed() {
	return new Dimension(this)
		.transpose();
}

/**
 * Creates a new Dimension representing the union of this Dimension with the one 
 * specified. Union is defined as the max() of the values from each Dimension.
 * 
 * @param d the Dimension to be unioned
 * @return a new Dimension
 * @since 2.0
 */
public Dimension getUnioned(Dimension d) { 
	return new Dimension(this).union(d);
}

/**
 * @see java.lang.Object#hashCode()
 */
public int hashCode() {
	return (width * height) ^ (width + height);
}


/**
 * This Dimension is intersected with the one specified. Intersection is performed by 
 * taking the min() of the values from each dimension.
 * 
 * @param d the Dimension used to perform the min()
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension intersect(Dimension d) {
	width = Math.min(d.width, width);
	height = Math.min(d.height, height);
	return this;
}

/**
 * Returns <code>true</code> if either dimension is less than or equal to 0.
 *
 * @return <code>true</code> if either dimension is less than or equal to 0.
 * @since 2.0
 */
public boolean isEmpty() {
	return (width <= 0) || (height <= 0);
}

/**
 * Negates the width and height of this Dimension.
 *
 * @return	<code>this</code> for convenience
 * @since 2.0
 */
public Dimension negate() {
	width = 0 - width;
	height = 0 - height;
	return this;
}

/**
 * @see org.eclipse.draw2d.geometry.Translatable#performScale(double)
 */
public void performScale(double factor) {
	scale(factor);
}

/**
 * @see org.eclipse.draw2d.geometry.Translatable#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) { }

/**
 * Scales the width and height of this Dimension by the amount supplied, and returns this 
 * for convenience. 
 *
 * @param amount value by which this Dimension's width and height are to be scaled
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension scale(double amount) {
	return scale(amount, amount);
}

/**
 * Scales the width of this Dimension by <i>w</i> and scales the height of this Dimension 
 * by <i>h</i>. Returns this for convenience. 
 *
 * @param w the value by which the width is to be scaled
 * @param h the value by which the height is to be scaled
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension scale(double w, double h) {
	width  = (int)(Math.floor(width * w));
	height = (int)(Math.floor(height * h));
	return this;
}

/**
 * Reduces the width of this Dimension by <i>w</i>, and reduces the height of this 
 * Dimension by <i>h</i>. Returns this for convenience.
 *
 * @param w the value by which the width is to be reduced
 * @param h the value by which the height is to be reduced
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension shrink(int w, int h) {
	return expand(-w, -h);
}

/**
 * @see Object#toString()
 */

public String toString() {
	return "Dimension(" +  //$NON-NLS-1$
		width + ", " +  //$NON-NLS-1$
		height + ")"; //$NON-NLS-1$
}

/**
 * Swaps the width and height of this Dimension, and returns this for convenience. Can be 
 * useful in orientation changes.
 *
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension transpose() {
	int temp = width;
	width = height;
	height = temp;
	return this;
}

/**
 * Sets the width of this Dimension to the greater of this Dimension's width and 
 * <i>d</i>.width. Likewise for this Dimension's height.
 * 
 * @param d the Dimension to union with this Dimension
 * @return <code>this</code> for convenience
 * @since 2.0
 */
public Dimension union (Dimension d) {
	width = Math.max(width, d.width);
	height = Math.max(height, d.height);
	return this;
}

/**
 * Returns <code>double</code> width
 * 
 * @return <code>double</code> width
 * @since 3.4
 */
public double preciseWidth() {
	return width;
}

/**
 * Returns <code>double</code> height
 * 
 * @return <code>double</code> height
 * @since 3.4
 */
public double preciseHeight() {
	return height;
}

}
