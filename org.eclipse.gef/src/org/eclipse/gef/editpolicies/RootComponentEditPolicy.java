package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.*;
import org.eclipse.gef.requests.GroupRequest;

/**
 * The <i>root</i> component cannot be removed from its parent. This EditPolicy is
 * typically installed on the Viewer's {@link org.eclipse.gef.EditPartViewer#getContents()
 * contents}.
 */
public class RootComponentEditPolicy
	extends ComponentEditPolicy
{

/**
 * Overridden to prevent the host from being deleted.
 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(DeleteRequest) */
protected Command createDeleteCommand(GroupRequest request) {
	return UnexecutableCommand.INSTANCE;
}

}