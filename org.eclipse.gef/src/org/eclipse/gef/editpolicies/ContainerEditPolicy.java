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

public abstract class ContainerEditPolicy
	extends AbstractEditPolicy
{

protected final void createCreateCommand(Object parent, CreationFactory factory) { }

protected Command getAddCommand(GroupRequest request) {
	return null;
}

public Command getCommand(Request request) {
	if (REQ_CREATE.equals(request.getType()))
		return getCreateCommand((CreateRequest)request);
	if (REQ_ADD.equals(request.getType()))
		return getAddCommand((GroupRequest)request);
	if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand((GroupRequest)request);
	return null;
}

protected abstract Command getCreateCommand(CreateRequest request);

protected Command getOrphanChildrenCommand(GroupRequest request) {
	return null;
}

}