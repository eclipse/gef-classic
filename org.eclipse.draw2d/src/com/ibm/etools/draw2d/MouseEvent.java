package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

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