package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.*;

/**
 * Repositions a {@link Bendpoint} attached to a {@link Connection} when the Connection is
 * moved.
 */
public class BendpointLocator 
	extends ConnectionLocator 
{

private int index;

/**
 * Creates a BendpointLocator associated with passed Connection c and index i.
 * 
 * @param c Connection associated with BendpointLocator
 * @param i Index of bendpoint, represents the position of the bendpoint on Connection c
 * @since 2.0
 */
public BendpointLocator(Connection c, int i) {
	super(c);
	index = i;
}

/**
 * Returns the index of this BendpointLocator. This index is the position of the reference
 * point in this BendpointLocator's {@link Connection}.
 * 
 * @return The index
 * @since 2.0
 */
protected int getIndex() {
	return index;
}

/**
 * Returns reference point associated with the BendpointLocator.  This Point is taken from
 * the BendpointLocator's connection and is point number 'index'.
 * 
 * @return The reference point
 * @since 2.0
 */
protected Point getReferencePoint() {
	Point p = getConnection().getPoints().getPoint(Point.SINGLETON, getIndex());
	getConnection().translateToAbsolute(p);
	return p;
}

}