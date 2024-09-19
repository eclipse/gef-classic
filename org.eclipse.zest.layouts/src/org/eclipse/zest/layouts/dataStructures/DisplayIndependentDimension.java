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
package org.eclipse.zest.layouts.dataStructures;

/**
 * This is a dimension that isn't dependent on awt, swt, or any other library,
 * except layout.
 *
 * @author Casey Best
 */
public class DisplayIndependentDimension {
	public double width, height;

	public DisplayIndependentDimension(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public DisplayIndependentDimension(DisplayIndependentDimension dimension) {
		this.width = dimension.width;
		this.height = dimension.height;
	}

	@Override
	public String toString() {
		return "(%f, %f)".formatted(width, height); //$NON-NLS-1$
	}
}
