package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.tools.*;

/**
 * A BendpointHandle that is used to move an existing bendpoint.
 */
public class BendpointMoveHandle extends BendpointHandle {

{
	setCursor(SharedCursors.SIZEALL);
}

/**
 * Creates a new BendpointMoveHandle.
 */
public BendpointMoveHandle() {}

/**
 * Creates a new BendpointMoveHandle, sets its owner to <code>owner</code>
 * and its index to <code>index</code>, and sets its locator to a new
 * {@link BendpointLocator}.
 */
public BendpointMoveHandle(ConnectionEditPart owner, int index) {
	setOwner(owner);
	setIndex(index);
	setLocator(new BendpointLocator(getConnection(), index+1));
}

/**
 * Creates a new BendpointMoveHandle and sets its owner to <code>owner</code>,
 * sets its index to <code>index</code>, and sets its locator to 
 * <code>locator</code>.
 */
public BendpointMoveHandle(ConnectionEditPart owner, int index, Locator locator) {
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
	tracker.setType(RequestConstants.REQ_MOVE_BENDPOINT);
	tracker.setDefaultCursor(getCursor());
	return tracker;
}

}