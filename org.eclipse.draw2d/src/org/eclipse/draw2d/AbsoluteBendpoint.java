package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * AbsoluteBendpoint is a Bendpoint that defines its location simply as its X and Y
 * coordinates. It is used by bendable {@link Connection Connections}. 
 */
public class AbsoluteBendpoint 
	extends Point 
	implements Bendpoint 
{

/**
 * Creates a new AbsoluteBendpoint at the Point p.
 * @param p The absolute location of the bendpoint
 * @since 2.0
 */
public AbsoluteBendpoint(Point p) {
	super(p);
}

/**
 * Creates a new AbsoluteBendpoint at the Point (x,y).
 * @param x The X coordinate
 * @param y The Y coordinate
 * @since 2.0
 */
public AbsoluteBendpoint(int x, int y) {
	super(x, y);
}

/**
 * @see org.eclipse.draw2d.Bendpoint#getLocation()
 */
public Point getLocation() {
	return this;
}

}