/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph2.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * An anchor for a connection to a node in the directed graph. The source
 * connection is anchored to the bottom center of the node and the target
 * connection is anchored to the top center of the node.
 * 
 * @author Anthony Hunter
 */
public class Digraph2ConnectionAnchor extends AbstractConnectionAnchor {

	/**
	 * Constructor for a Digraph2ConnectionAnchor.
	 * 
	 * @param owner
	 *            the figure that owns this anchor.
	 */
	public Digraph2ConnectionAnchor(IFigure owner) {
		super(owner);
	}

	/*
	 * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
	 */
	public Point getLocation(Point reference) {
		Point point = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(point);
		if (reference.x < point.x)
			point = getOwner().getBounds().getTop();
		else
			point = getOwner().getBounds().getBottom();
		getOwner().translateToAbsolute(point);
		return point;
	}

}
