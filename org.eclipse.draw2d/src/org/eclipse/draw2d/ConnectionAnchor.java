package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * An object to which a {@link Connection} will be anchored.  If the ConnectionAnchor
 * moves, the Connection should move with it.
 */
public interface ConnectionAnchor {
	
/**
 * Adds a listener interested in the movement of this ConnectionAnchor.
 * @param listener The AnchorListener to be added
 */
void addAnchorListener(AnchorListener listener);

/**
 * Returns the location where the Connection should be anchored.  The anhor may use the
 * given reference Point to calculate this location.
 * @param reference The reference Point
 * @return The anchor's location
 */
Point getLocation(Point reference);

/**
 * Returns the IFigure that contains this ConnectionAnchor.  Moving this figure will cause
 * the anchor to move with it.
 * @return The IFigure that contains this ConnectionAnchor
 */
IFigure getOwner();

/**
 * Returns the reference point for this anchor. This might be used by another anchor to
 * determine its own location (i.e. {@link ChopboxAnchor}).
 * @return The reference Point
 */
Point getReferencePoint();

/**
 * Removes the listener.
 * @param listener The AnchorListener to be removed
 */
void removeAnchorListener(AnchorListener listener);

}