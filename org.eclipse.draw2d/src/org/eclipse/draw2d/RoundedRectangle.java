package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draws a Rectangle whose corners are rounded in
 * appearance. The size of the rectangle is determined 
 * by the bounds set to it.
 */
public class RoundedRectangle extends Shape {

protected Dimension corner = new Dimension(8,8);

/**
 * Constructs a round cornered rectangle.
 */
public RoundedRectangle(){}

/**
 * Fill the RoundedRectangle with the background color
 * set by <i>graphics</i>.
 * 
 * @since 2.0
 */
protected void fillShape(Graphics graphics){
	graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
}

/**
 * Draw the outline of the RoundedRectangle.
 * 
 * @since 2.0
 */
protected void outlineShape(Graphics graphics){
	Rectangle f = Rectangle.SINGLETON;
	Rectangle r = getBounds();
	f.x = r.x + lineWidth/2;
	f.y = r.y + lineWidth/2;
	f.width = r.width - lineWidth;
	f.height = r.height - lineWidth;
	graphics.drawRoundRectangle(f, corner.width, corner.height);
}

/**
 * Sets the dimensions of each corner. This will form the
 * radii of the arcs which form the corners.
 *
 * @param d  Dimensions of the corner.
 * @since 2.0
 */
public void setCornerDimensions(Dimension d){
	corner.width = d.width;
	corner.height = d.height;
}

}