/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.commands.ConnectionCommand;

public class WireEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEditPolicy
{

protected Command getDeleteCommand(GroupRequest request) {
	ConnectionCommand c = new ConnectionCommand();
	c.setWire((Wire)getHost().getModel());
	return c;
}

}
