package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.*;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * @author Randy Hudson, Pratik Shah
 */
class DrawerAnimationController {


private long startTime = System.currentTimeMillis();
private long endTime = 0;
private static int numberOfMilliSeconds = 150;
private boolean inProgress;
private List drawers = new ArrayList();
private DrawerFigure[] animate;

private int autoCollapseMode;
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
	drawer.getDrawerFigure().setController(this);
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
	} else {
		animate = new DrawerFigure[] {drawer.getDrawerFigure()};
	}

	for (int i = 0; i < animate.length; i++)
		animate[i].setAnimating(true);
	start();
	runAnimation();
	for (int i = 0; i < animate.length; i++)
		animate[i].setAnimating(false);
	animate = null;
}

void runAnimation() {
	while (inProgress)
		step();
	step();
}

void step() {
	for (int i = 0; i < animate.length; i++) {
		animate[i].revalidate();
	}
	animate[0].getUpdateManager().performUpdate();
	if (System.currentTimeMillis() > endTime)
		inProgress = false;
}

/**
 * Returns value of current position (between 0.0 and 1.0).
 */
public float getAnimationProgress() {
	long presentTime = System.currentTimeMillis();
	if (presentTime > endTime)
		return (float)1.0;
	long timePassed = (presentTime - startTime);
	float progress = ((float)timePassed) / ((float)numberOfMilliSeconds);
	return progress;
}

public boolean isAnimationInProgress() {
	return inProgress;
}

public void removeDrawer(DrawerEditPart drawer) {
	drawer.getDrawerFigure().setController(null);
	drawers.remove(drawer);
}

public void start() {
	inProgress = true;
	startTime = new Date().getTime();
	endTime = startTime + numberOfMilliSeconds;
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

}
