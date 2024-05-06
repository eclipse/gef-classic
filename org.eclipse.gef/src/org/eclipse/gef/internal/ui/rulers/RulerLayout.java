/*******************************************************************************
 * Copyright (c) 2003, 2024 IBM Corporation and others.
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
package org.eclipse.gef.internal.ui.rulers;

import org.eclipse.draw2d.AbstractConstraintLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A custom layout manager for rulers. It is not meant to be used externally or
 * with any figure other than a
 * {@link org.eclipse.gef.internal.ui.rulers.RulerFigure ruler}.
 *
 * @author Pratik Shah
 * @since 3.0
 */
public class RulerLayout extends AbstractConstraintLayout {

	/**
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure,
	 *      int, int)
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		return new Dimension(1, 1);
	}

	/**
	 * @see org.eclipse.draw2d.AbstractLayout#getConstraint(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public Integer getConstraint(IFigure child) {
		return (Integer) constraints.get(child);
	}

	/**
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public void layout(IFigure container) {
		Rectangle rulerSize = container.getClientArea();
		for (IFigure child : container.getChildren()) {
			Dimension childSize = child.getPreferredSize();

			Integer childPos = getConstraint(child);
			if (childPos == null) {
				continue;
			}

			int position = childPos.intValue();
			if (((RulerFigure) container).isHorizontal()) {
				childSize.height = rulerSize.height - 1;
				Rectangle.SINGLETON.setLocation(position - (childSize.width / 2), rulerSize.y);
			} else {
				childSize.width = rulerSize.width - 1;
				Rectangle.SINGLETON.setLocation(rulerSize.x, position - (childSize.height / 2));
			}
			Rectangle.SINGLETON.setSize(childSize);
			child.setBounds(Rectangle.SINGLETON);
		}
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only be
	 * of type {@link Integer}.
	 *
	 * @see org.eclipse.draw2d.LayoutManager#setConstraint(IFigure, Object)
	 */
	@Override
	public void setConstraint(IFigure figure, Object newConstraint) {
		if (newConstraint != null && !(newConstraint instanceof Integer)) {
			throw new IllegalArgumentException("RulerLayout was given " + newConstraint.getClass().getName() //$NON-NLS-1$
					+ " as constraint for Figure. Integer expected!"); //$NON-NLS-1$
		}
		super.setConstraint(figure, newConstraint);
	}

}
