package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;

public class WireEndpointEditPolicy
	extends org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy 
{

protected void addSelectionHandles(){
	super.addSelectionHandles();
	getConnectionFigure().setLineWidth(2);
}

protected PolylineConnection getConnectionFigure(){
	return (PolylineConnection)((GraphicalEditPart)getHost()).getFigure();
}

protected void removeSelectionHandles(){
	super.removeSelectionHandles();
	getConnectionFigure().setLineWidth(1);
}

}