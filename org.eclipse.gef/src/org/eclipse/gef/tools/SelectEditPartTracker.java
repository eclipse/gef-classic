package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gef.*;
import org.eclipse.gef.ui.parts.TreeViewer;

public class SelectEditPartTracker
	extends TargetingTool
	implements DragTracker
{

protected static final int FLAG_SELECTION_PERFORMED = TargetingTool.MAX_FLAG << 1;
protected static final int MAX_FLAG = FLAG_SELECTION_PERFORMED;

private EditPart editpart;

public SelectEditPartTracker(EditPart owner){
	setSourceEditPart(owner);
}

protected Cursor calculateCursor(){
	if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
		return getDefaultCursor();
	return super.calculateCursor();
}

protected String getCommandName(){
	return "Select Tracker";//$NON-NLS-1$
}

protected String getDebugName(){
	return "Select Tracker";//$NON-NLS-1$
}

protected EditPart getSourceEditPart(){
	return editpart;
}

protected boolean handleButtonDown(int button) {
	if (button == 3 || button == 1){
		if (isInState(STATE_INITIAL))
			performConditionalSelection();
	}
	if (button != 1){
		setState(STATE_INVALID);
		if (button == 3)
			setState(STATE_TERMINAL);
		handleInvalidInput();
	} else
		stateTransition(STATE_INITIAL, STATE_DRAG);
	return true;
}

protected boolean handleButtonUp(int button){
	if (isInState(STATE_DRAG)){
		performSelection();
		setState(STATE_TERMINAL);
		return true;
	}
	return false;
}

protected boolean handleDoubleClick(int button){
	getSourceEditPart().performRequest(new Request(REQ_DIRECT_EDIT));
	return true;
}

protected boolean handleDragStarted(){
	return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
}

protected boolean hasSelectionOccurred(){
	return getFlag(FLAG_SELECTION_PERFORMED);
}

/**
 * Calls performSelection if the source is not selected.  If the source is selected, it may be
 * part of a larger selection which the user is trying to drag or operate on, so do nothing.
 */
protected void performConditionalSelection(){
	if (getSourceEditPart().getSelected() == EditPart.SELECTED_NONE)
		performSelection();
}

protected void performSelection(){
	if (hasSelectionOccurred())
		return;
	setFlag(FLAG_SELECTION_PERFORMED, true);
	EditPartViewer viewer = getCurrentViewer();
	List selectedObjects = viewer.getSelectedEditParts();

	if (getCurrentInput().isControlKeyDown()){
		if (selectedObjects.contains(getSourceEditPart()))
			viewer.deselect(getSourceEditPart());
		else
			viewer.appendSelection(getSourceEditPart());
	} else if (getCurrentInput().isShiftKeyDown())
		viewer.appendSelection(getSourceEditPart());
	else
		viewer.select(getSourceEditPart());
}

protected void resetFlags(){
	super.resetFlags();
	setFlag(FLAG_SELECTION_PERFORMED, false);
}

protected void setSourceEditPart(EditPart part){
	this.editpart = part;
}

}