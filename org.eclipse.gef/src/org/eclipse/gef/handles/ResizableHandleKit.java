package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.tools.ResizeTracker;

/**
 * A set of utility methods to create Handles for the common
 * locations on a figure's bounds.
 * @see Handle
 */
public class ResizableHandleKit {

/**
 * Fills the given List with handles at each corner
 * and the north, south, east, and west of the GraphicalEditPart.
 */
public static void addHandles(GraphicalEditPart part, List handles) {
	addMoveHandle(part, handles);
	handles.add(createHandle(part, PositionConstants.EAST));
	handles.add(createHandle(part, PositionConstants.SOUTH_EAST));
	handles.add(createHandle(part, PositionConstants.SOUTH));
	handles.add(createHandle(part, PositionConstants.SOUTH_WEST));
	handles.add(createHandle(part, PositionConstants.WEST));
	handles.add(createHandle(part, PositionConstants.NORTH_WEST));
	handles.add(createHandle(part, PositionConstants.NORTH));
	handles.add(createHandle(part, PositionConstants.NORTH_EAST));
}

/**
 * Fills the given List with move borders at each side of a
 * figure.
 */
static public void addMoveHandle(GraphicalEditPart f, List handles) {
	handles.add(moveHandle(f));
}

static Handle createHandle(GraphicalEditPart owner, int direction) {
	ResizeHandle handle = new ResizeHandle(
		owner,
		direction);
	handle.setDragTracker(new ResizeTracker(direction));
	return handle;
}

static public Handle moveHandle(GraphicalEditPart owner) {
	return new MoveHandle(owner);
}

}
