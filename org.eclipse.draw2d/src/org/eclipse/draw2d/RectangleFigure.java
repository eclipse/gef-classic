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
	int x = r.x + lineWidth/2;
	int y = r.y + lineWidth/2;
	int w = r.width - lineWidth;
	int h = r.height - lineWidth;
	graphics.drawRectangle(x,y,w,h);
}

}