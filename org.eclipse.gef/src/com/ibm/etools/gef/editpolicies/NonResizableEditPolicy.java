package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.IFigure;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.gef.handles.*;

public class NonResizableEditPolicy
	extends SelectionHandlesEditPolicy
{

private IFigure focusRect;

private IFigure feedback;
private boolean useRealtimeFeedback = false;
private Rectangle originalLocation;

protected IFigure createDragSourceFeedbackFigure() {
	if (useRealtimeFeedback) {
		// Use the actual figure for feedback
		return ((GraphicalEditPart) getHost()).getFigure();
	}
	else {
		// Use a ghost rectangle for feedback
		RectangleFigure r = new RectangleFigure();
		FigureUtilities.makeGhostShape(r);
		r.setLineStyle(Graphics.LINE_DASHDOT);
		r.setForegroundColor(ColorConstants.white);
		r.setBounds(getBounds());
		addFeedback(r);
		return r;
	}
}

protected List createSelectionHandles() {
 	List list = new ArrayList();
 	NonResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
 	return list;
}

public void deactivate(){
	if (feedback != null){
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
 * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
	if (feedback != null) {
		if (useRealtimeFeedback) {
			feedback.setBounds(originalLocation);
			feedback.revalidate();
		}
		else removeFeedback(feedback);
	}
	feedback = null;
	originalLocation = null;
}

/**
 * Erase feedback indicating that the receiver object is 
 * being dragged.  This method is called when a drag is
 * completed or cancelled on the receiver object.
 * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
public void eraseSourceFeedback(Request request) {
	if (	REQ_MOVE.equals(request.getType()) ||
		REQ_ADD.equals(request.getType())
	)
		eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
}

private Rectangle getBounds(){
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
	if (feedback == null) {
		IFigure fig = ((GraphicalEditPart) getHost()).getFigure();
		originalLocation = new Rectangle(fig.getBounds());
		feedback = createDragSourceFeedbackFigure();
	}
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

protected Command getOrphanCommand(Request req){
	return null;
}

protected void hideFocus(){
	if (focusRect != null)
		removeFeedback(focusRect);
	focusRect = null;
}

/**
 * Display feedback to indicate that the receiver object
 * is being dragged.  The default feedback is a rectangle
 * the same size as the figure.
 * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag tracker of the tool performing the drag.
 */
protected void showChangeBoundsFeedback(ChangeBoundsRequest request){
	IFigure p = getDragSourceFeedbackFigure();
	Rectangle r = originalLocation.getTranslated(request.getMoveDelta());
	Dimension resize = request.getSizeDelta();
	r.width += resize.width;
	r.height+= resize.height;

	((GraphicalEditPart)getHost()).getFigure().translateToAbsolute(r);
	p.translateToRelative(r);
	p.setBounds(r);
	if (useRealtimeFeedback) 
		p.validate();
}

protected void showFocus(){
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
			protected DragTracker createDragTracker(){
				return null;
			}
		};
	addFeedback(focusRect);
}

public void showSourceFeedback(Request request){
	if (REQ_MOVE.equals(request.getType()) ||
		REQ_ADD.equals(request.getType()))
		showChangeBoundsFeedback((ChangeBoundsRequest)request);
}

public boolean understandsRequest(Request request){
	if (REQ_MOVE.equals(request.getType()) ||
	    REQ_ADD.equals(request.getType()) ||
	    REQ_ORPHAN.equals(request.getType()) ||
	    REQ_ALIGN.equals(request.getType()))
		return true;
	return super.understandsRequest(request);
}

}