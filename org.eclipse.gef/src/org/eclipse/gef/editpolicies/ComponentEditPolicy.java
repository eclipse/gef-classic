package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.GEF;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.*;

public abstract class ComponentEditPolicy
	extends AbstractEditPolicy
{

protected abstract Command createDeleteCommand(DeleteRequest req);

/**
 * Get the command that performs an operation
 * of the type indicated by @commandString on the
 * receiver.  Data needed to create the command is
 * contained in @tool
 *
 * Possible values for the commandString depend on
 * the tool.  Default tools send "create" and "move".
 *
 * @return org.eclipse.gef.commands.ICommand  The command that performs the operation
 * @param commandString java.lang.String The type of command to create
 * @param commandData org.eclipse.gef.ICommandData Data needed to create the command
 */
public Command getCommand(Request request) {
	if (REQ_ORPHAN.equals(request.getType()))
		return getOrphanCommand();
	if (REQ_DELETE.equals(request.getType()))
		return getDeleteCommand((DeleteRequest)request);
	return null;
}

/**
 * Create a delete command to delete the receiver from the composition
 */
protected Command getDeleteCommand(DeleteRequest request) {
	CompoundCommand cc = new CompoundCommand();
	cc.setDebugLabel("Delete in ComponentEditPolicy");//$NON-NLS-1$

	cc.add(createDeleteCommand(request));

	ForwardedRequest deleteRequest = new ForwardedRequest(REQ_DELETE_DEPENDANT, getHost());
	cc.add(getHost().getParent().getCommand(deleteRequest));

	//Note that if CompoundCommand cc isEmpty(), the delete will not be executable.
	return cc.unwrap();
}

/**
 * Returns any contribution to orphaning this component from its container.
 * It is unusual that a child would have any additional orphaning requirements.
 * The orphan is typically handled by the parent's ContainerEditPolicy.
 * @see ContainerEditPolicy
 */
protected Command getOrphanCommand() {
	GEF.hack();
	GroupRequest req = new GroupRequest(REQ_ORPHAN_CHILDREN);
	req.setEditParts(getHost());
	return getHost().getParent().getCommand(req);
}

}