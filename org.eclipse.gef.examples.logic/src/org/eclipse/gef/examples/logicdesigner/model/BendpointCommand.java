package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.Command;
import org.eclipse.draw2d.geometry.*;

public class BendpointCommand 
	extends Command 
{

protected int index;
protected Point location;
protected Wire wire;
private Dimension d1, d2;

protected Dimension getFirstRelativeDimension() {
	return d1;
}

protected Dimension getSecondRelativeDimension() {
	return d2;
}

protected int getIndex() {
	return index;
}

protected Point getLocation() {
	return location;
}

protected Wire getWire() {
	return wire;
}

public void redo() {
	execute();
}

public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
	d1 = dim1;
	d2 = dim2;
}

public void setIndex(int i) {
	index = i;
}

public void setLocation(Point p) {
	location = p;
}

public void setWire(Wire w) {
	wire = w;
}

}


