package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * A Request that needs to keep track of a location.
 */
public class LocationRequest
	extends org.eclipse.gef.Request
{

private Point location;

/**
 * Constructs a LocationRequest with no type.
 */
public LocationRequest() { }

/** * Constructs a LocationRequest with the given type.
 * @param type the type
 */
public LocationRequest(Object type) {
	super(type);
}

/**
 * Returns the current location.
 * @return The current location
 */
public Point getLocation() {
	return location;
}

/**
 * Sets the current location.
 *
 * @param p The current location.
 */
public void setLocation(Point p) {
	location = p;
}

}


