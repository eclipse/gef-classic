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
package org.eclipse.gef.editpolicies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;

/**
 * An EditPolicy for use with container editparts.  This policy can be used to contribute
 * commands to add, create, and orphan requests.
 * @author hudsonr
 */
public abstract class ContainerEditPolicy
	extends AbstractEditPolicy
{

/**
 * @deprecated this is an archaic method left behind to force compile errors on old code.
 * @param parent the parent object
 * @param factory the factory
 */
protected final void createCreateCommand(Object parent, CreationFactory factory) { }

/**
 * Override to contribute to add requests.
 * @param request the add request
 * @return the command contribution to the add
 */
protected Command getAddCommand(GroupRequest request) {
	return null;
}

/**
 * Overridden to check for add, create, and orphan.
 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
 */
public Command getCommand(Request request) {
	if (REQ_CREATE.equals(request.getType()))
		return getCreateCommand((CreateRequest)request);
	if (REQ_ADD.equals(request.getType()))
		return getAddCommand((GroupRequest)request);
	if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand((GroupRequest)request);
	return null;
}

/**
 * Clients must implement to contribute to create requests.
 * @param request the create request
 * @return <code>null</code> or a command contribution
 */
protected abstract Command getCreateCommand(CreateRequest request);

/**
 * Override to contribute to orphan requests.
 * @param request the orphan request
 * @return a command contribution for the orphan
 */
protected Command getOrphanChildrenCommand(GroupRequest request) {
	return null;
}

}