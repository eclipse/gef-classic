package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.*;

/**
 * A model-based EditPolicy for <i>components within a </i>container</i>. A model-based
 * EditPolicy only knows about the host's model and the basic operations it supports. A
 * <i>component</i> is anything that is inside a container. By default,
 * ComponentEditPolicy understands being DELETEd from its container, and being ORPHANed
 * from its container. Subclasses can add support to handle additional behavior specific
 * to the model.
 * <P>ORPHAN is forwarded to the <i>parent</i> EditPart for it to handle.
 * <P>DELETE is also forwarded to the <i>parent</i> EditPart, but subclasses may also
 * contribute to the delete by overriding {@link #createDeleteCommand(DeleteRequest)}.
 * <P>
 * This EditPolicy is not a {@link org.eclipse.gef.editpolicies.GraphicalEditPolicy}, and
 * should not be used to show feedback or interact with the host's visuals in any way.
 * <P>
 * This EditPolicy should not be used with {@link org.eclipse.gef.ConnectionEditPart}.
 * Connections do not really have a parent; use {@link ConnectionEditPolicy}.
 * @since 2.0 */
public abstract class ComponentEditPolicy
	extends AbstractEditPolicy
{

/**
 * Override to contribute to the component's being deleted. DELETE will also be sent to
 * the parent. DELETE must be handled by either the child or the parent, or both.
 * @param deleteRequest the DeleteRequest * @return Command <code>null</code> or a contribution to the delete */
protected Command createDeleteCommand(DeleteRequest deleteRequest) {
	return null;
}

/**
 * Factors the incoming Request into ORPHANs and DELETEs.
 * @see org.eclipse.gef.EditPolicy#getCommand(Request) */
public Command getCommand(Request request) {
	if (REQ_ORPHAN.equals(request.getType()))
		return getOrphanCommand();
	if (REQ_DELETE.equals(request.getType()))
		return getDeleteCommand((DeleteRequest)request);
	return null;
}

/**
 * Combines the DELETE contribution from this class and the parent. This classes
 * contribution is obtained by calling {@link #createDeleteCommand(DeleteRequest)}. The
 * parent is sent {@link RequestConstants#REQ_DELETE_DEPENDANT REQ_DELETE_DEPENDANT}. The
 * parent's contribution is combined with the local contribution and returned.
 * @param request the DeleteRequest * @return the combined contributions from this EditPolicy and the parent EditPart */
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
 * Returns any contribution to ORPHANing this component from its container. By default,
 * ORPHAN is sent to the parent as an ORPHAN_CHILDREN Request.
 * @return the Command obtained from the host's parent.
 */
protected Command getOrphanCommand() {
	GroupRequest req = new GroupRequest(REQ_ORPHAN_CHILDREN);
	req.setEditParts(getHost());
	return getHost().getParent().getCommand(req);
}

}