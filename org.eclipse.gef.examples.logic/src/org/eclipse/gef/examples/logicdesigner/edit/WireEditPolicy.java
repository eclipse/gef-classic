package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.model.ConnectionCommand;
import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.requests.GroupRequest;

public class WireEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEditPolicy
{

protected Command getDeleteCommand(GroupRequest request) {
	ConnectionCommand c = new ConnectionCommand();
	c.setWire((Wire)getHost().getModel());
	return c;
}

}