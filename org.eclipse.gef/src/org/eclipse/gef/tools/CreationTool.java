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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.requests.CreateRequest;

public class CreationTool
	extends TargetingTool
{

private CreationFactory factory;

public CreationTool() {
	setDefaultCursor(SharedCursors.CURSOR_TREE_ADD);
	setDisabledCursor(SharedCursors.NO);
}

public CreationTool(CreationFactory aFactory) {
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

protected CreationFactory getFactory(){
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

protected boolean handleFocusLost() {
	if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
		eraseTargetFeedback();
		setState(STATE_INVALID);
		handleFinished();
		return true;
	}
	return false;
}

/**
 * @see org.eclipse.gef.tools.TargetingTool#handleHover()
 */
protected boolean handleHover() {
	if (isInState(STATE_INITIAL))
		updateAutoexposeHelper();
	return true;
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
	EditPartViewer viewer = getCurrentViewer();
	Object editpart = viewer.getEditPartRegistry().get(model);
	if (editpart instanceof EditPart){
		viewer.flush();
		viewer.select((EditPart)editpart);
	}
}

public void setFactory(CreationFactory factory){
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
