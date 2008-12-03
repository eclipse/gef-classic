/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Draws a rectangle whose size is determined by the bounds set to it.
 */
public class RectangleFigure extends Shape {

/**
 * Creates a RectangleFigure.
 */
public RectangleFigure() { }

/**
 * @see Shape#fillShape(Graphics)
 */
protected void fillShape(Graphics graphics) {
	graphics.fillRectangle(getBounds());
}

/**
 * @see Shape#outlineShape(Graphics)
 */
protected void outlineShape(Graphics graphics) {
	int lineInset = (int)Math.ceil(Math.max(1.0, getLineWidthFloat() / 2.0));
	Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
	r.shrink(lineInset, lineInset);
	graphics.drawRectangle(r);
}

}
