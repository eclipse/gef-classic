package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

import org.eclipse.gef.*;
import org.eclipse.draw2d.*;

/**
 * A handle used for bendpoints.
 */
public class BendpointHandle
	extends ConnectionHandle
	implements PropertyChangeListener
{

private int index;

/**
 * Adds a PropertyChangeListener to this handle's owner figure (a
 * Connection), so that the handle can be revalidated when the 
 * Connection's points change.
 */
public void addNotify() {
	super.addNotify();
	getOwnerFigure().addPropertyChangeListener(Connection.PROPERTY_POINTS, this);
}

/**
 * By default, <code>null</code> is returned for the DragTracker.
 */
protected DragTracker createDragTracker() {
	return null;
}

/**
 * Returns the index.  This could mean different things for different
 * subclasses.  It could be the index of the point the handle belongs 
 * to.  Or it could be the index of the handle itself.  For
 * {@link BendpointCreationHandle}s and {@link BendpointMoveHandle}s,
 * this is the index of the handle itself, where these two types of
 * handles are indexed separately.  For example, if you have one bendpoint,
 * you will have 2 creation handles, indexed as 0 and 1, and 1 move handle,
 * indexed as 0. 
 */
public int getIndex() {
	return index;
}

/**
 * Revalidates this handle when the connection's points change.
 */
public void propertyChange(PropertyChangeEvent event) {
	revalidate();
}

/**
 * Removes this PropertyChangeListener from its owner figure.
 */
public void removeNotify() {
	getOwnerFigure().removePropertyChangeListener(Connection.PROPERTY_POINTS, this);
	super.removeNotify();
}

/**
 * Sets the index.
 *
 * @see #getIndex()
 */
protected void setIndex(int i) {
	index = i;
}

}


