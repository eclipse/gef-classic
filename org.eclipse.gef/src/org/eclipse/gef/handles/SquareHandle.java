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
 * A square handle.
 */
abstract public class SquareHandle
	extends AbstractHandle
{
	
protected final static int DEFAULT_HANDLE_SIZE = 7;

{
	init();
}

public SquareHandle(){}

/**
 * Creates a SquareHandle for the given <code>GraphicalEditPart</code>
 * using the given <code>Locator</code>.
 */
public SquareHandle(GraphicalEditPart owner, Locator loc) {
	super(owner, loc);
}

/**
 * Creates a SquareHandle for the given <code>GraphicalEditPart</code>
 * with the given <code>Cursor</code> using the given <code>Locator</code>.
 */
public SquareHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
	super(owner, loc, c);
}

/**
 * Returns the color of the handle's border.
 *
 * @return The color of the handle's border.
 */
protected Color getBorderColor() {
	return (isPrimary()) ? 
		ColorConstants.white : 
		ColorConstants.black;
}

/**
 * Returns the color of the handle.
 *
 * @return The color of the handle.
 */
protected Color getFillColor() {
	return (isPrimary()) ? 
		ColorConstants.black : 
		ColorConstants.white;
}

protected void init(){
	setPreferredSize(new Dimension(DEFAULT_HANDLE_SIZE, DEFAULT_HANDLE_SIZE));
}

/**
 * Returns <code>true</code> if the handle's owner is the
 * primary object in the selection.
 *
 * @return Whether or not the handle's owner is the primary object.
 */
protected boolean isPrimary(){
	return getOwner().getSelected() == EditPart.SELECTED_PRIMARY;
}

/**
 * Draws the handle with fill color and outline color dependent 
 * on the primary selection status of the owner editpart.
 *
 * @param g The graphics used to paint the figure.
 */
public void paintFigure(Graphics g) {
	Rectangle r = getBounds();
	r.shrink(1,1);
	try {
		g.setBackgroundColor(getFillColor());
		g.fillRectangle(r.x, r.y, r.width, r.height);
		g.setForegroundColor(getBorderColor()); 
		g.drawRectangle(r.x, r.y, r.width, r.height);
	} finally {
		//We don't really own rect 'r', so fix it.
		r.expand(1,1);
	}
}

}