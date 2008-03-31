/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
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
	/* Values are casted to long to avoid integer overflows */
	long usX = (long) ux - sx;
	long usY = (long) uy - sy;
	long vsX = (long) vx - sx;
	long vsY = (long) vy - sy;
	long stX = (long) sx - tx;
	long stY = (long) sy - ty;
	if (productSign(cross(vsX, vsY, stX, stY), cross(stX, stY, usX, usY)) >= 0) {
		long vuX = (long) vx - ux;
		long vuY = (long) vy - uy;
		long utX = (long) ux - tx;
		long utY = (long) uy - ty;
		return productSign(cross(-usX, -usY, vuX, vuY), cross(vuX, vuY, utX, utY)) <= 0;
	}
	return false;
}

private static int productSign(long x, long y) {
	if (x == 0 || y == 0) {
		return 0;
	} else if (x < 0 ^ y < 0) {
		return -1;
	}
	return 1;
}

private static long cross(long x1, long y1, long x2, long y2) {
	return x1 * y2 - x2 * y1;
}

}