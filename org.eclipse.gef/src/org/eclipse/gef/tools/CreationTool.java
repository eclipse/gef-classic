package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Display;

public class CreationTool
	extends TargetingTool
{

private CreateRequest.Factory factory;

public CreationTool() {
	setDefaultCursor(SharedCursors.CURSOR_TREE_ADD);
	setDisabledCursor(SharedCursors.NO);
}

public CreationTool(CreateRequest.Factory aFactory) {
	this();
	setFactory(aFactory);
}

protected Request createTargetRequest(){
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory());
	return request;
}

protected String getCommandName(){
	return REQ_CREATE;
}

protected CreateRequest getCreateRequest(){
	return (CreateRequest)getTargetRequest();
}

protected String getDebugName(){
	return "Creation Tool";//$NON-NLS-1$
}

protected CreateRequest.Factory getFactory(){
	return factory;
}

protected boolean handleButtonDown(int button) {
	if (button != 1){
		setState(STATE_INVALID);
		handleInvalidInput();
		return true;
	}
	if (stateTransition(STATE_INITIAL, STATE_DRAG)){
		getCreateRequest().setLocation(getLocation());
		lockTargetEditPart(getTargetEditPart());
	}
	return true;
}

protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG | STATE_DRAG_IN_PROGRESS,
		STATE_TERMINAL))
	{
		eraseTargetFeedback();
		unlockTargetEditPart();
		performCreation(button);
	}

	setState(STATE_TERMINAL);
	handleFinished();

	return true;
}

protected boolean handleDragInProgress() {
	if (isInState(STATE_DRAG_IN_PROGRESS)){
		updateTargetRequest();
		setCurrentCommand(getCommand());
		showTargetFeedback();
	}
	return true;
}

protected boolean handleDragStarted(){
	return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
}

protected boolean handleMove() {
	updateTargetRequest();
	updateTargetUnderMouse();
	setCurrentCommand(getCommand());
	showTargetFeedback();
	return true;
}


protected void performCreation(int button) {
	executeCurrentCommand();
	selectAddedObject();
}

/**
 * Add the newly created object to the viewer's
 * selected objects.
 */
private void selectAddedObject() {
	final Object model = getCreateRequest().getNewObject();
	if (model == null)
		return;
	final EditPartViewer viewer = getCurrentViewer();
	final Object editpart = viewer.getEditPartRegistry().get(model);
	if (editpart instanceof EditPart){
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				viewer.select((EditPart)editpart);
			}
		});
	}
}

public void setFactory(CreateRequest.Factory factory){
	this.factory = factory;
}

protected void updateTargetRequest(){
	if (isInState(STATE_DRAG_IN_PROGRESS)){
		Point loq = getStartLocation();
		Rectangle bounds = new Rectangle(loq, loq);
		bounds.union(loq.getTranslated(getDragMoveDelta()));
		getCreateRequest().setSize(bounds.getSize());
		getCreateRequest().setLocation(bounds.getLocation());
	} else {
		getCreateRequest().setSize(null);
		getCreateRequest().setLocation(getLocation());
	}
}

}
