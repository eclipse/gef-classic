package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;

public class FreeformViewport
	extends Viewport
{

public FreeformViewport(){
	super(true);
	setLayoutManager(new ViewportLayout(){
		public void layout(IFigure figure){
			//Do nothing, contents updates itself.
		}
	});
}

protected void readjustScrollBars(){
	if (getContents() == null)
		return;
	if (!(getContents() instanceof FreeformFigure))
		return;
	FreeformFigure ff = (FreeformFigure)getContents();
	Rectangle clientArea = getClientArea();
	ff.updateFreeformBounds(new Rectangle(0,0,clientArea.width, clientArea.height));

	Rectangle viewBounds = getContents().getBounds();

	getVerticalRangeModel().setExtent(clientArea.height);
	getVerticalRangeModel().setMinimum(viewBounds.y);
	getVerticalRangeModel().setMaximum(viewBounds.bottom());		
	getHorizontalRangeModel().setExtent(clientArea.width);
	getHorizontalRangeModel().setMinimum(viewBounds.x);
	getHorizontalRangeModel().setMaximum(viewBounds.right());
}

protected boolean useLocalCoordinates(){
	return true;
}

}
