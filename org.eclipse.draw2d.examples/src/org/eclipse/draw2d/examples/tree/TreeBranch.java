package org.eclipse.draw2d.examples.tree;

import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author hudsonr
 * Created on Apr 17, 2003
 */
public class TreeBranch extends Figure {

private int[] cachedContourLeft;
private int[] cachedContourRight;
private IFigure contents = new Figure();
private int depth = -1;
private int aligment;
private int[] preferredRowHeights;
private int[] rowHeights;

private IFigure title;

public TreeBranch(IFigure title) {
	FlowLayout layout = new FlowLayout(FlowLayout.VERTICAL);
	layout.setMinorSpacing(20);
	layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
//	layout.setSpacing(10);
	layout.setStretchMinorAxis(false);
	setLayoutManager(layout);
	this.title = title;
	title.setBorder(new LineBorder(ColorConstants.gray, 4));
	add(title);
	add(contents);
	contents.setLayoutManager(new TreeLayout());
//	contents.setBorder(new LineBorder(ColorConstants.black, 4));
}

public int getAlignment() {
	return aligment;
}

public IFigure getContentsPane() {
	return contents;
}

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

int getDepth() {
	if (depth != -1)
		return depth;
	depth = 0;
	List subtrees = contents.getChildren();
	for (int i = 0; i < subtrees.size(); i++)
		depth = Math.max(depth, ((TreeBranch)subtrees.get(i)).getDepth());
	depth++;
	return depth;
}

public int[] getPreferredRowHeights() {
	if (preferredRowHeights == null) {
		preferredRowHeights = new int[getDepth()];
		List subtrees = contents.getChildren();
		TreeBranch subtree;
		for (int i = 0; i < subtrees.size(); i++) {
			subtree = (TreeBranch)subtrees.get(i);
			int rowHeights[] = subtree.getPreferredRowHeights();
			for (int row = 0; row < rowHeights.length; row++)
				preferredRowHeights[row + 1] =
					Math.max(preferredRowHeights[row + 1], rowHeights[row]);
		}
	}
	return preferredRowHeights;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	validate();
	return super.getPreferredSize(wHint, hHint);
}

/**
 * @see org.eclipse.draw2d.Figure#invalidate()
 */
public void invalidate() {
	preferredRowHeights = null;
	cachedContourLeft = null;
	cachedContourRight = null;
	depth = -1;
	super.invalidate();
}

void mergeContour(int[] destination, int[] source, int startdepth, int offset) {
	for (int i = startdepth; i<source.length; i++)
		destination[i+1] = source[i] + offset;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	int xMid = title.getBounds().getCenter().x;
	int top = title.getBounds().bottom();
	int bottom = contents.getBounds().y - 1;
	int yMid = (top + bottom) / 2;
	List children = contents.getChildren();
	if (children.size() == 0)
		return;
	g.drawLine(xMid, top, xMid, yMid);
	int xMin = Integer.MAX_VALUE;
	int xMax = Integer.MIN_VALUE;
	for (int i=0; i<children.size(); i++){
		int x = ((IFigure)children.get(i)).getBounds().getCenter().x;
		g.drawLine(x, yMid, x, bottom);
		xMin = Math.min(xMin, x);
		xMax = Math.max(xMax, x);
	}
	g.drawLine(xMin, yMid, xMax, yMid);
}

public void setAlignment(int value) {
	aligment = value;
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return toString(0);
}

String toString(int level) {
	String result = "";
	for (int i=0; i<level; i++)
		result += "  ";
	try {
		result += ((Label)getChildren().get(0)).getText() + "\n";
	} catch (ClassCastException e) {
		result += getChildren().get(0);
	}
	for (int i=0; i<contents.getChildren().size(); i++)
		result += ((TreeBranch)contents.getChildren().get(i)).toString(level + 1);
	return result;
}

void updateContours() {
	//Make sure we layout first
	validate();
	cachedContourLeft = new int[getDepth()];
	cachedContourRight = new int[getDepth()];
	cachedContourLeft[0] = title.getBounds().x - getClientArea().x;
	cachedContourRight[0] = getClientArea().right() - title.getBounds().right();

	List subtrees = contents.getChildren();
	TreeBranch subtree;

	int currentDepth = 0;
	for (int i = 0; i < subtrees.size() && currentDepth < getDepth(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		if (subtree.getDepth() > currentDepth) {
			int leftContour[] = subtree.getContourLeft();
			int leftOffset = subtree.getBounds().x - contents.getClientArea().x;
			mergeContour(cachedContourLeft, leftContour, currentDepth, leftOffset);
			currentDepth = subtree.getDepth() + 1;
		}
	}

	currentDepth = 0;
	for (int i = subtrees.size() - 1; i >= 0 && currentDepth < getDepth(); i--) {
		subtree = (TreeBranch)subtrees.get(i);
		if (subtree.getDepth() > currentDepth) {
			int rightContour[] = subtree.getContourRight();
			int rightOffset = contents.getClientArea().right() - subtree.getBounds().right();
			mergeContour(cachedContourRight, rightContour, currentDepth, rightOffset);
			currentDepth = subtree.getDepth() + 1;
		}
	}
}

/**
 * @see org.eclipse.draw2d.Figure#validate()
 */
public void validate() {
	if (isValid())
		return;
	contents.validate();
	setSize(super.getPreferredSize(-1, -1));
	super.validate();
}

}
