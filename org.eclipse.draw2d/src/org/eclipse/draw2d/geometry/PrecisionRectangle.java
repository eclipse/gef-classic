/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
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
 * A Rectangle implementation using floating point values which are truncated into the inherited
 * integer fields. The use of floating point prevents rounding errors from accumulating.
 * @author hudsonr
 * Created on Apr 9, 2003
 */
public final class PrecisionRectangle extends Rectangle {

/** Double value for height */
public double preciseHeight;

/** Double value for width */
public double preciseWidth;

/** Double value for X */
public double preciseX;

/** Double value for Y */
public double preciseY;

/**
 * Constructs a new PrecisionRectangle with all values 0.
 */
public PrecisionRectangle() { }

/**
 * Constructs a new PrecisionRectangle from the given integer Rectangle.
 * @param rect the base rectangle
 */
public PrecisionRectangle(Rectangle rect) {
	preciseX = rect.preciseX();
	preciseY = rect.preciseY();
	preciseWidth = rect.preciseWidth();
	preciseHeight = rect.preciseHeight();
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#getCopy()
 */
public Rectangle getCopy() {
	return getPreciseCopy();
}

/**
 * Returns a precise copy of this.
 * @return a precise copy
 */
public PrecisionRectangle getPreciseCopy() {
	PrecisionRectangle result = new PrecisionRectangle();
	result.preciseX = preciseX;
	result.preciseY = preciseY;
	result.preciseWidth = preciseWidth;
	result.preciseHeight = preciseHeight;
	result.updateInts();
	return result;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#crop(org.eclipse.draw2d.geometry.Insets)
 */
public Rectangle crop(Insets insets) {
    if (insets == null) 
        return this;
    setX(preciseX + insets.left);
    setY(preciseY + insets.top);
    setWidth(preciseWidth - (insets.getWidth()));
    setHeight(preciseHeight - (insets.getHeight()));
    
    return this;
}

/**
 * @see Rectangle#equals(Object)
 */
public boolean equals(Object o) {
    if (o instanceof PrecisionRectangle) {
        PrecisionRectangle pr = (PrecisionRectangle)o;
        return super.equals(o)
        	&& Math.abs(pr.preciseX - preciseX) < 0.000000001
        	&& Math.abs(pr.preciseY - preciseY) < 0.000000001
        	&& Math.abs(pr.preciseWidth - preciseWidth) < 0.000000001
        	&& Math.abs(pr.preciseHeight - preciseHeight) < 0.00000001;
    }
    
    return super.equals(o);
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#performScale(double)
 */
public void performScale(double factor) {
	preciseX *= factor;
	preciseY *= factor;
	preciseWidth *= factor;
	preciseHeight *= factor;
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) {
	preciseX += dx;
	preciseY += dy;
	x += dx;
	y += dy;
}

/**
 * Returns the bottom coordinte in double precision.
 * @return the precise bottom
 */
public double preciseBottom() {
	return preciseHeight + preciseY;
}

/**
 * Returns the right side in double precision.
 * @return the precise right
 */
public double preciseRight() {
	return preciseWidth + preciseX;
}


/**
 * @see org.eclipse.draw2d.geometry.Rectangle#resize(org.eclipse.draw2d.geometry.Dimension)
 */
public Rectangle resize(Dimension sizeDelta) {
	preciseWidth += sizeDelta.preciseWidth();
	preciseHeight += sizeDelta.preciseHeight();
	updateInts();
	return this;
}

/**
 * Sets the height.
 * @param value the new height
 */
public void setHeight(double value) {
	preciseHeight = value;
	height = (int)Math.floor(preciseHeight + 0.000000001);	
}

/**
 * Sets the width.
 * @param value the new width
 */
public void setWidth(double value) {
	preciseWidth = value;
	width = (int)Math.floor(preciseWidth + 0.000000001);
}

/**
 * Sets the x value.
 * @param value the new x value
 */
public void setX(double value) {
	preciseX = value;
	x = (int)Math.floor(preciseX + 0.000000001);
}

/**
 * Sets the y value.
 * @param value the new y value
 */
public void setY(double value) {
	preciseY = value;
	y = (int)Math.floor(preciseY + 0.000000001);
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#translate(org.eclipse.draw2d.geometry.Point)
 */
public Rectangle translate(Point p) {
	preciseX += p.preciseX();
	preciseY += p.preciseY();
	updateInts();
	return this;
}

/**
 * Unions the given PrecisionRectangle with this rectangle and returns <code>this</code>
 * for convenience.
 * @since 3.0
 * @param other the rectangle being unioned
 * @return <code>this</code> for convenience
 * @deprecated
 * Use {@link #union(Rectangle)} instead
 */
public PrecisionRectangle union(PrecisionRectangle other) {
	double newright = Math.max(preciseRight(), other.preciseRight());
	double newbottom = Math.max(preciseBottom(), other.preciseBottom());
	preciseX = Math.min(preciseX, other.preciseX);
	preciseY = Math.min(preciseY, other.preciseY);
	preciseWidth = newright - preciseX;
	preciseHeight = newbottom - preciseY;
	updateInts();
	
	return this;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#union(org.eclipse.draw2d.geometry.Rectangle)
 */
public Rectangle union(Rectangle other) {
	double newright = Math.max(preciseRight(), other.preciseX() + other.preciseWidth());
	double newbottom = Math.max(preciseBottom(), other.preciseY() + other.preciseHeight());
	preciseX = Math.min(preciseX, other.preciseX());
	preciseY = Math.min(preciseY, other.preciseY());
	preciseWidth = newright - preciseX;
	preciseHeight = newbottom - preciseY;
	updateInts();
	
	return this;
}

/**
 * Updates the integer values based on the current precise values.  The integer values ar
 * the floor of the double values.  This is called automatically when calling api which is
 * overridden in this class.
 * @since 3.0
 */
public void updateInts() {
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);
	width = (int)Math.floor(preciseWidth + preciseX + 0.000000001) - x;
	height = (int)Math.floor(preciseHeight + preciseY + 0.000000001) - y;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#union(org.eclipse.draw2d.geometry.Point)
 */
public void union(Point p) {
	if (p.preciseX() < preciseX) {
		preciseWidth += (preciseX - p.preciseX());
		preciseX = p.preciseX();
	} else {
		double right = preciseX + preciseWidth;
		if (p.preciseX() > right) {
			preciseWidth = p.preciseX() - preciseX;
		}
	}
	if (p.preciseY() < preciseY) {
		preciseHeight += (preciseY - p.preciseY());
		preciseY = p.preciseY();
	} else {
		double bottom = preciseY + preciseHeight;
		if (p.preciseY() > bottom) {
			preciseHeight = p.preciseY() - preciseY;
		}
	}
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#transpose()
 */
public Rectangle transpose() {
	double temp = preciseX;
	preciseX = preciseY;
	preciseY = temp;
	temp = preciseWidth;
	preciseWidth = preciseHeight;
	preciseHeight = temp;
	super.transpose();
	return this;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#setLocation(org.eclipse.draw2d.geometry.Point)
 */
public Rectangle setLocation(Point loc) {
	preciseX = loc.preciseX();
	preciseY = loc.preciseY();
	updateInts();
	return this;
}

/**
 * Returns the precise geometric centre of the rectangle
 * 
 * @return <code>PrecisionPoint</code> geometric center of the rectangle
 * @since 3.4
 */
public Point getCenter() {
	return new PrecisionPoint(preciseX + preciseWidth / 2.0, preciseY + preciseHeight / 2.0);
}

/**
 * Shrinks the sides of this Rectangle by the horizontal and vertical values 
 * provided as input, and returns this Rectangle for convenience. The center of 
 * this Rectangle is kept constant.
 *
 * @param h  Horizontal reduction amount
 * @param v  Vertical reduction amount
 * @return  <code>this</code> for convenience
 * @since 3.4
 */
public Rectangle shrink(double h, double v) {
	preciseX += h; 
	preciseWidth -= (h + h);
	preciseY += v; 
	preciseHeight -= (v + v);
	updateInts();
	return this;
}

/**
 * Expands the horizontal and vertical sides of this Rectangle with the values 
 * provided as input, and returns this for convenience. The location of its 
 * center is kept constant.
 * 
 * @param h  Horizontal increment
 * @param v  Vertical increment
 * @return  <code>this</code> for convenience
 * @since 3.4
 */
public Rectangle expand(double h, double v) {
	return shrink(-h, -v);
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#shrink(int, int)
 */
public Rectangle shrink(int h, int v) {
	return shrink((double)h, (double)v);
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#contains(org.eclipse.draw2d.geometry.Point)
 */
public boolean contains(Point p) {
	return preciseX <= p.preciseX() && p.preciseX() <= preciseX + preciseWidth
	&& preciseY <= p.preciseY() && p.preciseY() <= preciseY + preciseHeight;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#preciseX()
 */
public double preciseX() {
	return preciseX;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#preciseY()
 */
public double preciseY() {
	return preciseY;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#preciseWidth()
 */
public double preciseWidth() {
	return preciseWidth;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#preciseHeight()
 */
public double preciseHeight() {
	return preciseHeight;
}

/**
 * @see org.eclipse.draw2d.geometry.Rectangle#setSize(org.eclipse.draw2d.geometry.Dimension)
 */
public Rectangle setSize(Dimension d) {
	preciseWidth = d.preciseWidth();
	preciseHeight = d.preciseHeight();
	return super.setSize(d);
}

}
