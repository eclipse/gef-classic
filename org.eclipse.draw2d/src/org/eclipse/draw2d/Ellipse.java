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
 * Draws an ellipse whose major and
 * minor radii are determined by the 
 * bounds set to it.
 */
public class Ellipse
	extends Shape
{

/**
 * Constructs a default Ellipse.
 * 
 * @since 2.0
 */
public Ellipse(){}

/**
 * Fill the Ellipse with the background color
 * set by <i>graphics</i>.
 * 
 * @since 2.0
 */
protected void fillShape(Graphics graphics){
	graphics.fillOval(getBounds());
}

/**
 * Draw the outline of the Ellipse.
 * 
 * @since 2.0
 */
protected void outlineShape(Graphics graphics){
	Rectangle r = Rectangle.SINGLETON;
	r.setBounds(getBounds());
	r.width--;
	r.height--;
	r.shrink((lineWidth-1)/2,(lineWidth-1)/2);
	graphics.drawOval(r);
}

}