package org.eclipse.gef.ui.palette;

import java.util.*;

/**
 * @author Randy Hudson, Pratik Shah
 */
class CategoryAnimationController {


private long startTime = System.currentTimeMillis();
private long endTime = 0;
private static int numberOfMilliSeconds = 150;
private boolean inProgress;
private List categories = new ArrayList();
private CategoryFigure[] animate;

private int autoCollapseMode;
private PaletteViewerPreferences prefs;

/**
 * Constructor for CategoryAnimationController.
 */
public CategoryAnimationController(PaletteViewerPreferences prefs) {
	super();
	this.prefs = prefs;
}

public void addCategory(CategoryEditPart category) {
	categories.add(category);
	category.getCategoryFigure().setController(this);
}

public void animate(CategoryEditPart cat) {
	inProgress = true;
	if (cat.getCategoryFigure().isExpanded()) {
		List categoriesToCollapse = calculate(cat);
		animate = new CategoryFigure[categoriesToCollapse.size() + 1];
		int count = 1;
		for (Iterator iter = categoriesToCollapse.iterator(); iter.hasNext();) {
			CategoryEditPart category = (CategoryEditPart) iter.next();
			category.setExpanded(false);
			animate[count++] = category.getCategoryFigure();
		}
		animate[0] = cat.getCategoryFigure();
	} else {
		animate = new CategoryFigure[] {cat.getCategoryFigure()};
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

public void removeCategory(CategoryEditPart category) {
	category.getCategoryFigure().setController(null);
	categories.remove(category);
}

public void start() {
	inProgress = true;
	startTime = new Date().getTime();
	endTime = startTime + numberOfMilliSeconds;
}

private List calculate(CategoryEditPart category) {
	int autoCollapseMode = prefs.getAutoCollapseSetting();
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_NEVER
		|| autoCollapseMode == PaletteViewerPreferences.COLLAPSE_AS_NEEDED) {
		return Collections.EMPTY_LIST;
	} 
	List categoriesToCollapse = new ArrayList();
	for (Iterator iter = categories.iterator(); iter.hasNext();) {
		CategoryEditPart cat = (CategoryEditPart) iter.next();
		if (cat.isExpanded() && !cat.getCategoryFigure().isPinnedOpen() && cat != category) {
			categoriesToCollapse.add(cat);
		}
	}
	return categoriesToCollapse;
}

}
