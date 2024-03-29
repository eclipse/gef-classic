/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.commands.OrphanChildCommand;

public class LogicContainerEditPolicy extends ContainerEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	@Override
	public Command getOrphanChildrenCommand(GroupRequest request) {
		CompoundCommand result = new CompoundCommand(LogicMessages.LogicContainerEditPolicy_OrphanCommandLabelText);
		for (EditPart child : request.getEditParts()) {
			OrphanChildCommand orphan = new OrphanChildCommand();
			orphan.setChild((LogicSubpart) child.getModel());
			orphan.setParent((LogicDiagram) getHost().getModel());
			orphan.setLabel(LogicMessages.LogicElementEditPolicy_OrphanCommandLabelText);
			result.add(orphan);
		}
		return result.unwrap();
	}

}
