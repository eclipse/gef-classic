package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.IToolBarManager;


import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.DeleteRequest;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.*;
import org.eclipse.gef.examples.logicdesigner.model.*;

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