/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.draw2d.geometry.*;

/**
 * The root of a Flow hierarchy. A flow page can be treated as a normal figure, but
 * contains FlowFigures.
 * 
 * <P>A FlowPage will not have a defined width unless it is inside a figure whose layout
 * provides width hints when calling {@link IFigure#getPreferredSize(int, int)}.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 */
public class FlowPage
	extends BlockFlow
{

private Dimension pageSize = new Dimension();
private int recommendedWidth;

/**
 * @see org.eclipse.draw2d.text.BlockFlow#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new PageFlowLayout(this);
}

/**
 * @see org.eclipse.draw2d.Figure#getMinimumSize()
 */
public Dimension getMinimumSize(int w, int h) {
	return getPreferredSize(w, h);
}

public Dimension getPreferredSize(int w, int h) {
	int width = w;
	if (w >= 0) {
		width = Math.max(0, w - getInsets().getWidth());
	}
	
	//Flowpage must temporarily layout to determine its preferred size
	int oldWidth = getRecommendedWidth();
	setRecommendedWidth(width);
	validate();
	Dimension result = pageSize.getExpanded(getInsets().getWidth(), getInsets().getHeight());
	
	//Undo the temporary layout by restoring the old recommended width, and laying out if needed
	if (getRecommendedWidth() != oldWidth) {
		setRecommendedWidth(oldWidth);
		getUpdateManager().addInvalidFigure(this);
	}
	return result;
}

int getRecommendedWidth() {
	return recommendedWidth;
}

public void postValidate(){
	Rectangle r = getBlockBox().toRectangle();
	pageSize.width = r.width;
	pageSize.height = r.height;
	List v = getChildren();
	for (int i=0; i<v.size(); i++)
		((FlowFigure)v.get(i)).postValidate();
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#setBounds(Rectangle)
 */
public void setBounds(Rectangle r) {
	super.setBounds(r);
	int newWidth = r.width - getInsets().getWidth();
	if (getRecommendedWidth() != newWidth) {
		setRecommendedWidth(newWidth);
		getUpdateManager().addInvalidFigure(this);
	}
}

private void setRecommendedWidth(int width) {
	if (recommendedWidth == width)
		return;
	recommendedWidth = width;
	invalidate();
}

public void validate() {
	if (isValid())
		return;
	super.validate();
	postValidate();
}

}