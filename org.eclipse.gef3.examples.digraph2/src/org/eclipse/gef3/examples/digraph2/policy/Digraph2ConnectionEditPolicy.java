/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef3.examples.digraph2.policy;

import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef3.requests.GroupRequest;

/**
 * A ConnectionEditPolicy for the Directed Graph Example Editor.
 * 
 * @author Anthony Hunter
 */
public class Digraph2ConnectionEditPolicy extends ConnectionEditPolicy {

	/*
	 * @see org.eclipse.gef3.editpolicies.ConnectionEditPolicy#getDeleteCommand(org.eclipse.gef3.requests.GroupRequest)
	 */
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		return null;
	}

}
