package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.*;

public class ConnectionEndpointTracker
	extends TargetingTool
	implements DragTracker
{

private static final int FLAG_SOURCE_FEEBBACK = TargetingTool.MAX_FLAG << 1;
protected static final int MAX_FLAG = FLAG_SOURCE_FEEBBACK;

private String commandName;
private List exclusionSet;

private ConnectionEditPart connectionEditPart;

public ConnectionEndpointTracker(ConnectionEditPart cep) {
	setConnectionEditPart(cep);
	setDisabledCursor(SharedCursors.NO);
}

protected Cursor calculateCursor(){
	if (isInState(STATE_INITIAL | STATE_DRAG | STATE_ACCESSIBLE_DRAG))
		return getDefaultCursor();
	return super.calculateCursor();
}

public void commitDrag(){
		eraseSourceFeedback();
		eraseTargetFeedback();
		executeCurrentCommand();
}

protected Request createTargetRequest(){
	ReconnectRequest request = new ReconnectRequest(getCommandName());
	request.setConnectionEditPart(getConnectionEditPart());
	return request;
}

public void deactivate() {
	eraseSourceFeedback();
	super.deactivate();
}

protected void eraseSourceFeedback() {
	if (getFlag(FLAG_SOURCE_FEEBBACK) != true)
		return;
	setFlag(FLAG_SOURCE_FEEBBACK, false);
	getConnectionEditPart().eraseSourceFeedback(getTargetRequest());
}

protected String getCommandName(){
	return commandName;
}

protected Connection getConnection() {
	return (Connection)getConnectionEditPart().getFigure();
}

protected ConnectionEditPart getConnectionEditPart(){
	return connectionEditPart;
}

protected String getDebugName(){
	return "Connection Endpoint Tool";//$NON-NLS-1$
}

protected Collection getExclusionSet() {
	if (exclusionSet == null) {
		exclusionSet = new ArrayList();
		exclusionSet.add(getConnection());
	}
	return exclusionSet;
}

protected boolean handleButtonUp(int button){
	if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)){
		eraseSourceFeedback();
		eraseTargetFeedback();
		executeCurrentCommand();
	}
	return true;
}

protected boolean handleDragInProgress(){
	updateTargetRequest();
	updateTargetUnderMouse();
	showSourceFeedback();
	showTargetFeedback();
	setCurrentCommand(getCommand());
	return true;
}

protected boolean handleDragStarted(){
	stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS);
	return false;
}

protected boolean handleKeyDown(KeyEvent e){
	if (acceptArrowKey(e)) {
		if (stateTransition(STATE_INITIAL, STATE_ACCESSIBLE_DRAG_IN_PROGRESS)){
			//When the drag first starts, set the focus Part to be one end of the connection
			if (isTarget())
				getCurrentViewer().setFocus(getConnectionEditPart().getTarget());
			else
				getCurrentViewer().setFocus(getConnectionEditPart().getSource());
		}
		int direction = 0;
		switch (e.keyCode) {
			case SWT.ARROW_DOWN :
				direction = PositionConstants.SOUTH;
				break;
			case SWT.ARROW_UP:
				direction = PositionConstants.NORTH;
				break;
			case SWT.ARROW_RIGHT:
				direction = PositionConstants.EAST;
				break;
			case SWT.ARROW_LEFT:
				direction = PositionConstants.WEST;
				break;
		}

		boolean consumed = false;
		if (direction != 0 && e.stateMask == 0)
			consumed = navigateNextAnchor(direction);
		if (!consumed){
			e.stateMask |= SWT.CONTROL;
			e.stateMask &= ~SWT.SHIFT;
			if (getCurrentViewer().getKeyHandler().keyPressed(e)){
				navigateNextAnchor(0);
				return true;
			}
		}
	}
	if (e.character == '/' || e.character == '\\'){
			e.stateMask |= SWT.CONTROL;
			if (getCurrentViewer().getKeyHandler().keyPressed(e)){
				//Do not try to connect to the same connection being dragged.
				if (getCurrentViewer().getFocusEditPart() != getConnectionEditPart())
					navigateNextAnchor(0);
				return true;
			}
	}
		
	return false;
}

private boolean isTarget(){
	return getCommandName() == RequestConstants.REQ_RECONNECT_TARGET;
}

boolean navigateNextAnchor(int direction){
	EditPart focus = getCurrentViewer().getFocusEditPart();
	AccessibleAnchorProvider provider;
	provider = (AccessibleAnchorProvider)focus.getAdapter(AccessibleAnchorProvider.class);
	if (provider == null)
		return false;

	List list;
	if (isTarget())
		list = provider.getTargetAnchorLocations();
	else
		list = provider.getSourceAnchorLocations();

	Point start = getLocation();
	int distance = Integer.MAX_VALUE;
	Point next = null;
	for (int i = 0; i < list.size(); i++) {
		Point p = (Point)list.get(i);
		if (p.equals(start)
			|| (direction != 0
				&& (start.getPosition(p) != direction)))
			continue;
		int d = p.getDistanceOrthogonal(start);
		if (d < distance){
			distance = d;
			next = p;
		}
	}

	if (next != null){
		placeMouseInViewer(next);
		return true;
	}
	return false;	
}

public void setCommandName(String newCommandName) {
	commandName = newCommandName;
}

public void setConnectionEditPart(ConnectionEditPart cep){
	this.connectionEditPart = cep;
}

protected void showSourceFeedback() {
	getConnectionEditPart().showSourceFeedback(getTargetRequest());
	setFlag(FLAG_SOURCE_FEEBBACK, true);
}

protected void updateTargetRequest(){
	ReconnectRequest request = (ReconnectRequest)getTargetRequest();
	Point p = getLocation();
	request.setLocation(p);
}

}


