package org.eclipse.gef.examples.logicdesigner.model.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;

public class DeleteBendpointCommand 
	extends BendpointCommand 
{

private Bendpoint bendpoint;

public void execute() {
	bendpoint = (Bendpoint)getWire().getBendpoints().get(getIndex());
	getWire().removeBendpoint(getIndex());
	super.execute();
}

public void undo() {
	super.undo();
	getWire().insertBendpoint(getIndex(), bendpoint);
}

}


