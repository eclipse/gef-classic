/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;


import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.commands.AddCommand;
import org.eclipse.gef.examples.logicdesigner.model.commands.CloneCommand;
import org.eclipse.gef.examples.logicdesigner.model.commands.CreateCommand;
import org.eclipse.gef.examples.logicdesigner.model.commands.ReorderPartCommand;

public class LogicFlowEditPolicy
	extends org.eclipse.gef.editpolicies.FlowLayoutEditPolicy
{

/**
 * Override to return the <code>Command</code> to perform an {@link
 * RequestConstants#REQ_CLONE CLONE}. By default, <code>null</code> is
 * returned.
 * @param request the Clone Request
 * @return A command to perform the Clone.
 */
protected Command getCloneCommand(ChangeBoundsRequest request) {
	CloneCommand clone = new CloneCommand();
	clone.setParent((LogicDiagram)getHost().getModel());
	
	EditPart after = getInsertionReference(request);
	int index = getHost().getChildren().indexOf(after);
	
	Iterator i = request.getEditParts().iterator();
	GraphicalEditPart currPart = null;
	
	while (i.hasNext()) {
		currPart = (GraphicalEditPart)i.next();
		clone.addPart((LogicSubpart)currPart.getModel(), index++);
	}
	
	return clone;
}
	
protected Command createAddCommand(EditPart child, EditPart after) {
	AddCommand command = new AddCommand();
	command.setChild((LogicSubpart)child.getModel());
	command.setParent((LogicFlowContainer)getHost().getModel());
	int index = getHost().getChildren().indexOf(after);
	command.setIndex(index);
	return command;
}

/**
 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
 */
protected EditPolicy createChildEditPolicy(EditPart child) {
	LogicResizableEditPolicy policy = new LogicResizableEditPolicy();
	policy.setResizeDirections(0);
	return policy;
}

protected Command createMoveChildCommand(EditPart child, EditPart after) {
	LogicSubpart childModel = (LogicSubpart)child.getModel();
	LogicDiagram parentModel = (LogicDiagram)getHost().getModel();
	int oldIndex = getHost().getChildren().indexOf(child);
	int newIndex = getHost().getChildren().indexOf(after);
	if (newIndex > oldIndex)
		newIndex--;
	ReorderPartCommand command = new ReorderPartCommand(childModel, parentModel, newIndex);
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