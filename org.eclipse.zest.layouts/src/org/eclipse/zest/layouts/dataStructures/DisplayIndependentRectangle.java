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
 * This is a rectangle that isn't dependent on awt, swt, or any other library,
 * except layout.
 *
 * @author Casey Best
 */
public class DisplayIndependentRectangle {

	public double x, y, width, height;

	public DisplayIndependentRectangle() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	public DisplayIndependentRectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public DisplayIndependentRectangle(DisplayIndependentRectangle rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
	}

	@Override
	public String toString() {
		return "(%d, %d, %d, %d)".formatted(x, y, width, height); //$NON-NLS-1$
	}

	public boolean intersects(DisplayIndependentRectangle rect) {
		return rect.x < x + width && rect.y < y + height && rect.x + rect.width > x && rect.y + rect.height > y;
	}
}
