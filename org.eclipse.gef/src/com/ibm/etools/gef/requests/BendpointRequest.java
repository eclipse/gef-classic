package com.ibm.etools.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.gef.*;

public class BendpointRequest
	extends com.ibm.etools.gef.requests.LocationRequest
{

private int index;
private ConnectionEditPart source;

public int getIndex() {
	return index;
}

public ConnectionEditPart getSource() {
	return source;
}

public void setIndex(int i) {
	index = i;
}

public void setSource(ConnectionEditPart s) {
	source = s;
}

}


