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
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * A set of utility methods to create Handles for 
 * NonResizable Figures.
 * 
 * @see Handle
 */
public class NonResizableHandleKit {

/**
 * Fills the given List with handles at each corner of a
 * figure.
 */
static public void addCornerHandles(GraphicalEditPart part, List handles) {
	handles.add(createHandle(part, PositionConstants.SOUTH_EAST));
	handles.add(createHandle(part, PositionConstants.SOUTH_WEST));
	handles.add(createHandle(part, PositionConstants.NORTH_WEST));
	handles.add(createHandle(part, PositionConstants.NORTH_EAST));
}

/**
 * Fills the given List with handles at each corner.
 */
public static void addHandles(GraphicalEditPart part, List handles) {
	addMoveHandle(part, handles);
	addCornerHandles(part, handles);
}

/**
 * Fills the given List with move borders at each side of a
 * figure.
 */
static public void addMoveHandle(GraphicalEditPart f, List handles) {
	handles.add(moveHandle(f));
}

static Handle createHandle(GraphicalEditPart owner, int direction) {
	ResizeHandle handle = new ResizeHandle(owner,direction);
	handle.setCursor(SharedCursors.SIZEALL);
	handle.setDragTracker(new DragEditPartsTracker(owner));
	return handle;
}

/**
 * Adds a MoveHandle to the passed GraphicalEditPart.
 */
static public Handle moveHandle(GraphicalEditPart owner) {
	return new MoveHandle(owner);
}

}