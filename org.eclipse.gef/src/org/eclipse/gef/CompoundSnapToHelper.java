/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

/**
 * Combines multiple SnapToHelpers into one compound helper.  The compound helper
 * deletages to multiple other helpers.
 * @author Pratik Shah
 */
public class CompoundSnapToHelper 
	extends SnapToHelper 
{

private SnapToHelper[] delegates;

/**
 * Constructs a compound snap to helper which will delegate to the provided array of
 * helpers.  The first helper in the array has highest priority and will be given the
 * first opportunity to perform snapping.
 * @since 3.0
 * @param delegates an array of helpers
 */
public CompoundSnapToHelper(SnapToHelper delegates[]) {
	Assert.isTrue(delegates.length != 0);
	this.delegates = delegates;
}

/**
 * @see SnapToHelper#snapRectangle(Request, int, PrecisionRectangle, PrecisionRectangle)
 */
public int snapRectangle(Request request, int snapOrientation, 
		PrecisionRectangle baseRect, PrecisionRectangle result) {
	for (int i = 0; i < delegates.length && snapOrientation != NONE; i++)
		snapOrientation = delegates[i].snapRectangle(request, snapOrientation,
				baseRect, result);
	return snapOrientation;
}

}