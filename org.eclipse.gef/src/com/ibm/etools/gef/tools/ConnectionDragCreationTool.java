package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.requests.CreateRequest;

public class ConnectionDragCreationTool 
	extends AbstractConnectionCreationTool
	implements com.ibm.etools.gef.DragTracker
{

public ConnectionDragCreationTool(){}

public ConnectionDragCreationTool(CreateRequest.Factory factory){
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