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
package org.eclipse.gef.handles;

import org.eclipse.draw2d.ConnectionLocator;

import org.eclipse.gef.*;
import org.eclipse.gef.tools.ConnectionEndpointTracker;

/**
 * A handle used at the start of the {@link org.eclipse.draw2d.Connection}.  
 * This is treated differently than the end of the Connection.
 */
final public class ConnectionStartHandle
	extends ConnectionHandle
{
	
/**
 * Creates a new ConnectionStartHandle, sets its owner to <code>owner</code>,
 * and sets its locator to a {@link ConnectionLocator}.
 */
public ConnectionStartHandle(ConnectionEditPart owner) {
	setOwner(owner);
	setLocator(new ConnectionLocator(getConnection(),ConnectionLocator.SOURCE));
}

public ConnectionStartHandle(ConnectionEditPart owner, boolean fixed) {
	super(fixed);
	setOwner(owner);
	setLocator(new ConnectionLocator(getConnection(),ConnectionLocator.SOURCE));
}

/**
 * Creates a new ConnectionStartHandle.
 */
public ConnectionStartHandle(){}

/**
 * Creates and returns a new {@link ConnectionEndpointTracker}.
 */
protected DragTracker createDragTracker() {
	if (isFixed()) 
		return null;
	ConnectionEndpointTracker tracker;
	tracker = new ConnectionEndpointTracker((ConnectionEditPart)getOwner());
	tracker.setCommandName(RequestConstants.REQ_RECONNECT_SOURCE);
	tracker.setDefaultCursor(getCursor());
	return tracker;
}

}
