/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Shatalin (Borland) - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.Geometry;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.PointList;

/**
 * Renders a {@link PointList} as a polygonal shape.
 * This class is similar to {@link org.eclipse.draw2dl.PolylineShape}, except the
 * {@link PointList} is closed and can be filled in
 * as a solid shape.
 * 
 * @see PolylineShape
 * @since 3.5
 */
public class PolygonShape extends AbstractPointListShape {

	protected boolean shapeContainsPoint(int x, int y) {
		Point location = getLocation();
		return Geometry.polygonContainsPoint(points, x - location.x, y
				- location.y);
	}

	protected void fillShape(org.eclipse.draw2dl.Graphics graphics) {
		graphics.pushState();
		graphics.translate(getLocation());
		graphics.fillPolygon(getPoints());
		graphics.popState();
	}

	protected void outlineShape(Graphics graphics) {
		graphics.pushState();
		graphics.translate(getLocation());
		graphics.drawPolygon(getPoints());
		graphics.popState();
	}

}
