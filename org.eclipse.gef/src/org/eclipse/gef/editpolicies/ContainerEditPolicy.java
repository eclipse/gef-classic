package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.*;

public abstract class ContainerEditPolicy
	extends AbstractEditPolicy
{

protected final void createCreateCommand(Object parent, CreateRequest.Factory factory) { }

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