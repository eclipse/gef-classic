package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.lang.ref.WeakReference;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

/**
 * Tool to select and manipulate figures.
 * A selection tool is in one of three states, e.g., background
 * selection, figure selection, handle manipulation. The different
 * states are handled by different child tools.
 */
public class SelectionTool
	extends TargetingTool
{

private static final int FLAG_HOVER_FEEDBACK = TargetingTool.MAX_FLAG << 1;
protected static final int MAX_FLAG = FLAG_HOVER_FEEDBACK;

protected static final int STATE_TRAVERSE_HANDLE = TargetingTool.MAX_STATE << 1;
protected static final int MAX_STATE = STATE_TRAVERSE_HANDLE;

private int  handleIndex;
private DragTracker dragTracker;
private LocationRequest hoverRequest;

private WeakReference cachedHandlePart;

public SelectionTool() {}

private boolean acceptTraverseHandle(KeyEvent e){
	return (e.character == '.' || e.character == '>') &&
		isInState(
			STATE_INITIAL |
			STATE_ACCESSIBLE_DRAG |
			STATE_ACCESSIBLE_DRAG_IN_PROGRESS
		) &&
		((e.stateMask & (SWT.ALT | SWT.CONTROL)) == 0);
}

protected void createHoverRequest() {
	hoverRequest = new LocationRequest();
	hoverRequest.setType(RequestConstants.REQ_SELECTION_HOVER);
}

protected Request createTargetRequest(){
	SelectionRequest request = new SelectionRequest();
	request.setType(getCommandName());
	return request;
}

/**
 * Deactivates the tool. This method is called whenever the user
 * switches to another tool. Use this method to do some clean-up
 * when the tool is switched. 
 */
public void deactivate() {
	setDragTracker(null);  // deactivates the current drag tracker
	super.deactivate();
}

protected void eraseHoverFeedback(){
	if (getTargetEditPart() == null)
		return;
	if (getTargetHoverRequest() == null)
		return;
	getTargetEditPart().eraseTargetFeedback(getTargetHoverRequest());
}

protected String getCommandName(){
	return REQ_SELECTION;
}

protected String getDebugName(){
	return "Selection Tool";//$NON-NLS-1$
}

protected DragTracker getDragTracker() {
	return dragTracker;
}

private EditPart getLastHandleProvider(){
	if (cachedHandlePart == null)
		return null;
	EditPart part = (EditPart)cachedHandlePart.get();
	if (cachedHandlePart.isEnqueued())
		return null;
	return part;
}

/**
 * @see org.eclipse.gef.tools.TargetingTool#getTargetConditional()
 */
protected EditPartViewer.Conditional getTargetingConditional() {
	return new EditPartViewer.Conditional() {
		public boolean evaluate(EditPart editpart) {
			return editpart.isSelectable();
		}
	};
}

protected Request getTargetHoverRequest() {
	if (hoverRequest == null)
		createHoverRequest();
	return hoverRequest;
}

protected boolean handleButtonDown(int button) {
	if (!stateTransition(STATE_INITIAL, STATE_DRAG)) {
		resetHover();
		return true;
	}
	resetHover();
	EditPartViewer viewer = getCurrentViewer();
	Point p = getLocation();

	if (getDragTracker() != null)
		getDragTracker().deactivate();

	if (viewer instanceof GraphicalViewer) {
		Handle handle = ((GraphicalViewer) viewer).findHandleAt(p);
		if (handle != null) {
			setDragTracker(handle.getDragTracker());
			return true;
		}
	}
	updateTargetRequest();
	((SelectionRequest)getTargetRequest()).setLastButtonPressed(button);
	updateTargetUnderMouse();
	EditPart editpart = getTargetEditPart();
	if(editpart != null){
		setDragTracker(editpart.getDragTracker(getTargetRequest()));
		lockTargetEditPart(editpart);
		return true;
	}
	return false;
}

protected boolean handleButtonUp(int button) {
	if (getCurrentInput().isAnyButtonDown())
		return false;
	((SelectionRequest)getTargetRequest()).setLastButtonPressed(0);
	setDragTracker(null);
	setState(STATE_INITIAL);
	unlockTargetEditPart();
	return true;
}

protected boolean handleFocusLost() {
	if (isInState(STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
		if (getDragTracker() != null)
			setDragTracker(null);
		setState(STATE_INITIAL);
		return true;
	}
	return false;
}


protected boolean handleHover(){
	setHoverActive(true);
	showHoverFeedback();
	return true;
}

protected boolean handleHoverStop(){
	eraseHoverFeedback();
	return true;
}

protected boolean handleKeyDown(KeyEvent e) {
	resetHover();
	
	if (acceptArrowKey(e))
		if (stateTransition(STATE_ACCESSIBLE_DRAG, STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
			return true;
	
	if (acceptAbort(e)){
		if (getDragTracker() != null)
			setDragTracker(null);
		if (isInState(STATE_TRAVERSE_HANDLE | STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
			placeMouseInViewer(getStartLocation().getTranslated(6, 6));
		setState(STATE_INITIAL);
		setLastHandleProvider(null);
		return true;
	}
	
	if (acceptTraverseHandle(e)) {
		if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
			if (getDragTracker() != null)
				getDragTracker().commitDrag();
		if (isInState(STATE_ACCESSIBLE_DRAG | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)){
			setDragTracker(null);
			getCurrentViewer().flush();
		}
		if (!handleTraverseHandle(e))
			setState(STATE_INITIAL);
		return true;
	}
	
	if (acceptDragCommit(e)){
		if (getDragTracker() != null)
			getDragTracker().commitDrag();
		setDragTracker(null);
		setState(STATE_INITIAL);
		handleIndex--;
		placeMouseInViewer(getLocation().getTranslated(6, 6));
		return true;
	}
	
	if (isInState(STATE_INITIAL)){
		if (getCurrentViewer().getKeyHandler() != null &&
			getCurrentViewer().getKeyHandler().keyPressed(e))
				return true;
	}

	return false;
}

protected boolean handleKeyUp(KeyEvent e){
	if (isInState(STATE_INITIAL)
		&& getCurrentViewer().getKeyHandler() != null
		&& getCurrentViewer().getKeyHandler().keyReleased(e))
		return true;

	return false;
}

protected boolean handleMove() {
	if (stateTransition(STATE_ACCESSIBLE_DRAG, STATE_INITIAL))
		setDragTracker(null);
	if (isInState(STATE_INITIAL)){
		updateTargetRequest();
		updateTargetUnderMouse();
		showTargetFeedback();
		return true;
	} else if (isInState(STATE_TRAVERSE_HANDLE)){
		EditPartViewer viewer = getCurrentViewer();
		if (viewer instanceof GraphicalViewer) {
			Handle handle = ((GraphicalViewer) viewer).findHandleAt(getLocation());
			if (handle != null) {
				setState(STATE_ACCESSIBLE_DRAG);
				setStartLocation(getLocation());
				setDragTracker(handle.getDragTracker());
				return true;
			} else {
				setState(STATE_INITIAL);
			}
		}
	}
	return false;
}

/**
 * @see org.eclipse.gef.Tool#nativeDragFinished(DragSourceEvent, EditPartViewer)
 */
public boolean handleNativeDragFinished(DragSourceEvent event) {
	if (getDragTracker() != null)
		getDragTracker().nativeDragFinished(event, getCurrentViewer());
	setDragTracker(null);
	unlockTargetEditPart();
	return true;
}

/**
 * @see org.eclipse.gef.Tool#nativeDragStarted(DragSourceEvent, EditPartViewer)
 */
public boolean handleNativeDragStarted(DragSourceEvent event) {
	if (getDragTracker() != null)
		getDragTracker().nativeDragStarted(event, getCurrentViewer());
	setState(STATE_INITIAL);
	return true;
}

private boolean handleTraverseHandle(KeyEvent e){
	EditPart focus = getCurrentViewer().getFocusEditPart();
	if (focus.getSelected() == EditPart.SELECTED_NONE)
		return false;

	AccessibleHandleProvider provider;
	provider = (AccessibleHandleProvider)focus.getAdapter(AccessibleHandleProvider.class);
	if (provider == null || provider.getAccessibleHandleLocations().isEmpty())
		return false;

	/*
	 * At this point, a handle provider with 1 or more handles has been obtained
	 */
	setState(STATE_TRAVERSE_HANDLE);
	List locations = provider.getAccessibleHandleLocations();
	//Goto next index, wrapping if necessary
	if (e.character == '.')
		handleIndex = (++handleIndex) % locations.size();
	else
		handleIndex = (--handleIndex + locations.size()) % locations.size();
	if (getLastHandleProvider() != focus){
		handleIndex = 0;
		setLastHandleProvider(focus);
	}

	Point loc = (Point)locations.get(handleIndex);
	placeMouseInViewer(loc);
	return true;
}

protected boolean handleViewerExited() {
	if (isInState(STATE_ACCESSIBLE_DRAG | 
					STATE_ACCESSIBLE_DRAG_IN_PROGRESS | 
					STATE_TRAVERSE_HANDLE |
					STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
		if (getDragTracker() != null)
			setDragTracker(null);
		setState(STATE_INITIAL);
	}
	return super.handleViewerExited();
}


/*
 * defined on interface
 */
public void keyDown(KeyEvent evt, EditPartViewer viewer){
	if (getDragTracker() != null)
		getDragTracker().keyDown(evt, viewer);
	super.keyDown(evt,viewer);
}

/*
 * defined on interface
 */
public void keyUp(KeyEvent evt, EditPartViewer viewer){
	if (getDragTracker() != null)
		getDragTracker().keyUp(evt, viewer);
	super.keyUp(evt,viewer);
}

public void mouseDown(MouseEvent e, EditPartViewer viewer) {
	super.mouseDown(e,viewer);
	if (getDragTracker() != null)
		getDragTracker().mouseDown(e, viewer);
}

public void mouseDoubleClick(MouseEvent e, EditPartViewer viewer) {
	super.mouseDoubleClick(e,viewer);
	if (getDragTracker() != null)
		getDragTracker().mouseDoubleClick(e, viewer);
}

/**
 * Handles mouse drag events. The events are forwarded to the
 * current tracker.
 */
public void mouseDrag(MouseEvent e, EditPartViewer viewer) {
	if (getDragTracker() != null)
		getDragTracker().mouseDrag(e, viewer);
	super.mouseDrag(e, viewer);
}

/**
 * Handles mouse moves (if the mouse button is up) within a viewer.
 */
public void mouseHover(MouseEvent me, EditPartViewer viewer) {
	if (getDragTracker() != null)
		getDragTracker().mouseHover(me, viewer);
	super.mouseHover(me, viewer);
}

public void mouseMove(MouseEvent me, EditPartViewer viewer){
	if (getDragTracker() != null)
		getDragTracker().mouseMove(me, viewer);
	super.mouseMove(me, viewer);
}

/**
 * Handles mouse up events. The events are forwarded to the
 * current tracker.
 */
public void mouseUp(MouseEvent e, EditPartViewer viewer) {
	if (getDragTracker() != null)
		getDragTracker().mouseUp(e, viewer);
	super.mouseUp(e, viewer);
}

protected void refreshCursor(){
	//If we have a DragTracker, let it control the Cursor
	if (getDragTracker() == null)
		super.refreshCursor();
}

public void setDragTracker(DragTracker newDragTracker) {
	if (newDragTracker == dragTracker)
		return;
	if (dragTracker != null)
		dragTracker.deactivate();
	dragTracker = newDragTracker;
	refreshCursor();
//	if (!getCurrentInput().isMouseButtonDown(3))
//		setMouseCapture(dragTracker != null);
	if (newDragTracker != null){
		newDragTracker.setEditDomain(getDomain());
		newDragTracker.activate();
		newDragTracker.setViewer(getCurrentViewer());
	}
}

private void setLastHandleProvider(EditPart part){
	if (part == null)
		cachedHandlePart = null;
	else
		cachedHandlePart = new WeakReference(part);
}

protected void showHoverFeedback(){
	if (getTargetEditPart() == null)
		return;
	if (getTargetHoverRequest() == null)
		return;
	getTargetEditPart().showTargetFeedback(getTargetHoverRequest());
}

protected void updateHoverRequest() {
	LocationRequest request = (LocationRequest)getTargetHoverRequest();
	request.setLocation(getLocation());
}

protected void updateTargetRequest(){
	SelectionRequest request = (SelectionRequest)getTargetRequest();
	request.setModifiers(getCurrentInput().getModifiers());
	request.setType(getCommandName());
	request.setLocation(getLocation());
	updateHoverRequest();
}

protected String getDebugNameForState(int state) {
	if (state == STATE_TRAVERSE_HANDLE)
		return "Traverse Handle";  //$NON-NLS-1$
	return super.getDebugNameForState(state);
}

}

