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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;

/**
 * A Tool which selects multiple objects inside a rectangular area of a Graphical Viewer. 
 * If the SHIFT key is pressed at the beginning of the drag, the enclosed items will be
 * appended to the current selection.  If the CONTROL key is pressed at the beginning of
 * the drag, the enclosed items will have their selection state inverted.
 * <P>
 * By default, only editparts whose figure's are on the primary layer will be considered
 * within the enclosed rectangle.
 */
public class MarqueeSelectionTool
	extends AbstractTool
{

/**
 * The property to be used in {@link AbstractTool#setProperties(java.util.Map)} for 
 * {@link #setMarqueeBehavior(int)}.
 */
public static final Object PROPERTY_MARQUEE_BEHAVIOR = "marqueeBehavior"; //$NON-NLS-1$

/**
 * For marquee tools that should select nodes.  This is the default type for this
 * tool.
 * @since 3.1
 */
public static final int BEHAVIOR_NODES_CONTAINED = new Integer(1).intValue();
/**
 * For marquee tools that should select connections
 * @since 3.1
 */
public static final int BEHAVIOR_CONNECTIONS_TOUCHED = new Integer(2).intValue();
/**
 * For marquee tools that should select both nodes and connections.
 * @since 3.1
 */
public static final int BEHAVIOR_NODES_AND_CONNECTIONS = new Integer(3).intValue();


static final int TOGGLE_MODE = 1;
static final int APPEND_MODE = 2;

private int mode;

private Figure marqueeRectangleFigure;
private Set allChildren = new HashSet();
private List selectedEditParts;
private Request targetRequest;
private int marqueeBehavior = BEHAVIOR_NODES_CONTAINED;

private static final Request MARQUEE_REQUEST =
		new Request(RequestConstants.REQ_SELECTION); 

/**
 * Creates a new MarqueeSelectionTool of default type {@link #BEHAVIOR_NODES_CONTAINED}.
 */
public MarqueeSelectionTool() {
	setDefaultCursor(SharedCursors.CROSS); 
	setUnloadWhenFinished(false);
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#applyProperty(java.lang.Object, java.lang.Object)
 */
protected void applyProperty(Object key, Object value) {
	if (PROPERTY_MARQUEE_BEHAVIOR.equals(key)) {
		if (value instanceof Integer)
			setMarqueeBehavior(((Integer)value).intValue());
		return;
	}
	super.applyProperty(key, value);
}

private List calculateNewSelection() {
	List newSelections = new ArrayList();
	// Calculate new selections based on which children fall
	// inside the marquee selection rectangle.  Do not select
	// children that are not visible.
	Rectangle marqueeRect = getMarqueeSelectionRectangle();
	for (Iterator itr = getAllChildren().iterator(); itr.hasNext();) {
		EditPart child = (EditPart)itr.next();
		IFigure figure = ((GraphicalEditPart)child).getFigure();
		if (!child.isSelectable() 
				|| child.getTargetEditPart(MARQUEE_REQUEST) != child
				|| !isFigureVisible(figure)
				|| !figure.isShowing())
			continue;
		Rectangle r = figure.getBounds().getCopy();
		figure.translateToAbsolute(r);
		boolean included = false;
		if (child instanceof ConnectionEditPart && marqueeRect.intersects(r)) {
			figure.translateToRelative(r.setBounds(marqueeRect));
			included = ((PolylineConnection)figure).getPoints().intersects(r);
		} else
			included = marqueeRect.contains(r);
		if (included)
			newSelections.add(child);
	}
	return newSelections;
}

private Request createTargetRequest() {
	return MARQUEE_REQUEST;
}

/**
 * Erases feedback if necessary and puts the tool into the terminal state.
 */
public void deactivate() {
	if (isInState(STATE_DRAG_IN_PROGRESS)) {
		eraseMarqueeFeedback();
		eraseTargetFeedback();
	}
	super.deactivate();
	allChildren.clear();
	setState(STATE_TERMINAL);
}

private void eraseMarqueeFeedback() {
	if (marqueeRectangleFigure != null) {
		removeFeedback(marqueeRectangleFigure);
		marqueeRectangleFigure = null;
	}
}

private void eraseTargetFeedback() {
	if (selectedEditParts == null)
		return;
	ListIterator oldEditParts = selectedEditParts.listIterator();
	while (oldEditParts.hasNext()) {
		EditPart editPart = (EditPart)oldEditParts.next();
		editPart.eraseTargetFeedback(getTargetRequest());
	}
}

/**
 * Adds all the children of the given editpart to the given set
 */
private void getAllChildren(EditPart editPart, Set allChildren) {
	List children = editPart.getChildren();
	for (int i = 0; i < children.size(); i++) {
		GraphicalEditPart child = (GraphicalEditPart) children.get(i);
		if (marqueeBehavior == BEHAVIOR_NODES_CONTAINED)
			allChildren.add(child);
		else if (marqueeBehavior == BEHAVIOR_CONNECTIONS_TOUCHED) {
			allChildren.addAll(child.getSourceConnections());
			allChildren.addAll(child.getTargetConnections());
		} else if (marqueeBehavior == BEHAVIOR_NODES_AND_CONNECTIONS) {
			allChildren.add(child);
			allChildren.addAll(child.getSourceConnections());
			allChildren.addAll(child.getTargetConnections());
		}
		getAllChildren(child, allChildren);
	}
}

/**
 * Return a set including all of the children of the root editpart
 */
private Set getAllChildren() {
	if (allChildren.isEmpty())
		getAllChildren(getCurrentViewer().getRootEditPart(), allChildren);
	return allChildren;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
 */
protected String getCommandName() {
	return REQ_SELECTION;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
 */
protected String getDebugName() {
	return "Marquee Tool: " + marqueeBehavior;//$NON-NLS-1$
}

private IFigure getMarqueeFeedbackFigure() {
	if (marqueeRectangleFigure == null) {
		marqueeRectangleFigure = new MarqueeRectangleFigure();
		addFeedback(marqueeRectangleFigure);
	}
	return marqueeRectangleFigure;
}

private Rectangle getMarqueeSelectionRectangle() {
	return new Rectangle(getStartLocation(), getLocation());
}

private int getSelectionMode() {
	return mode;
}

private Request getTargetRequest() {
	if (targetRequest == null)
		targetRequest = createTargetRequest();
	return targetRequest;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
 */
protected boolean handleButtonDown(int button) {
	if (!isGraphicalViewer())
		return true;
	if (button != 1) {
		setState(STATE_INVALID);
		handleInvalidInput();
	}
	if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)) {
		if (getCurrentInput().isControlKeyDown())
			setSelectionMode(TOGGLE_MODE);
		else if (getCurrentInput().isShiftKeyDown())
			setSelectionMode(APPEND_MODE);
	}
	return true;
}

/**
 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
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
 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
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
 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
 */
protected boolean handleFocusLost() {
	if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
		handleFinished();
		return true;
	}
	return false;
}

/**
 * This method is called when mouse or keyboard input is invalid and erases the feedback.
 * @return <code>true</code>
 */
protected boolean handleInvalidInput() {
	eraseTargetFeedback();
	eraseMarqueeFeedback();
	return true;
}

/**
 * Handles high-level processing of a key down event. 
 * KeyEvents are forwarded to the current viewer's {@link KeyHandler}, 
 * via {@link KeyHandler#keyPressed(KeyEvent)}.
 * @see AbstractTool#handleKeyDown(KeyEvent)
 */
protected boolean handleKeyDown(KeyEvent e) {
	if (super.handleKeyDown(e))
		return true;
	if (getCurrentViewer().getKeyHandler() != null
		&& getCurrentViewer().getKeyHandler().keyPressed(e))
		return true;
	return false;		
}

private boolean isFigureVisible(IFigure fig) {
	Rectangle figBounds = fig.getBounds().getCopy();
	IFigure walker = fig.getParent();
	while (!figBounds.isEmpty() && walker != null) {
		walker.translateToParent(figBounds);
		figBounds.intersect(walker.getBounds());
		walker = walker.getParent();
	}
	return !figBounds.isEmpty();
}

private boolean isGraphicalViewer() {
	return getCurrentViewer() instanceof GraphicalViewer;
}

/**
 * MarqueeSelectionTool is only interested in GraphicalViewers, not TreeViewers.
 * @see org.eclipse.gef.tools.AbstractTool#isViewerImportant(org.eclipse.gef.EditPartViewer)
 */
protected boolean isViewerImportant(EditPartViewer viewer) {
	return viewer instanceof GraphicalViewer;
}

private void performMarqueeSelect() {
	EditPartViewer viewer = getCurrentViewer();

	List newSelections = calculateNewSelection();

	// If in multi select mode, add the new selections to the already
	// selected group; otherwise, clear the selection and select the new group
	if (getSelectionMode() == APPEND_MODE) {
		for (int i = 0; i < newSelections.size(); i++) {
			EditPart editPart = (EditPart)newSelections.get(i);	
			viewer.appendSelection(editPart); 
		} 
	} else if (getSelectionMode() == TOGGLE_MODE) {
		List selected = new ArrayList(viewer.getSelectedEditParts());
		for (int i = 0; i < newSelections.size(); i++) {
			EditPart editPart = (EditPart)newSelections.get(i);	
			if (editPart.getSelected() != EditPart.SELECTED_NONE)
				selected.remove(editPart);
			else
				selected.add(editPart);
		}
		viewer.setSelection(new StructuredSelection(selected));
	} else {
		viewer.setSelection(new StructuredSelection(newSelections));
	}
}

/**
 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
 */
public void setViewer(EditPartViewer viewer) {
	if (viewer == getCurrentViewer())
		return;
	super.setViewer(viewer);
	if (viewer instanceof GraphicalViewer)
		setDefaultCursor(SharedCursors.CROSS);
	else
		setDefaultCursor(SharedCursors.NO);
}

private void setSelectionMode(int mode) {
	this.mode = mode;
}

/**
 * Sets the type of parts that this tool will select.
 * @param type {@link #BEHAVIOR_CONNECTIONS_TOUCHED} or {@link #BEHAVIOR_NODES_CONTAINED}
 *        or {@link #BEHAVIOR_NODES_AND_CONNECTIONS}
 * @since 3.1
 */
public void setMarqueeBehavior(int type) {
	if (type != BEHAVIOR_CONNECTIONS_TOUCHED && type != BEHAVIOR_NODES_CONTAINED
			&& type != BEHAVIOR_NODES_AND_CONNECTIONS)
		throw new IllegalArgumentException("Invalid marquee behaviour specified."); //$NON-NLS-1$
	marqueeBehavior = type;
}

private void showMarqueeFeedback() {
	Rectangle rect = getMarqueeSelectionRectangle().getCopy();
	getMarqueeFeedbackFigure().translateToRelative(rect);
	getMarqueeFeedbackFigure().setBounds(rect);
}

private void showTargetFeedback() {
	for (int i = 0; i < selectedEditParts.size(); i++) {
		EditPart editPart = (EditPart) selectedEditParts.get(i);
		editPart.showTargetFeedback(getTargetRequest());
	}
}

class MarqueeRectangleFigure 
	extends Figure {
	
	private int offset = 0;
	private boolean schedulePaint = true;
	private static final int DELAY = 110; //animation delay in millisecond
	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {	
		Rectangle bounds = getBounds().getCopy();
		graphics.translate(getLocation());
		
		graphics.setXORMode(true);
		graphics.setForegroundColor(ColorConstants.white);
		graphics.setBackgroundColor(ColorConstants.black);
		
		graphics.setLineStyle(Graphics.LINE_DOT);
		
		int[] points = new int[6];
		
		points[0] = 0 + offset;
		points[1] = 0;
		points[2] = bounds.width - 1;
		points[3] = 0;
		points[4] = bounds.width - 1;
		points[5] = bounds.height - 1;
		
		graphics.drawPolyline(points);
		
		points[0] = 0;
		points[1] = 0 + offset;
		points[2] = 0;
		points[3] = bounds.height - 1;
		points[4] = bounds.width - 1;
		points[5] = bounds.height - 1;
		
		graphics.drawPolyline(points);
		
		graphics.translate(getLocation().getNegated());
		
		if (schedulePaint) {
			Display.getCurrent().timerExec(DELAY, new Runnable() {
				public void run() {
					offset++;
					if (offset > 5)
						offset = 0;	
					
					schedulePaint = true;
					repaint();
				}
			});
		}
		
		schedulePaint = false;
	}
		
}

}
