package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.Collection;

/**
 * A Transparent figure is a collection of its children.  Default figures represent a
 * rectangular region.  TransparentFigure only represents the regions defined by its
 * children.  TransparentFigure can be made rectangular again by setting it to be opaque.
 */
class TransparentFigure 
	extends Figure {

/**
 * Overridden to implement transparent behavior.
 * 
 * @param x X coordiante of point to search children for.
 * @param y Y coordinate of point to search children for.
 * @since 2.0
 * 
 */ 
public boolean containsPoint(int x, int y){
	if (isOpaque())
		return super.containsPoint(x, y);
	for(int i=0;i<getChildren().size();i++){
		IFigure child = (IFigure)getChildren().get(i);
		if(child.containsPoint(x,y))
			return true;
	}
	return false;
}

/**
 * Overridden to implement transparent behavior.
 * @since 2.0
 */
public IFigure findFigureAtExcluding(int x, int y, Collection collection){
	if (isOpaque())
		return super.findFigureAtExcluding(x, y, collection);

	IFigure f = super.findFigureAtExcluding(x,y,collection);
	if (f == this) 
		return null;
	return f;
}

}