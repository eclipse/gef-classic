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

import java.util.*;


import org.eclipse.draw2d.Connection;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

public class ConnectionBendpointTracker
	extends SimpleDragTracker
{

private Object type;
private ConnectionEditPart editpart;
private int index;

protected ConnectionBendpointTracker(){}

public ConnectionBendpointTracker(ConnectionEditPart editpart, int i) {
	setConnectionEditPart(editpart);
	setIndex(i);
}

protected List createOperationSet(){
	List list = new ArrayList();
	list.add(getConnectionEditPart());
	return list;
}

protected Request createSourceRequest() {
	BendpointRequest request = new BendpointRequest();
	request.setType(getType());
	request.setIndex(getIndex());
	request.setSource(getConnectionEditPart());
	return request;
}

protected Command getCommand(){
	return getConnectionEditPart().getCommand(getSourceRequest());
}

protected String getCommandName(){
	return getType().toString();
}

protected Connection getConnection(){
	return (Connection)getConnectionEditPart().getFigure();
}

protected ConnectionEditPart getConnectionEditPart(){
	return editpart;
}

protected String getDebugName() {
	return "Bendpoint Handle Tracker " + getCommandName();//$NON-NLS-1$
}

protected int getIndex() {
	return index;
}

protected Object getType() {
	return type;
}

public void setConnectionEditPart(ConnectionEditPart cep){
	editpart = cep;
}

public void setIndex(int i) {
	index = i;
}

public void setType(Object type){
	this.type = type;
}

protected void updateSourceRequest() {
	BendpointRequest request = (BendpointRequest)getSourceRequest();
	request.setLocation(getLocation());
}

}
