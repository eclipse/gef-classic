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

public class MouseEvent
	extends InputEvent
{

public int x,y;

/**
 * The button that was pressed or released: {1, 2, 3}.
 */
public int button;

MouseEvent(int x, int y, EventDispatcher dispatcher,
	IFigure f, int button, int stateMask)
{
	super(dispatcher, f, stateMask);
	Point pt = Point.SINGLETON;
	pt.setLocation(x,y);
	f.translateToRelative(pt);
	this.button = button;
	this.x=pt.x;
	this.y=pt.y;
}

public Point getLocation(){
	return new Point(x,y);
}

public String toString(){
	return "MouseEvent(" + x + ',' + y + " to Figure: " + source;//$NON-NLS-2$//$NON-NLS-1$
}

}