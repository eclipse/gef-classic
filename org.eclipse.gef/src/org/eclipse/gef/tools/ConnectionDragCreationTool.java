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