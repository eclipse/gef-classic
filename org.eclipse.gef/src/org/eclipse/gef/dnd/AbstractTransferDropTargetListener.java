package org.eclipse.gef.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.dnd.*;

/**
 * An abstract implementation of TransferDropTargetListener that adds a reference to 
 * the {@link EditPartViewer} that contains the {@link DropTarget} widget.
 */
abstract public class AbstractTransferDropTargetListener 
	implements TransferDropTargetListener
{

private boolean showingFeedback;
private DropTargetEvent currentEvent;
private EditPartViewer viewer;
private Transfer transfer;
private EditPart target;
private Request request;

/**
 * Creates a new AbstractTransferDropTargetListener with the given EditPartViewer.
 */
public AbstractTransferDropTargetListener(EditPartViewer viewer) {
	setViewer(viewer);
}

/**
 * Creates a new AbstractTransferDropTargetListener with the given EditPartViewer 
 * and Transfer.
 */
public AbstractTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
	setViewer(viewer);
	setTransfer(xfer);
}

/**
 * @see TransferDropTargetListener#activate()
 */
public void activate() {}

/**
 * Creates and returns a new Request.  Subclasses can override this method to
 * create specialized requests.
 */
protected Request createTargetRequest() {
	return new Request();
}

/**
 * Called when this listener is temporarily disabled.  Sets the Request and target 
 * EditPart to <code>null</code>.
 * 
 * @see TransferDropTargetListener#deactivate()
 */
public void deactivate() {
	setTargetEditPart(null);
	request = null;
}

/**
 * Updates the DropData and then handles the event.  Subclasses should 
 * override {@link #handleDragEnter()} to perform actions for this event.
 */
public void dragEnter(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Enter: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
	handleDragEnter();
}

/**
 * Updates the DropData, handles the event, and then unloads the drag 
 * event.  Subclasses should override {@link #handleDragLeave()} to 
 * perform actions for this event.
 */
public void dragLeave(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Leave: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
	unload();
}

/**
 * Updates the DropData and then handles the event.  Subclasses should 
 * override {@link #handleDragOperationChanged()} to perform actions for this event.
 */
public void dragOperationChanged(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Operation Changed: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
	handleDragOperationChanged();
}

/**
 * Updates the DropData and then handles the event.  Subclasses should 
 * override {@link #handleDragOver()} to perform actions for this event.
 */
public void dragOver(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drag Over: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
	handleDragOver();
}

/**
 * Updates the DropData and then handles the event.  Subclasses should 
 * override {@link #handleDrop()} to perform actions for this event.
 */
public void drop(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
	handleDrop();
	unload();
}

/**
 * Updates the DropData and then handles the event.  Subclasses should 
 * override {@link #handleDropAccept()} to perform actions for this event.
 */
public void dropAccept(DropTargetEvent event) {
	if (GEF.DebugDND)
		GEF.debug("Drop Accept: " + toString()); //$NON-NLS-1$
	setCurrentEvent(event);
}

/**
 * Tells the target EditPart to erase its target feedback.
 * Does nothing if there is no target, or if the target has not been requested to
 * show target feedback.
 */
protected void eraseTargetFeedback() {
	if (getTargetEditPart() != null && showingFeedback){
		showingFeedback = false;
		getTargetEditPart().eraseTargetFeedback(getTargetRequest());
	}
}

/**
 * Returns the current DropTargetEvent.
 */
public DropTargetEvent getCurrentEvent() {
	return currentEvent;
}

/**
 * Returns the current mouse location, as a Draw2d Point, relative to the Control.
 */
protected Point getDropLocation(){
	org.eclipse.swt.graphics.Point swt;
	swt = new org.eclipse.swt.graphics.Point(getCurrentEvent().x, getCurrentEvent().y);
	DropTarget target = (DropTarget)getCurrentEvent().widget;
	swt = target.getControl().toControl(swt);
	return new Point(swt.x, swt.y);
}

/**
 * Returns the target EditPart.
 */
protected EditPart getTargetEditPart() {
	return target;
}

/**
 * Returns the target request.  If the request is null, 
 * {@link #createTargetRequest()} is called and the newly
 * created request is returned.
 */
protected Request getTargetRequest() {
	if (request == null)
		request = createTargetRequest();
	return request;
}

/**
 * Returns the Transfer type that this listener can handle.
 */
