package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DeleteRequest;

abstract public class ConnectionEditPolicy
	extends AbstractEditPolicy
{

/**@deprecated */
protected final void createDeleteCommand(DeleteRequest req)
	throws Exception{}

public Command getCommand(Request request){
	if (REQ_SOURCE_DELETED.equals(request.getType()))
		return getSourceDeletedCommand((DeleteRequest)request);
	if (REQ_TARGET_DELETED.equals(request.getType()))
		return getTargetDeletedCommand((DeleteRequest)request);
	if (REQ_DELETE.equals(request.getType()))
		return getDeleteCommand((DeleteRequest)request);
	return null;
}

abstract protected Command getDeleteCommand(DeleteRequest request);

protected Command getSourceDeletedCommand(DeleteRequest request){
	return getDeleteCommand(request);
}

protected Command getTargetDeletedCommand(DeleteRequest request){
	return getDeleteCommand(request);
}

}