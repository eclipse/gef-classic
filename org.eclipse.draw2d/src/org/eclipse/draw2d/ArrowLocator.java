/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

/**
 * Repositions a {@link Figure Figure} attached to a {@link Connection} when the
 * Connection is moved.  The ArrowLocator is used to reposition and place 
 * {@link RotatableDecoration RotatableDecorations} at the start or end of a connection.
 */
public class ArrowLocator extends ConnectionLocator {

/**
 * Constructs an ArrowLocator associated with passed connection and tip location (either
 * {@link ConnectionLocator#START} or {@link ConnectionLocator#END}).
 * 
 * @param connection The connection associated with the locator
 * @param tip Orientation of the tip
 * @since 2.0
 */
public ArrowLocator(Connection connection, int tip) {
	super(connection, tip);
}

/**
 * Relocates the passed in figure (which must be a {@link RotatableDecoration}) at either
 * the start or end of the connection.
 * @param target The RotatableDecoration to relocate
 */
public void relocate(IFigure target) {
	PointList points = getConnection().getPoints();
	RotatableDecoration arrow = (RotatableDecoration)target;
	arrow.setLocation(getLocation(points));

	if (getAlignment() == SOURCE)
		arrow.setReferencePoint(points.getPoint(1));
	else if (getAlignment() == TARGET)
		arrow.setReferencePoint(points.getPoint(points.size() - 2));
}

}