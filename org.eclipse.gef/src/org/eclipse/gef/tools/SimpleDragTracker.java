package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;


import org.eclipse.gef.*;
import org.eclipse.gef.GEF;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import org.eclipse.draw2d.geometry.*;

/**
 * A tool that interacts with one or more sources only.
 * For example, bending a connection involves only that connection.
 */
abstract public class SimpleDragTracker
	extends AbstractTool
	implements DragTracker
{

private static final int FLAG_SOURCE_FEEDBACK = AbstractTool.MAX_FLAG << 1;
protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;
private Request sourceRequest;

/**
 * Null constructor.
 */
protected SimpleDragTracker(){}

/**
 * Calculates and returns the current cursor based on the
 * tool's current state.
 */
protected Cursor calculateCursor(){
	if (isInState(STATE_DRAG | STATE_ACCESSIBLE_DRAG))
		return getDefaultCursor();
	return super.calculateCursor();
}

public void commitDrag(){
	eraseSourceFeedback();
	performDrag();
	setState(STATE_TERMINAL);
}

/**
 * Creates and returns a new Request.
 */
protected Request createSourceRequest(){
	return new Request();
}

/**
 * Deactivates the tool. This method is called whenever the user
 * switches to another tool. Use this method to do some clean-up
 * when the tool is switched. 
 */
public void deactivate() {
	eraseSourceFeedback();
	sourceRequest = null;
	super.deactivate();
}

/**
 * Show the source drag feedback for the drag occurring
 * within the viewer.
 */
protected void eraseSourceFeedback() {
	if (!isShowingFeedback())
		return;
	setFlag(FLAG_SOURCE_FEEDBACK, false);
	List editParts = getOperationSet();
	for (int i = 0; i < editParts.size(); i++) {
		EditPart editPart = (EditPart) editParts.get(i);
		editPart.eraseSourceFeedback(getSourceRequest());
	}
}

/**
 * Returns the request for the source of the drag, creating it
 * if necessary.
 */
protected Request getSourceRequest(){
	if (sourceRequest == null)
		sourceRequest = createSourceRequest();
	return sourceRequest;
}

/**
 * Handles the button down event.  Transitions the tool into
 * the drag state.
 */
protected boolean handleButtonDown(int button) {
	if (button != 1){
		setState(STATE_INVALID);
		handleInvalidInput();
	} else
		stateTransition(STATE_INITIAL, STATE_DRAG);
	return true;
}

/**
 * Handles the button up event.  Gets the current command
 * and executes it.
 */
protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
		eraseSourceFeedback();
		performDrag();
	}
	return true;
}

/**
 * Called after the mouse has been dragged past the
 * threshold.  Updates the request and sets the 
 * current command.
 */
protected boolean handleDragInProgress() {
	if (isInDragInProgress()){
		updateSourceRequest();
		showSourceFeedback();
		setCurrentCommand(getCommand());
	}
	return true;
}

/**
 * Called once when the drag starts.  Transistions the tool
 * into the drag-in-progress state.
 */
protected boolean handleDragStarted(){
	return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
}

/**
 * Called when the mouse and/or keyboard input is invalid.
 */
protected boolean handleInvalidInput(){
	eraseSourceFeedback();
	setCurrentCommand(UnexecutableCommand.INSTANCE);
	return true;
}

protected boolean handleKeyDown(KeyEvent e){
	if (acceptArrowKey(e)) {
		accStepIncrement();
		if (stateTransition(STATE_INITIAL, STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
			setStartLocation(getLocation());
		switch (e.keyCode) {
			case SWT.ARROW_DOWN :
				placeMouseInViewer(getLocation().getTranslated(0, accGetStep()));
				break;
			case SWT.ARROW_UP:
				placeMouseInViewer(getLocation().getTranslated(0, -accGetStep()));
				break;
			case SWT.ARROW_RIGHT:
				placeMouseInViewer(getLocation().getTranslated(accGetStep(), 0));
				break;
			case SWT.ARROW_LEFT:
				placeMouseInViewer(getLocation().getTranslated(-accGetStep(), 0));
				break;
		}
		return true;
	}
	return false;
}

protected boolean handleKeyUp(KeyEvent e){
	if (acceptArrowKey(e)){
		accStepReset();
		return true;
	}
	return false;
}

/**
 * Returns <code>true</code> if feedback is being shown.
 */
protected boolean isShowingFeedback(){
	return getFlag(FLAG_SOURCE_FEEDBACK);
}

protected void performDrag(){
	executeCurrentCommand();
}

/**
 * Show the source drag feedback for the drag occurring
 * within the viewer.
 */
protected void showSourceFeedback() {
	List editParts = getOperationSet();
	for (int i = 0; i < editParts.size(); i++) {
		EditPart editPart = (EditPart) editParts.get(i);
		editPart.showSourceFeedback(getSourceRequest());
	}
	setFlag(FLAG_SOURCE_FEEDBACK, true);
}

/**
 * Updates the source request.
 */
protected void updateSourceRequest(){
	getSourceRequest().setType(getCommandName());
}

}