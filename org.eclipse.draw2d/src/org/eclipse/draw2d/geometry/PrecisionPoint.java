/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * @author danlee
 */
public class PrecisionPoint extends Point {

/** Double value for X **/
public double preciseX;

/** Double value for Y **/
public double preciseY;

/**
 * Constructor for PrecisionPoint.
 */
public PrecisionPoint() {
	super();
}

/**
 * Constructor for PrecisionPoint.
 * @param copy Point from which the initial values are taken
 */
public PrecisionPoint(Point copy) {
	preciseX = copy.preciseX();
	preciseY = copy.preciseY();
	updateInts();
}

/**
 * Constructor for PrecisionPoint.
 * @param x X value
 * @param y Y value
 */
public PrecisionPoint(int x, int y) {
	super(x, y);
	preciseX = x;
	preciseY = y;
}

/**
 * Constructor for PrecisionPoint.
 * @param x X value
 * @param y Y value
 */
public PrecisionPoint(double x, double y) {
	preciseX = x;
	preciseY = y;
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Point#getCopy()
 */
public Point getCopy() {
	return new PrecisionPoint(preciseX, preciseY);
}


/**
 * @see org.eclipse.draw2d.geometry.Point#performScale(double)
 */
public void performScale(double factor) {
	preciseX = preciseX * factor;
	preciseY = preciseY * factor;
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Point#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) {
	preciseX += dx;
	preciseY += dy;
	updateInts();
}

/**
 * @see org.eclipse.draw2d.geometry.Point#setLocation(Point)
 */
public Point setLocation(Point pt) {
	preciseX = pt.preciseX();
	preciseY = pt.preciseY();
	updateInts();
	return this;
}

/**
 * Updates the integer fields using the precise versions.
 */
public final void updateInts() {
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);
}

/**
 * @see org.eclipse.draw2d.geometry.Point#preciseX()
 */
public double preciseX() {
	return preciseX;
}

/**
 * @see org.eclipse.draw2d.geometry.Point#preciseY()
 */
public double preciseY() {
	return preciseY;
}

/**
 * @see org.eclipse.draw2d.geometry.Point#equals(java.lang.Object)
 */
public boolean equals(Object o) {
	if (o instanceof PrecisionPoint) {
		PrecisionPoint p = (PrecisionPoint)o;
		return p.preciseX == preciseX && p.preciseY == preciseY;
	}
	return super.equals(o);
}


}
