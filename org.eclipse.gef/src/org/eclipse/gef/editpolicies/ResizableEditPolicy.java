package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class ResizableEditPolicy
	extends NonResizableEditPolicy
{

protected List createSelectionHandles() {
	List list = new ArrayList();
 	ResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
 	return list;
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
	else
		super.eraseSourceFeedback(request);
}

/**
 * Get the command that performs an operation
 * of the type indicated by @commandString on the
 * receiver.  Data needed to create the command is
 * contained in @tool
 *
 * Possible values for the commandString depend on
 * the tool.  Default tools send "create" and "move".
 *
 * @return org.eclipse.gef.commands.ICommand  The command that performs the operation
 * @param commandString java.lang.String The type of command to create
 * @param commandData org.eclipse.gef.ICommandData Data needed to create the command
 */
public Command getCommand(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		return getResizeCommand((ChangeBoundsRequest)request);

	return super.getCommand(request);
}

protected Command getResizeCommand(ChangeBoundsRequest request) {
	ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
	req.setEditParts(getHost());
	
	req.setMoveDelta(request.getMoveDelta());
	req.setSizeDelta(request.getSizeDelta());
	req.setLocation(request.getLocation());
	req.setResizeDirection(request.getResizeDirection());
	return getHost().getParent().getCommand(req);
}

public void showSourceFeedback(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		showChangeBoundsFeedback((ChangeBoundsRequest)request);
	else
		super.showSourceFeedback(request);
}

public boolean understandsRequest(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		return true;
	return super.understandsRequest(request);
}

}