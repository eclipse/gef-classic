/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	if (copy instanceof PrecisionPoint) {
		PrecisionPoint precPt = (PrecisionPoint)copy;
		preciseX = precPt.preciseX;
		preciseY = precPt.preciseY;
		updateInts();
	} else {
		preciseX = x = copy.x;
		preciseY = y = copy.y;
	}
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
	super(x, y);
	preciseX = x;
	preciseY = y;
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
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);	
}

/**
 * @see org.eclipse.draw2d.geometry.Point#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) {
	preciseX += dx;
	preciseY += dy;
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);
}

/**
 * @see org.eclipse.draw2d.geometry.Point#setLocation(Point)
 */
public Point setLocation(Point pt) {
	if (pt instanceof PrecisionPoint) {
		preciseX = ((PrecisionPoint)pt).preciseX;
		preciseY = ((PrecisionPoint)pt).preciseY;
	} else {
		preciseX = pt.x;
		preciseY = pt.y;
	}
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);
	return this;
}

/**
 * Updates the integer fields using the precise versions.
 */
public final void updateInts() {
	x = (int)Math.floor(preciseX + 0.000000001);
	y = (int)Math.floor(preciseY + 0.000000001);
}

}
