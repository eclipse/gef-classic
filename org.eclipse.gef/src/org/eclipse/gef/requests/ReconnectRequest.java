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
package org.eclipse.gef.requests;

import org.eclipse.gef.*;

/**
 * A Request to reconnect a connection.
 */
public class ReconnectRequest
	extends LocationRequest
	implements DropRequest, TargetRequest
{

private ConnectionEditPart connection;
private EditPart target;

/**
 * Default constructor.
 */
public ReconnectRequest(){}

/**
 * Creates a ReconnectRequest with the given type.
 */
public ReconnectRequest(Object type){
	setType(type);
}

/**
 * Returns the ConnectionEditPart to be reconnected.
 *
 * @return The ConnectionEditPart to be reconnected.S
 */
public ConnectionEditPart getConnectionEditPart(){
	return connection;
}

public EditPart getTarget(){
	return target;
}

public boolean isMovingStartAnchor() {
	return RequestConstants.REQ_RECONNECT_SOURCE.equals(getType());
}

/**
 * Sets the ConnectionEditPart to be reconnected.
 *
 * @param conn The ConnectionEditPart.
 */
public void setConnectionEditPart(ConnectionEditPart conn){
	connection = conn;
}

public void setTargetEditPart(EditPart ep){
	target = ep;
}

}