package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;

import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

public class WireBendpointEditPolicy 
	extends org.eclipse.gef.editpolicies.BendpointEditPolicy
{

protected Command getCreateBendpointCommand(BendpointRequest request) {
	CreateBendpointCommand com = new CreateBendpointCommand();
	Point p = request.getLocation();
	Connection conn = getConnection();
	
	conn.translateToRelative(p);
	
	com.setLocation(p);
	Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
	Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
	
	conn.translateToRelative(ref1);
	conn.translateToRelative(ref2);
	
	
	com.setRelativeDimensions(p.getDifference(ref1),
					p.getDifference(ref2));
	com.setWire((Wire)request.getSource().getModel());
	com.setIndex(request.getIndex());
	return com;
}

protected Command getMoveBendpointCommand(BendpointRequest request) {
	MoveBendpointCommand com = new MoveBendpointCommand();
	Point p = request.getLocation();
	Connection conn = getConnection();
	
	conn.translateToRelative(p);
	
	com.setLocation(p);
	
	Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
	Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
	
	conn.translateToRelative(ref1);
	conn.translateToRelative(ref2);
	
	com.setRelativeDimensions(p.getDifference(ref1),
					p.getDifference(ref2));
	com.setWire((Wire)request.getSource().getModel());
	com.setIndex(request.getIndex());
	return com;
}

protected Command getDeleteBendpointCommand(BendpointRequest request) {
	BendpointCommand com = new DeleteBendpointCommand();
	Point p = request.getLocation();
	com.setLocation(p);
	com.setWire((Wire)request.getSource().getModel());
	com.setIndex(request.getIndex());
	return com;
}

}