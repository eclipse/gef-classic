package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.gef.commands.*;

import com.ibm.etools.gef.examples.logicdesigner.LogicResources;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

public class LogicContainerEditPolicy
	extends com.ibm.etools.gef.editpolicies.ContainerEditPolicy
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
	CompoundCommand result = new CompoundCommand(LogicResources.getString("LogicContainerEditPolicy.OrphanCommandLabelText"));//$NON-NLS-1$
	for (int i=0; i<parts.size(); i++){
		OrphanChildCommand orphan = new OrphanChildCommand();
		orphan.setChild((LogicSubpart)((EditPart)parts.get(i)).getModel());
		orphan.setParent((LogicDiagram)getHost().getModel());
		orphan.setLabel(LogicResources.getString("LogicElementEditPolicy.OrphanCommandLabelText")); //$NON-NLS-1$
		result.add(orphan);
	}
	return result.unwrap();
}

}