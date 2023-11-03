/*******************************************************************************
 * Copyright 2005 CHISEL Group, University of Victoria, Victoria, BC,
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
package org.eclipse.zest.layouts;

import org.eclipse.zest.layouts.constraints.LayoutConstraint;

/**
 * This represents a single entity, providing the layout algorithms with a
 * common interface to run on.
 *
 * @author Casey Best
 * @author Ian Bull
 * @author Chris Bennett
 */
public interface LayoutEntity extends Comparable, LayoutItem {

	public final static String ATTR_PREFERRED_WIDTH = "tree-preferred-width";
	public final static String ATTR_PREFERRED_HEIGHT = "tree-preferred-height";

	public void setLocationInLayout(double x, double y);

	public void setSizeInLayout(double width, double height);

	public double getXInLayout();

	public double getYInLayout();

	public double getWidthInLayout();

	public double getHeightInLayout();

	public Object getLayoutInformation();

	public void setLayoutInformation(Object internalEntity);

	/**
	 * Classes should update the specified layout constraint if recognized
	 *
	 * @return
	 */
	public void populateLayoutConstraint(LayoutConstraint constraint);
}
