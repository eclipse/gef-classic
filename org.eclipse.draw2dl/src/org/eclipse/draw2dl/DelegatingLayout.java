/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2dl.geometry.Dimension;

/**
 * Figures using a DelegatingLayout as their layout manager give location
 * responsibilities to their children. The children of a Figure using a
 * DelegatingLayout should have a {@link org.eclipse.draw2dl.Locator Locator} as a constraint whose
 * {@link org.eclipse.draw2dl.Locator#relocate(org.eclipse.draw2dl.IFigure target) relocate} method is responsible for
 * placing the child.
 */
public class DelegatingLayout extends AbstractLayout {

	private Map<IFigure, Object> constraints = new HashMap<>();

	/**
	 * Calculates the preferred size of the given Figure. For the
	 * DelegatingLayout, this is the largest width and height values of the
	 * passed Figure's children.
	 * 
	 * @param parent
	 *            the figure whose preferred size is being calculated
	 * @param wHint
	 *            the width hint
	 * @param hHint
	 *            the height hint
	 * @return the preferred size
	 * @since 2.0
	 */
	protected Dimension calculatePreferredSize(org.eclipse.draw2dl.IFigure parent, int wHint,
                                               int hHint) {
		List<IFigure> children = parent.getChildren();
		Dimension d = new Dimension();
		for (IFigure child : children) {
			d.union(child.getPreferredSize());
		}
		return d;
	}

	/**
	 * @see LayoutManager#getConstraint(org.eclipse.draw2dl.IFigure)
	 */
	public Object getConstraint(org.eclipse.draw2dl.IFigure child) {
		return constraints.get(child);
	}

	/**
	 * Lays out the given figure's children based on their {@link org.eclipse.draw2dl.Locator}
	 * constraint.
	 * 
	 * @param parent
	 *            the figure whose children should be layed out
	 */
	public void layout(org.eclipse.draw2dl.IFigure parent) {
		List<IFigure> children = parent.getChildren();
		for (IFigure child : children) {
			Locator locator = (Locator) constraints.get(child);
			if (locator != null) {
				locator.relocate(child);
			}
		}
	}

	/**
	 * Removes the locator for the given figure.
	 * 
	 * @param child
	 *            the child being removed
	 */
	public void remove(org.eclipse.draw2dl.IFigure child) {
		constraints.remove(child);
	}

	/**
	 * Sets the constraint for the given figure.
	 * 
	 * @param figure
	 *            the figure whose contraint is being set
	 * @param constraint
	 *            the new constraint
	 */
	public void setConstraint(IFigure figure, Object constraint) {
		super.setConstraint(figure, constraint);
		if (constraint != null)
			constraints.put(figure, constraint);
	}

}
