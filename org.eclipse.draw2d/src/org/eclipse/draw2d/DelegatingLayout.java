/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Figures using a DelegatingLayout as their layout manager give location
 * responsibilities to their children. The children of a Figure using a
 * DelegatingLayout should have a {@link Locator Locator} as a constraint whose
 * {@link Locator#relocate(IFigure target) relocate} method is responsible for
 * placing the child.
 */
public class DelegatingLayout extends AbstractConstraintLayout {

	/**
	 * Calculates the preferred size of the given Figure. For the DelegatingLayout,
	 * this is the largest width and height values of the passed Figure's children.
	 *
	 * @param parent the figure whose preferred size is being calculated
	 * @param wHint  the width hint
	 * @param hHint  the height hint
	 * @return the preferred size
	 * @since 2.0
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure parent, int wHint, int hHint) {
		Dimension d = new Dimension();
		parent.getChildren().forEach(child -> d.union(child.getPreferredSize()));
		return d;
	}

	/**
	 * Lays out the given figure's children based on their {@link Locator}
	 * constraint.
	 *
	 * @param parent the figure whose children should be layed out
	 */
	@Override
	public void layout(IFigure parent) {
		for (IFigure child : parent.getChildren()) {
			Locator locator = (Locator) getConstraint(child);
			if (locator != null) {
				locator.relocate(child);
			}
		}
	}

	/**
	 * Removes the locator for the given figure.
	 *
	 * @param child the child being removed
	 */
	@Override
	public void remove(IFigure child) {
		constraints.remove(child);
	}

}
