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
package org.eclipse.gef.examples.flow.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.model.commands.AddCommand;
import org.eclipse.gef.examples.flow.model.commands.CreateCommand;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Daniel Lee
 */
public class StructuredActivityLayoutEditPolicy extends FlowLayoutEditPolicy {

/**
 * @see OrderedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, 
 * 													org.eclipse.gef.EditPart)
 */
protected Command createAddCommand(EditPart child, EditPart after) {
	Activity activity = (Activity)child.getModel();
	AddCommand add = new AddCommand();
	add.setParent((StructuredActivity)getHost().getModel());
	add.setChild(activity);
	int index = getHost().getChildren().indexOf(after);
	add.setIndex(index);
	return add;
}

/**
 * @see OrderedLayoutEditPolicy#createMoveChildCommand(org.eclipse.gef.EditPart, 
 * 														org.eclipse.gef.EditPart)
 */
protected Command createMoveChildCommand(EditPart child, EditPart after) {
	return null;
}

/**
 * @see LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
 */
protected Command getCreateCommand(CreateRequest request) {
	CreateCommand command = new CreateCommand();
	EditPart after = getInsertionReference(request);
	command.setParent((StructuredActivity)getHost().getModel());
	command.setChild((Activity)request.getNewObject());
	int index = getHost().getChildren().indexOf(after);
	command.setIndex(index);
	return command;
}

/**
 * @see LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
 */
protected Command getDeleteDependantCommand(Request request) {
	return null;
}

/**
 * @see org.eclipse.gef.editpolicies.FlowLayoutEditPolicy#isHorizontal()
 */
protected boolean isHorizontal() {
	return false;
}

}
