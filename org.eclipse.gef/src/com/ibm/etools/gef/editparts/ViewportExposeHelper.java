package com.ibm.etools.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.EditPart;

public class ViewportExposeHelper
	implements ExposeHelper
{

private GraphicalEditPart owner;

public ViewportExposeHelper(GraphicalEditPart owner){
	this.owner = owner;
}

public void exposeDescendant(EditPart part) {
	Viewport port = findViewport(owner);
	if (port == null)
		return;
	IFigure target = ((GraphicalEditPart)part).getFigure();
	Rectangle exposeRegion = target.getBounds().getExpanded(5,5);
	target = target.getParent();
	while (target != null && target != port){
		target.translateToParent(exposeRegion);
		target = target.getParent();
	}
	Dimension viewportSize = port.getClientArea().getSize();
	Point topLeft = exposeRegion.getTopLeft();
	Point bottomRight = exposeRegion.
		getBottomRight().
		translate(viewportSize.getNegated());

	Point finalLocation = Point.min(topLeft,
		Point.max(bottomRight, port.getViewLocation()));
	Point startLocation = port.getViewLocation();

	int dx = finalLocation.x - startLocation.x;
	int dy = finalLocation.y - startLocation.y;

	int FRAMES = (Math.abs(dx)+Math.abs(dy))/15;
	FRAMES = Math.max(FRAMES, 2);
	FRAMES = Math.min(FRAMES, 6);

	int stepX = Math.min((dx / FRAMES), (viewportSize.width/3));
	int stepY = Math.min((dy / FRAMES), (viewportSize.height/3));

	for (int i=1; i<FRAMES; i++){
		port.setViewLocation(startLocation.x + stepX*i, startLocation.y + stepY*i);
		port.getUpdateManager().performUpdate();
	}
	port.setViewLocation(finalLocation);
}

private Viewport findViewport(GraphicalEditPart part) {
	IFigure figure = null;
	Viewport port = null;
	do {
		if (figure == null)
			figure= part.getContentPane();
		else
			figure = figure.getParent();
		if (figure instanceof Viewport) {
			port = (Viewport) figure;
			break;
		}
	} while (figure != part.getFigure() && figure != null);
	return port;
}

}
