package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.IFigure;

/**
 * 
 * @author hudsonr
 * Created on Apr 23, 2003
 */
public class TreeRoot extends TreeBranch {

private int major = 10;
private int minor = 10;

/**
 * @param title
 */
public TreeRoot(IFigure title) {
	super(title);
	// TODO Auto-generated constructor stub
}

/**
 * @param title
 * @param style
 */
public TreeRoot(IFigure title, int style) {
	super(title, style);
}

public int getMajorSpacing() {
	return major;
}

/**
 * @return
 */
public int getMinor() {
	return minor;
}

public TreeRoot getRoot() {
	return this;
}

/**
 * sets the space (in pixels) between this branch's node and its subtrees.
 */
public void setMajorSpacing(int value) {
	this.major = value;
	invalidateTree();
	revalidate();
}

/**
 * @param i
 */
public void setMinor(int i) {
	minor = i;
	invalidateTree();
	revalidate();
}

/**
 * @see org.eclipse.draw2d.Figure#validate()
 */
public void validate() {
	if (isValid())
		return;
	setRowHeights(getPreferredRowHeights(), 0);
	super.validate();
}

}
