/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

package org.eclipse.draw2d.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * LineRoot is the top-most container on a line of text displayed in Draw2d.
 * Hence, a LineRoot can tell you of things like the highest ascent or descent
 * on a line, which is required to display selection and such. All
 * {@link org.eclipse.draw2d.text.ContentBox fragments} know of the LineRoot
 * they belong to.
 *
 * @author Randy Hudson
 * @author Pratik Shah
 * @since 3.1
 */
public class LineRoot extends LineBox {

	private int baseline;
	private final boolean isMirrored;

	/**
	 * Constructor
	 *
	 * @param isMirrored <code>true</code> if the line is to be displayed in a
	 *                   mirrored control
	 */
	public LineRoot(boolean isMirrored) {
		this.isMirrored = isMirrored;
	}

	/**
	 * @see org.eclipse.draw2d.text.CompositeBox#add(org.eclipse.draw2d.text.FlowBox)
	 */
	@Override
	public void add(FlowBox child) {
		super.add(child);
		child.setLineRoot(this);
	}

	private void bidiCommit() {
		int xLocation = getX();
		BidiLevelNode root = new BidiLevelNode();
		List<NestedLine> branches = new ArrayList<>();
		// branches does not include this LineRoot; all the non-leaf child
		// fragments of a
		// parent will be listed before the parent itself in this list
		buildBidiTree(this, root, branches);
		List<FlowBox> result = new ArrayList<>();
		root.emit(result);
		int i = isMirrored ? result.size() - 1 : 0;
		while (i >= 0 && i < result.size()) {
			FlowBox box = result.get(i);
			box.setX(xLocation);
			xLocation += box.getWidth();
			i += isMirrored ? -1 : 1;
		}
		// set the bounds of the composite boxes, and break overlapping ones
		// into two
		layoutNestedLines(branches);
	}

	private void buildBidiTree(FlowBox box, BidiLevelNode node, List<NestedLine> branches) {
		if (box instanceof LineBox lineBox) {
			for (FlowBox child : lineBox.getFragments()) {
				buildBidiTree(child, node, branches);
			}
			if (box != this && box instanceof NestedLine nestedLine) {
				branches.add(nestedLine);
			}
		} else {
			ContentBox leafBox = (ContentBox) box;
			while (leafBox.getBidiLevel() < node.level) {
				node = node.pop();
			}
			while (leafBox.getBidiLevel() > node.level) {
				node = node.push();
			}
			node.add(leafBox);
		}
	}

	/**
	 * Committing a LineRoot will position its children correctly. All children
	 * boxes are made to have the same baseline, and are laid out according to the
	 * Unicode BiDi Algorithm, or left-to-right if Bidi is not necessary.
	 */
	public void commit() {
		if (requiresBidi()) {
			bidiCommit();
		} else {
			contiguousCommit(this, getX());
		}
	}

	/**
	 * A LineRoot cannot be targetted.
	 *
	 * @see org.eclipse.draw2d.text.FlowBox#containsPoint(int, int)
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		return false;
	}

	/*
	 * Simply lays out all fragments from left-to-right in the order in which
	 * they're contained.
	 */
	private void contiguousCommit(FlowBox box, int x) {
		box.setX(x);
		if (box instanceof LineBox lineBox) {
			List<FlowBox> fragments = lineBox.getFragments();
			int i = isMirrored ? fragments.size() - 1 : 0;
			while (i >= 0 && i < fragments.size()) {
				FlowBox child = fragments.get(i);
				contiguousCommit(child, x);
				x += child.getWidth();
				i += isMirrored ? -1 : 1;
			}
		}
	}

