package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */



public class CreateBendpointCommand 
	extends BendpointCommand 
{

public void execute() {
	WireBendpoint wbp = new WireBendpoint();
	wbp.setRelativeDimensions(getFirstRelativeDimension(), 
					getSecondRelativeDimension());
	getWire().insertBendpoint(getIndex(), wbp);
	super.execute();
}

public void undo() {
	super.undo();
	getWire().removeBendpoint(getIndex());
}

}


