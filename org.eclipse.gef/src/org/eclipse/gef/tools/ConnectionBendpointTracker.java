package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;


import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

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

private boolean lineContainsPoint(Point p1, Point p2, Point p) {
	int tolerance = 7;
	Rectangle rect = Rectangle.SINGLETON;
	rect.setSize(0, 0);
	rect.setLocation(p1.x, p1.y);
	rect.union(p2.x, p2.y);
	rect.expand(tolerance, tolerance);
	if (!rect.contains(p.x, p.y))
		return false;

	int v1x, v1y, v2x, v2y;
	int numerator, denominator;
	double result = 0.0;

	if (p1.x != p2.x && p1.y != p2.y) {
		
		v1x = p2.x - p1.x;
		v1y = p2.y - p1.y;
		v2x = p.x - p1.x;
		v2y = p.y - p1.y;
		
		numerator = v2x * v1y - v1x * v2y;
		denominator = v1x * v1x + v1y * v1y;

		result = ((numerator << 10) / denominator * numerator) >> 10;
	}
	
	// if it is the same point, and it passes the bounding box test,
	// the result is always true.
	return result <= tolerance * tolerance;
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
	Point p = getLocation();
	request.setLocation(p);
	if (REQ_CREATE_BENDPOINT.equals(getType()))
		return;
	PointList points = getConnection().getPoints();
	Point p1 = points.getPoint(index);
	getConnection().translateToAbsolute(p1);
	int index2 = REQ_DELETE_BENDPOINT.equals(request.getType()) ? index + 1 : index + 2;
	Point p2 = points.getPoint(index2);
	getConnection().translateToAbsolute(p2);
	if (lineContainsPoint(p1, p2, p)) {
		if (REQ_MOVE_BENDPOINT.equals(getType())) {
			request.setType(REQ_DELETE_BENDPOINT);
			setType(REQ_DELETE_BENDPOINT);
			eraseSourceFeedback();
		}
	} else if (REQ_DELETE_BENDPOINT.equals(getType())) {
		request.setType(REQ_MOVE_BENDPOINT);
		setType(REQ_MOVE_BENDPOINT);
		eraseSourceFeedback();
	}
}

}
