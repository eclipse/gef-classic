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

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.*;

public class AbstractConnectionCreationTool
	extends TargetingTool
{

protected static final int STATE_CONNECTION_STARTED = TargetingTool.MAX_STATE << 1;
protected static final int MAX_STATE = STATE_CONNECTION_STARTED;

private static final int FLAG_SOURCE_FEEDBACK = TargetingTool.MAX_FLAG << 1;
protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;

private Command command;
private EditPart connectionSource;
private Request sourceRequest;
private CreationFactory factory;

public AbstractConnectionCreationTool() {
	setDefaultCursor(SharedCursors.CURSOR_PLUG);
	setDisabledCursor(SharedCursors.NO);
}

public AbstractConnectionCreationTool(CreationFactory factory) {
	this();
	setFactory(factory);
}

protected Cursor calculateCursor(){
	if (isInState(STATE_INITIAL)){
		if (getCurrentCommand() != null)
			return getDefaultCursor();
	}
	return super.calculateCursor();
}

protected Request createTargetRequest(){
	CreateRequest req = new CreateConnectionRequest();
	req.setFactory(getFactory());
	return req;
}

public void deactivate(){
	eraseSourceFeedback();
	super.deactivate();
	setState(STATE_TERMINAL);
}

protected void eraseSourceFeedback() {
	if (!isShowingSourceFeedback())
		return;
	setFlag(FLAG_SOURCE_FEEDBACK, false);
	if (connectionSource != null){
		connectionSource.eraseSourceFeedback(getSourceRequest());
	}
}

protected String getCommandName() {
	if (isInState(STATE_CONNECTION_STARTED | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
		return REQ_CONNECTION_END;
	else
		return REQ_CONNECTION_START;
}

protected String getDebugName(){
	return "Connection Creation Tool";//$NON-NLS-1$
}

protected String getDebugNameForState(int s) {
	if (s == STATE_CONNECTION_STARTED || s == STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
		return "Connection Started";//$NON-NLS-1$
	return super.getDebugNameForState(s);
}

protected CreationFactory getFactory(){
	return factory;
}

protected Request getSourceRequest(){
	return getTargetRequest();
}

protected boolean handleButtonDown(int button) {
	if (isInState(STATE_INITIAL) && button == 1) {
		updateTargetRequest();
		updateTargetUnderMouse();
		setConnectionSource(getTargetEditPart());
		command = getCommand();
		((CreateConnectionRequest)getTargetRequest()).setSourceEditPart(getTargetEditPart());
		if (command != null) {
			setState(STATE_CONNECTION_STARTED);
			setCurrentCommand(command);
		}
	}

	if (isInState(STATE_INITIAL) && button != 1){
		setState(STATE_INVALID);
		handleInvalidInput();
	}
	return true;
}

protected boolean handleButtonUp(int button){
	if (isInState(STATE_TERMINAL | STATE_INVALID))
		handleFinished();
	return true;
}

protected boolean handleCreateConnection() {
	eraseSourceFeedback();
	Command endCommand = getCommand();
	setCurrentCommand(endCommand);
	executeCurrentCommand();

	return true;
}

protected boolean handleDrag() {
	if (isInState(STATE_CONNECTION_STARTED))
		return handleMove();
	return false;
}

protected boolean handleDragInProgress() {
	if (isInState(STATE_CONNECTION_STARTED | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
		return handleMove();
	return false;
}

protected boolean handleFocusLost() {
	if (isInState(STATE_CONNECTION_STARTED)) {
		eraseSourceFeedback();
		eraseTargetFeedback();
		setState(STATE_INVALID);
		handleFinished();
	}
	return super.handleFocusLost();
}

protected boolean handleInvalidInput(){
	eraseSourceFeedback();
	return super.handleInvalidInput();
}

protected boolean handleMove() {
	if (isInState(STATE_CONNECTION_STARTED | STATE_INITIAL 
			| STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
		updateTargetRequest();
		updateTargetUnderMouse();
		showSourceFeedback();
		showTargetFeedback();
		setCurrentCommand(getCommand());
	}
	return true;
}

protected boolean isShowingSourceFeedback(){
	return getFlag(FLAG_SOURCE_FEEDBACK);
}

protected void setConnectionSource(EditPart source) {
	connectionSource = source;
}

public void setFactory(CreationFactory factory){
	this.factory = factory;
}

protected void showSourceFeedback() {
	if (connectionSource != null){
		connectionSource.showSourceFeedback(getSourceRequest());
	}
	setFlag(FLAG_SOURCE_FEEDBACK, true);
}

protected void updateTargetRequest(){
 	CreateConnectionRequest request = (CreateConnectionRequest)getTargetRequest();
	request.setType(getCommandName());
	request.setLocation(getLocation());
}

}
