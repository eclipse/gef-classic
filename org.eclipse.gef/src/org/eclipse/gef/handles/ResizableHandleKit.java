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

import java.util.List;

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;

/**
 * A set of utility methods to create Handles for the common
 * locations on a figure's bounds.
 * @see Handle
 */
public class ResizableHandleKit {

/**
 * Fills the given List with handles at each corner
 * and the north, south, east, and west of the GraphicalEditPart.
 * @param part the owner GraphicalEditPart of the handles
 * @param handles the List to add the handles to
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
 * @param f the GraphicalEditPart thatis the owner of the handles
 * @param handles the List to add the handles to
 */
public static void addMoveHandle(GraphicalEditPart f, List handles) {
	handles.add(moveHandle(f));
}

static Handle createHandle(GraphicalEditPart owner, int direction) {
	ResizeHandle handle = new ResizeHandle(
		owner,
		direction);
//	handle.setDragTracker(new ResizeTracker(direction));
	return handle;
}

/**
 * Returns a new {@link MoveHandle} with the given owner.
 * @param owner the GraphicalEditPart that is the owner of the new MoveHandle 
 * @return the new MoveHandle
 */
public static Handle moveHandle(GraphicalEditPart owner) {
	return new MoveHandle(owner);
}

}
