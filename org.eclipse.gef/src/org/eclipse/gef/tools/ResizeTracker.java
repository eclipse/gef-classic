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
package org.eclipse.gef.tools;

import java.util.List;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * A Tracker for dragging a resize handle.  The ResizeTracker will resize all of the
 * selected editparts in the viewer which understand a RESIZE request.  A
 * {@link ChangeBoundsRequest} is sent to each member of the operation set.  The tracker
 * allows for the resize direction to be specified in the constructor.
 * @author hudsonr
 */
public class ResizeTracker
	extends SimpleDragTracker
{

private int direction;
private GraphicalEditPart owner;
private PrecisionRectangle sourceRect;

/**
 * Constructs a resize tracker that resizes in the specified direction.  The direction is
 * specified using {@link PositionConstants#NORTH}, {@link PositionConstants#NORTH_EAST},
 * etc.
 * @param owner of the resize handle which returned this tracker
 * @param direction the direction
 */
public ResizeTracker(GraphicalEditPart owner, int direction) {
	this.owner = owner;
	this.direction = direction;
	setDisabledCursor(SharedCursors.NO);
}

/**
 * @see org.eclipse.gef.Tool#activate()
 */
public void activate() {
	super.activate();
	IFigure figure = owner.getFigure();
	sourceRect = new PrecisionRectangle(figure.getBounds());
	figure.translateToAbsolute(sourceRect);	
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
 */
protected Cursor calculateCursor() {
	if (isInState(STATE_INITIAL))
		return getDefaultCursor();
	return super.calculateCursor();
}

/**
 * Returns all selected parts which understand resizing.
 * @see org.eclipse.gef.tools.AbstractTool#createOperationSet()
 */
protected List createOperationSet() {
	List list = super.createOperationSet();
	ToolUtilities.filterEditPartsUnderstanding(list, getSourceRequest());
	return list;
}

/**
 * @see org.eclipse.gef.tools.SimpleDragTracker#createSourceRequest()
 */
protected Request createSourceRequest() {
	ChangeBoundsRequest request;
	request = new ChangeBoundsRequest(REQ_RESIZE);
	request.setResizeDirection(getResizeDirection());
	return request;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommand()
 */
protected Command getCommand() {
	List editparts = getOperationSet();
	EditPart part;
	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel("Resize Handle Tracker");//$NON-NLS-1$
	for (int i = 0; i < editparts.size(); i++) {
		part = (EditPart)editparts.get(i);
		command.add(part.getCommand(getSourceRequest()));
	}
	return command.unwrap();
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
 */
protected String getCommandName() {
	return REQ_RESIZE;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDefaultCursor()
 */
protected Cursor getDefaultCursor() {
	return SharedCursors.getDirectionalCursor(direction);
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
 */
protected String getDebugName() {
	return "Resize Handle Tracker";//$NON-NLS-1$
}

/**
 * Returns the direction of the resize (NORTH, EAST, NORTH_EAST, etc.).  These constants
 * are from {@link PositionConstants}.
 * @return the resize direction.
 */
protected int getResizeDirection() {
	return direction;
}

/**
 * @see org.eclipse.gef.tools.SimpleDragTracker#updateSourceRequest()
 */
protected void updateSourceRequest() {
	ChangeBoundsRequest request = (ChangeBoundsRequest)getSourceRequest();

	Dimension d = getDragMoveDelta();

	Point location = new Point(getLocation());
	Point corner = new Point(0, 0);
	Dimension resize = new Dimension(0, 0);

	if ((getResizeDirection() & PositionConstants.NORTH) != 0) {
		corner.y += d.height;
		resize.height -= d.height;
	}
	if ((getResizeDirection() & PositionConstants.SOUTH) != 0) {
		resize.height += d.height;
	}
	if ((getResizeDirection() & PositionConstants.WEST) != 0) {
		corner.x += d.width;
		resize.width -= d.width;
	}
	if ((getResizeDirection() & PositionConstants.EAST) != 0) {
		resize.width += d.width;
	}
	request.setMoveDelta(corner);
	request.setSizeDelta(resize);
	request.setLocation(location);
	request.setEditParts(getOperationSet());
	
	SnapToStrategy strategy = (SnapToStrategy)owner.getViewer().getContents()
		.getAdapter(SnapToStrategy.class);

	if (!getCurrentInput().isShiftKeyDown() && strategy != null)
		strategy.snapResizeRequest(request, sourceRect);
}

}
