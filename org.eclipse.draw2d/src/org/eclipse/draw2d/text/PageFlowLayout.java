/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * A block layout which requires no FlowContext to perform its layout. This class is used
 * by {@link FlowPage}.
 * <p>
 * WARNING: This class is not intended to be subclassed by clients.
 */
public class PageFlowLayout
	extends BlockFlowLayout
{

/**
 * Creates a new PageFlowLayout with the given FlowPage
 * @param page the FlowPage
 */
public PageFlowLayout(FlowPage page) {
	super(page);
}

/**
 * Setup blockBox to the initial bounds of the Page
 */
protected void setupBlock() {
	int newWidth = ((FlowPage)getFlowFigure()).getRecommendedWidth();
	if (blockBox.recommendedWidth == newWidth)
		return;
	blockInvalid = true;
	blockBox.clear();
	blockBox.x = blockBox.y = 0;
	blockBox.setRecommendedWidth(newWidth);
}

}