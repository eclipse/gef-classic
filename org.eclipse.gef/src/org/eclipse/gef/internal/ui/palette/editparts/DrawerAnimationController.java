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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * @author Randy Hudson, Pratik Shah
 */
class DrawerAnimationController {

private long startTime = System.currentTimeMillis();
private long endTime = 0, presentTime = 0;
private static final int NUM_OF_MILLISECONDS = 150;
private boolean inProgress;
private boolean recording;
private List drawers = new ArrayList();
private DrawerFigure[] animate;

private PaletteViewerPreferences prefs;

/**
 * Constructor
 */
public DrawerAnimationController(PaletteViewerPreferences prefs) {
	super();
	this.prefs = prefs;
}

public void addDrawer(DrawerEditPart drawer) {
	drawers.add(drawer);
}

public void animate(DrawerEditPart drawer) {
	inProgress = true;
	if (drawer.getDrawerFigure().isExpanded()) {
		List categoriesToCollapse = getDrawersToCollapse(drawer);
		animate = new DrawerFigure[categoriesToCollapse.size() + 1];
		int count = 1;
		for (Iterator iter = categoriesToCollapse.iterator(); iter.hasNext();) {
			DrawerEditPart drwr = (DrawerEditPart) iter.next();
			drwr.setExpanded(false);
			animate[count++] = drwr.getDrawerFigure();
		}
		animate[0] = drawer.getDrawerFigure();
	} else
		animate = new DrawerFigure[] {drawer.getDrawerFigure()};
	

	for (int i = 0; i < animate.length; i++)
		animate[i].revalidate();
	
	recording = true;
	FigureUtilities.getRoot(animate[0]).validate();
	recording = false;
	
	for (int i = 0; i < animate.length; i++)
		animate[i].setAnimating(true);
	
	start();
	runAnimation();
	for (int i = 0; i < animate.length; i++)
		animate[i].setAnimating(false);
	
	animate[0].getUpdateManager().performUpdate();
	
	animate = null;
}

void runAnimation() {
	while (inProgress)
		step();
	step();
}

void step() {
	presentTime = System.currentTimeMillis();
	if (presentTime > endTime) {
		inProgress = false;
		return;
	}
	
	for (int i = 0; i < animate.length; i++)
		animate[i].revalidate();
	animate[0].getUpdateManager().performUpdate();
}

/**
 * Returns value of current position (between 0.0 and 1.0).
 */
public float getAnimationProgress() {
	if (presentTime > endTime)
		return (float)1.0;
	long timePassed = (presentTime - startTime);
	float progress = ((float)timePassed) / NUM_OF_MILLISECONDS;
	return progress;
}

public int getNumberOfOpenDrawers() {
	int count = 0;
	for (Iterator iter = drawers.iterator(); iter.hasNext();) {
		DrawerEditPart part = (DrawerEditPart) iter.next();
		if (part.isExpanded()) {
			count++;
		}
	}
	return count;
}

public boolean isAnimationInProgress() {
	return inProgress;
}

public void removeDrawer(DrawerEditPart drawer) {
	drawers.remove(drawer);
}

public void start() {
	inProgress = true;
	startTime = System.currentTimeMillis();
	presentTime = startTime;
	endTime = startTime + NUM_OF_MILLISECONDS;
}

protected List getDrawersToCollapse(DrawerEditPart drawer) {
	int autoCollapseMode = prefs.getAutoCollapseSetting();
	
	// Collapse never
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_NEVER) {
		return Collections.EMPTY_LIST;
	} 
	
	// Collapse always
	List drawersToCollapse = new ArrayList();
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_ALWAYS) {
		for (Iterator iter = drawers.iterator(); iter.hasNext();) {
			DrawerEditPart cat = (DrawerEditPart) iter.next();
			if (cat.isExpanded() && cat != drawer) {
				drawersToCollapse.add(cat);
			}
		}
		return drawersToCollapse;
	}
	
	// Collapse as needed
	List potentialDrawersToCollapse = new ArrayList();
	DrawerFigure catFigure = drawer.getDrawerFigure();
	int availableWidth = catFigure.getParent().getClientArea().width;
	int availableHeight = catFigure.getParent().getSize().height;
	int requiredHeight = 0;
	for (Iterator iter = drawer.getParent().getChildren().iterator(); iter.hasNext();) {
		PaletteEditPart part = (PaletteEditPart) iter.next();
		IFigure fig = part.getFigure();
		int height = fig.getPreferredSize(availableWidth, -1).height;
		requiredHeight += height;
		if (!(part instanceof DrawerEditPart)) {
			continue;
		}
		DrawerFigure figure = (DrawerFigure)fig;
		if (figure.isExpanded() && !figure.isPinnedOpen()) {
			potentialDrawersToCollapse.add(part);
		}
	}
	for (int i = potentialDrawersToCollapse.size() - 1; i >= 0
				&& requiredHeight > availableHeight; i--) {
		DrawerEditPart part = (DrawerEditPart)potentialDrawersToCollapse.get(i);
		if (part == drawer) {
			continue;
		}
		int expandedHeight = part.getFigure().getPreferredSize(availableWidth, -1).height;
		part.setExpanded(false);
		int collapsedHeight = part.getFigure().getPreferredSize(availableWidth, -1).height;
		requiredHeight -= (expandedHeight - collapsedHeight);
		drawersToCollapse.add(part);
	}
	return drawersToCollapse;
}

/**
 * @since 3.0
 * @return
 */
public boolean isRecording() {
	return recording;
}

}
