package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

public class WireBendpoint 
	implements java.io.Serializable, Bendpoint
{

private float weight = 0.5f;
private Dimension d1, d2;

public WireBendpoint() {}

public Dimension getFirstRelativeDimension() {
	return d1;
}

public Point getLocation() {
	return null;
}

public Dimension getSecondRelativeDimension() {
	return d2;
}

public float getWeight() {
	return weight;
}

public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
	d1 = dim1;
	d2 = dim2;
}

public void setWeight(float w) {
	weight = w;
}

}