/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 * @since 3.1
 */
public class LineRoot extends LineBox {

private static class BidiLevelNode extends ArrayList
{
	int level;
	final BidiLevelNode parent;

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

/*
 * Simply lays out all fragments from left-to-right in the order in which they're 
 * contained. 
 */
private static void contiguousCommit(FlowBox box, int x, int baseline) {
	box.setX(x);
	// Don't lay out the children of any blocks.  If there is a BlockBox, it will be the 
	// first and only child (i.e., on a line by itself).
	if (box instanceof LineBox) {
		List fragments = ((LineBox)box).getFragments();
		for (int i = 0; i < fragments.size(); i++) {
			FlowBox child = (FlowBox)fragments.get(i);
			contiguousCommit(child, x, baseline);
			x += child.getWidth();
		}
	}
}

private int baseline;

public void add(FlowBox child) {
	super.add(child);
	child.setLineRoot(this);
}

private void buildBidiTree(FlowBox box, BidiLevelNode node, List branches) {
	if (box instanceof LineBox) {
		List children = ((LineBox)box).getFragments();
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
 * or left-to-right if Bidi is not necessary.
 */
public void commit() {
	int baseline = getBaseline();
	if (requiresBidi()) {
		int xLocation = getX();
		BidiLevelNode root = new BidiLevelNode();
		List branches = new ArrayList();
		// branches does not include this LineBox
		buildBidiTree(this, root, branches);
		List result = new ArrayList();
		root.emit(result);
		for (int i = 0; i < result.size(); i++) {
			FlowBox box = (FlowBox)result.get(i);
			box.setX(xLocation);
			xLocation += box.getWidth();
		}
		// change each composite box's bounds to encompass all its children.  the
		// following algorithm works because all the children (including non-leaves)
		// of a parent will be listed before the parent is.
		for (int i = 0; i < branches.size(); i++) {
			LineBox parent = (LineBox)branches.get(i);
			Rectangle bounds = new Rectangle();
			for (Iterator iter = parent.getFragments().iterator(); iter.hasNext();) {
				FlowBox child = (FlowBox) iter.next();
				bounds.union(child.getX(), 0, child.getWidth(), 0);
			}
			parent.setX(bounds.x);
			parent.setWidth(bounds.width);
		}
	} else
		contiguousCommit(this, getX(), baseline);
}

public boolean containsPoint(int x, int y) {
	return false;
}

public int getBaseline() {
	return baseline;
}

LineRoot getLineRoot() {
	return this;
}

int getVisibleBottom() {
	return baseline + contentDescent;
}

int getVisibleTop() {
	return baseline - contentAscent;
}

public void setBaseline(int baseline) {
	this.baseline = baseline;
}

public void setLineTop(int top) {
	this.baseline = top + getAscent();
}

}
