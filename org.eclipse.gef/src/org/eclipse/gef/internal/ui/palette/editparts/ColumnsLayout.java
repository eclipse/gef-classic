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
package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;


/**
 * Special FlowLayout to display the palette in the columns view.
 * 
 * @author Pratik Shah
 */
public class ColumnsLayout 
	extends FlowLayout 
{

private Dimension defaultConstraint = null;
private Dimension cachedConstraint = null;

/**
 * Constructs a new layout
 */
public ColumnsLayout() {
	super(true);
	setMinorSpacing(0);
	setMajorSpacing(0);
	setStretchMinorAxis(true);
	setDefaultConstraint(new Dimension(50, 50));
}

/**
 * @see org.eclipse.draw2d.FlowLayout#getChildPreferredSize(IFigure, int, int)
 */
protected Dimension getChildSize (IFigure child, int wHint, int hHint) {
	if (!(child instanceof SeparatorEditPart.SeparatorFigure)) {
		Dimension hints = getMinimumHints(child, wHint, hHint);
		int numOfColumns = (wHint + majorSpacing) / (hints.width + majorSpacing);
//		numOfColumns = Math.min(numOfColumns, maxChildrenInRowWith(child));
		if (numOfColumns == 0) {
			wHint = hints.width;
		} else {
			wHint = (wHint - ((numOfColumns - 1) * majorSpacing)) / numOfColumns;
		}
		hHint = hints.height;
	}

	return super.getChildSize(child, wHint, hHint);
}

/**
 * @see org.eclipse.draw2d.AbstractLayout#getMinimumSize(IFigure, int, int)
 */
public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
	return super.getMinimumSize(container, wHint, hHint);
}


/*
 * Returns a dimension which has a width that is the greater of the following two: the
 * default width (set on defaultConstraint), and the minimum width of the widest child.
 */
private Dimension getMinimumHints(IFigure figure, int wHint, int hHint) {
	if (cachedConstraint == null) {
		cachedConstraint = defaultConstraint.getCopy();
		List children = figure.getParent().getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			IFigure child = (IFigure) iter.next();
			Dimension childSize = child.getPreferredSize(cachedConstraint.width,
				                                       cachedConstraint.height);
			cachedConstraint.width = Math.max(cachedConstraint.width, childSize.width);
		}
		cachedConstraint.height = hHint;
	}
	return cachedConstraint;
}

/**
 * @see org.eclipse.draw2d.AbstractHintLayout#invalidate()
 */
public void invalidate() {
	super.invalidate();
	cachedConstraint = null;
}

/*
 * Returns the maximum number of children that can be put in the row that has the given
 * figure.
 */
private int maxChildrenInRowWith(IFigure figure) {
	int count = 0;
	boolean foundFigure = false;
	List children = figure.getParent().getChildren();
	for (int i = 0; i < children.size(); i++) {
		count++;
		IFigure child = (IFigure) children.get(i);
		if (child == figure) {
			foundFigure = true;
			continue;
		} else if (child instanceof SeparatorEditPart.SeparatorFigure) {
			if (foundFigure) {
				count--;
				break;
			} else {
				count = 0;
				continue;
			}
		}
	}

	if (count == 0) {
		// This should never happen
		count = children.size();
	}
	
	return count;
}

/**
 * For use by the palette
 * 
 * @param d	The constraints to be respected by the children of the figure that has this
 * 				layout; Should not be <code>null</code>.
 */
public void setDefaultConstraint(Dimension d) {
	defaultConstraint = d;
}

}
