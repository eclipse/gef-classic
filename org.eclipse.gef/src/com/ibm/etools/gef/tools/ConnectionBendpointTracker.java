package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;

import com.ibm.etools.draw2d.Connection;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.requests.BendpointRequest;

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
