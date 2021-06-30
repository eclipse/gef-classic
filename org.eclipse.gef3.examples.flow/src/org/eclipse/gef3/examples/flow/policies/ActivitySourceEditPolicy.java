/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.policies;

import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.commands.CompoundCommand;
import org.eclipse.gef3.editpolicies.AbstractEditPolicy;
import org.eclipse.gef3.editpolicies.ContainerEditPolicy;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.StructuredActivity;
import org.eclipse.gef3.examples.flow.model.commands.AddAndAssignSourceCommand;
import org.eclipse.gef3.examples.flow.model.commands.CreateAndAssignSourceCommand;
import org.eclipse.gef3.requests.CreateRequest;
import org.eclipse.gef3.requests.GroupRequest;

/**
 * @author Daniel Lee
 */
public class ActivitySourceEditPolicy extends ContainerEditPolicy {

	/**
	 * @see org.eclipse.gef3.editpolicies.ContainerEditPolicy#getAddCommand(GroupRequest)
	 */
	protected Command getAddCommand(GroupRequest request) {
		CompoundCommand cmd = new CompoundCommand();
		for (int i = 0; i < request.getEditParts().size(); i++) {
			AddAndAssignSourceCommand add = new AddAndAssignSourceCommand();
			add.setParent((StructuredActivity) getHost().getParent().getModel());
			add.setSource((Activity) getHost().getModel());
			add.setChild(((Activity) ((EditPart) request.getEditParts().get(i))
					.getModel()));
			cmd.add(add);
		}
		return cmd;
	}

	/**
	 * @see ContainerEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		CreateAndAssignSourceCommand cmd = new CreateAndAssignSourceCommand();
		cmd.setParent((StructuredActivity) getHost().getParent().getModel());
		cmd.setChild((Activity) request.getNewObject());
		cmd.setSource((Activity) getHost().getModel());
		return cmd;
	}

	/**
	 * @see AbstractEditPolicy#getTargetEditPart(org.eclipse.gef3.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			return getHost();
		if (REQ_ADD.equals(request.getType()))
			return getHost();
		if (REQ_MOVE.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

}
