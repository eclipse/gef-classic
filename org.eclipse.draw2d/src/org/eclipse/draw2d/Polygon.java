/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.List;

/**
 * Renders a {@link org.eclipse.draw2d.geometry.PointList} as a polygonal shape.
 * This class is similar to Polyline, except the PointList is closed and can be filled in
 * as a solid shape.
 * @see Polyline
 */
public class Polygon
	extends Polyline
{

/**
 * Returns whether the point (x,y) is contained inside this polygon.
 * @param x the X coordinate
 * @param y the Y coordinate
 * @return whether the point (x,y) is contained in this polygon
 */
public boolean containsPoint(int x, int y) {
	if (!getBounds().contains(x, y))
		return false;

	boolean isOdd = false;
	int[] pointsxy = getPoints().toIntArray();
	int n = pointsxy.length;
	if (n > 3) { //If there are at least 2 Points (4 ints)
		int x1, y1;
		int x0 = pointsxy[n - 2];
		int y0 = pointsxy[n - 1];

		for (int i = 0; i < n; x0 = x1, y0 = y1) {
			x1 = pointsxy[i++];
			y1 = pointsxy[i++];
			
			if (y0 <= y && y < y1
			  && crossProduct(x1, y1, x0, y0, x, y) > 0)
			  	isOdd = !isOdd;
			if (y1 <= y && y < y0
			  && crossProduct(x0, y0, x1, y1, x, y) > 0)
			  	isOdd = !isOdd;
		}
		if (isOdd)
			return true;
	}

	List children = getChildren();
	for (int i = 0; i < children.size(); i++)
		if (((IFigure) children.get(i)).containsPoint(x, y))
			return true;

	return false;
}

private int crossProduct(int ax, int ay, int bx, int by, int cx, int cy) {
	return (ax - cx) * (by - cy) - (ay - cy) * (bx - cx);
}

/**
 * Fill the Polygon with the background color set by <i>g</i>.
 * 
 * @param g the Graphics object
 * @since 2.0
 */
protected void fillShape(Graphics g) {
	g.fillPolygon(getPoints());
}

/**
 * Draw the outline of the Polygon.
 * 
 * @param g the Graphics object
 * @since 2.0
 */
protected void outlineShape(Graphics g) {
	g.drawPolygon(getPoints());
}

}