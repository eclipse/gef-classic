package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;

public class BendpointRequest
	extends org.eclipse.gef.requests.LocationRequest
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


