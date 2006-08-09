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
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A simple figure that displays an icon for minimizing/maximizing panels.
 * @author Del Myers
 *
 */

//@tag bug(152613-Client-Supplier(fix)) : this figure allows the client and supplier pains have a method for users to minimize/maximize them.
public class MinMaxFigure extends Toggle {
		
	/**
	 * 
	 */
	public MinMaxFigure() {
		super();
		setForegroundColor(ColorConstants.black);
		setBackgroundColor(ColorConstants.white);
		setPreferredSize(14,14);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setValid(boolean)
	 */
	public void setValid(boolean value) {
		super.setValid(value);
		repaint();
	}
	
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		Rectangle bounds = getBounds().getCopy();
		bounds.width = bounds.width - 3;
		bounds.height = bounds.height - 4;
		bounds.x = bounds.x+1;
		bounds.y = bounds.y+1;
		if (isSelected()) {
			graphics.fillRectangle(bounds);
			graphics.drawRectangle(bounds);
		}
		Rectangle bar = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height/4);
		graphics.fillRectangle(bar);
		graphics.drawRectangle(bar);
	}
	
		
	/**
	 * @return true if image for maximization should be drawn.
	 */
	public boolean isMax() {
		return isSelected();
	}
	
	/**
	 * @param max true if the image for maximizing should be drawn.
	 */
	public void setMax(boolean max) {
		setSelected(max);
	}

}