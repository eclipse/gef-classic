/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Vector;

/**
 * Automatic router that spreads its {@link Connection Connections} in a
 * fan-like fashion upon collision.
 */
public class FanRouter extends AutomaticRouter {

	private int separation = 10;

	/**
	 * Returns the separation in pixels between fanned connections.
	 *
	 * @return the separation
	 * @since 2.0
	 */
	public int getSeparation() {
		return separation;
	}

	/**
	 * Modifies a given PointList that collides with some other PointList. The given
	 * <i>index</i> indicates that this it the i<sup>th</sup> PointList in a group
	 * of colliding points.
	 *
	 * @param points the colliding points
	 * @param index  the index
	 */
	@Override
	protected void handleCollision(PointList points, int index) {
		Point start = points.getFirstPoint();
		Point end = points.getLastPoint();

		if (start.equals(end)) {
			return;
		}

		Point midPoint = new Point((end.x + start.x) / 2, (end.y + start.y) / 2);
		int position = end.getPosition(start);
		Vector ray;
		if (position == PositionConstants.SOUTH || position == PositionConstants.EAST) {
			ray = new Vector(start, end);
		} else {
			ray = new Vector(end, start);
		}
		double length = ray.getLength();

		double xSeparation = separation * ray.x / length * (index / 2);
		double ySeparation = separation * ray.y / length * (index / 2);

		Point bendPoint;

		if (index % 2 == 0) {
			bendPoint = new PrecisionPoint(midPoint.x + (-1 * ySeparation), midPoint.y + xSeparation);
		} else {
			bendPoint = new PrecisionPoint(midPoint.x + ySeparation, midPoint.y + (-1 * xSeparation));
		}
		if (!bendPoint.equals(midPoint)) {
			points.insertPoint(bendPoint, 1);
		}
	}

	/**
	 * Sets the colliding {@link Connection Connection's} separation in pixels.
	 *
	 * @param value the separation
	 * @since 2.0
	 */
	public void setSeparation(int value) {
		separation = value;
	}

}
