package com.ibm.etools.gef.handles;
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

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.tools.*;

/**
 * A set of utility methods to create Handles for the common
 * locations on a figure's bounds.
 * @see Handle
 */
public class ResizableHandleKit {

protected final static Cursor NORTH_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEN);
protected final static Cursor SOUTH_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZES);
protected final static Cursor EAST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEE);
protected final static Cursor WEST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZEW);
protected final static Cursor NORTHEAST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZENE);
protected final static Cursor NORTHWEST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZENW);
protected final static Cursor SOUTHEAST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZESE);
protected final static Cursor SOUTHWEST_CURSOR = new Cursor(Display.getDefault(), SWT.CURSOR_SIZESW);


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
