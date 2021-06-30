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
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.commands.CompoundCommand;
import org.eclipse.gef3.editpolicies.ContainerEditPolicy;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.StructuredActivity;
import org.eclipse.gef3.examples.flow.model.commands.OrphanChildCommand;
import org.eclipse.gef3.requests.CreateRequest;
import org.eclipse.gef3.requests.GroupRequest;

/**
 * ActivityContainerEditPolicy
 * 
 * @author Daniel Lee
 */
public class ActivityContainerEditPolicy extends ContainerEditPolicy {

	/**
	 * @see ContainerEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/**
	 * @see org.eclipse.gef3.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(GroupRequest)
	 */
	protected Command getOrphanChildrenCommand(GroupRequest request) {
		List parts = request.getEditParts();
		CompoundCommand result = new CompoundCommand();
		for (int i = 0; i < parts.size(); i++) {
			OrphanChildCommand orphan = new OrphanChildCommand();
			orphan.setChild((Activity) ((EditPart) parts.get(i)).getModel());
			orphan.setParent((StructuredActivity) getHost().getModel());
			result.add(orphan);
		}
		return result.unwrap();
	}

}
