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
private PrecisionPoint sourceFigureOffset;
private SnapToStrategy helper;

private PrecisionRectangle sourceRectangle;

/**
 * Constructs a new DragEditPartsTracker with the given source edit part.
 * @param sourceEditPart the source edit part
 */
public DragEditPartsTracker(EditPart sourceEditPart) {
	super(sourceEditPart);
	setDisabledCursor(SharedCursors.NO);
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
	ChangeBoundsRequest request = new ChangeBoundsRequest(REQ_MOVE);
	return request;
}

/**
 * Erases source feedback and sets the autoexpose helper to <code>null</code>.
 * @see org.eclipse.gef.Tool#deactivate()
 */
public void deactivate() {
	eraseSourceFeedback();
	super.deactivate();
	exclusionSet = null;
	sourceFigureOffset = null;
	sourceRectangle = null;
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

	Request  request = getTargetRequest();
	request.setType(isMove() ? REQ_MOVE : REQ_ORPHAN);

	while (iter.hasNext()) {
		EditPart editPart = (EditPart)iter.next();
		command.add(editPart.getCommand(request));
	}

	//If reparenting, add all editparts to target editpart.
	if (!isMove()) {
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
	if (isMove())
		return REQ_MOVE;
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
	if (isInDragInProgress()) {
		if (sourceFigureOffset == null) {
			Point offset;
			IFigure figure = ((GraphicalEditPart)getSourceEditPart()).getFigure();
			sourceFigureOffset = new PrecisionPoint(getStartLocation());
			figure.translateToRelative(sourceFigureOffset);
			offset = figure.getBounds().getLocation();
			sourceFigureOffset.preciseX -= offset.x;
			sourceFigureOffset.preciseY -= offset.y;
		}
		updateTargetRequest();
		updateTargetUnderMouse();
		showTargetFeedback();
		showSourceFeedback();
		setCurrentCommand(getCommand());
	}
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
	}
	return false;
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
	if (sourceFigureOffset == null)
		return;
	IFigure figure = ((GraphicalEditPart)getSourceEditPart()).getFigure();
	PrecisionPoint newStart = (PrecisionPoint)sourceFigureOffset.getCopy();
	Point offset = figure.getBounds().getLocation();
	newStart.preciseX += offset.x;
	newStart.preciseY += offset.y;
	figure.translateToAbsolute(newStart);
	setStartLocation(newStart);
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
	request.setMoveDelta(new Point(delta.width, delta.height));
	request.getExtendedData().clear();
	if (helper != null && !getCurrentInput().isShiftKeyDown())
		helper.snapMoveRequest(request, sourceRectangle.getPreciseCopy());

	request.setLocation(getLocation());
	request.setType(getCommandName());
}

}
