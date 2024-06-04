/*******************************************************************************
 * Copyright (c) 2009-2010, 2024 Mateusz Matela and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Mateusz Matela - initial API and implementation
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

/**
 * Layout algorithm that places all elements in one column or one row, depending
 * on set orientation.
 *
 * @since 2.0
 */
public class BoxLayoutAlgorithm extends GridLayoutAlgorithm {

	public static final int HORIZONTAL = 1;

	public static final int VERTICAL = 2;

	private int orientation = HORIZONTAL;

	public BoxLayoutAlgorithm() {
	}

	public BoxLayoutAlgorithm(int orientation) {
		setOrientation(orientation);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		if ((orientation != HORIZONTAL) && (orientation != VERTICAL)) {
			throw new RuntimeException("Invalid orientation: " + orientation);
		}
		this.orientation = orientation;
	}

	@Override
	protected int[] calculateNumberOfRowsAndCols(int numChildren, double boundX, double boundY, double boundWidth,
			double boundHeight) {
		if (orientation == HORIZONTAL) {
			return new int[] { numChildren, 1 };
		}
		return new int[] { 1, numChildren };
	}
}
