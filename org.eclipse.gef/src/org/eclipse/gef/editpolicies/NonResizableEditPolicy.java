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

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class NonResizableEditPolicy
	extends SelectionHandlesEditPolicy
{

private IFigure focusRect;
private IFigure feedback;

protected IFigure createDragSourceFeedbackFigure() {
	// Use a ghost rectangle for feedback
	RectangleFigure r = new RectangleFigure();
	FigureUtilities.makeGhostShape(r);
	r.setLineStyle(Graphics.LINE_DASHDOT);
	r.setForegroundColor(ColorConstants.white);
	r.setBounds(getBounds());
	addFeedback(r);
	return r;
}

protected List createSelectionHandles() {
 	List list = new ArrayList();
 	NonResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
 	return list;
}

public void deactivate() {
	if (feedback != null) {
		removeFeedback(feedback);
		feedback = null;
	}
	hideFocus();
	super.deactivate();
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
	if (feedback != null) {
		removeFeedback(feedback);
	}
	feedback = null;
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker org.eclipse.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType()))
		eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
}

private Rectangle getBounds() {
	return ((GraphicalEditPart)getHost()).getFigure().getBounds();
}

public Command getCommand(Request request) {
	Object type = request.getType();

	if (REQ_MOVE.equals(type))
		return getMoveCommand((ChangeBoundsRequest)request);
	if (REQ_ORPHAN.equals(type))
		return getOrphanCommand(request);
	if (REQ_ALIGN.equals(type))
		return getAlignCommand((AlignmentRequest)request);

	return null;
}

/**
 * Return the Figure to be used to paint the drag source
 * feedback.
 */
protected IFigure getDragSourceFeedbackFigure() {
	if (feedback == null)
		feedback = createDragSourceFeedbackFigure();
	return feedback;
}

protected Command getAlignCommand(AlignmentRequest request) {
	AlignmentRequest req = new AlignmentRequest(REQ_ALIGN_CHILDREN);
	req.setEditParts(getHost());
	req.setAlignment(request.getAlignment());
	req.setAlignmentRectangle(request.getAlignmentRectangle());
	return getHost().getParent().getCommand(req);
}

protected Command getMoveCommand(ChangeBoundsRequest request) {
	ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
	req.setEditParts(getHost());
	
	req.setMoveDelta(request.getMoveDelta());
	req.setSizeDelta(request.getSizeDelta());
	req.setLocation(request.getLocation());
	return getHost().getParent().getCommand(req);
}

protected Command getOrphanCommand(Request req) {
	return null;
}

protected void hideFocus() {
	if (focusRect != null)
		removeFeedback(focusRect);
	focusRect = null;
}

/**
 * Display feedback to indicate that the receiver object
 * is being dragged.  The default feedback is a rectangle
 * the same size as the figure.
 */
protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
	IFigure p = getDragSourceFeedbackFigure();
	
	Rectangle r = getHostFigure().getBounds().getCopy();
	getHostFigure().translateToAbsolute(r);
	r.translate(request.getMoveDelta());
	Dimension resize = request.getSizeDelta();
	r.width += resize.width;
	r.height += resize.height;

	p.translateToRelative(r);
	p.setBounds(r);
}

protected void showFocus() {
	focusRect = new AbstractHandle(
		(GraphicalEditPart)getHost(),
		new Locator() {
			public void relocate(IFigure target) {
				Rectangle r = getHostFigure().getBounds().getCopy();
				getHostFigure().translateToAbsolute(r);
				target.translateToRelative(r);
				target.setBounds(r.expand(5, 5));
			}
		})
		{
			{
				setBorder(new FocusBorder());
			}
			protected DragTracker createDragTracker() {
				return null;
			}
		};
	addFeedback(focusRect);
}

public void showSourceFeedback(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType()))
		showChangeBoundsFeedback((ChangeBoundsRequest) request);
}

public boolean understandsRequest(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType())
		|| REQ_ORPHAN.equals(request.getType())
		|| REQ_ALIGN.equals(request.getType()))
		return true;
	return super.understandsRequest(request);
}

}