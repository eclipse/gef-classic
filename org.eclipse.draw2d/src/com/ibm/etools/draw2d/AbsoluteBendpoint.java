package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.Point;

/**
 * A Point that implements Bendpoint, used
 * by bendable {@link Connection Connections}.
 */
public class AbsoluteBendpoint 
	extends Point 
	implements Bendpoint 
{

/**
 * Creates a new AbsoluteBendpoint at the Point p.
 * 
 * @since 2.0
 */
public AbsoluteBendpoint(Point p) {
	super(p);
}

/**
 * Creates a new AbsoluteBendpoint at the Point (x,y).
 * 
 * @since 2.0
 */
public AbsoluteBendpoint(int x, int y) {
	super(x, y);
}

/**
 * Returns this Point as its location.
 * 
 * @since 2.0
 */
public Point getLocation() {
	return this;
}

}