package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;

/**
 * A Handle used to resize a GraphicalEditPart.
 */
public class ResizeHandle
	extends SquareHandle
{

private int cursorDirection = 0;

/**
 * Creates a new ResizeHandle for the given GraphicalEditPart.
 * <code>direction</code> is the relative direction from the 
 * center of the owner figure.  For example, <code>SOUTH_EAST</code>
 * would place the handle in the lower-right corner of its
 * owner figure.  These direction constants can be found in
 * {@link PositionConstants}.
 */
public ResizeHandle(GraphicalEditPart owner, int direction){
	setOwner(owner);
	setLocator(new RelativeHandleLocator(owner.getFigure(), direction));
	setCursor(Cursors.getDirectionalCursor(direction));
	cursorDirection = direction;
}

/**
 * Creates a new ResizeHandle for the given GraphicalEditPart.
 * 
 * @see SquareHandle#SquareHandle(GraphicalEditPart, Locator, Cursor)
 */
public ResizeHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
	super(owner, loc, c);
}

/**
 * Returns <code>null</code> for the DragTracker.
 */
protected DragTracker createDragTracker() {
	return null;
}

}
