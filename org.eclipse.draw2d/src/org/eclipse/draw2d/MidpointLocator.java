package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.*;

public class MidpointLocator 
	extends ConnectionLocator {

private int index;

/**
 * Constructs a MidpointLocator object with 
 * associated Connection c and index i.
 * 
 * @param c The Connection associated with the locator
 * @param i The point from where the Connection's midpoint
 *           will be calculated. (I.E. The ith point and point
 *           i + 1 on the Connection are used to calculate the
 * 			 midpoint of the Connection.
 * @since 2.0          
 */
public MidpointLocator(Connection c, int i) {
	super(c);
	index = i;
}

/**
 * Returns this MidpointLocator's index
 * 
 * This integer represents the position of the start point in this 
 * MidpointLocator's associated {@link Connection Connection} 
 * from where midpoint calculation will be made.
 * 
 * @since 2.0  
 */

protected int getIndex() {
	return index;
}

/**
 * Returns the point of reference associated with 
 * this locator. This point will be midway between
 * point numbers 'index' and 'index' + 1.
 * 
 * @since 2.0
 */
protected Point getReferencePoint() {
	Connection conn = getConnection();
	Point p = Point.SINGLETON;
	Point p1 = conn.getPoints().getPoint(getIndex());
	Point p2 = conn.getPoints().getPoint(getIndex()+1);
	p.x = (p2.x-p1.x)/2 + p1.x;
	p.y = (p2.y-p1.y)/2 + p1.y;
	return p;
}

}