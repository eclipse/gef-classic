package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

public class LogicTreeEditPolicy
	extends AbstractEditPolicy
{

public Command getCommand(Request req){
	if (REQ_MOVE.equals(req.getType()))
		return getMoveCommand(req);
	return null;	
}

protected Command getMoveCommand(Request req){
	EditPart parent = getHost().getParent();
	if(parent != null){
		req.setType(REQ_MOVE_CHILDREN);
		Command cmd = parent.getCommand(req);
		req.setType(REQ_MOVE);
		return cmd;
	} else {
		return UnexecutableCommand.INSTANCE;
	}
}

}