package com.ibm.etools.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.tools.*;

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
	setBorder(new LineBorder());
	setCursor(Cursors.SIZEALL);
}

}