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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;

public class ConnectionCreationTool
	extends AbstractConnectionCreationTool
{

public ConnectionCreationTool() { }

public ConnectionCreationTool(CreationFactory factory) {
	setFactory(factory);
}

boolean acceptConnectionFinish(KeyEvent event) {
	return isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS) && event.character == 13;
}

boolean acceptConnectionStart(KeyEvent event) {
	return isInState(STATE_INITIAL) && event.character == 13;
}

protected boolean handleButtonDown(int button) {
	if (button == 1 && stateTransition(STATE_CONNECTION_STARTED, STATE_TERMINAL))
		return handleCreateConnection();

	super.handleButtonDown(button);
	if (isInState(STATE_CONNECTION_STARTED))
		//Fake a drag to cause feedback to be displayed immediately on mouse down.
		handleDrag();
	return true;
}

protected boolean handleFocusLost() {
	if (isInState(STATE_CONNECTION_STARTED | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
		eraseSourceFeedback();
		eraseTargetFeedback();
		setState(STATE_INVALID);
		handleFinished();
	}
	return super.handleFocusLost();
}

protected boolean handleKeyDown(KeyEvent event) {
	if (acceptArrowKey(event)) {
		int direction = 0;
		switch (event.keyCode) {
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
		if (direction != 0 && event.stateMask == 0)
			consumed = navigateNextAnchor(direction);
		if (!consumed) {
			event.stateMask |= SWT.CONTROL;
			event.stateMask &= ~SWT.SHIFT;
			if (getCurrentViewer().getKeyHandler().keyPressed(event)) {
				navigateNextAnchor(0);
				updateTargetRequest();
				updateTargetUnderMouse();
				Command command = getCommand();
				if (command != null)
					setCurrentCommand(command);
				return true;
			}
		}
	}
	
	if (acceptConnectionStart(event)) {
		updateTargetUnderMouse();
		setConnectionSource(getTargetEditPart());
		((CreateConnectionRequest)getTargetRequest()).setSourceEditPart(getTargetEditPart());
		setState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS);
		placeMouseInViewer(getLocation().getTranslated(6, 6));
		return true;
	}
	
	if (acceptConnectionFinish(event)) {
		Command command = getCommand();
		if (command != null && command.canExecute()) {
			setState(STATE_INITIAL);
			placeMouseInViewer(getLocation().getTranslated(6, 6));
			eraseSourceFeedback();
			eraseTargetFeedback();
			setCurrentCommand(command);
			executeCurrentCommand();
		}
		return true;
	}
	
	return super.handleKeyDown(event);
}

boolean navigateNextAnchor(int direction) {
	EditPart focus = getCurrentViewer().getFocusEditPart();
	AccessibleAnchorProvider provider;
	provider = (AccessibleAnchorProvider)focus.getAdapter(AccessibleAnchorProvider.class);
	if (provider == null)
		return false;

	List list;
	if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
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
		if (d < distance) {
			distance = d;
			next = p;
		}
	}

	if (next != null) {
		placeMouseInViewer(next);
		return true;
	}
	return false;	
}

}