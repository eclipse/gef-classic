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
package org.eclipse.mylar.zest.core.internal.treegraphviewer;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Transposer;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public abstract class BranchLayout extends AbstractLayout {

	private Transposer transposer;
	final TreeBranch branch;
	int[] cachedContourLeft;
	int[] cachedContourRight;
	int depth = -1;
	public boolean horizontal = true;
	int[] preferredRowHeights;
	int rowHeight;

	public BranchLayout(TreeBranch branch) {
		this.branch = branch;
	}

	abstract void calculateDepth();

	public int[] getContourLeft() {
		if (cachedContourLeft == null)
			updateContours();
		return cachedContourLeft;
	}

	public int[] getContourRight() {
		if (cachedContourRight == null)
			updateContours();
		return cachedContourRight;
	}

	public int getDepth() {
		if (!branch.isExpanded())
			return 1;
		if (depth == -1)
			calculateDepth();
		return depth;
	}

	public int[] getPreferredRowHeights() {
		if (preferredRowHeights == null)
			updateRowHeights();
		return preferredRowHeights;
	}

	List getSubtrees() {
		return branch.getContentsPane().getChildren();
	}

	Transposer getTransposer() {
		if (transposer == null)
			transposer = branch.getRoot().getTransposer();
		return transposer;
	}

	int getMajorSpacing() {
		return branch.getRoot().getMajorSpacing();
	}

	public void invalidate() {
		preferredRowHeights = null;
		cachedContourLeft = null;
		cachedContourRight = null;
		depth = -1;
		super.invalidate();
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	abstract void paintLines(Graphics g);

	public void setHorizontal(boolean value) {
		horizontal = value;
	}

	void setRowHeights(int heights[], int offset) {
		if (rowHeight != heights[offset]) {
			rowHeight = heights[offset];
			branch.revalidate();
		}
	}

	abstract void updateContours();

	abstract void updateRowHeights();

}
