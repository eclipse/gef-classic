package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
			.union(wHint, hHint)
			.getSize();
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
