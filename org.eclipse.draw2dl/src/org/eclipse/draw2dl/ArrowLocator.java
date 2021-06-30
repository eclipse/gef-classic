/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.PointList;

/**
 * Locator used to place a {@link org.eclipse.draw2dl.RotatableDecoration} on a {@link org.eclipse.draw2dl.Connection}.
 * The decoration can be placed at the source or target end of the connection
 * figure. The default connection implementation uses a {@link DelegatingLayout}
 * which requires locators.
 */
public class ArrowLocator extends org.eclipse.draw2dl.ConnectionLocator {

	/**
	 * Constructs an ArrowLocator associated with passed connection and tip
	 * location (either {@link org.eclipse.draw2dl.ConnectionLocator#SOURCE} or
	 * {@link ConnectionLocator#TARGET}).
	 * 
	 * @param connection
	 *            The connection associated with the locator
	 * @param location
	 *            Location of the arrow decoration
	 * @since 2.0
	 */
	public ArrowLocator(Connection connection, int location) {
		super(connection, location);
	}

	/**
	 * Relocates the passed in figure (which must be a
	 * {@link org.eclipse.draw2dl.RotatableDecoration}) at either the start or end of the
	 * connection.
	 * 
	 * @param target
	 *            The RotatableDecoration to relocate
	 */
	public void relocate(IFigure target) {
		PointList points = getConnection().getPoints();
		org.eclipse.draw2dl.RotatableDecoration arrow = (RotatableDecoration) target;
		arrow.setLocation(getLocation(points));

		if (getAlignment() == SOURCE)
			arrow.setReferencePoint(points.getPoint(1));
		else if (getAlignment() == TARGET)
			arrow.setReferencePoint(points.getPoint(points.size() - 2));
	}

}
