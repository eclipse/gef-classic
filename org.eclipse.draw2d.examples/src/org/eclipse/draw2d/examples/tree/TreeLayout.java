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

private int pointOfContact;
private int gap = 10;

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
	pointOfContact = 0;
	if (leftSubtree == null)
		return 0;
	int min = Math.min(leftSubtree.length, rightSubtree.length);
	int result = Integer.MAX_VALUE;
	for (int i=0; i<min; i++) {
		int current = leftSubtree[i] + rightSubtree[i];
		if (current < result) {
			result = current;
			pointOfContact = i + 1;
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
	int previousSubtreeDepth = 0;
	int rightContour[] = null;
	int leftContour[];
	int contactDepth;
	
	Point reference = container.getClientArea().getLocation();
	Point currentXY = reference.getCopy();
	
	for (int i = 0; i < subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		subtree.toString();
		leftContour = subtree.getContourLeft();
		int overlap = calculateOverlap(rightContour, leftContour);
		contactDepth = pointOfContact;
		subtree.setLocation(currentXY.getTranslated(-overlap, 0));

		//Setup value for next sibling
		int advance = gap + subtree.getSize().width - overlap;
		rightContour = calculateNewRightContour(
			rightContour,
			subtree.getContourRight(),
			advance);
		currentXY.x += advance;
		
		/* 
		 * In some cases, the current child may extend beyond the left edge of the
		 * container because of the way it overlaps with the previous child. When this
		 * happens, shift all children right. 
		 */
		int shiftRight = reference.x - subtree.getBounds().x;
		if (shiftRight > 0) {
			currentXY.x += shiftRight;
			for (int j=0; j<=i; j++)
				((IFigure)subtrees.get(j)).translate(shiftRight, 0);
		}
		
		/*
		 * In some cases, the current child "i" only touches the contour of a distant
		 * sibling "i-n", where n>1.  This means that there is extra space that can be
		 * distributed among the intermediate siblings
		 */

		if (contactDepth > previousSubtreeDepth) {
			TreeBranch branch = (TreeBranch)subtrees.get(i-1);
			int slack = subtree.getBounds().x - branch.getBounds().right() - gap
				+ calculateOverlap(branch.getContourRight(), subtree.getContourLeft());
			int end = i;
			int begin = end - 1;
			while (begin > 0
				&& ((TreeBranch)subtrees.get(begin)).getDepth() < contactDepth)
				begin--;
			
			for (int j = begin + 1; j < end; j++) {
				branch = (TreeBranch)subtrees.get(j);
				int shift = slack * (j - begin) / (end - begin);
				branch.translate(shift, 0);
			}
		}
		 
		 previousSubtreeDepth = subtree.getDepth();
	}
}

}
