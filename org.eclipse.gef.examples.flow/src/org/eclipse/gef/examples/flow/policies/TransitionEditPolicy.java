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
package org.eclipse.gef.examples.flow.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.flow.model.Transition;
import org.eclipse.gef.examples.flow.model.commands.DeleteConnectionCommand;

/**
 * @author Daniel Lee
 */
public class TransitionEditPolicy extends ConnectionEditPolicy {

/**
 * @see ConnectionEditPolicy#getDeleteCommand(org.eclipse.gef.requests.GroupRequest)
 */
protected Command getDeleteCommand(GroupRequest request) {
	DeleteConnectionCommand cmd = new DeleteConnectionCommand();
	Transition t = (Transition)getHost().getModel();
	cmd.setTransition(t);
	cmd.setSource(t.source);
	cmd.setTarget(t.target);
	return cmd;
}

}
