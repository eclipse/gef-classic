package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Routes a {@link Connection}, possibly using a constraint.
 */
public interface ConnectionRouter {

/**
 * The default router for Connections.
 */
ConnectionRouter NULL = new NullConnectionRouter();

/**
 * Returns the constraint for the Connection.
 * @param connection The connection
 * @return The constraint
 */
Object getConstraint(Connection connection);

/**
 * Invalidates the given Connection.
 * @param connection The connection to be invalidated
 */
void invalidate(Connection connection);

/**
 * Routes the Connection.
 * @param connection The Connection to route
 */
void route(Connection connection);

/**
 * Removes the Connection from this router.
 * @param connection The Connection to remove
 */
void remove(Connection connection);

/**
 * Maps the given constraint to the given Connection.
 * @param connection The Connection
 * @param constraint The constraint
 */
void setConstraint(Connection connection, Object constraint);

/**
 * Routes Connections directly from the source anchor to the target anchor with no
 * bendpoints in between.
 */
class NullConnectionRouter
	extends AbstractRouter
{

	/**
	 * Constructs a new NullConnectionRouter.	 */
	NullConnectionRouter() { }

	/**
	 * Routes the given Connection directly between the source and target anchors.	 */
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