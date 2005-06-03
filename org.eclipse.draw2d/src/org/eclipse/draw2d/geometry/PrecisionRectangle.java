/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
	if (rect instanceof PrecisionRectangle) {
		PrecisionRectangle rectangle = (PrecisionRectangle)rect;
		preciseX = rectangle.preciseX;
		preciseY = rectangle.preciseY;
		preciseWidth = rectangle.preciseWidth;
		preciseHeight = rectangle.preciseHeight;
	} else {
		preciseX = rect.x;
		preciseY = rect.y;
		preciseWidth = rect.width;
		preciseHeight = rect.height;
	}
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
	if (sizeDelta instanceof PrecisionDimension) {
		PrecisionDimension pd = (PrecisionDimension)sizeDelta;
		preciseWidth += pd.preciseWidth;
		preciseHeight += pd.preciseHeight;
	} else {
		preciseWidth += sizeDelta.width;
		preciseHeight += sizeDelta.height;
	}
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
	if (p instanceof PrecisionPoint) {
		PrecisionPoint pp = (PrecisionPoint)p;
		preciseX += pp.preciseX;
		preciseY += pp.preciseY;
	} else {
		preciseX += p.x;
		preciseY += p.y;
	}
	updateInts();
	return this;
}

/**
 * Unions the given PrecisionRectangle with this rectangle and returns <code>this</code>
 * for convenience.
 * @since 3.0
 * @param other the rectangle being unioned
 * @return <code>this</code> for convenience
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

}
