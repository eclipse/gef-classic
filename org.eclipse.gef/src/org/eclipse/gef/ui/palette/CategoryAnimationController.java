package org.eclipse.gef.ui.palette;

import java.util.*;

import org.eclipse.draw2d.IFigure;

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
 * Constructor
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
		List categoriesToCollapse = getCategoriesToCollapse(cat);
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

protected List getCategoriesToCollapse(CategoryEditPart category) {
	int autoCollapseMode = prefs.getAutoCollapseSetting();
	
	// Collapse never
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_NEVER) {
		return Collections.EMPTY_LIST;
	} 
	
	// Collapse always
	List categoriesToCollapse = new ArrayList();
	if (autoCollapseMode == PaletteViewerPreferences.COLLAPSE_ALWAYS) {
		for (Iterator iter = categories.iterator(); iter.hasNext();) {
			CategoryEditPart cat = (CategoryEditPart) iter.next();
			if (cat.isExpanded() && !cat.getCategoryFigure().isPinnedOpen() && cat != category) {
				categoriesToCollapse.add(cat);
			}
		}
		return categoriesToCollapse;
	}
	
	// Collapse as needed
	List potentialCategoriesToCollapse = new ArrayList();
	CategoryFigure catFigure = category.getCategoryFigure();
	int availableWidth = catFigure.getParent().getClientArea().width;
	int availableHeight = catFigure.getParent().getSize().height;
	int requiredHeight = 0;
	for (Iterator iter = category.getParent().getChildren().iterator(); iter.hasNext();) {
		PaletteEditPart part = (PaletteEditPart) iter.next();
		IFigure fig = part.getFigure();
		int height = fig.getPreferredSize(availableWidth, -1).height;
		requiredHeight += height;
		if (!(part instanceof CategoryEditPart)) {
			continue;
		}
		CategoryFigure figure = (CategoryFigure)fig;
		if (figure.isExpanded() && !figure.isPinnedOpen()) {
			potentialCategoriesToCollapse.add(part);
		}
	}
	for (int i = potentialCategoriesToCollapse.size() - 1; i >= 0
				&& requiredHeight > availableHeight; i--) {
		CategoryEditPart part = (CategoryEditPart)potentialCategoriesToCollapse.get(i);
		if (part == category) {
			continue;
		}
		int expandedHeight = part.getFigure().getPreferredSize(availableWidth, -1).height;
		part.setExpanded(false);
		int collapsedHeight = part.getFigure().getPreferredSize(availableWidth, -1).height;
		requiredHeight -= (expandedHeight - collapsedHeight);
		categoriesToCollapse.add(part);
	}
	return categoriesToCollapse;
	
}

}
