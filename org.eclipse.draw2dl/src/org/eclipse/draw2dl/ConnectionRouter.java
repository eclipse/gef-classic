/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.PointList;

/**
 * Routes a {@link org.eclipse.draw2dl.Connection}, possibly using a constraint.
 */
public interface ConnectionRouter {

	/**
	 * The default router for Connections.
	 */
	ConnectionRouter NULL = new NullConnectionRouter();

	/**
	 * Returns the constraint for the Connection.
	 * 
	 * @param connection
	 *            The connection
	 * @return The constraint
	 */
	Object getConstraint(org.eclipse.draw2dl.Connection connection);

	/**
	 * Invalidates the given Connection.
	 * 
	 * @param connection
	 *            The connection to be invalidated
	 */
	void invalidate(org.eclipse.draw2dl.Connection connection);

	/**
	 * Routes the Connection.
	 * 
	 * @param connection
	 *            The Connection to route
	 */
	void route(org.eclipse.draw2dl.Connection connection);

	/**
	 * Removes the Connection from this router.
	 * 
	 * @param connection
	 *            The Connection to remove
	 */
	void remove(org.eclipse.draw2dl.Connection connection);

	/**
	 * Maps the given constraint to the given Connection.
	 * 
	 * @param connection
	 *            The Connection
	 * @param constraint
	 *            The constraint
	 */
	void setConstraint(org.eclipse.draw2dl.Connection connection, Object constraint);

	/**
	 * Routes Connections directly from the source anchor to the target anchor
	 * with no bendpoints in between.
	 */
	class NullConnectionRouter extends AbstractRouter {

		/**
		 * Constructs a new NullConnectionRouter.
		 */
		NullConnectionRouter() {
		}

		/**
		 * Routes the given Connection directly between the source and target
		 * anchors.
		 * 
		 * @param conn
		 *            the connection to be routed
		 */
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
