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

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.parts.RulerProvider;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

public class LogicXYLayoutEditPolicy
	extends org.eclipse.gef.editpolicies.XYLayoutEditPolicy
{

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
	Integer guidePos = (Integer)request.getExtendedData()
			.get(SnapToGuides.HORIZONTAL_GUIDE);
	if (guidePos != null) {
		int hAlignment = ((Integer)request.getExtendedData()
				.get(SnapToGuides.HORIZONTAL_ANCHOR)).intValue();
		cmd.setHorizontalGuide(findGuideAt(guidePos.intValue(), true), hAlignment);
	}
	guidePos = (Integer)request.getExtendedData()
			.get(SnapToGuides.VERTICAL_GUIDE);
	if (guidePos != null) {
		int vAlignment = ((Integer)request.getExtendedData()
				.get(SnapToGuides.VERTICAL_ANCHOR)).intValue();
		cmd.setVerticalGuide(findGuideAt(guidePos.intValue(), false), vAlignment);
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

protected LogicGuide findGuideAt(int pos, boolean horizontal) {
	List guides = ((RulerProvider)getHost().getViewer().getProperty(
			horizontal ? RulerProvider.VERTICAL : RulerProvider.HORIZONTAL)).getGuides();
	for (int i = 0; i < guides.size(); i++) {
		LogicGuide guide = (LogicGuide)guides.get(i);
		if (pos == guide.getPosition()) {
			return guide;
		}
	}
	throw new RuntimeException("LogicXYLayoutEditPolicy: Guide not found at position " + pos); //$NON-NLS-1$
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

}