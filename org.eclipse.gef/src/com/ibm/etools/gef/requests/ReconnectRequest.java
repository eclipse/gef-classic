package com.ibm.etools.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.Point;
import com.ibm.etools.draw2d.*;

import com.ibm.etools.gef.*;

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