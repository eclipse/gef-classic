/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.tree;

import java.util.List;

import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PositionConstants;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.geometry.Transposer;

/**
 * @author hudsonr
 * Created on Apr 22, 2003
 */
class NormalLayout extends BranchLayout {

NormalLayout(TreeBranch branch) {
	super(branch);
}

/**
 * @see BranchLayout#calculateDepth()
 */
void calculateDepth() {
	depth = 0;
	List subtrees = getSubtrees();
	for (int i = 0; i < subtrees.size(); i++)
		depth = Math.max(depth, ((TreeBranch)subtrees.get(i)).getDepth());
	depth++;
}

protected Dimension calculatePreferredSize(org.eclipse.draw2dl.IFigure container, int wHint, int hHint) {
	org.eclipse.draw2dl.geometry.Rectangle union = branch.getNodeBounds().getCopy();
//	if (branch.isExpanded())
		union.union(branch.getContentsPane().getBounds());
	
	return union.getSize();
}

public void layout(org.eclipse.draw2dl.IFigure f) {
	Animation.recordInitialState(f);
	if (Animation.playbackState(f))
		return;

	org.eclipse.draw2dl.geometry.Transposer transposer = getTransposer();
	org.eclipse.draw2dl.IFigure contents = branch.getContentsPane();
	org.eclipse.draw2dl.IFigure node = branch.getNode();
	contents.validate();

	org.eclipse.draw2dl.geometry.Rectangle branchBounds = transposer.t(branch.getBounds());
	Point topLeft = branchBounds.getTopLeft();
	org.eclipse.draw2dl.geometry.Rectangle nodeLocation = new org.eclipse.draw2dl.geometry.Rectangle(topLeft, transposer.t(node.getPreferredSize()));
	nodeLocation.height = rowHeight - getMajorSpacing();
	
	if (!contents.isVisible() || contents.getChildren().isEmpty()) {
		nodeLocation.x += (branchBounds.width - nodeLocation.width)/2;
		node.setBounds(transposer.t(nodeLocation));
		contents.setBounds(
			transposer.t(nodeLocation.getTranslated(0, rowHeight).setSize(0, 0)));
		return;
	}

	org.eclipse.draw2dl.geometry.Rectangle contentsLocation =
		new org.eclipse.draw2dl.geometry.Rectangle(topLeft, transposer.t(contents.getPreferredSize()));
	contents.setSize(contents.getPreferredSize());
	contentsLocation.y += rowHeight;

	TreeBranch firstChild = (TreeBranch)contents.getChildren().get(0);
	TreeBranch lastChild =
		(TreeBranch)contents.getChildren().get(contents.getChildren().size() - 1);
	int leftInset =
		firstChild.getContourLeft()[0]
			+ transposer.t(firstChild.getBounds()).x
			- transposer.t(contents.getBounds()).x;
	int rightInset =
		lastChild.getContourRight()[0]
			- transposer.t(lastChild.getBounds()).right()
			+ transposer.t(contents.getBounds()).right();
	rightInset = Math.max(rightInset, 0);
	leftInset = Math.max(leftInset, 0);
	int childrenSpan = contentsLocation.width - leftInset - rightInset;

	switch (branch.getAlignment()) {
		case PositionConstants.CENTER :
			leftInset += (childrenSpan - nodeLocation.width) / 2;
	}

	if (leftInset > 0)
		nodeLocation.x += leftInset;
	else
		contentsLocation.x -= leftInset;
	
	int adjust =
		branchBounds.width
			- org.eclipse.draw2dl.geometry.Rectangle.SINGLETON.setBounds(contentsLocation).union(nodeLocation).width;
	adjust /= 2;
	nodeLocation.x += adjust;
	contentsLocation.x += adjust;
	node.setBounds(transposer.t(nodeLocation));
//	Animation.setBounds(node, transposer.t(nodeLocation));
//	Animation.setBounds(contents, transposer.t(contentsLocation));
	contents.setBounds(transposer.t(contentsLocation));
}

void mergeContour(int[] destination, int[] source, int startdepth, int offset) {
	for (int i = startdepth; i<source.length; i++)
		destination[i+1] = source[i] + offset;
}

/**
 * @see BranchLayout#paintLines(org.eclipse.draw2dl.Graphics)
 */
void paintLines(Graphics g) {
	if (getTransposer().isEnabled()) {
		org.eclipse.draw2dl.IFigure node = branch.getNode();
		int left = node.getBounds().right();
		int right = branch.getContentsPane().getBounds().x - 1;
		int yMid = node.getBounds().getCenter().y;
		int xMid = (left + right) / 2;
		List children = getSubtrees();
		if (children.size() == 0)
			return;
		g.drawLine(left, yMid, xMid, yMid);
		int yMin = yMid;
		int yMax = yMid;
		for (int i=0; i<children.size(); i++){
			int y = ((TreeBranch)children.get(i)).getNodeBounds().getCenter().y;
			g.drawLine(xMid, y, right, y);
			yMin = Math.min(yMin, y);
			yMax = Math.max(yMax, y);
		}
		g.drawLine(xMid, yMin, xMid, yMax);

	} else {
		IFigure node = branch.getNode();
		int xMid = node.getBounds().getCenter().x;
		int top = node.getBounds().bottom();
		int bottom = branch.getContentsPane().getBounds().y - 1;
		int yMid = (top + bottom) / 2;
		List children = getSubtrees();
		if (children.size() == 0)
			return;
		g.drawLine(xMid, top, xMid, yMid);
		int xMin = xMid;
		int xMax = xMid;
		for (int i=0; i<children.size(); i++){
			int x = ((TreeBranch)children.get(i)).getNodeBounds().getCenter().x;
			g.drawLine(x, yMid, x, bottom);
			xMin = Math.min(xMin, x);
			xMax = Math.max(xMax, x);
		}
		g.drawLine(xMin, yMid, xMax, yMid);
	}
}

/**
 * @see BranchLayout#updateContours()
 */
void updateContours() {
	org.eclipse.draw2dl.geometry.Transposer transposer = getTransposer();
	//Make sure we layout first
	branch.validate();

	cachedContourLeft = new int[getDepth()];
	cachedContourRight = new int[getDepth()];

	org.eclipse.draw2dl.geometry.Rectangle clientArea =
		transposer.t(branch.getNodeBounds().getUnion(branch.contents.getBounds()));
	org.eclipse.draw2dl.geometry.Rectangle nodeBounds = transposer.t(branch.getNodeBounds());
	Rectangle contentsBounds = transposer.t(branch.getContentsPane().getBounds());
	
	cachedContourLeft[0] = nodeBounds.x - clientArea.x;
	cachedContourRight[0] = clientArea.right() - nodeBounds.right();
	if (!branch.isExpanded())
		return;

	List subtrees = getSubtrees();
	TreeBranch subtree;

	int currentDepth = 0;
	for (int i = 0; i < subtrees.size() && currentDepth < getDepth(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		if (subtree.getDepth() > currentDepth) {
			int leftContour[] = subtree.getContourLeft();
			int leftOffset = transposer.t(subtree.getBounds()).x - clientArea.x;
			mergeContour(cachedContourLeft, leftContour, currentDepth, leftOffset);
			currentDepth = subtree.getDepth();
		}
	}

	currentDepth = 0;
	for (int i = subtrees.size() - 1; i >= 0 && currentDepth < getDepth(); i--) {
		subtree = (TreeBranch)subtrees.get(i);
		if (subtree.getDepth() > currentDepth) {
			int rightContour[] = subtree.getContourRight();
			int rightOffset =
				clientArea.right() - transposer.t(subtree.getBounds()).right();
			mergeContour(cachedContourRight, rightContour, currentDepth, rightOffset);
			currentDepth = subtree.getDepth();
		}
	}
}

/**
 * @see BranchLayout#updateRowHeights()
 */
void updateRowHeights() {
	Transposer transposer = getTransposer();
	preferredRowHeights = new int[getDepth()];

	preferredRowHeights[0] =
		transposer.t(branch.getNode().getPreferredSize()).height + getMajorSpacing();

	if (!branch.isExpanded())
		return;

	List subtrees = getSubtrees();
	TreeBranch subtree;
	
	for (int i = 0; i < subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		int rowHeights[] = subtree.getPreferredRowHeights();
		for (int row = 0; row < rowHeights.length; row++)
			preferredRowHeights[row + 1] =
				Math.max(preferredRowHeights[row + 1], rowHeights[row]);
	}
}

/**
 * @see BranchLayout#setRowHeights(int[], int)
 */
void setRowHeights(int[] heights, int offset) {
	super.setRowHeights(heights, offset);
	if (branch.isExpanded()) {
		List subtrees = getSubtrees();
		offset++;
		for (int i=0; i<subtrees.size(); i++)
			((TreeBranch)subtrees.get(i)).setRowHeights(heights, offset);
	}
}

}
