/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * A locator that finds the middle of a connection based on the bendpoints.
 * @author Del Myers
 *
 */
//@tag bug(154391-ArcEnds(fix)) : use this locator to ensure that labels will be in the middle of connections.
public class MidBendpointLocator extends ConnectionLocator {

	/**
	 * @param connection
	 */
	public MidBendpointLocator(Connection connection) {
		super(connection);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionLocator#getReferencePoint()
	 */
	protected Point getReferencePoint() {
		PointList points = getConnection().getPoints();
		return points.getMidpoint().getCopy();
	}

}
