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

/**
 * A block layout which requires no FlowContext to perform its layout. This class is used
 * by {@link FlowPage}.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 */
public class PageFlowLayout
	extends BlockFlowLayout
{

public PageFlowLayout(FlowPage page) {
	super(page);
}

protected void endBlock() { }

public void postValidate() { }

/**
 * Setup blockBox to the initial bounds of the Page
 */
protected void setupBlock() {
	//Remove all current Fragments
	blockBox.clear();

	//Setup the one fragment for this Block with the correct X and available width
	blockBox.setRecommendedWidth(((FlowPage)getFlowFigure()).getRecommendedWidth());
	blockBox.x = 0;
}

}