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
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.model.commands.CreateAndAssignSourceCommand;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Daniel Lee
 */
public class ActivitySourceEditPolicy extends ContainerEditPolicy {

/**
 * @see ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
 */
protected Command getCreateCommand(CreateRequest request) {
	CreateAndAssignSourceCommand cmd = new CreateAndAssignSourceCommand();
	cmd.setParent((StructuredActivity)getHost().getParent().getModel());
	cmd.setChild((Activity)request.getNewObject());
	cmd.setSource((Activity)getHost().getModel());
	return cmd;
}

/**
 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
 */
public EditPart getTargetEditPart(Request request) {
	if (REQ_CREATE.equals(request.getType()))
		return getHost();
	return super.getTargetEditPart(request);
}

}
