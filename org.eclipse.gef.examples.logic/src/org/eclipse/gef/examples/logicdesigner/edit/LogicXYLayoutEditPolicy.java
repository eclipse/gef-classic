package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.requests.CreateRequest;

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

protected EditPolicy createChildEditPolicy(EditPart child){
	if (child instanceof LEDEditPart ||
	    child instanceof OutputEditPart || 
	    child instanceof LogicLabelEditPart) {
		return new org.eclipse.gef.editpolicies.NonResizableEditPolicy();
	}
	return new org.eclipse.gef.editpolicies.ResizableEditPolicy();
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

protected void showDragTargetFeedback(Request request) {
	// Don't show any target feedback on the GraphViewer
	// background.
}

//protected void showSizeOnDropFeedback(Tool tool) {
//	if (((CreationTool)tool).getNewObject() instanceof Circuit) {
//		super.showSizeOnDropFeedback(tool);
//	}
//}

}