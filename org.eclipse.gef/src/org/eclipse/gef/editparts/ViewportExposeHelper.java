package org.eclipse.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.EditPart;

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

//exposeRegion starts off as target's expanded bounds in absolute coordinates
	Rectangle exposeRegion = target.getBounds().getExpanded(5,5);
	target.translateToAbsolute(exposeRegion);

//Offset is the contents top-left corner in absolute coordinates
	Point offset = port.getContents().getBounds().getLocation();
	port.getContents().translateToAbsolute(offset);
//Subtract the offset, making the region now "view" relative.
	exposeRegion.translate(offset.negate());

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
	FRAMES = Math.max(FRAMES, 3);
	FRAMES = Math.min(FRAMES, 8);

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
