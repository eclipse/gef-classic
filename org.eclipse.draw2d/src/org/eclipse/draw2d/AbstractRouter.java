package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
 * @since 2.0
 */
public Object getConstraint(Connection connection){
	return null;
}

/**
 * Returns a Point representing the end of the given 
 * Connection.
 * 
 * @since 2.0
 */
protected Point getEndPoint(Connection conn) {
	Point ref = conn.getSourceAnchor().getReferencePoint();
	return conn.getTargetAnchor().getLocation(ref);
}

/**
 * Returns a Point representing the start of the given
 * Connection.
 * 
 * @since 2.0
 */
protected Point getStartPoint(Connection conn) {
	Point ref = conn.getTargetAnchor().getReferencePoint();
	return conn.getSourceAnchor().getLocation(ref);
}

/**
 * Causes the router to discard any cached information
 * about the given Connection.
 * 
 * @since 2.0
 */
public void invalidate(Connection connection) {}

/**
 * Removes the given Connection from this routers list
 * of Connections it is responsible for.
 * 
 * @since 2.0
 */
public void remove(Connection connection){}

/**
 * Sets the constraint for the given Connection.
 * 
 * @since 2.0
 */
public void setConstraint (Connection connection, Object constraint){}

}