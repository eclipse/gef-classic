package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * Created on Apr 21, 2003
 */
public class TreeBranch extends Figure {

class AnimatingLayer extends Layer {

public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension size = super.getPreferredSize(wHint, hHint);
	if (animation >= 0) {
		size = size.getCopy().scale((double)animation / DURATION);
	}
	return size;
}

}

private static final int DURATION = 280;
private static final int JUMP = 70;

public static final int STYLE_HANGING = 1;
public static final int STYLE_NORMAL = 2;

int aligment = PositionConstants.CENTER;
long animation = -1;

/*
 * A layer is being used simply because it is the only "transparent" figure in draw2d. See
 * the implementation of Layer.containsPoint(...) for what is meant by "transparent". If a
 * layer is not used, then overlapping branches will cause hit-test problems.
 */
AnimatingLayer contents = new AnimatingLayer();
boolean expanded = true;

int left[];

IFigure node;
int right[];
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

public void expand() {
	if (expanded)
		return;
	setExpanded(true);

	revalidate();
	getParent().validate();
	right = getContourRight();
	left = getContourLeft();

	long start = System.currentTimeMillis();
	long current = start;
	do {
		animation = current - start + JUMP;
		revalidate();
		getUpdateManager().performUpdate();
		current = System.currentTimeMillis();
	} while ((current - start) < (DURATION - JUMP));
	right = null;
	left = null;
	animation = -1;
	revalidate();
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
	if (animation != -1) {
		return scaledContour (left, getBranchLayout().getContourLeft());
	}
	return getBranchLayout().getContourLeft();
}

public int[] getContourRight() {
	if (animation != -1)
		return scaledContour(right, getBranchLayout().getContourRight());
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
 * @return
 */
public boolean isExpanded() {
	return expanded;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
	if (animation == -1 && isExpanded())
		getBranchLayout().paintLines(graphics);
}

int[] scaledContour(int [] source, int[] actual) {
	int result[] = new int[source.length];
	result[0] = Math.min(source[0], actual[0]);
	for (int i=1; i<source.length; i++)
		result[i] = (int)(source[i] + 50 - 50 * animation / DURATION);
	return result;
}

public void setAlignment(int value) {
	aligment = value;
	revalidate();
}

/**
 * @param b
 */
public void setExpanded(boolean b) {
	if (expanded == b)
		return;
	expanded = b;
	contents.setVisible(b);
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
	result += getChildren().get(0) + "\n";
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
