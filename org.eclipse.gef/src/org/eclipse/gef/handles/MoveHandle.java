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

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * A Handle used for moving {@link GraphicalEditPart}s.
 */
public class MoveHandle
	extends AbstractHandle
{

protected static int INNER_PAD = 2;

/**
 * Creates a MoveHandle for the given <code>GraphicalEditPart</code>
 * using a default {@link Locator}.
 *
 * @param owner The GraphicalEditPart to be moved by this handle.
 */
public MoveHandle(GraphicalEditPart owner) {
	this(owner, new MoveHandleLocator(owner.getFigure()));
}

/**
 * Creates a MoveHandle for the given <code>GraphicalEditPart</code>
 * using the given <code>Locator</code>.
 *
 * @param owner The GraphicalEditPart to be moved by this handle.
 * @param loc   The Locator used to place the handle.
 */
public MoveHandle(GraphicalEditPart owner, Locator loc) {
	super(owner, loc);
	initialize();
}

protected DragTracker createDragTracker() {
	DragEditPartsTracker tracker = new DragEditPartsTracker(getOwner());
	tracker.setDefaultCursor(getCursor());
	return tracker;
}

/**
 * Returns true if the point (x,y) is contained within this handle.
 *
 * @param x The x coordinate.
 * @param y The y coordinate.
 *
 * @return True if the point (x,y) is contained within this handle.
 */
public boolean containsPoint(int x, int y) {
	if (!super.containsPoint(x,y))
		return false;
	return !Rectangle.SINGLETON.
		setBounds(getBounds()).
		shrink(INNER_PAD, INNER_PAD).
		contains(x,y);
}

public Point getAccessibleLocation(){
	Point p = getBounds().
		getTopRight().
		translate(-1, getBounds().height/4);
	translateToAbsolute(p);
	return p;
}

/**
 * Initializes the handle.  Sets the {@link DragTracker} and
 * DragCursor.
 */
protected void initialize() {
	setOpaque(false);
	setBorder(new LineBorder(1));
	setCursor(Cursors.SIZEALL);
}

}