/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;

/**
 * An event caused by the user interacting with the mouse.
 */
public class MouseEvent
	extends InputEvent
{

/** The X coordinate of the mouse event. */
public int x;
/** The Y coordinate of the mouse event. */
public int y;

/** The button that was pressed or released: {1, 2, 3}. */
public int button;

MouseEvent(int x, int y, EventDispatcher dispatcher, 
			IFigure f, int button, int stateMask) {
	super(dispatcher, f, stateMask);
	Point pt = Point.SINGLETON;
	pt.setLocation(x, y);
	f.translateToRelative(pt);
	this.button = button;
	this.x = pt.x;
	this.y = pt.y;
}

/**
 * @return the location of this mouse event
 */
public Point getLocation() {
	return new Point(x, y);
}

/**
 * @see Object#toString()
 */
public String toString() {
	return "MouseEvent(" + x + ',' + y + ") to Figure: " + source;//$NON-NLS-2$//$NON-NLS-1$
}

}
