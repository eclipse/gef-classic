/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alexander Shatalin (Borland) - Contribution for Bug 238874
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

	/**
	 * @see PointList#polylineContainsPoint(int, int, int)
	 * @since 3.5
	 */
	public static boolean polylineContainsPoint(PointList points, int x, int y, int tolerance) {
		int coordinates[] = points.toIntArray();
		/*
		 * For each segment of PolyLine calling isSegmentPoint
		 */
		for (int index = 0; index < coordinates.length - 3; index  += 2) {
			if (segmentContainsPoint(coordinates[index], coordinates[index + 1], coordinates[index + 2], coordinates[index + 3], x, y, tolerance)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if the least distance between point (px,py) and segment
	 * 	(x1,y1) - (x2,y2) is less then specified tolerance
	 */
	private static boolean segmentContainsPoint(int x1, int y1, int x2, int y2, int px, int py, int tolerance) {
		/*
		 * Point should be located inside Rectangle(x1 -+ tolerance, y1 -+
		 * tolerance, x2 +- tolerance, y2 +- tolerance)
		 */
		Rectangle lineBounds = Rectangle.SINGLETON;
		lineBounds.setSize(0, 0);
		lineBounds.setLocation(x1, y1);
		lineBounds.union(x2, y2);
		lineBounds.expand(tolerance, tolerance);
		if (!lineBounds.contains(px, py)) {
			return false;
		}

		/*
		 * If this is horizontal, vertical line or dot then the distance between
		 * specified point and segment is not more then tolerance (due to the
		 * lineBounds check above)
		 */
		if (x1 == x2 || y1 == y2) {
			return true;
		}

		/*
		 * Calculating square distance from specified point to this segment
		 * using formula for Dot product of two vectors.
		 */
		int v1x = x2 - x1;
		int v1y = y2 - y1;
		int v2x = px - x1;
		int v2y = py - y1;
		int numerator = v2x * v1y - v1x * v2y;
		int denominator = v1x * v1x + v1y * v1y;
		int squareDistance = (int) ((long) numerator * numerator / denominator);
		return squareDistance <= tolerance * tolerance;
	}

	/**
	 * One simple way of finding whether the point is inside or outside a simple
	 * polygon is to test how many times a ray starting from the point
	 * intersects the edges of the polygon. If the point in question is not on
	 * the boundary of the polygon, the number of intersections is an even
	 * number if the point is outside, and it is odd if inside.
	 * 
	 * @see PointList#polygonContainsPoint(int, int)
	 * @since 3.5
	 */
	public static boolean polygonContainsPoint(PointList points, int x, int y) {
		boolean isOdd = false;
		int coordinates[] = points.toIntArray();
		int n = coordinates.length;
		if (n > 3) { // If there are at least 2 Points (4 ints)
			int x1, y1;
			int x0 = coordinates[n - 2];
			int y0 = coordinates[n - 1];

			for (int i = 0; i < n; x0 = x1, y0 = y1) {
				x1 = coordinates[i++];
				y1 = coordinates[i++];
				if (!segmentContaintPoint(y0, y1, y)) {
					// Current edge has no intersection with the point by Y
					// coordinates
					continue;
				}
				int crossProduct = crossProduct(x1, y1, x0, y0, x, y);
				if (crossProduct == 0) {
					// cross product == 0 only if this point is on the line
					// containing selected edge
					if (segmentContaintPoint(x0, x1, x)) {
						// This point is on the edge
						return true;
					}
					// This point is outside the edge - simply skipping possible
					// intersection (no parity changes)
				} else if ((y0 <= y && y < y1 && crossProduct > 0) || (y1 <= y && y < y0 && crossProduct < 0)) {
					// has intersection
					isOdd = !isOdd;
				}
			}
			return isOdd;
		}
		return false;
	}
	
	/**
	 * @return true if segment with two ends x0, x1 contains point x 
	 */
	private static boolean segmentContaintPoint(int x0, int x1, int x) {
		return !((x < x0 && x < x1) || (x > x0 && x > x1));
	}
	
	/**
	 * Calculating cross product of two vectors:
	 * 1. [ax - cx, ay - cx]
	 * 2. [bx - cx, by - cy]
	 */
	private static int crossProduct(int ax, int ay, int bx, int by, int cx, int cy) {
		return (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
	}

}