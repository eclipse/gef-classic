/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A composite box representing a single line. LineBox calculates its ascent and descent
 * from the child boxes it contains. Clients can call {@link #getAscent()} or {@link
 * #getHeight()} at any time and expect valid values. The child boxes that are added to a
 * line have unspecified locations until {@link #commit()} is called, at which time the
 * child boxes are laid out, and their baselines are all aligned vertically.
 * @author hudsonr
 * @since 2.1 */
public class LineBox
	extends CompositeBox
{

private int ascent = 0;

/*
 * @since 3.1
 */
private void buildBidiTree(FlowBox box, BidiLevelNode node, List branches) {
	if (box instanceof CompositeBox) {
		List children = ((CompositeBox)box).getFragments();
		for (int i = 0; i < children.size(); i++)
			buildBidiTree((FlowBox)children.get(i), node, branches);
		if (box != this)
			branches.add(box);
	} else {
		ContentBox leafBox = (ContentBox)box;
		while (leafBox.getBidiLevel() < node.level)
			node = node.pop();
		while (leafBox.getBidiLevel() > node.level)
			node = node.push();
		node.add(leafBox);
	}
}

/**
 * Committing a LineBox will position its children correctly. All children boxes are made
 * to have the same baseline, and are laid out according to the Unicode BiDi Algorithm,
 * or left-to-right if Bidi is not required.
 */
public void commit() {
	int baseline = getBaseline();
	int xLocation = x;
	if (isBidi()) {
		BidiLevelNode root = new BidiLevelNode();
		List branches = new ArrayList();
		// branches does not include this LineBox
		buildBidiTree(this, root, branches);
		List result = new ArrayList();
		root.emit(result);
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			FlowBox box = (FlowBox)iter.next();
			box.x = xLocation;
			xLocation += box.width;
			box.makeBaseline(baseline);
		}
		// change each composite box's bounds to encompass all its children.  the
		// following algorithm works because all the children (including non-leaves)
		// of a parent will be listed before the parent is.
		for (int i = 0; i < branches.size(); i++) {
			CompositeBox parent = (CompositeBox)branches.get(i);
			Rectangle bounds = new Rectangle(0, 1, 0, 1);
			for (Iterator iter = parent.getFragments().iterator(); iter.hasNext();) {
				FlowBox child = (FlowBox) iter.next();
				bounds.union(child.x, 1, child.width, 1);
			}
			parent.x = bounds.x;
			parent.width = bounds.width;
			parent.makeBaseline(baseline);
		}
	} else
		contiguousCommit(this, x, baseline);
}

/*
 * Simply lays out all fragments from left-to-right in the order in which they're 
 * contained. 
 */
private void contiguousCommit(FlowBox box, int x, int baseline) {
	box.x = x;
	box.makeBaseline(baseline);
	// Don't lay out the children of any blocks.  If there is a BlockBox, it will be the 
	// first and only child (i.e., on a line by itself).
	if (box instanceof BlockBox)
		return;
	if (box instanceof CompositeBox) {
		List fragments = ((CompositeBox)box).getFragments();
		for (int i = 0; i < fragments.size(); i++) {
			FlowBox child = (FlowBox)fragments.get(i);
			contiguousCommit(child, x, baseline);
			x += child.width;
		}
	}
}

/** * @see org.eclipse.draw2d.text.FlowBox#getAscent() */
public int getAscent() {
	return ascent;
}

/**
 * Returns the width available to child fragments.
 * @return the width in pixels
 */
public int getAvailableWidth() {
	if (recommendedWidth < 0)
		return Integer.MAX_VALUE;
	return recommendedWidth - width;
}

/**
 * @see CompositeBox#resetInfo()
 */
protected void resetInfo() {
	super.resetInfo();
	ascent = 0;
}

/**
 * @see CompositeBox#unionInfo(FlowBox)
 */
protected void unionInfo(FlowBox blockInfo) {
	int descent = height - ascent;
	ascent = Math.max(ascent, blockInfo.getAscent());
	height = ascent + Math.max(descent, blockInfo.getDescent());
	width += blockInfo.width;
}

/*
 * @since 3.1
 */
private static class BidiLevelNode extends ArrayList
{
	private int level;
	private final BidiLevelNode parent;

	BidiLevelNode() {
		this(null, 0);
	}

	BidiLevelNode(BidiLevelNode parent, int level) {
		this.parent = parent;
		this.level = level;
	}

	void emit(List list) {
		if (level % 2 == 1) {
			for (int i = size() - 1; i >= 0; i--) {
				Object child = get(i);
				if (child instanceof BidiLevelNode) {
					BidiLevelNode node = (BidiLevelNode) child;
					node.emit(list);
				} else
					list.add(child);
			}
		} else {
			for (int i = 0; i < size(); i++) {
				Object child = get(i);
				if (child instanceof BidiLevelNode) {
					BidiLevelNode node = (BidiLevelNode) child;
					node.emit(list);
				} else
					list.add(child);
			}
		}
	}

	BidiLevelNode pop() {
		return parent;
	}

	BidiLevelNode push() {
		if (!isEmpty()) {
			Object last = get(size() - 1);
			if (last instanceof BidiLevelNode && ((BidiLevelNode)last).level == level + 1)
				return (BidiLevelNode)last;
		}
		BidiLevelNode child = new BidiLevelNode(this, level + 1);
		add(child);
		return child;
	}
}

}