package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;

import com.ibm.etools.gef.examples.logicdesigner.model.*;

public class WireEditPolicy
	extends com.ibm.etools.gef.editpolicies.ConnectionEditPolicy
{

protected Command getDeleteCommand(DeleteRequest request) {
	ConnectionCommand c = new ConnectionCommand();
	c.setWire((Wire)getHost().getModel());
	return c;
}

}