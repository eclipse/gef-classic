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

/**
 * @since 3.1
 */
public class NestedLine extends LineBox {

InlineFlow owner;
private LineRoot root;

NestedLine(InlineFlow owner) {
	this.owner = owner;
}

/**
 * @see org.eclipse.draw2d.text.FlowBox#containsPoint(int, int)
 */
public boolean containsPoint(int x, int y) {
	//$TODO should contains use LineRoot?
	return x >= getX()
		&& x < getX() + getWidth()
		&& y >= getBaseline() - getAscentWithBorder()
		&& y < getBaseline() + getDescentWithBorder();
}

int getAscentWithBorder() {
	return contentAscent + FlowUtilities.getBorderAscent(owner);
}

int getDescentWithBorder() {
	return contentDescent + FlowUtilities.getBorderDescent(owner);
}

public int getBaseline() {
	return root.getBaseline();
}

LineRoot getLineRoot() {
	return root;
}

//int getVisibleAscent() {
//	return contentAscent + FlowUtilities.getBorderAscent(owner);
//}
//
//int getVisibleDescent() {
//	return contentDescent + FlowUtilities.getBorderDescent(owner);
//}

public int getOuterAscent() {
	return contentAscent + FlowUtilities.getBorderAscentWithMargin(owner);
}

public int getOuterDescent() {
	return contentDescent + FlowUtilities.getBorderDescentWithMargin(owner);
}

void setLineRoot(LineRoot root) {
	this.root = root;
	for (int i = 0; i < fragments.size(); i++)
		((FlowBox)fragments.get(i)).setLineRoot(root);
}

/**
 * @see org.eclipse.draw2d.text.CompositeBox#setLineTop(int)
 */
public void setLineTop(int top) {
	throw new RuntimeException("not supported");
}

}
