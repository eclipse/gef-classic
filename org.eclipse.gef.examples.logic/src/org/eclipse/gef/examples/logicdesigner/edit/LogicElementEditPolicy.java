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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

import org.eclipse.gef.requests.GroupRequest;

public class LogicElementEditPolicy
	extends org.eclipse.gef.editpolicies.ComponentEditPolicy
{

protected Command createDeleteCommand(GroupRequest request) {
	Object parent = getHost().getParent().getModel();
	DeleteCommand deleteCmd = new DeleteCommand();
	deleteCmd.setParent((LogicDiagram)parent);
	deleteCmd.setChild((LogicSubpart)getHost().getModel());
	return deleteCmd;
}

}