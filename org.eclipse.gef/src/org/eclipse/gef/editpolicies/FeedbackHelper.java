package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

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
