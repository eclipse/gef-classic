package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.gef.requests.*;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.*;

abstract public class ContainerEditPolicy
	extends AbstractEditPolicy
{

final protected void createCreateCommand(Object parent, CreateRequest.Factory factory){}

protected Command getAddCommand(GroupRequest request){
	return null;
}

protected Command getAncestorDeletedCommand(DeleteRequest request){
	return getDeleteCommand(request);
}

public Command getCommand(Request request){
	if (REQ_ANCESTOR_DELETED.equals(request.getType()))
		return getAncestorDeletedCommand((DeleteRequest)request);
	if (REQ_DELETE.equals(request.getType()))
		return getDeleteCommand((DeleteRequest)request);
	if (REQ_DELETE_DEPENDANT.equals(request.getType()))
		return getDeleteDependantCommand(request);
	if (REQ_CREATE.equals(request.getType()))
		return getCreateCommand((CreateRequest)request);

	if (REQ_ADD.equals(request.getType()))
		return getAddCommand((GroupRequest)request);
	if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand((GroupRequest)request);

	return null;
}

abstract protected Command getCreateCommand(CreateRequest request);

abstract protected Command getDeleteDependantCommand(Request request);

/**
 * This policy's host is being deleted.
 */
protected Command getDeleteCommand(DeleteRequest delete) {
	CompoundCommand compound = new CompoundCommand();
	compound.setDebugLabel("delete in ContainerEditPolicy");//$NON-NLS-1$
	List children = getHost().getChildren();
	DeleteRequest ancestorDeletedReq = new DeleteRequest(REQ_ANCESTOR_DELETED);
	ancestorDeletedReq.setContributions(delete.getContributions());

	for (int i=0; i<children.size(); i++) {
		EditPart child = (EditPart)children.get(i);
		compound.add(child.getCommand(ancestorDeletedReq));
	}

	return compound.isEmpty() ? null : compound;
}

protected Command getOrphanChildrenCommand(GroupRequest request){
	return null;
}

}