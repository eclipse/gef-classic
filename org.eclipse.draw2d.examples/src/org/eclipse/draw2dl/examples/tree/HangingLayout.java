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
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.geometry.Transposer;

/**
 * 
 * @author hudsonr Created on Apr 22, 2003
 */
class HangingLayout extends BranchLayout {

	HangingLayout(TreeBranch branch) {
		super(branch);
	}

	void calculateDepth() {
		depth = 0;
		List subtrees = branch.contents.getChildren();
		for (int i = 0; i < subtrees.size(); i++)
			depth += ((TreeBranch) subtrees.get(i)).getDepth();
		depth++;
	}

	void setRowHeights(int[] heights, int offset) {
		super.setRowHeights(heights, offset);
		offset++;
		if (branch.isExpanded()) {
			List subtrees = branch.contents.getChildren();
			TreeBranch subtree;

			for (int i = 0; i < subtrees.size(); i++) {
				subtree = (TreeBranch) subtrees.get(i);
				subtree.setRowHeights(heights, offset);
				offset += subtree.getDepth();
			}
		}
	}

	/**
	 * @see BranchLayout#updateRowHeights()
	 */
	void updateRowHeights() {
		org.eclipse.draw2dl.geometry.Transposer transposer = branch.getRoot().getTransposer();
		preferredRowHeights = new int[getDepth()];
		preferredRowHeights[0] = transposer.t(branch.getNode()
				.getPreferredSize()).height + getMajorSpacing();
		if (!branch.isExpanded())
			return;

		List subtrees = getSubtrees();
		TreeBranch subtree;

		int offset = 1;
		for (int i = 0; i < subtrees.size(); i++) {
			subtree = (TreeBranch) subtrees.get(i);
			int rowHeights[] = subtree.getPreferredRowHeights();
			for (int row = 0; row < rowHeights.length; row++)
				preferredRowHeights[row + offset] = rowHeights[row];
			offset += subtree.getDepth();
		}
	}

	int getGap() {
		return branch.getRoot().getMinorSpacing();
	}

	protected org.eclipse.draw2dl.geometry.Dimension calculatePreferredSize(org.eclipse.draw2dl.IFigure container, int wHint,
                                                                            int hHint) {
		org.eclipse.draw2dl.geometry.Transposer transposer = branch.getRoot().getTransposer();
		org.eclipse.draw2dl.geometry.Dimension result = transposer.t(branch.getNode().getPreferredSize()
				.getCopy());
		result.height = rowHeight;
		org.eclipse.draw2dl.IFigure pane = branch.getContentsPane();
		if (!pane.isVisible() || pane.getChildren().isEmpty())
			return transposer.t(result);
		Dimension d = transposer.t(branch.getContentsPane().getPreferredSize());
		result.width = Math.max(result.width, d.width + getGap() * 2);
		result.height += d.height;
		return transposer.t(result);
	}

	public void layout(org.eclipse.draw2dl.IFigure f) {
		Animation.recordInitialState(f);
		if (Animation.playbackState(f))
			return;
		branch.getContentsPane().validate();

		org.eclipse.draw2dl.geometry.Transposer transposer = getTransposer();
		org.eclipse.draw2dl.geometry.Rectangle clientArea = new org.eclipse.draw2dl.geometry.Rectangle();
		branch.getClientArea(clientArea);
		clientArea = transposer.t(clientArea);
		org.eclipse.draw2dl.geometry.Rectangle nodeBounds = new org.eclipse.draw2dl.geometry.Rectangle();
		nodeBounds.setSize(transposer.t(branch.getNode().getPreferredSize()));
		nodeBounds.height = rowHeight - getMajorSpacing();
		nodeBounds.setLocation(clientArea.x, clientArea.y);
		branch.getNode().setBounds(transposer.t(nodeBounds));

		org.eclipse.draw2dl.IFigure contents = branch.getContentsPane();
		org.eclipse.draw2dl.geometry.Rectangle contentsBounds = new org.eclipse.draw2dl.geometry.Rectangle(clientArea.getLocation()
				.translate(getGap() * 2, rowHeight), transposer.t(contents
				.getPreferredSize()));
		contents.setBounds(transposer.t(contentsBounds));
	}

	/**
	 * @see BranchLayout#paintLines(org.eclipse.draw2dl.Graphics)
	 */
	void paintLines(Graphics g) {
		int gap = getGap();
		if (getTransposer().isEnabled()) {
			org.eclipse.draw2dl.IFigure node = branch.getNode();
			org.eclipse.draw2dl.IFigure contents = branch.getContentsPane();
			int x = node.getBounds().right();
			int y = node.getBounds().y() + gap;
			List children = contents.getChildren();
			if (children.size() == 0)
				return;
			int right = x;
			for (int i = 0; i < children.size(); i++) {
				org.eclipse.draw2dl.geometry.Point pt = ((TreeBranch) children.get(i)).getNodeBounds()
						.getTop();
				g.drawLine(pt.x(), y, pt.x(), pt.y());
				right = Math.max(right, pt.x());
			}
			g.drawLine(x, y, right, y);

		} else {
			org.eclipse.draw2dl.IFigure node = branch.getNode();
			IFigure contents = branch.getContentsPane();
			int x = node.getBounds().x() + gap;
			int y = node.getBounds().bottom();
			List children = contents.getChildren();
			if (children.size() == 0)
				return;
			int bottom = y;
			for (int i = 0; i < children.size(); i++) {
				Point pt = ((TreeBranch) children.get(i)).getNodeBounds()
						.getLeft();
				g.drawLine(x, pt.y(), pt.x(), pt.y());
				bottom = Math.max(bottom, pt.y());
			}
			g.drawLine(x, y, x, bottom);
		}
	}

	void updateContours() {
		// Make sure we layout first
		Transposer transposer = getTransposer();
		branch.validate();

		cachedContourLeft = new int[getDepth()];
		cachedContourRight = new int[getDepth()];

		org.eclipse.draw2dl.geometry.Rectangle clientArea = transposer.t(branch
				.getClientArea(org.eclipse.draw2dl.geometry.Rectangle.SINGLETON));
		Rectangle nodeBounds = transposer.t(branch.getNodeBounds());
		int rightEdge = clientArea.right();

		cachedContourLeft[0] = nodeBounds.x() - clientArea.x();
		cachedContourRight[0] = rightEdge - nodeBounds.right();

		if (!branch.isExpanded())
			return;

		List subtrees = branch.contents.getChildren();
		TreeBranch subtree;

		int leftSide = getGap();
		for (int i = 1; i < getDepth(); i++)
			cachedContourLeft[i] = leftSide;

		int rightMargin;
		int offset = 1;
		for (int i = 0; i < subtrees.size(); i++) {
			subtree = (TreeBranch) subtrees.get(i);
			rightMargin = rightEdge - transposer.t(subtree.getBounds()).right();
			int rightContour[] = subtree.getContourRight();
			for (int j = 0; j < rightContour.length; j++)
				cachedContourRight[j + offset] = rightContour[j] + rightMargin;
			offset += subtree.getDepth();
		}
	}

}