public Transfer getTransfer() {
	return transfer;
}

/**
 * Returns the EditPartViewer that is the target of the drop.
 */
public EditPartViewer getViewer() {
	return viewer;
}

protected void handleDragEnter() {} 

/**
 * Called when the user changes the Drag operation.
 */
protected void handleDragOperationChanged() {
//Erase any old feedback
	eraseTargetFeedback();
//Update request based on the new operation type
	updateTargetRequest();
//Update the target based on the updated request
	updateTargetEditPart();
//Show target feedback
	showTargetFeedback();
}

/**
 * Updates the target Request and EditPart.
 */
protected void handleDragOver() {
	updateTargetRequest();
	updateTargetEditPart();
	showTargetFeedback();
}

/**
 * Sets the event's <code>detail</code> field to <code>DND.DROP_NONE</code> if its
 * <code>data</code> field is <code>null</code>.  Otherwise, updates the target
 * Request and EditPart, gets a command from the target EditPart, then executes
 * that command.  
 */
protected void handleDrop() {
	updateTargetRequest();
	updateTargetEditPart();
	
	if (getTargetEditPart() != null){
		Command command = getTargetEditPart().getCommand(getTargetRequest());
		getViewer().getEditDomain().getCommandStack().execute(command);
	} else
		getCurrentEvent().detail = DND.DROP_NONE;
}

/**
 * Shows target feedback.  Subclasses should return <code>true</code> when 
 * overriding this method.
 */
protected boolean handleEnteredEditPart() {
	showTargetFeedback();
	return true;
}

/**
 * Erases target feedback.  Subclasses should return <code>true</code> when 
 * overriding this method.
 */
protected boolean handleExitingEditPart() {
	eraseTargetFeedback(); 
	return true;
}

/**
 * Returns <code>true</code> if one of the TransferData types in the event is 
 * supported by this listener.  Also has the side effect of setting 
 * <code>event.currentDataType</code> to the first supported TransferData type.  
 */
public boolean isEnabled(DropTargetEvent event) {
	for (int i=0; i<event.dataTypes.length; i++) {
		if (getTransfer().isSupportedType(event.dataTypes[i])) {
			setCurrentEvent(event);
			event.currentDataType = event.dataTypes[i];
			updateTargetRequest();
			updateTargetEditPart();
			Command command = null;
			if (getTargetEditPart() != null)
				command = getTargetEditPart().getCommand(getTargetRequest());
			if (command != null && command.canExecute())
				return true;
		}
	}
	return false;
}

/**
 * Sets the current DropTargetEvent.
 */
public void setCurrentEvent(DropTargetEvent currentEvent) {
	this.currentEvent = currentEvent;
}

/**
 * Sets the target EditPart.
 */
protected void setTargetEditPart(EditPart ep) {
	if (ep != target) {
		if (target != null) {
			handleExitingEditPart();
			if (GEF.DebugDND){
				GEF.debug("Exited EditPart: " + target.toString()); //$NON-NLS-1$
			}
		}
		target = ep;
		if (target != null){
			handleEnteredEditPart();
			if (GEF.DebugDND){
				GEF.debug("Entered EditPart: " + target); //$NON-NLS-1$
			}
		}
	}
}

/**
 * Sets the Tranfer type that this listener can handle.
 */
public void setTransfer(Transfer xfer) {
	transfer = xfer;
}

/**
 * Sets the EditPartViewer.
 */
protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

/**
 * Asks the target EditPart to show target feedback if it is not <code>null</code>.
 * 
 * @see EditPart#showTargetFeedback(Request)
 */
protected void showTargetFeedback() {
	if (getTargetEditPart() != null){
		showingFeedback = true;
		getTargetEditPart().showTargetFeedback(getTargetRequest());
	}
}

/**
 * Erases target feedback and sets the request to <code>null</code>.
 */
protected void unload() {
	eraseTargetFeedback();
	request = null;
	setTargetEditPart(null);
	setCurrentEvent(null);
}

/**
 * Updates the target EditPart.
 */
protected void updateTargetEditPart() {
	EditPart ep = getViewer().findObjectAt(getDropLocation());
	if (ep != null)
		ep = ep.getTargetEditPart(getTargetRequest());
	setTargetEditPart(ep);
}

/**
 * Subclasses should implement this to update the target Request.
 */
abstract protected void updateTargetRequest();

}
