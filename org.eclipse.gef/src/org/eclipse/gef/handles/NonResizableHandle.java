package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Locator;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * A MoveHandle for a non-resizable EditPart.
 */
public class NonResizableHandle
	extends MoveHandle
{
	
protected CornerTriangleBorder border;

/**
 * Creates a NonResizableHandle for the given <code>GraphicalEditPart</code>
 * using a default {@link Locator}.
 *
 * @param owner The GraphicalEditPart to be moved by this handle.
 */
public NonResizableHandle(GraphicalEditPart owner) {
	this(owner, new MoveHandleLocator(owner.getFigure()));
}

/**
 * Creates a NonResizableHandle for the given <code>GraphicalEditPart</code>
 * using the given <code>Locator</code>.
 *
 * @param owner The GraphicalEditPart to be moved by this handle.
 * @param loc   The Locator used to place the handle.
 */
public NonResizableHandle(GraphicalEditPart owner, Locator loc) {
	super(owner, loc);
}

/**
 * Initializes the handle.  Sets the {@link org.eclipse.gef.DragTracker} and
 * DragCursor.
 */
protected void initialize() {
	setOpaque(false);
	border = new CornerTriangleBorder(false);
	setBorder(border);
	setCursor(Cursors.SIZEALL);
	setDragTracker(new DragEditPartsTracker(getOwner()));
}

/**
 * Updates the handle's color by setting the border's primary
 * attribute.
 */
public void validate() {
	border.setPrimary(getOwner().getSelected() == EditPart.SELECTED_PRIMARY);
	super.validate();
}

}