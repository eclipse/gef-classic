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

public boolean containsPoint(int x, int y) {
	if (!super.containsPoint(x,y))
		return false;
	Rectangle r = getBounds();
	long ux = x - r.x - r.width/2;
	long uy = y - r.y - r.height/2;
	return ((ux*ux) << 10 ) / (r.width*r.width) 
		 + ((uy*uy) << 10) / (r.height*r.height) <= 256;
}

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