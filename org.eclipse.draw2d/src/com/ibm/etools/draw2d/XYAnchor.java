package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * Supports an anchor in the XY layout. This anchor exists
 * independently without an owner.
 */
public class XYAnchor
	extends ConnectionAnchorBase
{

private Point location;

/**
 * Constructs an XYAnchor at the Point p.
 * 
 * @since 2.0
 */
public XYAnchor(Point p) {
	location = new Point(p);
}

/*
 * Returns the location of this anchor relative to the reference
 * point given in as input. Since this is XY layout, the location of 
 * the point is independent of the reference point.
 */
public Point getLocation(Point reference) {
	return location;
}

/*
 * Returns <code>null</code> as these anchors inherently do not 
 * depend on other figures for their location.
 *
 * @return  Figure which is the owner of this anchor.
 * @since 2.0
 */
public IFigure getOwner(){
	return null;
}

/*
 * Returns the point which is used as the reference by this 
 * connection anchor. In the case of the XYAnchor, this point
 * is the same as its location. 
 */
public Point getReferencePoint(){
	return location;
}

/**
 * Sets the location of this anchor, and notifies all the
 * listeners of the update.
 *
 * @param p  The new location of this anchor.
 * @see  #getLocation(Point)
 * @since 2.0
 */
public void setLocation(Point p) {
	location.setLocation(p);
	fireAnchorMoved();
}

}