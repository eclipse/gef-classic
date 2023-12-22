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
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Figures using the StackLayout as their layout manager have their children
 * placed on top of one another. Order of placement is determined by the order
 * in which the children were added, first child added placed on the bottom.
 */
public class StackLayout extends AbstractHintLayout {

	/**
	 * Returns the minimum size required by the input container. This is the size of
	 * the largest child of the container, as all other children fit into this size.
	 *
	 * @see AbstractHintLayout#calculateMinimumSize(IFigure, int, int)
	 */
	@Override
	protected Dimension calculateMinimumSize(IFigure figure, int wHint, int hHint) {
		if (wHint > -1) {
			wHint = Math.max(0, wHint - figure.getInsets().getWidth());
		}
		if (hHint > -1) {
			hHint = Math.max(0, hHint - figure.getInsets().getHeight());
		}
		Dimension d = new Dimension();
		for (IFigure child : figure.getChildren()) {
			if (!isObservingVisibility() || child.isVisible()) {
				d.union(child.getMinimumSize(wHint, hHint));
			}
		}

		d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
		d.union(getBorderPreferredSize(figure));
		return d;

	}

	/**
	 * Calculates and returns the preferred size of the given figure. This is the
	 * union of the preferred sizes of the widest and the tallest of all its
	 * children.
	 *
	 * @see AbstractLayout#calculatePreferredSize(IFigure, int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure figure, int wHint, int hHint) {
		if (wHint > -1) {
			wHint = Math.max(0, wHint - figure.getInsets().getWidth());
		}
		if (hHint > -1) {
			hHint = Math.max(0, hHint - figure.getInsets().getHeight());
		}
		Dimension d = new Dimension();
		for (IFigure child : figure.getChildren()) {
			if (!isObservingVisibility() || child.isVisible()) {
				d.union(child.getPreferredSize(wHint, hHint));
			}
		}

		d.expand(figure.getInsets().getWidth(), figure.getInsets().getHeight());
		d.union(getBorderPreferredSize(figure));
		return d;
	}

	/**
	 * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
	 */
	@Override
	public void layout(IFigure figure) {
		Rectangle r = figure.getClientArea();
		figure.getChildren().forEach(child -> child.setBounds(r));
	}

}
