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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

/**
 * Generic support for ConnectionRouters.
 */
public abstract class AbstractRouter
	implements ConnectionRouter
{

/**
 * Returns the constraint for the given Connection.
 * 
 * @param connection The connection
 * @return The constraint
 * @since 2.0
 */
public Object getConstraint(Connection connection) {
	return null;
}

/**
 * Returns a Point representing the end of the given Connection.
 * 
 * @param conn The connection
 * @return The end point
 * @since 2.0
 */
protected Point getEndPoint(Connection conn) {
	Point ref = conn.getSourceAnchor().getReferencePoint();
	return conn.getTargetAnchor().getLocation(ref);
}

/**
 * Returns a Point representing the start of the given Connection.
 * 
 * @param conn The connection
 * @return The start point
 * @since 2.0
 */
protected Point getStartPoint(Connection conn) {
	Point ref = conn.getTargetAnchor().getReferencePoint();
	return conn.getSourceAnchor().getLocation(ref);
}

/**
 * Causes the router to discard any cached information about the given Connection.
 * 
 * @param connection The connection to invalidate
 * @since 2.0
 */
public void invalidate(Connection connection) { }

/**
 * Removes the given Connection from this routers list of Connections it is responsible
 * for.
 * 
 * @param connection The connection to remove
 * @since 2.0
 */
public void remove(Connection connection) { }

/**
 * Sets the constraint for the given Connection.
 * 
 * @param connection The connection
 * @param constraint The constraint
 * @since 2.0
 */
public void setConstraint(Connection connection, Object constraint) { }

}