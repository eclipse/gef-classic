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
package org.eclipse.gef.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Provides support for selecting, positioning, and resizing an editpart.  Selection is
 * indicated via eight square handles along the editpart's figure, and a rectangular
 * handle that outlines the editpart with a 1-pixel black line. The eight square handles
 * will resize the current selection in the eight primary directions. The rectangular
 * handle will drag the current selection using a {@link
 * org.eclipse.gef.tools.DragEditPartsTracker}.
 * <P>
 * During feedback, a rectangle filled using XOR and outlined with dashes is drawn. 
 * Subclasses may tailor the feedback.
 * 
 * @author hudsonr
 */
public class ResizableEditPolicy
	extends NonResizableEditPolicy
{

/**
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
 */
protected List createSelectionHandles() {
	List list = new ArrayList();
 	ResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
 	return list;
}

/**
 * Dispatches erase requests to more specific methods.
 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
	else
		super.eraseSourceFeedback(request);
}

/**
 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
 */
public Command getCommand(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		return getResizeCommand((ChangeBoundsRequest)request);

	return super.getCommand(request);
}

/**
 * Returns the command contribution for the given resize request. By default, the request
 * is redispatched to the host's parent as a {@link
 * org.eclipse.gef.RequestConstants#REQ_RESIZE_CHILDREN}.  The parent's editpolicies
 * determine how to perform the resize based on the layout manager in use.
 * @param request the resize request
 * @return the command contribution obtained from the parent
 */
protected Command getResizeCommand(ChangeBoundsRequest request) {
	ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
	req.setEditParts(getHost());
	
	req.setMoveDelta(request.getMoveDelta());
	req.setSizeDelta(request.getSizeDelta());
	req.setLocation(request.getLocation());
	req.getExtendedData().putAll(request.getExtendedData());
	req.setResizeDirection(request.getResizeDirection());
	return getHost().getParent().getCommand(req);
}

/**
 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
 */
public void showSourceFeedback(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		showChangeBoundsFeedback((ChangeBoundsRequest)request);
	else
		super.showSourceFeedback(request);
}

/**
 * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
 */
public boolean understandsRequest(Request request) {
	if (REQ_RESIZE.equals(request.getType()))
		return true;
	return super.understandsRequest(request);
}

}