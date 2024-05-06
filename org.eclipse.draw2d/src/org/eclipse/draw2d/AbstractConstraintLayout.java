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
 *     Alois Zoitl     - extracted from XYLayout
 *******************************************************************************/

package org.eclipse.draw2d;

import java.util.HashMap;
import java.util.Map;

/**
 * Common base class for layouts working on a constraint per figure.
 *
 * @since 3.16
 */
public abstract class AbstractConstraintLayout extends AbstractLayout {

	/** The layout constraints */
	protected Map<IFigure, Object> constraints = new HashMap<>();

	protected AbstractConstraintLayout() {
		// protected constructor to better indicate that the class can not be
		// instantiated
	}

	/**
	 * @see LayoutManager#getConstraint(IFigure)
	 */
	@Override
	public Object getConstraint(IFigure figure) {
		return constraints.get(figure);
	}

	/**
	 * @see LayoutManager#remove(IFigure)
	 */
	@Override
	public void remove(IFigure figure) {
		super.remove(figure);
		constraints.remove(figure);
	}

	/**
	 * Sets the layout constraint of the given figure.
	 *
	 * @see LayoutManager#setConstraint(IFigure, Object)
	 * @since 2.0
	 */
	@Override
	public void setConstraint(IFigure figure, Object newConstraint) {
		super.setConstraint(figure, newConstraint);
		if (newConstraint != null) {
			constraints.put(figure, newConstraint);
		}
	}

}