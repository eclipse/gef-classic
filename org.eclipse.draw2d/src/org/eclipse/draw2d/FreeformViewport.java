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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

public class FreeformViewport
	extends Viewport
{

class FreeformViewportLayout
	extends ViewportLayout
{
	protected Dimension calculatePreferredSize(IFigure parent, int wHint, int hHint) {
		getContents().validate();
		wHint = Math.max(0,wHint);
		hHint = Math.max(0,hHint);
		return ((FreeformFigure)getContents())
			.getFreeformExtent()
			.getExpanded(getInsets())
			.union(0,0)
			.union(wHint-1, hHint-1)
			.getSize();
	}

	protected boolean isSensitiveHorizontally(IFigure parent) {
		return true;
	}

	protected boolean isSensitiveVertically(IFigure parent) {
		return true;
	}


	public void layout(IFigure figure) {
		//Do nothing, contents updates itself.
	}
}

public FreeformViewport(){
	super(true); //Must use graphics translate to scroll freeforms.
	setLayoutManager(new FreeformViewportLayout());
}

protected void readjustScrollBars(){
	if (getContents() == null)
		return;
	if (!(getContents() instanceof FreeformFigure))
		return;
	FreeformFigure ff = (FreeformFigure)getContents();
	Rectangle clientArea = getClientArea();
	Rectangle bounds = ff.getFreeformExtent().getCopy();
	bounds.union(0, 0, clientArea.width, clientArea.height);
	ff.setFreeformBounds(bounds);

	getVerticalRangeModel().setExtent(clientArea.height);
	getVerticalRangeModel().setMinimum(bounds.y);
	getVerticalRangeModel().setMaximum(bounds.bottom());		
	getHorizontalRangeModel().setExtent(clientArea.width);
	getHorizontalRangeModel().setMinimum(bounds.x);
	getHorizontalRangeModel().setMaximum(bounds.right());
}

protected boolean useLocalCoordinates(){
	return true;
}

}
