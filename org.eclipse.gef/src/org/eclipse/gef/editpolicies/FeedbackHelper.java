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
package org.eclipse.gef.editpolicies;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

public class FeedbackHelper
{
private Connection connection;
private XYAnchor dummyAnchor;
private boolean isMovingStartAnchor = false;

public FeedbackHelper() {
	dummyAnchor = new XYAnchor(new Point(10,10));
}

protected Connection getConnection() {
	return connection;
}

protected boolean isMovingStartAnchor() {
	return isMovingStartAnchor;
}

public void setConnection(Connection c) {
	connection = c;
}

public void setMovingStartAnchor(boolean value) {
	isMovingStartAnchor = value;
}

protected void setAnchor(ConnectionAnchor anchor) {
	if (isMovingStartAnchor()) 
		getConnection().setSourceAnchor(anchor);
	else
		getConnection().setTargetAnchor(anchor);
}

public void update(ConnectionAnchor anchor, Point p) {
	if (anchor != null) {
		setAnchor(anchor);
	}
	else {
		dummyAnchor.setLocation(p);
		setAnchor(dummyAnchor);
	}
}

}
