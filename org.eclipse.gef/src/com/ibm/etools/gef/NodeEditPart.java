package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.ConnectionAnchor;

/**
 * A specialized GraphicalEditPart that supports both target and source
 * ConnectionEditParts.  By support, we mean that ConnectionEditParts can
 * obtain a ConnectionAnchor with which to anchor its figure,
 * and connection-related EditPolicies can locate a ConnectionAnchor
 * based on the mouse's <b>x</b> and <b>y</b> location.  Some applications
 * may have more than one type of connection, but choose to locate them all
 * in the same way.  In this case, the implementor may implement all methods
 * to return the same ConnectionAnchor instance.
 */
public interface NodeEditPart
	extends GraphicalEditPart
{

/**
 Returns the ConnectionAnchor that the passed ConnectionEditPart should
 use with its Connection figure.
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection);

/**
 Returns the ConnectionAnchor that the passed ConnectionEditPart should
 use with its Connection figure.
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection);

/**
 Returns the ConnectionAnchor that is closest to the point <x,y>.
 This is used during mouse interactions.
 */
public ConnectionAnchor getSourceConnectionAnchor(Request request);

/**
 Returns the ConnectionAnchor that is closest to the point <x,y>.
 This is used during mouse interactions.
 */
public ConnectionAnchor getTargetConnectionAnchor(Request request);

}