package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;

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