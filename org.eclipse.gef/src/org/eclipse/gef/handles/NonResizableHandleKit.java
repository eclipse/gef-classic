package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.tools.*;

/**
 * A set of utility methods to create Handles for 
 * NonResizable Figures.
 * 
 * @see Handle
 */
public class NonResizableHandleKit {

protected final static Cursor SIZEALL_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEALL);

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
	handle.setCursor(SIZEALL_CURSOR);
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