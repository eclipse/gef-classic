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

import java.util.List;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;

import org.eclipse.swt.graphics.Cursor;

public class SelectEditPartTracker
	extends TargetingTool
	implements DragTracker
{

protected static final int FLAG_SELECTION_PERFORMED = TargetingTool.MAX_FLAG << 1;
private static final int FLAG_ENABLE_DIRECT_EDIT = TargetingTool.MAX_FLAG << 2;
protected static final int MAX_FLAG = FLAG_ENABLE_DIRECT_EDIT;

private EditPart editpart;

public SelectEditPartTracker(EditPart owner) {
	setSourceEditPart(owner);
}

protected Cursor calculateCursor() {
	if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
		return getDefaultCursor();
	return super.calculateCursor();
}

protected String getCommandName() {
	return "Select Tracker";//$NON-NLS-1$
}

protected String getDebugName() {
	return "Select Tracker";//$NON-NLS-1$
}

protected EditPart getSourceEditPart() {
	return editpart;
}

protected boolean handleButtonDown(int button) {
	if ((button == 3 || button == 1)
	  && isInState(STATE_INITIAL))
		performConditionalSelection();

	if (button != 1) {
		setState(STATE_INVALID);
		if (button == 3)
			setState(STATE_TERMINAL);
		handleInvalidInput();
	} else
		stateTransition(STATE_INITIAL, STATE_DRAG);
	return true;
}

protected boolean handleButtonUp(int button) {
	if (isInState(STATE_DRAG)) {
		performSelection();
		if (getFlag(FLAG_ENABLE_DIRECT_EDIT))
			performDirectEdit();
		if (button == 1 && getSourceEditPart().getSelected() != EditPart.SELECTED_NONE)
			getCurrentViewer().reveal(getSourceEditPart());
		setState(STATE_TERMINAL);
		return true;
	}
	return false;
}

protected boolean handleDoubleClick(int button) {
	if (button == 1)
		performOpen();
	return true;
}

protected boolean handleDragStarted() {
	return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
}

protected boolean hasSelectionOccurred() {
	return getFlag(FLAG_SELECTION_PERFORMED);
}

/**
 * Calls performSelection if the source is not selected.  If the source is selected, it 
 * may be part of a larger selection which the user is trying to drag or operate on, so do
 * nothing.
 */
protected void performConditionalSelection() {
	if (getSourceEditPart().getSelected() == EditPart.SELECTED_NONE)
		performSelection();
	else
		if (getCurrentInput().getModifiers() == 0)
			setFlag(FLAG_ENABLE_DIRECT_EDIT, true);
}

protected void performDirectEdit() {
	DirectEditRequest req = new DirectEditRequest();
	req.setLocation(getCurrentInput().getMouseLocation());
	new DelayedDirectEditHelper(
		getSourceEditPart().getViewer(),
		req,
		getSourceEditPart());
}

protected void performOpen() {
	SelectionRequest request = new SelectionRequest();
	request.setLocation(getLocation());
	request.setType(RequestConstants.REQ_OPEN);
	getSourceEditPart().performRequest(request);
}

protected void performSelection() {
	if (hasSelectionOccurred())
		return;
	setFlag(FLAG_SELECTION_PERFORMED, true);
	EditPartViewer viewer = getCurrentViewer();
	List selectedObjects = viewer.getSelectedEditParts();

	if (getCurrentInput().isControlKeyDown()) {
		if (selectedObjects.contains(getSourceEditPart()))
			viewer.deselect(getSourceEditPart());
		else
			viewer.appendSelection(getSourceEditPart());
	} else if (getCurrentInput().isShiftKeyDown())
		viewer.appendSelection(getSourceEditPart());
	else
		viewer.select(getSourceEditPart());
}

protected void resetFlags() {
	super.resetFlags();
	setFlag(FLAG_SELECTION_PERFORMED, false);
	setFlag(FLAG_ENABLE_DIRECT_EDIT, false);
}

protected void setSourceEditPart(EditPart part) {
	this.editpart = part;
}

}