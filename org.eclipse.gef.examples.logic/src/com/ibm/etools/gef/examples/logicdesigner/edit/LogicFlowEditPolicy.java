package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;
import com.ibm.etools.gef.examples.logicdesigner.figures.*;

public class LogicFlowEditPolicy
	extends com.ibm.etools.gef.editpolicies.FlowLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, EditPart after) {
	AddCommand command = new AddCommand();
	command.setChild((LogicSubpart)child.getModel());
	command.setParent((LogicFlowContainer)getHost().getModel());
	int index = getHost().getChildren().indexOf(after);
	command.setIndex(index);
	return command;
}

protected EditPolicy createChildEditPolicy(EditPart child) {
	return new com.ibm.etools.gef.editpolicies.NonResizableEditPolicy();
}

protected Command createMoveChildCommand(EditPart child, EditPart after) {
	LogicSubpart childModel = (LogicSubpart)child.getModel();
	LogicDiagram parentModel = (LogicDiagram)getHost().getModel();
	int oldIndex = getHost().getChildren().indexOf(child);
	int newIndex = getHost().getChildren().indexOf(after);
	if (newIndex > oldIndex)
		newIndex--;
	ReorderPartCommand command = new ReorderPartCommand(childModel, parentModel, oldIndex, newIndex);
	return command;
}

protected Command getCreateCommand(CreateRequest request) {
	CreateCommand command = new CreateCommand();
	EditPart after = getInsertionReference(request);
	command.setChild((LogicSubpart)request.getNewObject());
	command.setParent((LogicFlowContainer)getHost().getModel());
	int index = getHost().getChildren().indexOf(after);
	command.setIndex(index);
	return command;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

protected Command getOrphanChildrenCommand(Request request) {
	return null;
}

}