	private Result findParent(NestedLine line, List<NestedLine> branches, int afterIndex) {
		for (int i = afterIndex + 1; i < branches.size(); i++) {
			NestedLine box = branches.get(i);
			int index = box.getFragments().indexOf(line);
			if (index >= 0) {
				return new Result(box, index);
			}
		}
		return new Result(this, getFragments().indexOf(line));
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBox#getBaseline()
	 */
	@Override
	public int getBaseline() {
		return baseline;
	}

	@Override
	LineRoot getLineRoot() {
		return this;
	}

	int getVisibleBottom() {
		return baseline + contentDescent;
	}

	int getVisibleTop() {
		return baseline - contentAscent;
	}

	private void layoutNestedLines(List<NestedLine> branches) {
		for (int i = 0; i < branches.size(); i++) {
			NestedLine parent = branches.get(i);
			FlowBox prevChild = null;
			Rectangle bounds = null;
			List<FlowBox> frags = parent.getFragments();
			for (int j = 0; j < frags.size(); j++) {
				FlowBox child = frags.get(j);
				if (prevChild != null && prevChild.getX() + prevChild.width != child.getX()
						&& child.getX() + child.width != prevChild.getX()) {
					// the boxes are not adjacent, and hence the parent box
					// needs to
					// be broken up
					InlineFlow parentFig = parent.owner;
					// Create and initialize a new line box
					NestedLine newBox = new NestedLine(parentFig);
					newBox.setLineRoot(this);
					// Add all remaining fragments from the current line box to
					// the new one
					for (int k = j; k < frags.size();) {
						newBox.fragments.add(frags.remove(k));
					}
					// Add the new line box to the parent box's list of
					// fragments
					Result result = findParent(parent, branches, i);
					result.parent.getFragments().add(result.index + 1, newBox);
					// Add the new line box to the flow figure's list of
					// fragments
					parentFig.fragments.add(parentFig.fragments.indexOf(parent) + 1, newBox);
					branches.add(i + 1, newBox);
					break;
				}
				if (bounds == null) {
					bounds = new Rectangle(child.getX(), 1, child.getWidth(), 1);
				} else {
					bounds.union(child.getX(), 1, child.getWidth(), 1);
				}
				prevChild = child;
			}
			parent.setX(bounds.x);
			parent.setWidth(bounds.width);
		}
	}

	/**
	 * Positions the line vertically by settings its baseline.
	 *
	 * @param baseline the baseline
	 */
	public void setBaseline(int baseline) {
		this.baseline = baseline;
	}

	/**
	 * @see org.eclipse.draw2d.text.CompositeBox#setLineTop(int)
	 */
	@Override
	public void setLineTop(int top) {
		this.baseline = top + getAscent();
	}

	private static class BidiLevelNode extends ArrayList<Object> {
		int level;
		final BidiLevelNode parent;

		BidiLevelNode() {
			this(null, 0);
		}

		BidiLevelNode(BidiLevelNode parent, int level) {
			this.parent = parent;
			this.level = level;
		}

		void emit(List<FlowBox> list) {
			if (level % 2 == 1) {
				for (int i = size() - 1; i >= 0; i--) {
					emitChild(list, get(i));
				}
			} else {
				for (int i = 0; i < size(); i++) {
					emitChild(list, get(i));
				}
			}
		}

		BidiLevelNode pop() {
			return parent;
		}

		BidiLevelNode push() {
			if (!isEmpty()) {
				Object last = get(size() - 1);
				if (last instanceof BidiLevelNode bidiLevelNode && bidiLevelNode.level == level + 1) {
					return bidiLevelNode;
				}
			}
			BidiLevelNode child = new BidiLevelNode(this, level + 1);
			super.add(child);
			return child;
		}

		private static void emitChild(List<FlowBox> list, Object child) {
			if (child instanceof BidiLevelNode bidiLevelNode) {
				bidiLevelNode.emit(list);
			} else if (child instanceof FlowBox box) {
				list.add(box);
			}
		}
	}

	private static class Result {
		private final int index;
		private final LineBox parent;

		private Result(LineBox box, int i) {
			parent = box;
			index = i;
		}
	}

}