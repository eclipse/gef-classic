package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.requests.CreateRequest;

public class ConnectionCreationTool 
	extends AbstractConnectionCreationTool 
{

public ConnectionCreationTool(){}

public ConnectionCreationTool(CreateRequest.Factory factory){
	setFactory(factory);
}

protected boolean handleButtonDown(int button) {
	if (button == 1
		&& stateTransition(STATE_CONNECTION_STARTED, STATE_TERMINAL))
		return handleCreateConnection();

	super.handleButtonDown(button);
	if (isInState(STATE_CONNECTION_STARTED))
		//Fake a drag to cause feedback to be displayed immediately on mouse down.
		handleDrag();
	return true;
}

}