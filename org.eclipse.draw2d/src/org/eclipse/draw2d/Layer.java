package org.eclipse.draw2d;

import java.util.Collection;

import org.eclipse.draw2d.geometry.Point;

/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Transparent Figure intended to be added exclusively to 
 * a {@link LayeredPane}, who has the responsibilty of managing 
 * its Layers. 
 */
public class Layer
	extends Figure
{

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
	Point pt = new Point(x,y);
	translateFromParent(pt);
	for(int i=0;i<getChildren().size();i++){
		IFigure child = (IFigure)getChildren().get(i);
		if(child.containsPoint(pt.x,pt.y))
			return true;
	}
	return false;
}

/**
 * Overridden to implement transparency.
 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, TreeSearch)
 */
public IFigure findFigureAt(int x, int y, TreeSearch search){
	if (!isEnabled())
		return null;
	if (isOpaque())
		return super.findFigureAt(x, y, search);

	IFigure f = super.findFigureAt(x, y, search);
	if (f == this)
		return null;
	return f;
}

}