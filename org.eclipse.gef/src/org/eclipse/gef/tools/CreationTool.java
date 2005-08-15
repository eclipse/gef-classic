/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.tools;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

/**
 * The CreationTool creates new {@link EditPart EditParts} via a {@link CreationFactory}.
 * If the user simply clicks on the viewer, the default sized EditPart will be created at
 * that point.  If the user clicks and drags, the created EditPart will be sized based on
 * where the user clicked and dragged.
 */
public class CreationTool
	extends TargetingTool
{

/**
 * Property to be used in {@link AbstractTool#setProperties(java.util.Map)} for 
 * {@link #setFactory(CreationFactory)}.
 */
public static final Object PROPERTY_CREATION_FACTORY = "factory"; //$NON-NLS-1$

private CreationFactory factory;
private SnapToHelper helper;


/**
 * Default constructor.  Sets the default and disabled cursors.
 */
public CreationTool() {
	setDefaultCursor(SharedCursors.CURSOR_TREE_ADD);
	setDisabledCursor(SharedCursors.NO);
}

/**
 * Constructs a new CreationTool with the given factory.
 * @param aFactory the creation factory
 */
public CreationTool(CreationFactory aFactory) {
	this();
	setFactory(aFactory);
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#applyProperty(java.lang.Object, java.lang.Object)
 */
protected void applyProperty(Object key, Object value) {
	if (PROPERTY_CREATION_FACTORY.equals(key)) {
		if (value instanceof CreationFactory)
			setFactory((CreationFactory)value);
		return;
	}
	super.applyProperty(key, value);
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
 */
protected Cursor calculateCursor() {
	/*
	 * Fix for Bug# 66010
	 * The following two lines of code were added for the case where a tool is
	 * activated via the keyboard (that code hasn't been released yet).  However, they
	 * were causing a problem as described in 66010.  Since the keyboard activation code
	 * is not being released for 3.0, the following lines are being commented out.
	 */
//	if (isInState(STATE_INITIAL)) 
//		return getDefaultCursor();
	return super.calculateCursor();
}

/**
 * Creates a {@link CreateRequest} and sets this tool's factory on the request.
 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
 */
protected Request createTargetRequest() {
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory());
	return request;
}

/**
 * @see org.eclipse.gef.Tool#deactivate()
 */
public void deactivate() {
	super.deactivate();
	helper = null;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
 */
protected String getCommandName() {
	return REQ_CREATE;
}

/**
 * Cast the target request to a CreateRequest and returns it.
 * @return the target request as a CreateRequest
 * @see TargetingTool#getTargetRequest()
 */
protected CreateRequest getCreateRequest() {
	return (CreateRequest)getTargetRequest();
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
 */
protected String getDebugName() {
	return "Creation Tool";//$NON-NLS-1$
}

/**
 * Returns the creation factory used to create the new EditParts.
 * @return the creation factory
 */
protected CreationFactory getFactory() {
	return factory;
}

/**
 * The creation tool only works by clicking mouse button 1 (the left mouse button in a
 * right-handed world).  If any other button is pressed, the tool goes into an invalid
 * state.  Otherwise, it goes into the drag state, updates the request's location and 
 * calls {@link TargetingTool#lockTargetEditPart(EditPart)} with the edit part that was
 * just clicked on.
 * 
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
 */
protected boolean handleButtonDown(int button) {
	if (button != 1) {
		setState(STATE_INVALID);
		handleInvalidInput();
		return true;
	}
	if (stateTransition(STATE_INITIAL, STATE_DRAG)) {
		getCreateRequest().setLocation(getLocation());
		lockTargetEditPart(getTargetEditPart());
		// Snap only when size on drop is employed
		if (getTargetEditPart() != null)
			helper = (SnapToHelper)getTargetEditPart().getAdapter(SnapToHelper.class);
	}
	return true;
}

/**
 * If the tool is currently in a drag or drag-in-progress state, it goes into the 
 * terminal state, performs some cleanup (erasing feedback, unlocking target edit part), 
 * and then calls {@link #performCreation(int)}.
 * 
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
 */
protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG | STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
		eraseTargetFeedback();
		unlockTargetEditPart();
		performCreation(button);
	}

	setState(STATE_TERMINAL);
	handleFinished();

	return true;
}

/**
 * Updates the request, sets the current command, and asks to show feedback.
 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
 */
protected boolean handleDragInProgress() {
	if (isInState(STATE_DRAG_IN_PROGRESS)) {
		updateTargetRequest();
		setCurrentCommand(getCommand());
		showTargetFeedback();
	}
	return true;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
 */
protected boolean handleDragStarted() {
	return stateTransition(STATE_DRAG, STATE_DRAG_IN_PROGRESS);
}

/**
 * If the user is in the middle of creating a new edit part, the tool erases feedback and 
 * goes into the invalid state when focus is lost.
 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
 */
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

/**
 * Updates the request and mouse target, gets the current command and asks to show
 * feedback.
 * @see org.eclipse.gef.tools.AbstractTool#handleMove()
 */
protected boolean handleMove() {
	updateTargetRequest();
	updateTargetUnderMouse();
	setCurrentCommand(getCommand());
	showTargetFeedback();
	return true;
}

/**
 * Executes the current command and selects the newly created object.  The button that was
 * released to cause this creation is passed in, but since {@link #handleButtonDown(int)}
 * goes into the invalid state if the button pressed is not button 1, this will always be 
 * button 1.  
 * 
 * @param button the button that was pressed
 */
protected void performCreation(int button) {
	EditPartViewer viewer = getCurrentViewer();
	executeCurrentCommand();
	selectAddedObject(viewer);
}

/*
 * Add the newly created object to the viewer's selected objects.
 */
private void selectAddedObject(EditPartViewer viewer) {
	final Object model = getCreateRequest().getNewObject();
	if (model == null || viewer == null)
		return;
	Object editpart = viewer.getEditPartRegistry().get(model);
	if (editpart instanceof EditPart) {
		//Force the new object to get positioned in the viewer. 
		viewer.flush();
		viewer.select((EditPart)editpart);
	}
}

/**
 * Sets the creation factory used to create the new edit parts.
 * @param factory the factory
 */
public void setFactory(CreationFactory factory) {
	this.factory = factory;
}

/**
 * Sets the location (and size if the user is performing size-on-drop) of the request.
 * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
 */
protected void updateTargetRequest() {
	CreateRequest req = getCreateRequest();
	if (isInState(STATE_DRAG_IN_PROGRESS)) {
		Point loq = getStartLocation();
		Rectangle bounds = new Rectangle(loq, loq);
		bounds.union(loq.getTranslated(getDragMoveDelta()));
		req.setSize(bounds.getSize());
		req.setLocation(bounds.getLocation());
		req.getExtendedData().clear();
		if (!getCurrentInput().isAltKeyDown() && helper != null) {
			PrecisionRectangle baseRect = new PrecisionRectangle(bounds);
			PrecisionRectangle result = baseRect.getPreciseCopy();
			helper.snapRectangle(req, PositionConstants.NSEW, 
				baseRect, result);
			req.setLocation(result.getLocation());
			req.setSize(result.getSize());
		}
	} else {
		req.setSize(null);
		req.setLocation(getLocation());
	}
}

}
