package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

/**
 * Provides support for a ConnectionAnchor. A ConnectionAnchor is one of the end points
 * of a {@link Connection}. It holds listeners and notifies them if the anchor is moved.
 */
public abstract class ConnectionAnchorBase
	implements ConnectionAnchor
{

/**
 * The list of listeners
 */
protected List listeners = new ArrayList(1);

/** * @see org.eclipse.draw2d.ConnectionAnchor#addAnchorListener(AnchorListener) */
public void addAnchorListener(AnchorListener listener) {
	listeners.add(listener);
}

/** * @see org.eclipse.draw2d.ConnectionAnchor#removeAnchorListener(AnchorListener) */
public void removeAnchorListener(AnchorListener listener) {
	listeners.remove(listener);
}

/** 
 * Notifies all the listeners in the list of a change in position of this anchor. This is
 * called from one of the implementing anchors when its location is changed.
 * 
 * @since 2.0
 */
protected void fireAnchorMoved() {
	Iterator iter = listeners.iterator();
	while (iter.hasNext())
		((AnchorListener)iter.next()).anchorMoved(this);
}

}