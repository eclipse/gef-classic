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
import org.eclipse.gef.handles.*;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Provide support for selecting and positioning a non-resizable editpart.  Selection is
 * indicated via four square handles at each corner of the editpart's figure, and a
 * rectangular handle that outlines the editpart with a 1-pixel black line.  All of these
 * handles return {@link org.eclipse.gef.tools.DragEditPartsTracker}s, which allows the
 * current selection to be dragged.
 * <P>
 * During feedback, a rectangle filled using XOR and outlined with dashes is drawn. 
 * Subclasses can tailor the feedback.
 * @author hudsonr
 */
public class NonResizableEditPolicy
	extends SelectionHandlesEditPolicy
{

private IFigure focusRect;
private IFigure feedback;

/**
 * Creates the figure used for feedback.
 * @return the new feedback figure
 */
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

/**
 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
 */
protected List createSelectionHandles() {
 	List list = new ArrayList();
 	NonResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
 	return list;
}

/**
 * @see org.eclipse.gef.EditPolicy#deactivate()
 */
public void deactivate() {
	if (feedback != null) {
		removeFeedback(feedback);
		feedback = null;
	}
	hideFocus();
	super.deactivate();
}

/**
 * Erases drag feedback.  This method called whenever an erase feedback request is
 * received of the appropriate type.
 * @param request the request
 */
protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
	if (feedback != null) {
		removeFeedback(feedback);
	}
	feedback = null;
}

/**
 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
 */
public void eraseSourceFeedback(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType()))
		eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
}

/**
 * Returns the bounds of the host's figure by reference.  The returned Rectangle should
 * not be modified.
 * @return the host figure's bounding Rectangle
 */
private Rectangle getBounds() {
	return ((GraphicalEditPart)getHost()).getFigure().getBounds();
}

/**
 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
 */
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
 * Lazily creates and returns the feedback figure used during drags.
 * @return the feedback figure
 */
protected IFigure getDragSourceFeedbackFigure() {
	if (feedback == null)
		feedback = createDragSourceFeedbackFigure();
	return feedback;
}

/**
 * Returns the command contribution to an alignment request
 * @param request the alignment request
 * @return the contribution to the alignment
 */
protected Command getAlignCommand(AlignmentRequest request) {
	AlignmentRequest req = new AlignmentRequest(REQ_ALIGN_CHILDREN);
	req.setEditParts(getHost());
	req.setAlignment(request.getAlignment());
	req.setAlignmentRectangle(request.getAlignmentRectangle());
	return getHost().getParent().getCommand(req);
}

/**
 * Returns the command contribution to a change bounds request. The implementation
 * actually redispatches the request to the host's parent editpart as a {@link
 * RequestConstants#REQ_MOVE_CHILDREN} request.  The parent's contribution is returned.
 * @param request the change bounds requesgt
 * @return the command contribution to the request
 */
protected Command getMoveCommand(ChangeBoundsRequest request) {
	ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
	req.setEditParts(getHost());
	
	req.setMoveDelta(request.getMoveDelta());
	req.setSizeDelta(request.getSizeDelta());
	req.setLocation(request.getLocation());
	return getHost().getParent().getCommand(req);
}

/**
 * Subclasses may override to contribute to the orphan request.  By default,
 * <code>null</code> is returned to indicate no participation.  Orphan requests are not
 * forwarded to the host's parent here.  That is done in {@link ComponentEditPolicy}. So,
 * if the host has a component editpolicy, then the parent will already have a chance to
 * contribute.
 * @param req the orphan request
 * @return <code>null</code> by default
 */
protected Command getOrphanCommand(Request req) {
	return null;
}

/**
 * Hides the focus rectangle displayed in <code>showFocus()</code>.
 * @see #showFocus()
 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideFocus()
 */
protected void hideFocus() {
	if (focusRect != null)
		removeFeedback(focusRect);
	focusRect = null;
}

/**
 * Shows or updates feedback for a change bounds request.
 * @param request the request
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

/**
 * Shows a focus rectangle around the host's figure. The focus rectangle is expanded by 5
 * pixels from the figure's bounds.
 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showFocus()
 */
protected void showFocus() {
	focusRect = new AbstractHandle(
		(GraphicalEditPart)getHost(),
		new Locator() {
			public void relocate(IFigure target) {
				IFigure figure = getHostFigure();
				Rectangle r;
				if (figure instanceof HandleBounds)
					r = ((HandleBounds)figure).getHandleBounds().getCopy();
				else
					r = getHostFigure().getBounds().getResized(-1, -1);
				getHostFigure().translateToAbsolute(r);
				target.translateToRelative(r);
				target.setBounds(r.expand(5, 5).resize(1, 1));
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

/**
 * Calls other methods as appropriate.
 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
 */
public void showSourceFeedback(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType()))
		showChangeBoundsFeedback((ChangeBoundsRequest) request);
}

/**
 * Returns <code>true</code> for move, align, add, and orphan request types.  This method
 * is never called for some of these types, but they are included for possible future use.
 * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
 */
public boolean understandsRequest(Request request) {
	if (REQ_MOVE.equals(request.getType())
		|| REQ_ADD.equals(request.getType())
		|| REQ_ORPHAN.equals(request.getType())
		|| REQ_ALIGN.equals(request.getType()))
		return true;
	return super.understandsRequest(request);
}

}