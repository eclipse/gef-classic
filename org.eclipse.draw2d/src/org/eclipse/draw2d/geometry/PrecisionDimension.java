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
 * @author Randy Hudson
 */
public class PrecisionDimension extends Dimension {

/**
 * The width in double precision.
 */
public double preciseWidth;
/**
 * The height in double precision.
 */
public double preciseHeight;

/**
 * Constructs a new precision dimension.
 */
public PrecisionDimension() {
}

/**
 * Constructs a new precision dimension with the given values.
 * @param width the width
 * @param height the height
 */
public PrecisionDimension(double width, double height) {
	preciseWidth = width;
	preciseHeight = height;
	updateInts();
}

/**
 * Constructs a precision representation of the given dimension.
 * @param d the reference dimension
 */
public PrecisionDimension(Dimension d) {
	preciseHeight = d.preciseHeight();
	preciseWidth = d.preciseWidth();
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Dimension#performScale(double)
 */
public void performScale(double factor) {
	preciseHeight *= factor;
	preciseWidth *= factor;
	updateInts();
}

/**
 * Updates the integer fields using the precise versions.
 */
public final void updateInts() {
	width = (int)Math.floor(preciseWidth + 0.000000001);
	height = (int)Math.floor(preciseHeight + 0.000000001);	
}

/**
 * @see org.eclipse.draw2d.geometry.Dimension#preciseWidth()
 */
public double preciseWidth() {
	return preciseWidth;
}

/**
 * @see org.eclipse.draw2d.geometry.Dimension#preciseHeight()
 */
public double preciseHeight() {
	return preciseHeight;
}

/**
 * @see org.eclipse.draw2d.geometry.Dimension#equals(java.lang.Object)
 */
public boolean equals(Object o) {
	if (o instanceof PrecisionDimension) {
		PrecisionDimension d = (PrecisionDimension)o;
		return d.preciseWidth == preciseWidth && d.preciseHeight == preciseHeight;
	}
	return super.equals(o);
}

}
