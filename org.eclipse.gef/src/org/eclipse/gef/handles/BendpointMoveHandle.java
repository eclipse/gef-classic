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

import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.*;
import org.eclipse.gef.tools.ConnectionBendpointTracker;

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