/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
 * A Utilities class for geometry operations.
 * @author Pratik Shah
 * @since 3.1
 */
public class Geometry
{

/**
 * Determines whether the two line segments formed by the given coordinates intersect.  If
 * one of the two line segments starts or ends on the other line, then they are considered
 * to be intersecting.
 * 
 * @param start1x x coordinate of starting point of line 1
 * @param start1y y coordinate of starting point of line 1
 * @param end1x x coordinate of ending point of line 1
 * @param end1y y coordinate of endpoing point of line 1
 * @param start2x x coordinate of the starting point of line 2
 * @param start2y y coordinate of the starting point of line 2
 * @param end2x x coordinate of the ending point of line 2
 * @param end2y y coordinate of the ending point of line 2
 * @return <code>true</code> if the two line segments formed by the given coordinates 
 *         cross
 * @since 3.1
 */
public static boolean linesIntersect(int start1x, int start1y, int end1x, int end1y, 
		int start2x, int start2y, int end2x, int end2y) {
	/*
	 * Given the segments: start1-------end1. start2-------end2. If start2->end2 is inside 
	 * the triangle start1-end1-start2, then check whether the line start1->end1 splits 
	 * the line start2->end2.
	 */
	int deltaS1S2x = start1x - start2x;
	int deltaS1S2y = start1y - start2y;
	int deltaE1S2x = end1x - start2x;
	int deltaE1S2y = end1y - start2y;
	int deltaS2E2x = start2x - end2x;
	int deltaS2E2y = start2y - end2y;
	long product = cross(deltaE1S2x, deltaE1S2y, deltaS2E2x, deltaS2E2y)
			* cross(deltaS2E2x, deltaS2E2y, deltaS1S2x, deltaS1S2y);
	if (product >= 0) {
		int deltaE1S1x = end1x - start1x;
		int deltaE1S1y = end1y - start1y;
		int deltaS1E2x = start1x - end2x;
		int deltaS1E2y = start1y - end2y;
		product = cross(-deltaS1S2x, -deltaS1S2y, deltaE1S1x, deltaE1S1y)
				* cross(deltaE1S1x, deltaE1S1y, deltaS1E2x, deltaS1E2y);
		boolean intersects = product <= 0;
		return intersects;
	}
	return false;
}

private static long cross(int x1, int y1, int x2, int y2) {
	return x1 * y2 - x2 * y1;
}

}