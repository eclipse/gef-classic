package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Transposer;

/**
 * 
 * @author hudsonr
 * Created on Apr 23, 2003
 */
public class TreeRoot extends TreeBranch {

private int major = 10;
private int minor = 10;
private Transposer transposer = new Transposer();
private boolean compression;

/**
 * @param title
 */
public TreeRoot(IFigure title) {
	super(title);
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
public int getMinorSpacing() {
	return minor;
}

public TreeRoot getRoot() {
	return this;
}

public Transposer getTransposer() {
	return transposer;
}

public boolean isHorizontal() {
	return !transposer.isEnabled();
}

/**
 * sets the space (in pixels) between this branch's node and its subtrees.
 */
public void setMajorSpacing(int value) {
	this.major = value;
	invalidateTree();
	revalidate();
}

public void setHorizontal(boolean value) {
	transposer.setEnabled(!value);
	invalidateTree();
	revalidate();
}

/**
 * @param i
 */
public void setMinorSpacing(int i) {
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

/**
 * @return
 */
public boolean isCompressed() {
	return compression;
}

/**
 * @param b
 */
public void setCompression(boolean b) {
	compression = b;
}

}
