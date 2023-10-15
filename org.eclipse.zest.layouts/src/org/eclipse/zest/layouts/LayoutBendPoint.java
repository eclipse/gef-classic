/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
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

/**
 * Specifies a single bend point in a graph relationship.
 *
 * @author Ian Bull
 * @author Chris Bennett
 */
public interface LayoutBendPoint {
	public double getX();

	public double getY();

	public boolean getIsControlPoint();
}
