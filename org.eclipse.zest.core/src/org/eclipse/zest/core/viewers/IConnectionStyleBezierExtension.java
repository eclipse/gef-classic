/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.viewers;

/**
 * An extension to the IConnectinStyleProvider that allows styling specific to
 * bezier curves.
 *
 * Bezier curves are defined by a set of four points: two point in the layout
 * (start and end), and two related control points (also start and end). The
 * control points are defined relative to their corresponding layout point. This
 * definition includes an angle between the layout point and the line between
 * the two layout points, as well as a ratio distance from the corresponding
 * layout point. The ratio distance is defined as a fraction between 0 and 1 of
 * the distance between the two layout points. Using this definition allows
 * bezier curves to have a consistant look regardless of the actual positions of
 * the nodes in the layouts.
 *
 * @author Del Myers
 *
 */
//@tag bug(152530-Bezier(fix)) : users can style bezier curves.
public interface IConnectionStyleBezierExtension {

	/**
	 * Gets the angle between the start point, and the line between the start and
	 * end, which will define the position of the start control point. If the start
	 * angle, and the end angle are the same sign, the two control points are
	 * guaranteed to be on the same side of the line.
	 *
	 * @param rel the relationship to base on.
	 * @return the start angle or <code>Double.NaN</code> for defaults.
	 */
	double getStartAngle(Object rel);

	/**
	 * Gets the angle between the end point, and the line between the start and end,
	 * which will define the position of the end control point. If the start angle,
	 * and the end angle are the same sign, the two control points are guaranteed to
	 * be on the same side of the line.
	 *
	 * @param rel the relationship to base on.
	 * @return the end angle or <code>Double.NaN</code> for defaults.
	 */
	double getEndAngle(Object rel);

	/**
	 * Gets the distance between the start point and the start control point, as a
	 * fraction of the distance between the start point and end point.
	 *
	 * @param rel the relationship to base on.
	 * @return the start distance or <code>Double.NaN</code> for defaults.
	 */
	double getStartDistance(Object rel);

	/**
	 * Gets the distance between the end point and the end control point, as a
	 * fraction of the distance between the start point and end point.
	 *
	 * @param rel the relationship to base on.
	 * @return the end distance or <code>Double.NaN</code> for defaults.
	 */
	double getEndDistance(Object rel);
}
