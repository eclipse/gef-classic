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
 * @param ux x coordinate of starting point of line 1
 * @param uy y coordinate of starting point of line 1
 * @param vx x coordinate of ending point of line 1
 * @param vy y coordinate of endpoing point of line 1
 * @param sx x coordinate of the starting point of line 2
 * @param sy y coordinate of the starting point of line 2
 * @param tx x coordinate of the ending point of line 2
 * @param ty y coordinate of the ending point of line 2
 * @return <code>true</code> if the two line segments formed by the given coordinates 
 *         cross
 * @since 3.1
 */
public static boolean linesIntersect(int ux, int uy, int vx, int vy, 
		int sx, int sy, int tx, int ty) {
	/*
	 * Given the segments: u-------v. s-------t. If s->t is inside the triangle u-v-s, 
	 * then check whether the line u->u splits the line s->t.
	 */
	int usX = ux - sx;
	int usY = uy - sy;
	int vsX = vx - sx;
	int vsY = vy - sy;
	int stX = sx - tx;
	int stY = sy - ty;
	long product = cross(vsX, vsY, stX, stY) * cross(stX, stY, usX, usY);
	if (product >= 0) {
		int vuX = vx - ux;
		int vuY = vy - uy;
		int utX = ux - tx;
		int utY = uy - ty;
		product = cross(-usX, -usY, vuX, vuY) * cross(vuX, vuY, utX, utY);
		boolean intersects = product <= 0;
		return intersects;
	}
	return false;
}

private static long cross(int x1, int y1, int x2, int y2) {
	return x1 * y2 - x2 * y1;
}

}