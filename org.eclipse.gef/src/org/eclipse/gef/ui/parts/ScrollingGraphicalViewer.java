package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.GraphicalEditPart;

public class ScrollingGraphicalViewer
	extends GraphicalViewerImpl
{

public ScrollingGraphicalViewer() { }

public final Control createControl(Composite parent){
	FigureCanvas canvas = new FigureCanvas(parent, getLightweightSystem());
	super.setControl(canvas);
	installRootFigure();
	return canvas;
}

protected void expose(org.eclipse.gef.EditPart part){
	super.expose(part);
	Viewport port = getFigureCanvas().getViewport();
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
		translate(viewportSize.negate());
	Point finalLocation = Point.min(topLeft,
		Point.max(bottomRight, port.getViewLocation()));
	getFigureCanvas().scrollSmoothTo(finalLocation.x, finalLocation.y);
}

private FigureCanvas getFigureCanvas(){
	return (FigureCanvas)getControl();
}

private void installRootFigure(){
	if (getFigureCanvas() == null)
		return;
	if (rootFigure instanceof Viewport)
		getFigureCanvas().setViewport((Viewport)rootFigure);
	else
		getFigureCanvas().setContents(rootFigure);
}

protected void setRootFigure(IFigure figure){
	rootFigure = figure;
	installRootFigure();
}

}