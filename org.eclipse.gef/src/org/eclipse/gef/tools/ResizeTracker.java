package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;


public class ResizeTracker
	extends SimpleDragTracker
{

private int direction;

public ResizeTracker(int direction) {
	this.direction = direction;
}

protected List createOperationSet(){
	List list = super.createOperationSet();
	ToolUtilities.filterEditPartsUnderstanding(list, getSourceRequest());
	return list;
}

protected Request createSourceRequest(){
	ChangeBoundsRequest request;
	request = new ChangeBoundsRequest(REQ_RESIZE);
	request.setResizeDirection(getResizeDirection());
	return request;
}

protected Command getCommand() {
	List editparts = getOperationSet();
	EditPart part;
	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel("Resize Handle Tracker");//$NON-NLS-1$
	for (int i=0; i < editparts.size(); i++){
		part = (EditPart)editparts.get(i);
		command.add(part.getCommand(getSourceRequest()));
	}
	return command.unwrap();
}

protected String getCommandName() {
	return REQ_RESIZE;
}

protected Cursor getDefaultCursor(){
	return SharedCursors.getDirectionalCursor(direction);
}

protected String getDebugName(){
	return "Resize Handle Tracker";//$NON-NLS-1$
}

protected int getResizeDirection(){
	return direction;
}


protected void updateSourceRequest(){
	ChangeBoundsRequest request = (ChangeBoundsRequest)getSourceRequest();

	Dimension d = getDragMoveDelta();

	Point location = new Point(getLocation());
	Point corner = new Point(0,0);
	Dimension resize = new Dimension(0,0);

	if ((getResizeDirection() & PositionConstants.NORTH) != 0){
		corner.y+= d.height;
		resize.height-= d.height;
	}
	if ((getResizeDirection() & PositionConstants.SOUTH) != 0){
		resize.height += d.height;
	}
	if ((getResizeDirection() & PositionConstants.WEST) != 0){
		corner.x+= d.width;
		resize.width-= d.width;
	}
	if ((getResizeDirection() & PositionConstants.EAST) != 0){
		resize.width += d.width;
	}
	request.setMoveDelta(corner);
	request.setSizeDelta(resize);
	request.setLocation(location);
	request.setEditParts(getOperationSet());
}

}
