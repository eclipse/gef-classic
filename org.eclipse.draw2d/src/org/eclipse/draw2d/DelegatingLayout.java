/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Figures using a DelegatingLayout as their layout manager give 
 * location responsibilities to their children. The children
 * of a Figure using a DelegatingLayout should have a 
 * {@link Locator Locator} as a constraint whose 
 * {@link Locator#relocate(IFigure target) relocate} method is 
 * responsible for placing the child.
 */
public class DelegatingLayout 
	extends AbstractLayout 
{

private Map constraints = new HashMap();

/**
 * Calculates the preferred size of the given Figure.
 * For the DelegatingLayout, this is the largest width and height
 * values of the passed Figure's children.
 * 
 * @since 2.0
 */
protected Dimension calculatePreferredSize(IFigure parent, int wHint, int hHint) {
	List children = parent.getChildren();
	Dimension d = new Dimension();
	for (int i=0; i<children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		d.union(child.getPreferredSize());
	}
	return d;
}

public void layout(IFigure parent) {

	List children = parent.getChildren();
	for (int i=0; i<children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		Locator locator = (Locator)constraints.get(child);
		if (locator != null) {
			locator.relocate(child);
		}
	}
}

public void setConstraint(IFigure figure, Object constraint) {
	super.setConstraint(figure, constraint);
	if (constraint != null)
		constraints.put(figure, constraint);
}

}