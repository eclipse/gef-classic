/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Pratik Shah
 */
public class DragGuidePolicy 
	extends GraphicalEditPolicy 
{

protected GraphicalViewer diagramViewer;
private IFigure dummyGuideFigure, dummyLineFigure;
private boolean dragInProgress = false;

protected IFigure createDummyLineFigure() {
	return new Figure();
}

protected GuideFigure createDummyGuideFigure() {
	return new GuidePlaceHolder(getGuideEditPart().isHorizontal());
}

public void eraseSourceFeedback(Request request) {	
	getGuideEditPart().updateLocationOfFigures(getGuideEditPart().getZoomedPosition());
	if (getDummyGuideFigure().getParent() != null) {
		getDummyGuideFigure().getParent().remove(getDummyGuideFigure());			
	}
	if (getDummyFeedbackFigure().getParent() != null) {
		getDummyFeedbackFigure().getParent().remove(getDummyFeedbackFigure());
	}
	getGuideEditPart().setCurrentCursor(null);
	dragInProgress = false;
}

public Command getCommand(Request request) {
	Command cmd;
	final ChangeBoundsRequest req = (ChangeBoundsRequest)request;
	if (isDeleteRequest(req)) {
		cmd = getGuideEditPart().getRulerProvider()
				.getDeleteGuideCommand(getHost().getModel());
	} else {
		int pDelta;
		if (getGuideEditPart().isHorizontal()) {
			pDelta = req.getMoveDelta().y;
		} else {
			pDelta = req.getMoveDelta().x;
		}
		ZoomManager zoomManager = getGuideEditPart().getZoomManager();
		if (zoomManager != null) {
			pDelta *= (zoomManager.getUIMultiplier() 
					/ zoomManager.getZoom());				
		}
		cmd = getGuideEditPart().getRulerProvider()
				.getMoveGuideCommand(getHost().getModel(), pDelta);
	}
	return cmd;
}

protected IFigure getDummyFeedbackFigure() {
	if (dummyLineFigure == null) {
		dummyLineFigure = createDummyLineFigure();
	}
	return dummyLineFigure;
}

protected IFigure getDummyGuideFigure() {
	if (dummyGuideFigure == null) {
		dummyGuideFigure = createDummyGuideFigure();
	}
	return dummyGuideFigure;
}

protected GuideEditPart getGuideEditPart() {
	return (GuideEditPart)getHost();
}

protected boolean isDeleteRequest(ChangeBoundsRequest req) {
	int pos, max, min;
	if (getGuideEditPart().isHorizontal()) {
		pos = req.getLocation().x;
		Rectangle zone = getHostFigure().getBounds().getExpanded(15, 0);
		min = zone.x;
		max = min + zone.width;
	} else {
		pos = req.getLocation().y;
		Rectangle zone = getHostFigure().getBounds().getExpanded(0, 15);
		min = zone.y;
		max = min + zone.height;
	}
	return pos < min || pos > max;	
}

public void showSourceFeedback(Request request) {
	if (!dragInProgress) {
		dragInProgress = true;
		// add the placeholder guide figure to the ruler
		getHostFigure().getParent().add(getDummyGuideFigure(), 0);
		((GraphicalEditPart)getHost().getParent()).setLayoutConstraint(
				getHost(), getDummyGuideFigure(), 
				new Integer(getGuideEditPart().getZoomedPosition()));
		getDummyGuideFigure().setBounds(getHostFigure().getBounds());
		// add the placeholder feedback figure to the primary viewer
		getGuideEditPart().getGuideLayer().add(getDummyFeedbackFigure(), 0);
		getDummyFeedbackFigure().setBounds(getGuideEditPart().getGuideLineFigure()
				.getBounds());
		// move the guide being dragged to the last index so that it's drawn on
		// top of other guides
		List children = getHostFigure().getParent().getChildren();
		children.remove(getHostFigure());
		children.add(getHostFigure());
	}
	ChangeBoundsRequest req = (ChangeBoundsRequest)request;
	if (isDeleteRequest(req)) {
		getHostFigure().setVisible(false);
		getGuideEditPart().getGuideLineFigure().setVisible(false);
		getGuideEditPart().setCurrentCursor(SharedCursors.ARROW);
	} else {
		int positionDelta;
		if (getGuideEditPart().isHorizontal()) {
			positionDelta = req.getMoveDelta().y;
		} else {
			positionDelta = req.getMoveDelta().x;
		}
		getGuideEditPart().updateLocationOfFigures(
				getGuideEditPart().getZoomedPosition() + positionDelta);
		getHostFigure().setVisible(true);
		getGuideEditPart().getGuideLineFigure().setVisible(true);
		getGuideEditPart().setCurrentCursor(null);
	}
}

public boolean understandsRequest(Request req) {
	return req.getType().equals(REQ_MOVE);
}
	
}