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