package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Routes a {@link Connection}, possibly using 
 * a constraint.
 */
public interface ConnectionRouter {

ConnectionRouter NULL = new NullConnectionRouter();

/**
 * Returns the constraint for the Connection.
 */
Object getConstraint(Connection connection);

/**
 * Invalidates the given Connection.
 */
void invalidate(Connection connection);

/**
 * Routes the Connection.
 */
void route(Connection connection);

/**
 * Removes the Connection from this router.
 */
void remove(Connection connection);

/**
 * Maps the given constraint to the given Connection.
 */
void setConstraint(Connection connection, Object constraint);

class NullConnectionRouter
	extends AbstractRouter
{

	NullConnectionRouter(){}

	public void route(Connection conn) {
		PointList points = conn.getPoints();
		points.removeAllPoints();
		Point p;
		conn.translateToRelative(p = getStartPoint(conn));
		points.addPoint(p);
		conn.translateToRelative(p = getEndPoint(conn));
		points.addPoint(p);
		conn.setPoints(points);
	}

}

}