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
package org.eclipse.gef.ui.parts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.EditPart;
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

protected FigureCanvas getFigureCanvas(){
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

public void reveal(EditPart part) {
	super.reveal(part);
	Viewport port = getFigureCanvas().getViewport();
	IFigure target = ((GraphicalEditPart)part).getFigure();
	Rectangle exposeRegion = target.getBounds().getExpanded(5, 5);
	target = target.getParent();
	while (target != null && target != port) {
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

protected void setRootFigure(IFigure figure){
	rootFigure = figure;
	installRootFigure();
}

}