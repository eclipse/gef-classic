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
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

public class LogicXYLayoutEditPolicy
	extends org.eclipse.gef.editpolicies.XYLayoutEditPolicy
{
	
private List figures = new ArrayList();

protected Command createAddCommand(EditPart childEditPart, Object constraint) {

	LogicSubpart part = (LogicSubpart)childEditPart.getModel();
	Rectangle rect = (Rectangle)constraint;

	AddCommand add = new AddCommand();
	add.setParent((LogicDiagram)getHost().getModel());
	add.setChild(part);
	add.setLabel(LogicMessages.LogicXYLayoutEditPolicy_AddCommandLabelText);
	add.setDebugLabel("LogicXYEP add subpart");//$NON-NLS-1$

	SetConstraintCommand setConstraint = new SetConstraintCommand();

	setConstraint.setLocation(rect);
	setConstraint.setPart(part);
	setConstraint.setLabel(LogicMessages.LogicXYLayoutEditPolicy_AddCommandLabelText);
	setConstraint.setDebugLabel("LogicXYEP setConstraint");//$NON-NLS-1$
	return add.chain(setConstraint);
}

protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
	SetConstraintCommand locationCommand = new SetConstraintCommand();
	locationCommand.setPart((LogicSubpart)child.getModel());
	locationCommand.setLocation((Rectangle)constraint);
	return locationCommand;
}

protected Command createChangeConstraintCommand(ChangeBoundsRequest request, 
		EditPart child, Object constraint) {
	SetConstraintCommand cmd = (SetConstraintCommand)createChangeConstraintCommand(
			child, constraint);
	Guide hGuide = (Guide)request.getExtendedData().get(SnapToGuides.HORIZONTAL_GUIDE);
	if (hGuide != null) {
		int hAlignment = ((Integer)request.getExtendedData()
				.get(SnapToGuides.HORIZONTAL_ANCHOR)).intValue();
		cmd.setHorizontalGuide(hGuide, hAlignment);
	}
	Guide vGuide = (Guide)request.getExtendedData().get(SnapToGuides.VERTICAL_GUIDE);
	if (vGuide != null) {
		int vAlignment = ((Integer)request.getExtendedData()
				.get(SnapToGuides.VERTICAL_ANCHOR)).intValue();
		cmd.setVerticalGuide(vGuide, vAlignment);
	}
	return cmd;
}

protected EditPolicy createChildEditPolicy(EditPart child) {
	if (child instanceof LEDEditPart ||
	    child instanceof OutputEditPart || 
	    child instanceof LogicLabelEditPart) {
		return new NonResizableEditPolicy();
	}
	return super.createChildEditPolicy(child);
}

protected void eraseLayoutTargetFeedback(Request request) {
	for (Iterator iter = figures.iterator(); iter.hasNext();) {
		IFigure fig = (IFigure)iter.next();
		if (getLayer(LayerConstants.FEEDBACK_LAYER).getChildren().contains(fig)) {
			fig.getParent().remove(fig);
		}
	}
	figures.clear();
}

protected Command getCreateCommand(CreateRequest request) {
	CreateCommand create = new CreateCommand();
	create.setParent((LogicDiagram)getHost().getModel());
	create.setChild((LogicSubpart)request.getNewObject());
	Rectangle constraint = (Rectangle)getConstraintFor(request);
	create.setLocation(constraint);
	create.setLabel(LogicMessages.LogicXYLayoutEditPolicy_CreateCommandLabelText);
	return create;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

protected Command getOrphanChildrenCommand(Request request) {
	return null;
}

protected void showLayoutTargetFeedback(Request request) {
	eraseLayoutTargetFeedback(request);
	if (request.getType().equals(REQ_MOVE) || request.getType().equals(REQ_RESIZE)) {
		ChangeBoundsRequest req = (ChangeBoundsRequest)request;
		Guide vGuide = (Guide)req.getExtendedData()
				.get(SnapToGuides.VERTICAL_GUIDE);
		if (vGuide != null) {
			int alignment = ((Integer)req.getExtendedData()
					.get(SnapToGuides.VERTICAL_ANCHOR)).intValue();
			IFigure part = ((GraphicalEditPart)req.getEditParts().get(0)).getFigure();
			Point start = null, end = null;
			Rectangle figBounds = part.getBounds();
			switch (alignment) {
				case -1:
					start = figBounds.getTopLeft().translate(req.getMoveDelta());
					end = figBounds.getBottomLeft().translate(req.getMoveDelta());
					break;
				case 0:
					start = figBounds.getTop().translate(req.getMoveDelta());
					end = figBounds.getBottom().translate(req.getMoveDelta());
					break;
				case 1:
					start = figBounds.getTopRight().translate(req.getMoveDelta());
					end = figBounds.getBottomRight().translate(req.getMoveDelta());
			}
			Figure fig = new Figure();
			fig.setOpaque(true);
			fig.setBackgroundColor(ColorConstants.red);
			getLayer(LayerConstants.FEEDBACK_LAYER).add(fig);
			fig.setBounds(new Rectangle(start.x - 1, start.y, 3, end.y - start.y));
			figures.add(fig);
		}
		Guide hGuide = (Guide)req.getExtendedData()
				.get(SnapToGuides.HORIZONTAL_GUIDE);
		if (hGuide != null) {
			int alignment = ((Integer)req.getExtendedData()
					.get(SnapToGuides.HORIZONTAL_ANCHOR)).intValue();
			IFigure part = ((GraphicalEditPart)req.getEditParts().get(0)).getFigure();
			Point start = null, end = null;
			Rectangle figBounds = part.getBounds();
			switch (alignment) {
				case -1:
					start = figBounds.getTopLeft().translate(req.getMoveDelta());
					end = figBounds.getTopRight().translate(req.getMoveDelta());
					break;
				case 0:
					start = figBounds.getLeft().translate(req.getMoveDelta());
					end = figBounds.getRight().translate(req.getMoveDelta());
					break;
				case 1:
					start = figBounds.getBottomLeft().translate(req.getMoveDelta());
					end = figBounds.getBottomRight().translate(req.getMoveDelta());
			}
			Figure fig = new Figure();
			fig.setOpaque(true);
			fig.setBackgroundColor(ColorConstants.red);
			getLayer(LayerConstants.FEEDBACK_LAYER).add(fig);
			fig.setBounds(new Rectangle(start.x, start.y - 1, end.x - start.x, 3));
			figures.add(fig);
		}
	}
}

}