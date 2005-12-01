/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * @author Randy Hudson, Pratik Shah
 */
public class PaletteAnimator extends LayoutAnimator {

private List drawers = new ArrayList();
private PaletteViewerPreferences prefs;

/**
 * Constructor
 */
public PaletteAnimator(PaletteViewerPreferences prefs) {
	this.prefs = prefs;
}

public void addDrawer(DrawerEditPart drawer) {
	drawers.add(drawer.getFigure());
}

protected void autoCollapse(DrawerFigure openDrawer) {
	int autoCollapseMode = prefs.getAutoCollapseSetting();
	
	// Collapse never
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_NEVER)
		return;
	
	DrawerFigure drawer;

	// Collapse always
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_ALWAYS) {
		for (Iterator iter = drawers.iterator(); iter.hasNext();) {
			drawer = (DrawerFigure) iter.next();
			if (drawer != openDrawer)
				drawer.setExpanded(false);
		}
		return;
	}
	
	// Collapse as needed
	int wHint = openDrawer.getParent().getClientArea().width;
	int availableHeight = openDrawer.getParent().getSize().height;
	int requiredHeight = 0;
	List closable = new ArrayList();
	for (Iterator iter = openDrawer.getParent().getChildren().iterator(); iter.hasNext();) {
		IFigure sibling = (IFigure) iter.next();
		int height = sibling.getPreferredSize(wHint, -1).height;
		requiredHeight += height;
		if (!(sibling instanceof DrawerFigure) || sibling == openDrawer)
			continue;
		drawer = (DrawerFigure)sibling;
		if (drawer.isExpanded() && !drawer.isPinnedOpen())
			closable.add(drawer);
	}
	
	//Start closing until requiredHeight <= available
	for (int i = closable.size() - 1; i >= 0 && requiredHeight > availableHeight; i--) {
		drawer = (DrawerFigure)closable.get(i);
		int expandedHeight = drawer.getPreferredSize(wHint, -1).height;
		drawer.setExpanded(false);
		int collapsedHeight = drawer.getPreferredSize(wHint, -1).height;
		requiredHeight -= (expandedHeight - collapsedHeight);
	}
}

public void playbackStarting(IFigure figure) {
	if (figure instanceof DrawerFigure)
		((DrawerFigure)figure).setAnimating(true);
}

public void removeDrawer(DrawerEditPart drawer) {
	drawers.remove(drawer.getFigure());
}

public void init(IFigure figure) {
	if (figure instanceof DrawerFigure) {
		DrawerFigure drawer = (DrawerFigure) figure;
		if (drawer.isExpanded())
			autoCollapse(drawer);
		return;
	}
	super.init(figure);
}

public void tearDown(IFigure figure) {
	if (figure instanceof DrawerFigure)
		((DrawerFigure) figure).setAnimating(false);
}

}
