package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.RGB;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draws a rectangle whose size is determined
 * by the bounds set to it.
 */
public class RectangleFigure extends Shape {

/**
 * Creates a RectangleFigure.
 */
public RectangleFigure(){}

/**
 * Fill the Polygon with the background color
 * set by <i>graphics</i>.
 * 
 * @since 2.0
 */
protected void fillShape(Graphics graphics){
	graphics.fillRectangle(getBounds());
}

/**
 * Draw the outline of the RectangleFigure.
 * 
 * @since 2.0
 */
protected void outlineShape(Graphics graphics){
	Rectangle r = getBounds();
	graphics.drawRectangle(r.x, r.y, r.width-1, r.height-1);
}

}