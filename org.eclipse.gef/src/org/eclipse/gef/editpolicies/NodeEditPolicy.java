package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

public class NodeEditPolicy
	extends AbstractEditPolicy
{

/**
 * By default, handle an ancestor deletion the same as if the host's model
 * were being deleted.
 */
protected Command getAncestorDeletedCommand(DeleteRequest req) {
	return getDeleteCommand(req);
}

/**
 * Returns a command or null based on the request
 */
public Command getCommand(Request request) {
	if (REQ_ANCESTOR_DELETED.equals(request.getType()))
		return getAncestorDeletedCommand((DeleteRequest)request);
	if (REQ_DELETE.equals(request.getType()))
		return getDeleteCommand((DeleteRequest)request);

	return null;
}

protected Command getDeleteCommand(DeleteRequest delRequest) {
	CompoundCommand cc = new CompoundCommand();
	cc.setDebugLabel("delete in NodeEditPolicy");//$NON-NLS-1$
	List connections = getHostNode().getSourceConnections();

	DeleteRequest fwdRequest = new DeleteRequest(REQ_SOURCE_DELETED);
	fwdRequest.setContributions(delRequest.getContributions());

	for (int i = 0; i < connections.size(); i++) {
		EditPart connection = (EditPart)connections.get(i);
		//If the EditPart has already been asked to delete, skip it.
		if (fwdRequest.containsContribution(connection))
			continue;
		//Mark the EditPart as having been asked for command before actually asking.
		fwdRequest.addContribution(connection);
		cc.add(connection.getCommand(fwdRequest));
	}

	connections = getHostNode().getTargetConnections();
	fwdRequest.setType(REQ_TARGET_DELETED);

	for (int i = 0; i < connections.size(); i++) {
		EditPart connection = (EditPart)connections.get(i);
		if (fwdRequest.containsContribution(connection))
			continue;
		//Mark the EditPart as having been asked for command before actually asking.
		fwdRequest.addContribution(connection);
		cc.add(connection.getCommand(fwdRequest));
	}

	return cc.isEmpty() ? null : cc;
}

private GraphicalEditPart getHostNode() {
	return (GraphicalEditPart)getHost();
}

}