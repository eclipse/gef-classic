/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.requests;

import org.eclipse.gef.ConnectionEditPart;

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


