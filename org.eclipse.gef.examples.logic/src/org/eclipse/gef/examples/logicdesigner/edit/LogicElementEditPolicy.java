package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.requests.DeleteRequest;

public class LogicElementEditPolicy
	extends org.eclipse.gef.editpolicies.ComponentEditPolicy
{

protected Command createDeleteCommand(DeleteRequest request) {
	Object parent = getHost().getParent().getModel();
	DeleteCommand deleteCmd = new DeleteCommand();
	deleteCmd.setParent((LogicDiagram)parent);
	deleteCmd.setChild((LogicSubpart)getHost().getModel());
	return deleteCmd;
}

}