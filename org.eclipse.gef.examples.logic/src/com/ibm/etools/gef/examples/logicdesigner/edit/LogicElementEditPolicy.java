package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.IToolBarManager;

import com.ibm.etools.common.command.Command;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.DeleteRequest;

import com.ibm.etools.gef.examples.logicdesigner.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

public class LogicElementEditPolicy
	extends com.ibm.etools.gef.editpolicies.ComponentEditPolicy
{

protected Command createDeleteCommand(DeleteRequest request) {
	Object parent = getHost().getParent().getModel();
	DeleteCommand deleteCmd = new DeleteCommand();
	deleteCmd.setParent((LogicDiagram)parent);
	deleteCmd.setChild((LogicSubpart)getHost().getModel());
	return deleteCmd;
}

}