package org.eclipse.draw2d.examples.tree;

import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * Created on Apr 21, 2003
 */
public class TreeBranch extends Figure {

class AnimatingLayer extends Layer {

/**
 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
 */
public void setBounds(Rectangle rect) {

	int x = bounds.x,
		y = bounds.y;

	boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height),
		  translate = (rect.x != x) || (rect.y != y);

	if (isVisible() && (resize || translate))
		erase();
	if (translate) {
		int dx = rect.x - x;
		int dy = rect.y - y;
		primTranslate(dx, dy);
	}
	bounds.width = rect.width;
	bounds.height = rect.height;
//	if (resize)  Layouts dont depend on size.
//		invalidate();
	if (resize || translate) {
		fireMoved();
		repaint();
	}
}

}

public static final int STYLE_HANGING = 1;
public static final int STYLE_NORMAL = 2;

int aligment = PositionConstants.CENTER;

/*
 * A layer is being used simply because it is the only "transparent" figure in draw2d. See
 * the implementation of Layer.containsPoint(...) for what is meant by "transparent". If a
 * layer is not used, then overlapping branches will cause hit-test problems.
 */
AnimatingLayer contents = new AnimatingLayer();
boolean expanded = true;

IFigure node;
int style;

public TreeBranch(IFigure title) {
	this(title, STYLE_NORMAL);
}

public TreeBranch(IFigure title, int style) {
	setStyle(style);
	if (title.getBorder() == null)
		title.setBorder(new LineBorder(ColorConstants.gray, 2));
	this.node = title;
	add(contents);
	add(title);
}

/**
 * recursively set all nodes and sub-treebranch nodes to the same location.  This gives
 * the appearance of all nodes coming from the same place.
 * @param bounds where to set
 */
public void animationReset(Rectangle bounds) {
	List subtrees = contents.getChildren();
	contents.setBounds(bounds);

	//Make the center of this node match the center of the given bounds
	Rectangle r = node.getBounds();
	int dx = bounds.x + bounds.width / 2 - r.x - r.width/2;
	int dy = bounds.y + bounds.height/ 2 - r.y - r.height/2;
	node.translate(dx, dy);
	revalidate(); //Otherwise, this branch will not layout
	
	//Pass the location to all children
	for (int i=0; i<subtrees.size(); i++){
		TreeBranch subtree = (TreeBranch)subtrees.get(i);
		subtree.setBounds(bounds);
		subtree.animationReset(bounds);
	}
}

public void collapse() {
	if (!expanded)
		return;
	setExpanded(false);
	IFigure root = this;
	while (root.getParent() != null)
		root = root.getParent();
	root.validate();
	setExpanded(true);
	animationReset(getNodeBounds());
	Animation.mark();
	Animation.captureLayout(getRoot());
	Animation.swap();
	while(Animation.step())
		getUpdateManager().performUpdate();
	Animation.end();
	setExpanded(false);
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
	animationReset(getNodeBounds());
	
	Animation.mark();
	Animation.captureLayout(getRoot());
	while(Animation.step())
		getUpdateManager().performUpdate();
	Animation.end();
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
	if (!Animation.PLAYBACK)
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
	if (!Animation.PLAYBACK)
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
	if (isExpanded())
		getBranchLayout().paintLines(graphics);
//	if (getDepth() == 2)
//		graphics.drawRectangle(getBounds().getResized(-1, -1));
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
	if (this.style == style)
		return;
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
	result += getChildren().get(1) + "\n";
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
		ToolbarLayout layout = new ToolbarLayout(!getRoot().isHorizontal()) {
			public void layout(IFigure parent) {
				Animation.recordInitialState(parent);
				if (Animation.playbackState(parent))
					return;
				
				super.layout(parent);
			}

		};
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		contents.setLayoutManager(layout);
	}
	repaint();
	super.validate();
}

}
