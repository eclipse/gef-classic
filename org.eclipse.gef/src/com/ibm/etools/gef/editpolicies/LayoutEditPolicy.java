package com.ibm.etools.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.common.command.Command;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.CompoundCommand;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.gef.tools.*;

/**
 * EditPolicy handles creating commands; providing target feedback; building/converting
 * model and layout constraints; and refreshing layout constraints based on model changes.
 * The EditPolicy handles the relationship between a EditPart (which wraps the actual visual
 * figure) and underlying model object.
 */
public abstract class LayoutEditPolicy
	extends GraphicalEditPolicy
{

private Shape sizeOnDropFeedback;

private EditPartListener listener;

public void activate(){
	setListener(createListener());
	decorateChildren();
	super.activate();
}

abstract protected EditPolicy createChildEditPolicy(EditPart child);

protected EditPartListener createListener(){
	return new EditPartListener.Stub(){
		public void childAdded(EditPart child, int index){
			decorateChild(child);
		}
		public void removingChild(EditPart child, int index){
			undecorateChild(child);
		}
	};
}

public void deactivate(){
	undecorateChildren();
	setListener(null);
	super.deactivate();
}

protected void decorateChild(EditPart child){
	EditPolicy policy = createChildEditPolicy(child);
	child.installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, policy);
}

protected void decorateChildren(){
	List children = getHost().getChildren();
	for (int i=0; i<children.size(); i++)
		decorateChild((EditPart)children.get(i));
}

protected void eraseDragTargetFeedback(Request request) {}

protected void eraseSizeOnDropFeedback(Request request) {
	if (sizeOnDropFeedback != null) {
		removeFeedback(sizeOnDropFeedback);
		sizeOnDropFeedback  = null;
	}
}

public void eraseTargetFeedback(Request request) {
	if (REQ_ADD.equals(request.getType()) ||
		REQ_MOVE.equals(request.getType())||
		REQ_CREATE.equals(request.getType()))
		eraseDragTargetFeedback(request);

	if (REQ_CREATE.equals(request.getType()))
		eraseSizeOnDropFeedback(request);
}

protected Command getAddCommand(Request request){return null;}

public Command getCommand(Request request){
	if (REQ_DELETE_DEPENDANT.equals(request.getType()))
		return getDeleteDependantCommand(request);

//	if (REQ_DELETE.equals(request.getType()))
//		return getDeleteCommand((DeleteRequest)request);

//	if (REQ_ANCESTOR_DELETED.equals(request.getType()))
//		return getAncestorDeletedCommand(request);

	if (REQ_ADD.equals(request.getType()))
		return getAddCommand(request);

	if (REQ_ORPHAN_CHILDREN.equals(request.getType()))
		return getOrphanChildrenCommand(request);

	if (REQ_MOVE_CHILDREN.equals(request.getType()))
		return getMoveChildrenCommand(request);

	if (REQ_CREATE.equals(request.getType()))
		return getCreateCommand((CreateRequest)request);

	return null;
}

abstract protected Command getCreateCommand(CreateRequest request);

/**
 * Returned command should remove the child from this layout.
 * The child will not be re-added elsewhere.
 */
abstract protected Command getDeleteDependantCommand(Request request);

/**@deprecated call getHostFigure*/
protected IFigure getFigure(){
	return ((GraphicalEditPart)getHost()).getFigure();
}

protected IFigure getLayoutContainer(){
	return ((GraphicalEditPart)getHost()).getContentPane();
}

abstract protected Command getMoveChildrenCommand(Request request);

/* Returned command should remove the child from this container and 
 * do any resulting operations on the layout.
 * The child will be re-added to another container.
 */
protected Command getOrphanChildrenCommand(Request request){
	return null;
}

protected IFigure getSizeOnDropFeedback() {
	if (sizeOnDropFeedback == null){
		sizeOnDropFeedback  = new RectangleFigure();
		FigureUtilities.makeGhostShape(sizeOnDropFeedback );
		sizeOnDropFeedback.setLineStyle(Graphics.LINE_DASHDOT);
		sizeOnDropFeedback.setForegroundColor(ColorConstants.white);
		addFeedback(sizeOnDropFeedback );
	}
	return sizeOnDropFeedback;

}

public EditPart getTargetEditPart(Request request){
	if (REQ_ADD.equals(request.getType()) ||
	    REQ_MOVE.equals(request.getType()) ||
	    REQ_CREATE.equals(request.getType())
	)
		return getHost();

	return null;
}

protected void setListener(EditPartListener listener){
	if (this.listener != null)
		getHost().removeEditPartListener(this.listener);
	this.listener = listener;
	if (this.listener != null)
		getHost().addEditPartListener(this.listener);
}

protected void showDragTargetFeedback(Request request) {}

protected void showSizeOnDropFeedback(CreateRequest request) {
}

public void showTargetFeedback(Request request){
	if (REQ_ADD.equals(request.getType()) ||
		REQ_MOVE.equals(request.getType()) ||
		REQ_RESIZE_CHILDREN.equals(request.getType()) ||
		REQ_CREATE.equals(request.getType())){
		debugFeedback("Request to show \"" + request.getType() + "\" target feedback");//$NON-NLS-2$//$NON-NLS-1$
		showDragTargetFeedback(request);
	}

	if (REQ_CREATE.equals(request.getType())){
		CreateRequest createReq = (CreateRequest)request;
		if (createReq.getSize() != null)
			showSizeOnDropFeedback(createReq);
	}
}

protected void undecorateChild(EditPart child){
	child.removeEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
}

protected void undecorateChildren(){
	List children = getHost().getChildren();
	for (int i=0; i<children.size(); i++)
		undecorateChild((EditPart)children.get(i));
}

}
