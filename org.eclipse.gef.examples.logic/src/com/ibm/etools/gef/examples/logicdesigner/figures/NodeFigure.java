package com.ibm.etools.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import java.util.*;

public class NodeFigure 
	extends Figure
{

protected Hashtable connectionAnchors = new Hashtable(7);
protected Vector inputConnectionAnchors = new Vector(2,2);
protected Vector outputConnectionAnchors = new Vector(2,2);

public ConnectionAnchor connectionAnchorAt(Point p) {
	ConnectionAnchor closest = null;
	long min = Long.MAX_VALUE;

	Enumeration e = getSourceConnectionAnchors().elements();
	while (e.hasMoreElements()) {
		ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
		Point p2 = c.getLocation(null);
		long d = p.getDistance2(p2);
		if (d < min) {
			min = d;
			closest = c;
		}
	}
	e = getTargetConnectionAnchors().elements();
	while (e.hasMoreElements()) {
		ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
		Point p2 = c.getLocation(null);
		long d = p.getDistance2(p2);
		if (d < min) {
			min = d;
			closest = c;
		}
	}
	return closest;
}

public ConnectionAnchor getConnectionAnchor(String terminal) {
	return (ConnectionAnchor)connectionAnchors.get(terminal);
}

public String getConnectionAnchorName(ConnectionAnchor c){
	Enumeration enum = connectionAnchors.keys();
	String key;
	while (enum.hasMoreElements()){
		key = (String)enum.nextElement();
		if (connectionAnchors.get(key).equals(c))
			return key;
	}
	return null;
}

public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
	ConnectionAnchor closest = null;
	long min = Long.MAX_VALUE;

	Enumeration e = getSourceConnectionAnchors().elements();
	while (e.hasMoreElements()) {
		ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
		Point p2 = c.getLocation(null);
		long d = p.getDistance2(p2);
		if (d < min) {
			min = d;
			closest = c;
		}
	}
	return closest;
}

public Vector getSourceConnectionAnchors() {
	return outputConnectionAnchors;
}

public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
	ConnectionAnchor closest = null;
	long min = Long.MAX_VALUE;

	Enumeration e = getTargetConnectionAnchors().elements();
	while (e.hasMoreElements()) {
		ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
		Point p2 = c.getLocation(null);
		long d = p.getDistance2(p2);
		if (d < min) {
			min = d;
			closest = c;
		}
	}
	return closest;
}

public Vector getTargetConnectionAnchors() {
	return inputConnectionAnchors;
}


}