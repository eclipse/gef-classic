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

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;


/**
 * A DragTracker that moves {@link org.eclipse.gef.EditPart EditParts}.
 */
public class DragEditPartsTracker
	extends SelectEditPartTracker
{

private static final int FLAG_SOURCE_FEEDBACK = SelectEditPartTracker.MAX_FLAG << 1;
/** Max flag */
protected static final int MAX_FLAG = FLAG_SOURCE_FEEDBACK;
private List exclusionSet;
private PrecisionPoint sourceRelativeStartPoint;
private SnapToStrategy helper;

private PrecisionRectangle sourceRectangle;

private boolean cloneActive;

/**
 * Constructs a new DragEditPartsTracker with the given source edit part.
 * @param sourceEditPart the source edit part
 */
public DragEditPartsTracker(EditPart sourceEditPart) {
	super(sourceEditPart);

	cloneActive = false;
	setDisabledCursor(SharedCursors.NO);
}

/**
 *  Returns true if the control key was the key in the key event and the 
 *  tool is in an acceptable state for this event.
 *  
 *  @param e the key event
 *  @return true if the key was control and can be accepted.
 */
private boolean acceptCTRL(KeyEvent e) {
	int key = e.keyCode;
	if (!(isInState(STATE_INITIAL
	  | STATE_DRAG
	  | STATE_DRAG_IN_PROGRESS
	  | STATE_ACCESSIBLE_DRAG 
	  | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)))
		return false;
	return (key == SWT.CTRL);
}

private boolean acceptSHIFT(KeyEvent e) {
	return isInState(STATE_INITIAL | STATE_DRAG | STATE_DRAG_IN_PROGRESS
		| STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
		&& e.keyCode == SWT.SHIFT;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#activate()
 */
public void activate() {
	super.activate();
	IFigure figure = ((GraphicalEditPart)getSourceEditPart()).getFigure();
	if (figure instanceof HandleBounds)
		sourceRectangle = new PrecisionRectangle(((HandleBounds)figure).getHandleBounds());
	else
		sourceRectangle = new PrecisionRectangle(figure.getBounds());

	figure.translateToAbsolute(sourceRectangle);
}

/**
 * Returns the cursor used under normal conditions.
 * @see #setDefaultCursor(Cursor)
 * @return the default cursor
 */
protected Cursor getDefaultCursor() {
	if (isCloneActive())
		return SharedCursors.CURSOR_TREE_ADD;
	return super.getDefaultCursor();
}

/**
 * Erases feedback and calls {@link #performDrag()}.  Sets the state to terminal.
 * @see org.eclipse.gef.tools.AbstractTool#commitDrag()
 */
public void commitDrag() {
	eraseSourceFeedback();
	eraseTargetFeedback();
	performDrag();
	setState(STATE_TERMINAL);
}

/**
 * Returns a List of top-level edit parts excluding dependants (by calling
 * {@link ToolUtilities#getSelectionWithoutDependants(EditPartViewer)} that understand the
 * current target request (by calling 
 * {@link ToolUtilities#filterEditPartsUnderstanding(List, Request)}.
 * 
 * @see org.eclipse.gef.tools.AbstractTool#createOperationSet()
 */
protected List createOperationSet() {
	List list = ToolUtilities.getSelectionWithoutDependants(
		getCurrentViewer());
	ToolUtilities.filterEditPartsUnderstanding(list, getTargetRequest());
	return list;
}

/**
 * Creates a {@link ChangeBoundsRequest}.  By default, the type is
 * {@link RequestConstants#REQ_MOVE}.  Later on when the edit parts are asked to
 * contribute to the overall command, the request type will be either 
 * {@link RequestConstants#REQ_MOVE} or {@link RequestConstants#REQ_ORPHAN}, depending on
 * the result of {@link #isMove()}.
 * 
 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
 */
protected Request createTargetRequest() {
	if (isCloneActive())
		return new ChangeBoundsRequest(REQ_CLONE);
	else
		return new ChangeBoundsRequest(REQ_MOVE);
}

/**
 * Erases source feedback and sets the autoexpose helper to <code>null</code>.
 * @see org.eclipse.gef.Tool#deactivate()
 */
public void deactivate() {
	eraseSourceFeedback();
	super.deactivate();
	exclusionSet = null;
	sourceRelativeStartPoint = null;
	sourceRectangle = null;
}

/**
 * Does the work for handleDragInProgress. 
 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
 */
protected boolean doDragInProgress() {
	if (isInDragInProgress()) {
		updateTargetRequest();
		updateTargetUnderMouse();
		showTargetFeedback();
		showSourceFeedback();
		setCurrentCommand(getCommand());
	}
	return true;	
}

/**
 * Asks the edit parts in the {@link AbstractTool#getOperationSet() operation set} to 
 * erase their source feedback.
 */
protected void eraseSourceFeedback() {
	if (!getFlag(FLAG_SOURCE_FEEDBACK))
		return;
	setFlag(FLAG_SOURCE_FEEDBACK, false);
	List editParts = getOperationSet();
	for (int i = 0; i < editParts.size(); i++) {
		EditPart editPart = (EditPart) editParts.get(i);
		editPart.eraseSourceFeedback(getTargetRequest());
	}
}

/**
 * Asks each edit part in the {@link AbstractTool#getOperationSet() operation set} to 
 * contribute to a {@link CompoundCommand} after first setting the request type to either
 * {@link RequestConstants#REQ_MOVE} or {@link RequestConstants#REQ_ORPHAN}, depending on
 * the result of {@link #isMove()}.
 * 
 * @see org.eclipse.gef.tools.AbstractTool#getCommand()
 */
protected Command getCommand() {
	CompoundCommand command = new CompoundCommand();
	command.setDebugLabel("Drag Object Tracker");//$NON-NLS-1$

	Iterator iter = getOperationSet().iterator();

	Request request = getTargetRequest();
	
	if (isCloneActive())
		request.setType(REQ_CLONE);
	else if (isMove())
		request.setType(REQ_MOVE);
	else
		request.setType(REQ_ORPHAN);
		
	if (!isCloneActive()) {
		while (iter.hasNext()) {
			EditPart editPart = (EditPart)iter.next();
			command.add(editPart.getCommand(request));
		}
	}
	
	if (!isMove() || isCloneActive()) {
		if (!isCloneActive())
			request.setType(REQ_ADD);
		
		if (getTargetEditPart() == null)
			command.add(UnexecutableCommand.INSTANCE);
		else 
			command.add(getTargetEditPart().getCommand(getTargetRequest()));
	}
	
	return command;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
 */
protected String getCommandName() {
	if (isCloneActive())
		return REQ_CLONE;
	else if (isMove())
		return REQ_MOVE;
	else
		return REQ_ADD;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
 */
protected String getDebugName() {
	return "DragEditPartsTracker:" + getCommandName();//$NON-NLS-1$
}

/**
 * Returns a list of all the edit parts in the {@link AbstractTool#getOperationSet() 
 * operation set}, plus the {@link org.eclipse.draw2d.ConnectionLayer}.
 * @see org.eclipse.gef.tools.TargetingTool#getExclusionSet()
 */
protected Collection getExclusionSet() {
	if (exclusionSet == null) {
		List set = getOperationSet();
		exclusionSet = new ArrayList(set.size() + 1);
		for (int i = 0; i < set.size(); i++) {
			GraphicalEditPart editpart = (GraphicalEditPart)set.get(i);
			exclusionSet.add(editpart.getFigure());
		}
		LayerManager layerManager = (LayerManager)getCurrentViewer().
			getEditPartRegistry().get(LayerManager.ID);
		if (layerManager != null) {
			exclusionSet.add(layerManager.getLayer(LayerConstants.CONNECTION_LAYER));			
		}
	}
	return exclusionSet;
}

/**
 * @see org.eclipse.gef.tools.TargetingTool#handleAutoexpose()
 */
protected void handleAutoexpose() {
	updateTargetRequest();
	updateTargetUnderMouse();
	showTargetFeedback();
	showSourceFeedback();
	setCurrentCommand(getCommand());
}

/**
 * Called when the mouse button has been pressed. By default, nothing happens
 * and <code>false</code> is returned. Subclasses may override this method to interpret
 * the meaning of a mouse down. Returning <code>true</code> indicates that the button down
 * was handled in some way.
 * @param button which button went down
 * @return <code>true</code> if the buttonDown was handled
 */
protected boolean handleButtonDown(int button) {
	if (getCurrentInput().isControlKeyDown())
		setCloneActive(true);
	
	return super.handleButtonDown(button);
}

/**
 * Erases feedback and calls {@link #performDrag()}.
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
 */
protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
		eraseSourceFeedback();
		eraseTargetFeedback();
		performDrag();
		return true;
	}
	return super.handleButtonUp(button);
}

/**
 * Updates the target request and mouse target, asks to show feedback, and sets the 
 * current command.
 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
 */
protected boolean handleDragInProgress() {
	return doDragInProgress();
}

/**
 * Calls {@link TargetingTool#updateAutoexposeHelper()} if a drag is in progress.
 * @see org.eclipse.gef.tools.TargetingTool#handleHover()
 */
protected boolean handleHover() {
	if (isInDragInProgress())
		updateAutoexposeHelper();
	return true;
}

/**
 * Erases source feedback.
 * @see org.eclipse.gef.tools.TargetingTool#handleInvalidInput()
 */
protected boolean handleInvalidInput() {
	super.handleInvalidInput();
	eraseSourceFeedback();
	return true;
}

/**
 * Processes arrow keys used to move edit parts.
 * @see org.eclipse.gef.tools.AbstractTool#handleKeyDown(org.eclipse.swt.events.KeyEvent)
 */
protected boolean handleKeyDown(KeyEvent e) {
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
	} else if (acceptCTRL(e)) {
		setCloneActive(true);
		return true;
	} else if (acceptSHIFT(e)) {
		doDragInProgress();
		return true;
	}
	
	return false;	
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleKeyUp(org.eclipse.swt.events.KeyEvent)
 */
protected boolean handleKeyUp(KeyEvent e) {
	if (acceptArrowKey(e)) {
		accStepReset();
		return true;
	} else if (acceptCTRL(e)) {
		setCloneActive(false);
		return true;
	} else if (acceptSHIFT(e)) {
		doDragInProgress();
		return true;
	}
	return false;
}

/**
 * Returns true if the current drag is a clone operation.
 * 
 * @return true if cloning is enabled and is currently active.
 */
protected boolean isCloneActive() {
	return cloneActive;
}

/**
 * Returns <code>true</code> if the source edit part is being moved within its parent. If 
 * the source edit part is being moved to another parent, this returns <code>false</code>.
 * @return <code>true</code> if the source edit part is not being reparented
 */
protected boolean isMove() {
	return getSourceEditPart().getParent() == getTargetEditPart();
}

/**
 * Calls {@link AbstractTool#executeCurrentCommand()}.
 */
protected void performDrag() {
	executeCurrentCommand();
}

/**
 * If auto scroll (also called auto expose) is being performed, the start location moves 
 * during the scroll. This method updates that location.
 */
protected void repairStartLocation() {
	if (sourceRelativeStartPoint == null)
		return;
	IFigure figure = ((GraphicalEditPart)getSourceEditPart()).getFigure();
	PrecisionPoint newStart = (PrecisionPoint)sourceRelativeStartPoint.getCopy();
	figure.translateToAbsolute(newStart);
	setStartLocation(newStart);
}

protected void setAutoexposeHelper(AutoexposeHelper helper) {
	super.setAutoexposeHelper(helper);
	if (helper != null && sourceRelativeStartPoint == null && isInDragInProgress()) {
		if (sourceRelativeStartPoint == null) {
			IFigure figure = ((GraphicalEditPart)getSourceEditPart()).getFigure();
			sourceRelativeStartPoint = new PrecisionPoint(getStartLocation());
			figure.translateToRelative(sourceRelativeStartPoint);
		}
	}
}

/**
 * Enables cloning if the value is true.  Calls {@link #doDragInProgress()} if
 * the value has been changed.
 * 
 * @param cloneActive <code>true</code> if cloning should be active
 */
protected void setCloneActive(boolean cloneActive) {
	if (this.cloneActive == cloneActive)
		return;
	eraseSourceFeedback();
	eraseTargetFeedback();
	this.cloneActive = cloneActive;
	doDragInProgress();
}

/**
 * Extended to update the current snap-to strategy.
 * @see org.eclipse.gef.tools.TargetingTool#setTargetEditPart(org.eclipse.gef.EditPart)
 */
protected void setTargetEditPart(EditPart editpart) {
	if (getTargetEditPart() == editpart)
		return;
	super.setTargetEditPart(editpart);
	helper = null;
	if (getTargetEditPart() != null)
		 helper = (SnapToStrategy)getTargetEditPart().getAdapter(SnapToStrategy.class);
}

/**
 * Asks the edit parts in the {@link AbstractTool#getOperationSet() operation set} to 
 * show source feedback.
 */
protected void showSourceFeedback() {
	List editParts = getOperationSet();
	for (int i = 0; i < editParts.size(); i++) {
		EditPart editPart = (EditPart) editParts.get(i);
		editPart.showSourceFeedback(getTargetRequest());
	}
	setFlag(FLAG_SOURCE_FEEDBACK, true);
}

/**
 * Calls {@link #repairStartLocation()} in case auto scroll is being performed.  Updates
 * the request with the current {@link AbstractTool#getOperationSet() operation set}, 
 * move delta, location and type.
 * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
 */
protected void updateTargetRequest() {
	repairStartLocation();
	ChangeBoundsRequest request = (ChangeBoundsRequest)getTargetRequest();
	request.setEditParts(getOperationSet());
	Dimension delta = getDragMoveDelta();
	
	// constrains the move to dx=0, dy=0, or dx=dy if shift is depressed
	if (getCurrentInput().isShiftKeyDown()) {
		float ratio = 0;
		
		if (delta.width != 0)
			ratio = (float)delta.height / (float)delta.width;
		
		ratio = Math.abs(ratio);
		if (ratio > 0.5 && ratio < 1.5) {
			if (Math.abs(delta.height) > Math.abs(delta.width)) {
				if (delta.height > 0)
					delta.height = Math.abs(delta.width);
				else
					delta.height = -Math.abs(delta.width);
			} else {
				if (delta.width > 0)
					delta.width = Math.abs(delta.height); 
				else
					delta.width = -Math.abs(delta.height);
			}
		} else {
			if (Math.abs(delta.width) > Math.abs(delta.height))
				delta.height = 0;
			else
				delta.width = 0;
		}
	}
	
	request.setMoveDelta(new Point(delta.width, delta.height));
	request.getExtendedData().clear();

	if (helper != null && !getCurrentInput().isAltKeyDown())
		helper.snapMoveRequest(request, sourceRectangle.getPreciseCopy());

	request.setLocation(getLocation()); 
	request.setType(getCommandName());	
}

}