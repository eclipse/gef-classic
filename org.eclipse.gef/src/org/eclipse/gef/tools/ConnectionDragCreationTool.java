package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.requests.CreationFactory;

public class ConnectionDragCreationTool 
	extends AbstractConnectionCreationTool
	implements org.eclipse.gef.DragTracker
{

public ConnectionDragCreationTool(){}

public ConnectionDragCreationTool(CreationFactory factory){
	setFactory(factory);
}

protected boolean handleButtonDown(int button) {
	super.handleButtonDown(button);
	setState(STATE_DRAG);
	return true;
}

protected boolean handleButtonUp(int button) {
	if (isInState(STATE_CONNECTION_STARTED))
		handleCreateConnection();
	setState(STATE_TERMINAL);
	return true;
}

/**
 * When the threshold is passed, transition to CONNECTION_STARTED.
 */
protected boolean handleDragStarted() {
	return stateTransition(STATE_DRAG, STATE_CONNECTION_STARTED);
}

protected void handleFinished() {
	//Don't handle finish because this is a DragTracker.
}

}