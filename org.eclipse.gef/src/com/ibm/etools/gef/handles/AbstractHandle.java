package com.ibm.etools.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;

/**
 * Base class for handles.
 */
abstract public class AbstractHandle
	extends Figure
	implements Handle, AncestorListener
{

private GraphicalEditPart editpart;
private DragTracker dragTracker;
private Cursor cursor;
private Locator locator;

public AbstractHandle(){}

/**
 * Creates a handle for the given <code>GraphicalEditPart</code> using
 * the given <code>Locator</code>.
 *
 * @param owner The <code>GraphicalEditPart</code> this handle is attached to.
 * @param loc   The <code>Locator</code> that places the handle.
 */
public AbstractHandle(GraphicalEditPart owner, Locator loc) {
	setOwner(owner);
	setLocator(loc);
}

/**
 * Creates a handle for the given <code>GraphicalEditPart</code> using
 * the given <code>Locator</code> and <code>Cursor</code>.
 *
 * @param owner The <code>GraphicalEditPart</code> this handle is attached to.
 * @param loc   The <code>Locator</code> that places the handle.
 * @param c     The <code>Cursor</code> that will appear over the handle.
 */
public AbstractHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
	this(owner, loc);
	setCursor(c);
}

/**
 * Adds this as a {@link FigureListener} to the owner's {@link Figure}.
 */
public void addNotify() {
	// Listen to the owner figure so the handle moves when the
	// figure moves.
	getOwnerFigure().addAncestorListener(this);
}

public void ancestorMoved(IFigure ancestor) {
	revalidate();
}

public void ancestorAdded(IFigure ancestor) {}

public void ancestorRemoved(IFigure ancestor) {}

abstract protected DragTracker createDragTracker();

public Point getAccessibleLocation(){
	Point p = getBounds().getCenter();
	translateToAbsolute(p);
	return p;
}

/**
 * Returns the Cursor that appears over the handle.
 */
public Cursor getDragCursor() {
	return cursor;
}

/**
 * Returns the handle tracker to use when dragging this handle.
 */
public DragTracker getDragTracker() {
	if (dragTracker == null) 
		dragTracker = createDragTracker();
	return dragTracker;
}

/**
 * Returns the <code>Locator</code> associated with this handle.
 */
public Locator getLocator() {
	return locator;
}

/**
 * Returns the <code>GraphicalEditPart</code> associated with
 * this handle.
 */
protected GraphicalEditPart getOwner() {
	return editpart;
}

/**
 *
 */
protected IFigure getOwnerFigure() {
	return getOwner().getFigure();
}

public void removeNotify() {
	getOwnerFigure().removeAncestorListener(this);
}

/**
 * Sets the Cursor for the handle.
 */
public void setDragCursor(Cursor c) throws Exception {
	setCursor(c);
}

/**
 * 
 */
public void setDragTracker(DragTracker t) {
	dragTracker = t;
}

protected void setLocator(Locator locator){
	this.locator = locator;
}

protected void setOwner(GraphicalEditPart editpart){
	this.editpart = editpart;
}

public void validate() {
	if (isValid())
		return;
	getLocator().relocate(this);
	super.validate();
}

}
