package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.gef.requests.BendpointRequest;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.examples.logicdesigner.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

public class WireBendpointEditPolicy 
	extends com.ibm.etools.gef.editpolicies.BendpointEditPolicy
{

protected Command getCreateBendpointCommand(BendpointRequest request) {
	CreateBendpointCommand com = new CreateBendpointCommand();
	Point p = request.getLocation();
	com.setLocation(p);
	Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
	Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
	com.setRelativeDimensions(p.getDifference(ref1),
					p.getDifference(ref2));
	com.setWire((Wire)request.getSource().getModel());
	com.setIndex(request.getIndex());
	return com;
}

protected Command getMoveBendpointCommand(BendpointRequest request) {
	MoveBendpointCommand com = new MoveBendpointCommand();
	Point p = request.getLocation();
	com.setLocation(p);
	Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
	Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
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