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

import java.util.List;

import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.commands.CompoundCommand;
import org.eclipse.gef3.editpolicies.LayoutEditPolicy;
import org.eclipse.gef3.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.StructuredActivity;
import org.eclipse.gef3.examples.flow.model.commands.AddCommand;
import org.eclipse.gef3.examples.flow.model.commands.CreateCommand;
import org.eclipse.gef3.examples.flow.parts.SimpleActivityPart;
import org.eclipse.gef3.requests.ChangeBoundsRequest;
import org.eclipse.gef3.requests.CreateRequest;

/**
 * @author Daniel Lee
 */
public class StructuredActivityLayoutEditPolicy extends LayoutEditPolicy {

	protected Command createAddCommand(EditPart child) {
		Activity activity = (Activity) child.getModel();
		AddCommand add = new AddCommand();
		add.setParent((StructuredActivity) getHost().getModel());
		add.setChild(activity);
		return add;
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.OrderedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef3.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof SimpleActivityPart)
			return new SimpleActivitySelectionEditPolicy();
		return new NonResizableEditPolicy();
	}

	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return null;
	}

	protected Command getAddCommand(Request req) {
		ChangeBoundsRequest request = (ChangeBoundsRequest) req;
		List editParts = request.getEditParts();
		CompoundCommand command = new CompoundCommand();
		for (int i = 0; i < editParts.size(); i++) {
			EditPart child = (EditPart) editParts.get(i);
			command.add(createAddCommand(child));
		}
		return command.unwrap();
	}

	/**
	 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		CreateCommand command = new CreateCommand();
		command.setParent((StructuredActivity) getHost().getModel());
		command.setChild((Activity) request.getNewObject());
		return command;
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.LayoutEditPolicy#getMoveChildrenCommand(org.eclipse.gef3.Request)
	 */
	protected Command getMoveChildrenCommand(Request request) {
		return null;
	}

}
