/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Alex Selkov - Fix for Bug# 22701
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An figure that draws an ellipse filling its bounds.
 */
public class Ellipse
	extends Shape
{

/**
 * Constructs a new Ellipse with the default values of a Shape.
 * @since 2.0
 */
public Ellipse() { }

/**
 * Returns <code>true</code> if the given point (x,y) is contained within this ellipse.
 * @param x the x coordinate
 * @param y the y coordinate
 * @return <code>true</code>if the given point is contained
 */
public boolean containsPoint(int x, int y) {
	if (!super.containsPoint(x, y))
		return false;
	Rectangle r = getBounds();
	long ux = x - r.x - r.width / 2;
	long uy = y - r.y - r.height / 2;
	return ((ux * ux) << 10) / (r.width * r.width) 
		 + ((uy * uy) << 10) / (r.height * r.height) <= 256;
}

/**
 * Fills the ellipse.
 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
 */
protected void fillShape(Graphics graphics) {
	graphics.fillOval(getBounds());
}

/**
 * Outlines the ellipse.
 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
 */
protected void outlineShape(Graphics graphics) {
	int lineInset = (int)Math.ceil(Math.max(1.0, getLineWidthFloat() / 2.0));
	Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
	r.shrink(lineInset, lineInset);
	graphics.drawOval(r);
}

}
