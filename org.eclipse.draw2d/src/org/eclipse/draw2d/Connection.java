package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import org.eclipse.draw2d.geometry.*;

/**
 * A Connection is a figure that connects two objects.
 */
public interface Connection extends IFigure {

String PROPERTY_CONNECTION_ROUTER = "connectionRouter"; //$NON-NLS-1$
String PROPERTY_POINTS = "points"; //$NON-NLS-1$

/**
 * Returns the <code>ConnectionRouter</code> used to route this 
 * Connection.  Does not return null.
 */
ConnectionRouter getConnectionRouter();

/**
 * Sets the ConnectionRouter for this Connection.
 */
void setConnectionRouter(ConnectionRouter router);

/**
 * @deprecated
 * Returns the ConnectionAnchor at the start of this 
 * Connection.
 */
ConnectionAnchor getStartAnchor();

/**
 * @deprecated
 * Returns the ConnectionAnchor at the end of this 
 * Connection.
 */
ConnectionAnchor getEndAnchor();


/**
 * Returns the ConnectionAnchor at the <b>source</b> end of this 
 * Connection.
 */
ConnectionAnchor getSourceAnchor();

/**
 * Returns the ConnectionAnchor at the <b>target</b> end of this 
 * Connection.
 */
ConnectionAnchor getTargetAnchor();

/**
 * Returns the routing constraint or null.
 */
Object getRoutingConstraint();

/**
 * Sets the routing constraint used by the router.
 */
void setRoutingConstraint(Object cons);

/**
 * Sets the ConnectionAnchor to be used at the
 * Source end of this Connection.
 */
void setSourceAnchor(ConnectionAnchor start);

/**
 * Sets the ConnectionAnchor to be used at the
 * target end of this Connection.
 */
void setTargetAnchor(ConnectionAnchor end);

/**
 * @deprecated Use setSourceAnchor().
 */
void setStartAnchor(ConnectionAnchor start);

/**
 * @deprecated Use setTargetAnchor.
 */
void setEndAnchor(ConnectionAnchor end);

/**
 * Returns the PointList containing the Points that
 * make up this Connection.  This may be returned by 
 * reference.
 */
PointList getPoints();

/**
 * Sets the PointList containing the Points that
 * make up this Connection.
 */
void setPoints(PointList list);

}