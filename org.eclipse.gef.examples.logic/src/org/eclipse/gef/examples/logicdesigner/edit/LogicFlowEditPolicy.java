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


import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

import org.eclipse.gef.requests.CreateRequest;

public class LogicFlowEditPolicy
	extends org.eclipse.gef.editpolicies.FlowLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, EditPart after) {
	AddCommand command = new AddCommand();
	command.setChild((LogicSubpart)child.getModel());
	command.setParent((LogicFlowContainer)getHost().getModel());
	int index = getHost().getChildren().indexOf(after);
	command.setIndex(index);
	return command;
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