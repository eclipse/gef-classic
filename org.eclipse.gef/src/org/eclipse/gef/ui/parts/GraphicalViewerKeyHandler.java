package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.lang.ref.WeakReference;
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.gef.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class GraphicalViewerKeyHandler
	extends KeyHandler
{

int counter;

/**
 * When navigating through connections, a "Node" EditPart is used as a reference.
 */
private WeakReference cachedNode;
private GraphicalViewer viewer;

public GraphicalViewerKeyHandler(GraphicalViewer viewer){
	this.viewer = viewer;
}

private boolean acceptConnection(KeyEvent event){
	return event.character == '/'
		|| event.character == '?'
		|| event.character == '\\'
		|| event.character == '|';
}

private boolean acceptIntoContainer(KeyEvent event){
	return ((event.stateMask & SWT.ALT) != 0) && (event.keyCode == SWT.ARROW_DOWN);
}

private boolean acceptLeaveConnection(KeyEvent event){
	int key = event.keyCode;
	if (getFocus() instanceof ConnectionEditPart)
		if ((key == SWT.ARROW_UP) ||
		(key == SWT.ARROW_RIGHT) ||
		(key == SWT.ARROW_DOWN) ||
		(key == SWT.ARROW_LEFT))
			return true;
	return false;
}

private boolean acceptLeaveContents(KeyEvent event){
	int key = event.keyCode;
	return getFocus() == getViewer().getContents()
		&& ((key == SWT.ARROW_UP)
			|| (key == SWT.ARROW_RIGHT)
			|| (key == SWT.ARROW_DOWN)
			|| (key == SWT.ARROW_LEFT));
}

private boolean acceptOutOf(KeyEvent event){
	return ((event.stateMask & SWT.ALT) != 0) && (event.keyCode == SWT.ARROW_UP);
}

private ConnectionEditPart findConnection(GraphicalEditPart node, ConnectionEditPart current, boolean forward){
	List connections = new ArrayList(node.getSourceConnections());
	connections.addAll(node.getTargetConnections());
	if (connections.isEmpty())
		return null;
	if (forward)
		counter ++;
	else
		counter --;
	while (counter < 0)
		counter += connections.size();
	counter %= connections.size();
	return (ConnectionEditPart)connections.get(counter % connections.size());
}

/*
 * pStart is a point in absolute coordinates.
 */
private GraphicalEditPart findSibling(List siblings, Point pStart, int direction, EditPart exclude){
	GraphicalEditPart epCurrent;
	GraphicalEditPart epFinal = null;
	IFigure figure;
	Point pCurrent;
	int distance = Integer.MAX_VALUE;

	Iterator iter = siblings.iterator();
	while (iter.hasNext()){
		epCurrent = (GraphicalEditPart)iter.next();
		if (epCurrent == exclude || !epCurrent.isSelectable())
			continue;
		figure = epCurrent.getFigure();
		pCurrent = getInterestingPoint(figure);
		figure.translateToAbsolute(pCurrent);
		if (pStart.getPosition(pCurrent) != direction)
			continue;

		int d = pCurrent.getDistanceOrthogonal(pStart);
		if (d < distance){
			distance = d;
			epFinal = epCurrent;
		}
	}
	return epFinal;
}

Point getInterestingPoint(IFigure figure) {
	return figure.getBounds().getCenter();
}


/**
 * Returns the cached node.  It is possible that the node is not longer in the viewer but has
 * not been garbage collected yet.
 */
private GraphicalEditPart getCachedNode(){
	if (cachedNode == null)
		return null;
	if (cachedNode.isEnqueued())
		return null;
	return (GraphicalEditPart)cachedNode.get();
}

GraphicalEditPart getFocus(){
	return (GraphicalEditPart)getViewer().getFocusEditPart();
}

List getNavigationSiblings(){
	return getFocus().getParent().getChildren();
}

protected GraphicalViewer getViewer(){
	return viewer;
}

public boolean keyPressed(KeyEvent event) {
	if (event.character == ' '){
		processSelect(event);
		return true;
	} else if (acceptIntoContainer(event)){
		navigateIntoContainer(event);
		return true;
	} else if (acceptOutOf(event)) {
		navigateOut(event);
		return true;
	} else if (acceptConnection(event)){
		navigateConnections(event);
		return true;
	} else if (acceptLeaveConnection(event)){
		navigateOutOfConnection(event);
		return true;
	} else if (acceptLeaveContents(event)){
		navigateIntoContainer(event);
		return true;
	}
	switch (event.keyCode) {
		case SWT.ARROW_LEFT:
			return navigateNextSibling(event, PositionConstants.WEST);
		case SWT.ARROW_RIGHT:
			return navigateNextSibling(event, PositionConstants.EAST);
		case SWT.ARROW_UP:
			return navigateNextSibling(event, PositionConstants.NORTH);
		case SWT.ARROW_DOWN:
			return navigateNextSibling(event, PositionConstants.SOUTH);

		case SWT.HOME:
			return navigateJumpSibling(event, PositionConstants.WEST);
		case SWT.END:
			return navigateJumpSibling(event, PositionConstants.EAST);
		case SWT.PAGE_DOWN:
			return navigateJumpSibling(event, PositionConstants.SOUTH);
		case SWT.PAGE_UP:
			return navigateJumpSibling(event, PositionConstants.NORTH);
	}
	
	return super.keyPressed(event);
}

private void navigateConnections(KeyEvent event){
	GraphicalEditPart focus = getFocus();
	ConnectionEditPart current = null;
	GraphicalEditPart node = getCachedNode();
	if (focus instanceof ConnectionEditPart){
		current = (ConnectionEditPart)focus;
		if (node == null
			|| (node != current.getSource()
				&& node != current.getTarget()))
		{
			node = (GraphicalEditPart)current.getSource();
			counter = 0;
		}
	} else {
		node = focus;
	}

	setCachedNode(node);
	boolean forward = event.character == '/'
		|| event.character == '?';
	ConnectionEditPart next = findConnection(node, current, forward);
	navigateTo(next, event);
}

private void navigateIntoContainer(KeyEvent event){
	GraphicalEditPart focus = getFocus();
	List childList = focus.getChildren();
	Point tl = focus.getContentPane().getBounds().getTopLeft();
	
	int minimum = Integer.MAX_VALUE;
	int current;
	GraphicalEditPart closestPart = null;

	for(int i=0; i<childList.size(); i++){	
		GraphicalEditPart ged = (GraphicalEditPart)childList.get(i);
		if (!ged.isSelectable())
			continue;
		Rectangle childBounds = ged.getFigure().getBounds();
		
		current = (childBounds.x - tl.x) + (childBounds.y - tl.y);
		if (current < minimum){
			minimum = current;
			closestPart = ged;
		}
	}
	if (closestPart != null)
		navigateTo(closestPart, event);
}

private boolean navigateJumpSibling(KeyEvent event, int direction){
	return false;
}

private boolean navigateNextSibling(KeyEvent event, int direction){
	GraphicalEditPart epStart = getFocus();
	IFigure figure = epStart.getFigure();
	Point pStart = getInterestingPoint(figure);
	figure.translateToAbsolute(pStart);
	EditPart next = findSibling(getNavigationSiblings(), pStart, direction, epStart);
	if (next == null)
		return false;
	navigateTo(next, event);
	return true;
}

private void navigateOut(KeyEvent event){
	if (getFocus() == null
		|| getFocus() == getViewer().getContents()
		|| getFocus().getParent() == getViewer().getContents())
		return;
	navigateTo(getFocus().getParent(), event);
}

private void navigateOutOfConnection(KeyEvent event){
	GraphicalEditPart cached = getCachedNode();
	ConnectionEditPart conn = (ConnectionEditPart)getFocus();
	if (cached != null
		&& (cached == conn.getSource()
			|| cached == conn.getTarget()))
		navigateTo(cached, event);
	else
		navigateTo(conn.getSource(), event);
}

void navigateTo(EditPart part, KeyEvent event){
	if (part == null)
		return;
	if ((event.stateMask & SWT.SHIFT) != 0){
		getViewer().appendSelection(part);
		getViewer().setFocus(part);
	}
	else if ((event.stateMask & SWT.CONTROL) != 0)
		getViewer().setFocus(part);
	else
		getViewer().select(part);
}

private void processSelect(KeyEvent event){
	EditPart part = getViewer().getFocusEditPart();
	if ((event.stateMask & SWT.CONTROL) != 0 &&
		part.getSelected() != EditPart.SELECTED_NONE)
	{
		getViewer().deselect(part);
	} else {
		getViewer().appendSelection(part);
	}
	getViewer().setFocus(part);
}

private void setCachedNode(GraphicalEditPart node){
	if (node == null)
		cachedNode = null;
	else 
		cachedNode = new WeakReference(node);
}

}
