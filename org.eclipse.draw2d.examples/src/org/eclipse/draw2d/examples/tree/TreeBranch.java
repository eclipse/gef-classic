package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * Created on Apr 21, 2003
 */
public class TreeBranch extends Figure {

public static final int STYLE_HANGING = 1;
public static final int STYLE_NORMAL = 2;

int aligment = PositionConstants.CENTER;

IFigure contents = new Layer();

IFigure node;
int style;

TreeBranch(IFigure title) {
	this(title, STYLE_NORMAL);
}

TreeBranch(IFigure title, int style) {
	setStyle(style);
	if (title.getBorder() == null)
		title.setBorder(new LineBorder(ColorConstants.gray, 2));
	this.node = title;
	add(title);
	add(contents);
}

/**
 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
 */
public boolean containsPoint(int x, int y) {
	return node.containsPoint(x, y)
		|| contents.containsPoint(x, y);
}

public int getAlignment() {
	return aligment;
}

protected BranchLayout getBranchLayout() {
	return (BranchLayout)getLayoutManager();
}

public IFigure getContentsPane() {
	return contents;
}

public int[] getContourLeft() {
	return getBranchLayout().getContourLeft();
}

public int[] getContourRight() {
	return getBranchLayout().getContourRight();
}


final int getDepth() {
	return getBranchLayout().getDepth();
}

/**
 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
 */
public Dimension getMinimumSize(int wHint, int hHint) {
	validate();
	return super.getMinimumSize(wHint, hHint);
}

public IFigure getNode() {
	return node;
}

public Rectangle getNodeBounds() {
	return node.getBounds();
}

public int[] getPreferredRowHeights() {
	return getBranchLayout().getPreferredRowHeights();
}
/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	validate();
	return super.getPreferredSize(wHint, hHint);
}

public TreeRoot getRoot() {
	return ((TreeBranch)getParent().getParent()).getRoot();
}

public int getStyle() {
	return style;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
	getBranchLayout().paintLines(graphics);
}

public void setAlignment(int value) {
	aligment = value;
	revalidate();
}

public void setNode(IFigure node) {
	remove(this.node);
	add(this.node, 0);
}

final void setRowHeights(int heights[], int offset) {
	getBranchLayout().setRowHeights(heights, offset);
}

public void setStyle(int style) {
	this.style = style;
	switch (style) {
		case STYLE_HANGING :
			setLayoutManager(new HangingLayout(this));
			break;

		default :
			setLayoutManager(new NormalLayout(this));
			contents.setLayoutManager(new TreeLayout());
			break;
	}
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
		result += getChildren().get(0) + "\n";
	}
	for (int i=0; i<contents.getChildren().size(); i++)
		result += ((TreeBranch)contents.getChildren().get(i)).toString(level + 1);
	return result;
}

/**
 * @see org.eclipse.draw2d.Figure#validate()
 */
public void validate() {
	if (isValid())
		return;
	if (style == STYLE_HANGING) {
		ToolbarLayout layout = new ToolbarLayout(!getRoot().isHorizontal());
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		contents.setLayoutManager(layout);
	}
	repaint();
	super.validate();
}

}
