package org.eclipse.draw2d.examples.tree;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * Performs a layout on a container containing {@link TreeBranch} figures. This layout is
 * similar to FlowLayout, except that the children are squeezed together to overlap by
 * comparing their left and right contours.
 * @author hudsonr
 * Created on Apr 18, 2003
 */
public class TreeLayout extends AbstractLayout {

private int overlapDepth;

/**
 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	container.validate();
	List children = container.getChildren();
	Rectangle result = new Rectangle(container.getClientArea().getLocation(), new Dimension());
	for (int i=0; i<children.size(); i++)
		result.union(((IFigure)children.get(i)).getBounds());
	result.resize(container.getInsets().getWidth(), container.getInsets().getHeight());
	return result.getSize();
}

private int[] calculateNewRightContour(int old[], int add[], int shift) {
	if (old == null)
		return add;
	int result[] = new int[Math.max(old.length, add.length)];
	System.arraycopy(add, 0, result, 0, add.length);
	for (int i = add.length; i < result.length; i++)
		result[i] = old[i] + shift;
	return result;
}

private int calculateOverlap(int leftSubtree[], int rightSubtree[]) {
	if (leftSubtree == null)
		return 0;
	int min = Math.min(leftSubtree.length, rightSubtree.length);
	int result = Integer.MAX_VALUE;
	overlapDepth = 0;
	for (int i=0; i<min; i++) {
		int current = leftSubtree[i] + rightSubtree[i];
		if (current < result) {
			result = current;
			overlapDepth = result;
		}
	}
	return result;
}

/**
 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
 */
public void layout(IFigure container) {
	List subtrees = container.getChildren();
	TreeBranch subtree;
	int rightContour[] = null;
	int leftContour[];

	Point reference = container.getClientArea().getLocation();
	Point currentXY = reference.getCopy();
	
	for (int i=0; i<subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		leftContour = subtree.getContourLeft();
		int overlap = calculateOverlap(rightContour, leftContour);
		subtree.setLocation(currentXY.getTranslated(-overlap, 0));

		//Setup value for next sibling
		int advance = 10 + subtree.getSize().width - overlap;
		rightContour = calculateNewRightContour(
			rightContour,
			subtree.getContourRight(),
			advance);
		currentXY.x += advance;
		int correction = reference.x - subtree.getLocation().x;
		if (correction > 0) {
			currentXY.x += correction;
			for (int j=0; j<=i; j++)
				((IFigure)subtrees.get(j)).translate(correction, 0);
		}
	}
}

}
