package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
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
	tracker.setCommandName(tracker.REQ_RECONNECT_SOURCE);
	tracker.setDefaultCursor(getCursor());
	return tracker;
}

}
