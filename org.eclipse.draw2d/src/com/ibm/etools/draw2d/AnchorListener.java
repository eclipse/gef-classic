package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A listener interface for receiving notification that
 * an anchor has moved.
 */
public interface AnchorListener {

/**
 * Called when an anchor has moved to a new location.
 */
public void anchorMoved(ConnectionAnchor anchor);

}