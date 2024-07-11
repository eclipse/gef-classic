/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria,
 *                      BC, Canada and others.
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
import org.eclipse.zest.layouts.interfaces.NodeLayout;

/**
 * This represents a single entity, providing the layout algorithms with a
 * common interface to run on.
 *
 * @author Casey Best
 * @author Ian Bull
 * @author Chris Bennett
 * @deprecated Use {@link NodeLayout} instead. This interface will be removed in
 *             a future release
 * @noextend This interface is not intended to be extended by clients.
 * @noreference This interface is not intended to be referenced by clients..
 * @noimplement This interface is not intended to be implemented by clients.
 */
@Deprecated(since = "2.0", forRemoval = true)
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
