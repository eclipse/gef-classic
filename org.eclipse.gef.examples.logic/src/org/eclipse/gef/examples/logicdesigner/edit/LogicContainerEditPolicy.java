package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.*;

public class LogicContainerEditPolicy
	extends org.eclipse.gef.editpolicies.ContainerEditPolicy
{

protected Command getAddCommand(GroupRequest request){
	return null;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

protected Command getCreateCommand(CreateRequest request) {
	return null;
}

public Command getOrphanChildrenCommand(GroupRequest request){
	List parts = request.getEditParts();
	CompoundCommand result = new CompoundCommand(LogicMessages.LogicContainerEditPolicy_OrphanCommandLabelText);
	for (int i=0; i<parts.size(); i++){
		OrphanChildCommand orphan = new OrphanChildCommand();
		orphan.setChild((LogicSubpart)((EditPart)parts.get(i)).getModel());
		orphan.setParent((LogicDiagram)getHost().getModel());
		orphan.setLabel(LogicMessages.LogicElementEditPolicy_OrphanCommandLabelText);
		result.add(orphan);
	}
	return result.unwrap();
}

}