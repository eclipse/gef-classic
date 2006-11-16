/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.parts.tools;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * TODO comment
 * @author Del Myers
 *
 */
public class TargetShape extends Shape {

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		//does nothing.

	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		Rectangle bounds = getClientArea().getCopy();
		//reduce the bounds by one pixel so that it can be properly
		//drawn within the clipping region.
		//graphics.setXORMode(true);
		bounds.width -= 1;
		bounds.height -= 1;
		Point center = bounds.getCenter();
		Point bottom = new Point(bounds.x+bounds.width, bounds.height+bounds.y);
		Point top = new Point(bounds.x, bounds.y);
		//draw the crosshairs
		graphics.drawLine(center.x, top.y, center.x, center.y-2);
		graphics.drawLine(center.x+2, center.y, bottom.x, center.y);
		graphics.drawLine(center.x, center.y+2, center.x, bottom.y);
		graphics.drawLine(top.x, center.y, center.x-2, center.y);
		//draw the circle target.
		bounds.shrink(bounds.width/4, bounds.height/4);
		bounds.width -=1;
		bounds.height -=1;
//		bounds.x+=1;
//		bounds.y+=1;
		graphics.drawOval(bounds);
		
	}

}
