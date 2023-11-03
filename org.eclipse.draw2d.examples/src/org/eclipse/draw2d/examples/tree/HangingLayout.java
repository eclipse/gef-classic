/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.tree;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;

/**
 *
 * @author hudsonr Created on Apr 22, 2003
 */
class HangingLayout extends AbstractBranchLayout {

	HangingLayout(TreeBranch branch) {
		super(branch);
	}

	@Override
	void calculateDepth() {
		depth = getSubtrees().stream().mapToInt(TreeBranch::getDepth).sum() + 1;
	}

	@Override
	void setRowHeights(int[] heights, int offset) {
		super.setRowHeights(heights, offset);
		offset++;
		if (branch.isExpanded()) {
			for (TreeBranch subtree : branch.getSubtrees()) {
				subtree.setRowHeights(heights, offset);
				offset += subtree.getDepth();
			}
		}
	}

	/**
	 * @see org.eclipse.draw2d.examples.tree.AbstractBranchLayout#updateRowHeights()
	 */
	@Override
	void updateRowHeights() {
		Transposer transposer = branch.getRoot().getTransposer();
		preferredRowHeights = new int[getDepth()];
		preferredRowHeights[0] = transposer.t(branch.getNode().getPreferredSize()).height + getMajorSpacing();
		if (!branch.isExpanded()) {
			return;
		}

		int offset = 1;
		for (TreeBranch subtree : branch.getSubtrees()) {
			int[] rowHeights = subtree.getPreferredRowHeights();
			for (int row = 0; row < rowHeights.length; row++) {
				preferredRowHeights[row + offset] = rowHeights[row];
			}
			offset += subtree.getDepth();
		}
	}

	int getGap() {
		return branch.getRoot().getMinorSpacing();
	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		Transposer transposer = branch.getRoot().getTransposer();
		Dimension result = transposer.t(branch.getNode().getPreferredSize().getCopy());
		result.height = rowHeight;
		IFigure pane = branch.getContentsPane();
		if (!pane.isVisible() || pane.getChildren().isEmpty()) {
			return transposer.t(result);
		}
		Dimension d = transposer.t(branch.getContentsPane().getPreferredSize());
		result.width = Math.max(result.width, d.width + (getGap() * 2));
		result.height += d.height;
		return transposer.t(result);
	}

	@Override
	public void layout(IFigure f) {
		Animation.recordInitialState(f);
		if (Animation.playbackState(f)) {
			return;
		}
		branch.getContentsPane().validate();

		Transposer transposer = getTransposer();
		Rectangle clientArea = new Rectangle();
		branch.getClientArea(clientArea);
		clientArea = transposer.t(clientArea);
		Rectangle nodeBounds = new Rectangle();
		nodeBounds.setSize(transposer.t(branch.getNode().getPreferredSize()));
		nodeBounds.height = rowHeight - getMajorSpacing();
		nodeBounds.setLocation(clientArea.x, clientArea.y);
		branch.getNode().setBounds(transposer.t(nodeBounds));

		IFigure contents = branch.getContentsPane();
		Rectangle contentsBounds = new Rectangle(clientArea.getLocation().translate(getGap() * 2, rowHeight),
				transposer.t(contents.getPreferredSize()));
		contents.setBounds(transposer.t(contentsBounds));
	}

	/**
	 * @see org.eclipse.draw2d.examples.tree.AbstractBranchLayout#paintLines(org.eclipse.draw2d.Graphics)
	 */
	@Override
	void paintLines(Graphics g) {
		List<TreeBranch> children = branch.getSubtrees();
		if (children.isEmpty()) {
			return;
		}

		int gap = getGap();
		IFigure node = branch.getNode();

		if (getTransposer().isEnabled()) {
			int x = node.getBounds().right();
			int y = node.getBounds().y() + gap;
			int right = x;
			for (TreeBranch subTree : children) {
				Point pt = subTree.getNodeBounds().getTop();
				g.drawLine(pt.x(), y, pt.x(), pt.y());
				right = Math.max(right, pt.x());
			}
			g.drawLine(x, y, right, y);

		} else {
			int x = node.getBounds().x() + gap;
			int y = node.getBounds().bottom();
			int bottom = y;
			for (TreeBranch subTree : children) {
				Point pt = subTree.getNodeBounds().getLeft();
				g.drawLine(x, pt.y(), pt.x(), pt.y());
				bottom = Math.max(bottom, pt.y());
			}
			g.drawLine(x, y, x, bottom);
		}
	}

	@Override
	void updateContours() {
		// Make sure we layout first
		Transposer transposer = getTransposer();
		branch.validate();

		cachedContourLeft = new int[getDepth()];
		cachedContourRight = new int[getDepth()];

		Rectangle clientArea = transposer.t(branch.getClientArea(Rectangle.SINGLETON));
		Rectangle nodeBounds = transposer.t(branch.getNodeBounds());
		int rightEdge = clientArea.right();

		cachedContourLeft[0] = nodeBounds.x() - clientArea.x();
		cachedContourRight[0] = rightEdge - nodeBounds.right();

		if (!branch.isExpanded()) {
			return;
		}

		int leftSide = getGap();
		for (int i = 1; i < getDepth(); i++) {
			cachedContourLeft[i] = leftSide;
		}

		int rightMargin;
		int offset = 1;
		for (TreeBranch subtree : branch.getSubtrees()) {
			rightMargin = rightEdge - transposer.t(subtree.getBounds()).right();
			int[] rightContour = subtree.getContourRight();
			for (int j = 0; j < rightContour.length; j++) {
				cachedContourRight[j + offset] = rightContour[j] + rightMargin;
			}
			offset += subtree.getDepth();
		}
	}

}
