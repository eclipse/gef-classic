package com.ibm.etools.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

public class FixedConnectionAnchor 
	extends AbstractConnectionAnchor
{

private Object direction;
public boolean leftToRight = true;
public int offsetH;
public int offsetV;
public boolean topDown = true;

public FixedConnectionAnchor(IFigure owner) {
	super(owner);
}

public Point getLocation(Point reference) {
	Rectangle r = getOwner().getBounds();
	int x,y;
	if (topDown)
		y = r.y + offsetV;
	else
		y = r.y + r.height - offsetV;

	if (leftToRight)
		x = r.x + offsetH;
	else
		x = r.x + r.width - offsetH;
	
	Point p = new Point(x, y);
	getOwner().translateToAbsolute(p);
	return p;
}

public Point getReferencePoint(){
	return getLocation(null);
}
	
}