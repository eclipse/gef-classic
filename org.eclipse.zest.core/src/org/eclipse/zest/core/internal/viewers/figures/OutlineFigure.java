/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;


/**
 * A rectangular figure which has a dotted outline and no fill.
 * 
 * @author Chris Callendar
 */
public class OutlineFigure extends RectangleFigure {

	/**
	 * 
	 */
	public OutlineFigure() {
		super();
		setOpaque(false);
		setFill(false);
		setXOR(false);
		super.setOutline(true);
		setLineStyle(Graphics.LINE_DOT);
	}

	/**
	 * Does nothing.  True by default.
	 * @see org.eclipse.draw2d.Shape#setOutline(boolean)
	 */
	public void setOutline(boolean b) {
		// does nothing
	}
	
	/**
	 * Does nothing.
	 * @see org.eclipse.draw2d.RectangleFigure#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		// do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.RectangleFigure#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.setForegroundColor(getForegroundColor());
		super.outlineShape(graphics);
	}
	
	
}
