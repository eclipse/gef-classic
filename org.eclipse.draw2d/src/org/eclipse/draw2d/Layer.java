package org.eclipse.draw2d;

import java.util.Collection;

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