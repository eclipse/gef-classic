package org.eclipse.draw2d.examples.tree;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;

/**
 * 
 * @author hudsonr
 * Created on Apr 22, 2003
 */
class HangingLayout extends BranchLayout {

HangingLayout(TreeBranch branch) {
	super(branch);
}

void calculateDepth() {
	depth = 0;
	List subtrees = branch.contents.getChildren();
	for (int i = 0; i < subtrees.size(); i++)
		depth += ((TreeBranch)subtrees.get(i)).getDepth();
	depth++;
}

void setRowHeights(int[] heights, int offset) {
	super.setRowHeights(heights, offset);
	offset++;
	List subtrees = branch.contents.getChildren();
	TreeBranch subtree;

	for (int i = 0; i < subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		subtree.setRowHeights(heights, offset);
		offset += subtree.getDepth();
	}
}

/**
 * @see org.eclipse.draw2d.examples.tree.BranchLayout#updateRowHeights()
 */
void updateRowHeights() {
	preferredRowHeights = new int[getDepth()];
	preferredRowHeights[0] = branch.getNode().getPreferredSize().height + getMajorSpacing();

	List subtrees = getSubtrees();
	TreeBranch subtree;
	
	int offset = 1;
	for (int i = 0; i < subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		int rowHeights[] = subtree.getPreferredRowHeights();
		for (int row = 0; row < rowHeights.length; row++)
			preferredRowHeights[row + offset] = rowHeights[row];
		offset += subtree.getDepth();
	}
}


protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	TreeBranch branch = (TreeBranch)container;
	Dimension result = branch.getNode().getPreferredSize().getCopy();
	result.height = rowHeight;
	if (branch.getContentsPane().getChildren().isEmpty())
		return result;
	Dimension d = branch.getContentsPane().getPreferredSize();
	result.width = Math.max(result.width, d.width + result.width / 2 + 10);
	result.height += d.height;
	return result;
}

public void layout(IFigure f) {
	TreeBranch branch = (TreeBranch)f;
	Rectangle clientArea = new Rectangle();
	branch.getClientArea(clientArea);
	Rectangle nodeBounds = new Rectangle();
	nodeBounds.setSize(branch.getNode().getPreferredSize());
	nodeBounds.height = rowHeight - getMajorSpacing();
	nodeBounds.setLocation(clientArea.x, clientArea.y);
	branch.getNode().setBounds(nodeBounds);

	IFigure contents = branch.getContentsPane();
	Rectangle contentsBounds = new Rectangle(
		clientArea.getLocation().translate(nodeBounds.width / 2 + 10, rowHeight),
		contents.getPreferredSize()
	);
	contents.setBounds(contentsBounds);
}

/**
 * @see org.eclipse.draw2d.examples.tree.BranchLayout#paintLines(org.eclipse.draw2d.Graphics)
 */
void paintLines(Graphics g) {
	IFigure node = branch.getNode();
	IFigure contents = branch.getContentsPane();
	int x = node.getBounds().getCenter().x;
	int y = node.getBounds().bottom();
	List children = contents.getChildren();
	if (children.size() == 0)
		return;
	int bottom = y;
	for (int i=0; i<children.size(); i++){
		Point pt = ((TreeBranch)children.get(i)).getNodeBounds().getLeft();
		g.drawLine(x, pt.y, pt.x, pt.y);
		bottom = Math.max(bottom, pt.y);
	}
	g.drawLine(x, y, x, bottom);
}

void updateContours() {
	//Make sure we layout first
	branch.validate();

	cachedContourLeft = new int[getDepth()];
	cachedContourRight = new int[getDepth()];

	Rectangle clientArea = branch.getClientArea(Rectangle.SINGLETON);
	int rightEdge = clientArea.right();

	cachedContourLeft[0] = branch.getNodeBounds().x - clientArea.x;
	cachedContourRight[0] = rightEdge - branch.getNodeBounds().right();

	List subtrees = branch.contents.getChildren();
	TreeBranch subtree;

	int leftSide = branch.getNodeBounds().width / 2;
	for (int i = 1; i < getDepth(); i++)
		cachedContourLeft[i] = leftSide;

	int rightMargin;
	int offset = 1;
	for (int i = 0; i < subtrees.size(); i++) {
		subtree = (TreeBranch)subtrees.get(i);
		rightMargin = rightEdge - subtree.getBounds().right();
		int rightContour[] = subtree.getContourRight();
		for (int j = 0; j < rightContour.length; j++)
			cachedContourRight[j + offset] = rightContour[j] + rightMargin;
		offset += subtree.getDepth();
	}
}

}
