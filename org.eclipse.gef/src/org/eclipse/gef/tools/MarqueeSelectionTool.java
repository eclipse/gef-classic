package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.*;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * This tool implements the selection of multiple objects in rectangular area.
 */
public class MarqueeSelectionTool
	extends AbstractTool
{

static final int TOGGLE_MODE = 1;
static final int APPEND_MODE = 2;

private int mode;

private Shape marqueeRectangleFigure;
private List allChildren = new ArrayList();
private List selectedEditParts;
private Request targetRequest;

private static final Request MARQUEE_REQUEST =
	new Request(RequestConstants.REQ_SELECTION); 

/**
 * Creates a new MarqueeSelectionTool.
 */
public MarqueeSelectionTool() {
	setDefaultCursor(SharedCursors.CROSS); 
}

private List calculateNewSelection() {

	List newSelections = new ArrayList();
	List children = getAllChildren();

	// Calculate new selections based on which children fall
	// inside the marquee selection rectangle.  Do not select
	// children who are not visible
	for (int i = 0; i<children.size(); i++) {
		EditPart child = (EditPart) children.get(i);
		IFigure figure = ((GraphicalEditPart)child).getFigure();
		Rectangle r = figure.getBounds().getCopy();
		figure.translateToAbsolute(r);

		if ( getMarqueeSelectionRectangle().contains(r.getTopLeft()) &&
			  getMarqueeSelectionRectangle().contains(r.getBottomRight()) && 
			  figure.isVisible() ){
				if(child.getTargetEditPart(MARQUEE_REQUEST) == child)
					newSelections.add(child);			
		}
	}	
	return newSelections;
}

private Request createTargetRequest(){
	return MARQUEE_REQUEST;
}

/**
 * Erases feedback if necessary and puts the tool into the terminal state.
 */
public void deactivate() {
	if (isInState(STATE_DRAG_IN_PROGRESS)){
		eraseMarqueeFeedback();
		eraseTargetFeedback();
	}
	super.deactivate();
	allChildren = new ArrayList();
	setState(STATE_TERMINAL);
}

private void eraseMarqueeFeedback() {
	if (marqueeRectangleFigure != null) {
		removeFeedback(marqueeRectangleFigure);
		marqueeRectangleFigure = null;
	}
}

private void eraseTargetFeedback() {
	if (selectedEditParts==null)
		return;
	ListIterator oldEditParts = selectedEditParts.listIterator();
	while(oldEditParts.hasNext()){
		EditPart editPart = (EditPart)oldEditParts.next();
		editPart.eraseTargetFeedback(getTargetRequest());
	}
}

/**
 * Returns a list including all of the children
 * of the edit part passed in.
 */
private List getAllChildren(EditPart editPart, List allChildren) {
	List children = editPart.getChildren();
	for (int i=0; i<children.size(); i++) {
		GraphicalEditPart child = (GraphicalEditPart) children.get(i);
		allChildren.add(child);
		getAllChildren(child, allChildren);
	}
	return allChildren;
}

/**
 * Return a vector including all of the children
 * of the root editpart
 */
private List getAllChildren(){
	if (allChildren.isEmpty())
		allChildren = getAllChildren(getCurrentViewer().getRootEditPart(), new ArrayList());
	return allChildren;
}

/**
 * Returns the name identifier of the command that the tool
 * is currently looking for.
 */
protected String getCommandName(){
	return REQ_SELECTION;
}

/**
 * Returns the debug name for this tool.
 */
protected String getDebugName(){
	return "Marquee Tool";//$NON-NLS-1$
}

private IFigure getMarqueeFeedbackFigure() {		
	if (marqueeRectangleFigure == null){
		marqueeRectangleFigure = new RectangleFigure();
		FigureUtilities.makeGhostShape(marqueeRectangleFigure);
		marqueeRectangleFigure.setFill(false);
		marqueeRectangleFigure.setLineStyle(Graphics.LINE_DASHDOT);
		marqueeRectangleFigure.setForegroundColor(ColorConstants.white);
		addFeedback(marqueeRectangleFigure);
	}
	return marqueeRectangleFigure;
}

private Rectangle getMarqueeSelectionRectangle() {
	return new Rectangle(getStartLocation(), getLocation());
}

private int getSelectionMode(){
	return mode;
}

private Request getTargetRequest(){
	if (targetRequest == null)
		targetRequest = createTargetRequest();
	return targetRequest;
}

/**
 * Sets the selection mode to <code>TOGGLE_MODE</code> or
 * <code>APPEND_MODE</code> depending on the keyboard input.
 */
protected boolean handleButtonDown(int button) {
	if (!isGraphicalViewer())
		return true;
	if (button != 1){
		setState(STATE_INVALID);
		handleInvalidInput();
	}
	if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)){
		if (getCurrentInput().isControlKeyDown())
			setSelectionMode(TOGGLE_MODE);
		else if (getCurrentInput().isShiftKeyDown())
			setSelectionMode(APPEND_MODE);
	}
	return true;
}

/**
 * Erases feedback and performs the selection.
 */
protected boolean handleButtonUp(int button) {
	if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
		eraseTargetFeedback();
		eraseMarqueeFeedback();
		performMarqueeSelect();
	}
	handleFinished();
	return true;
}

/**
 * Calculates the selection and updates the feedback.
 */
protected boolean handleDragInProgress() {
	if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
		showMarqueeFeedback();
		eraseTargetFeedback();
		selectedEditParts = calculateNewSelection();
		showTargetFeedback();
	}
	return true;
}

/**
 * This method is called when mouse or keyboard input is
 * invalid and erases the feedback.
 */
protected boolean handleInvalidInput(){
	eraseTargetFeedback();
	eraseMarqueeFeedback();
	return true;
}

private boolean isGraphicalViewer() {
	return getCurrentViewer() instanceof GraphicalViewer;
}

private void performMarqueeSelect() {
	EditPartViewer viewer = getCurrentViewer();

	List newSelections = calculateNewSelection();

	// If in multi select mode, add the new selections to the already
	// selected group; otherwise, clear the selection and select the new group
	if (getSelectionMode() == APPEND_MODE) {
		for (int i=0; i<newSelections.size(); i++) {
			EditPart editPart = (EditPart)newSelections.get(i);	
			viewer.appendSelection(editPart);
		}
	} else {
		viewer.setSelection(new StructuredSelection(newSelections));
	}
}

/**
 * Sets the EditPartViewer.  Also sets the appropriate default cursor
 * based on the type of viewer.
 */
public void setViewer(EditPartViewer viewer){
	if(viewer == getCurrentViewer())
		return;
	super.setViewer(viewer);
	if (viewer instanceof GraphicalViewer)
		setDefaultCursor(SharedCursors.CROSS);
	else
		setDefaultCursor(SharedCursors.NO);
}

private void setSelectionMode(int mode){
	this.mode = mode;
}

private void showMarqueeFeedback() {
	Rectangle rect = getMarqueeSelectionRectangle().getCopy();
	getMarqueeFeedbackFigure().translateToRelative(rect);
	getMarqueeFeedbackFigure().setBounds(rect);
}

private void showTargetFeedback() {
	for (int i=0; i < selectedEditParts.size(); i++) {
		EditPart editPart = (EditPart) selectedEditParts.get(i);
		editPart.showTargetFeedback(getTargetRequest());
	}
}

}