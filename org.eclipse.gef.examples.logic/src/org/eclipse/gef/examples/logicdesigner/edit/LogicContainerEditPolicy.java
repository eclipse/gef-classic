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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

public class LogicContainerEditPolicy
	extends ContainerEditPolicy
{

protected Command getCreateCommand(CreateRequest request) {
	return null;
}

public Command getOrphanChildrenCommand(GroupRequest request) {
	List parts = request.getEditParts();
	CompoundCommand result = 
		new CompoundCommand(LogicMessages.LogicContainerEditPolicy_OrphanCommandLabelText);
	for (int i = 0; i < parts.size(); i++) {
		OrphanChildCommand orphan = new OrphanChildCommand();
		orphan.setChild((LogicSubpart)((EditPart)parts.get(i)).getModel());
		orphan.setParent((LogicDiagram)getHost().getModel());
		orphan.setLabel(LogicMessages.LogicElementEditPolicy_OrphanCommandLabelText);
		result.add(orphan);
	}
	return result.unwrap();
}

}