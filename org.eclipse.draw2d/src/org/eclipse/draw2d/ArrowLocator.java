package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Repositions a {@link Figure Figure} attached to a 
 * {@link Connection Connection} when the Connection is moved. 
 * The ArrowLocator is used to reposition and place
 * {@link RotatableDecoration RotatableDecorations} at the start, 
 * end, or middle of a connection.
 */
public class ArrowLocator extends ConnectionLocator {

/**
 * Constructs an ArrowLocator associated with passed connection and
 * tip location.
 * 
 * @param connection Connection associated with the locator
 * @param tip Orientation of tip. Use constants 
 * 			   ConnectionLocator.START or ConnectionLocator.END
 * @since 2.0
 */
public ArrowLocator(Connection connection, int tip) {
	super(connection, tip);
}

public void relocate(IFigure target) {
	PointList points = getConnection().getPoints();
	RotatableDecoration arrow = (RotatableDecoration)target;
	arrow.setLocation(getReferencePoint());

	if (getAlignment() == START)
		arrow.setReferencePoint(points.getPoint(1));
	else if (getAlignment() == END)
		arrow.setReferencePoint(points.getPoint(points.size()-2));
}

}