package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.*;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

/**
 * A BendpointHandle that is used to create a new bendpoint.
 */
public class BendpointCreationHandle
	extends BendpointHandle
{

{
	setCursor(SharedCursors.SIZEALL);
	setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE-2, DEFAULT_HANDLE_SIZE-2));
}

/**
 * Creates a new BendpointCreationHandle.
 */
public BendpointCreationHandle() {}

/**
 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
 * and its index to <code>index</code>, and sets its locator to a new
 * {@link MidpointLocator}.
 */
public BendpointCreationHandle(ConnectionEditPart owner, int index) {
	setOwner(owner);
	setIndex(index);
	setLocator(new MidpointLocator(getConnection(), index));
}

/**
 * Creates a new BendpointCreationHandle and sets its owner to 
 * <code>owner</code>, sets its index to <code>index</code>, and
 * sets its locator to <code>locator</code>.
 */
public BendpointCreationHandle(ConnectionEditPart owner, int index, Locator locator) {
	setOwner(owner);
	setIndex(index);
	setLocator(locator);
}

/**
 * Creates and returns a new {@link ConnectionBendpointTracker}.
 */
protected DragTracker createDragTracker() {
	ConnectionBendpointTracker tracker;
	tracker = new ConnectionBendpointTracker(
		(ConnectionEditPart)getOwner(),
		getIndex());
	tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
	tracker.setDefaultCursor(getCursor());
	return tracker;
}

}