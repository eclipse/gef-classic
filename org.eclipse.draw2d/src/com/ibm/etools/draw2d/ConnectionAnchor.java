package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * An object to which a {@link Connection} will 
 * be anchored.  If the ConnectionAnchor moves, 
 * the Connection should move with it.
 */
public interface ConnectionAnchor {
	
/**
 * Adds a listener interested in the movement of
 * this ConnectionAnchor.
 */
void addAnchorListener(AnchorListener anchor);

/**
 * Returns the location where the Connection
 * should be anchored.
 */
Point getLocation(Point reference);

/**
 * Returns the IFigure that contains this
 * ConnectionAnchor.
 */
IFigure getOwner();

/**
 * Returns the reference point for this anchor.
 * This might be used by another anchor to determine
 * its own location (i.e. {@link ChopboxAnchor}).
 */
Point getReferencePoint();

/**
 * Removes the listener.
 */
void removeAnchorListener(AnchorListener anchor);

}