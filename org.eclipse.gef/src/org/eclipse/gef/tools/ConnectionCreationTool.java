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

public class ConnectionCreationTool 
	extends AbstractConnectionCreationTool 
{

public ConnectionCreationTool(){}

public ConnectionCreationTool(CreationFactory factory){
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