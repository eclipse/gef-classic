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

import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.IZestColorConstants;
import org.eclipse.mylar.zest.core.ZestPlugin;


/**
 * A plus/minus toggle figure.
 * @author Chris Callendar
 */
public class PlusMinusFigure extends Toggle {
	
	public PlusMinusFigure() {
		this(16);
	}
	
	/**
	 * Initializes this figure with the given listener (for 
	 * being notified when the figure is toggled).
	 * @param plusSize The plus size
	 */
	public PlusMinusFigure(int plusSize) {
		setPreferredSize(plusSize, plusSize);		
	}
	
	/**
	 * Adds an action listener to be notified when the PlusMinusFigure is toggled.
	 * @see org.eclipse.draw2d.Clickable#addActionListener(org.eclipse.draw2d.ActionListener)
	 */
	public void addActionListener(ActionListener listener) {
		if (listener != null) {
			super.addActionListener(listener);
		}
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getBounds());
		g.fillRectangle(r);
		
		Insets insets = getInsets();
		if (!insets.isEmpty()) {
			r.resize(0 - (insets.left + insets.right), 0 - (insets.top + insets.bottom));
			r.translate(insets.left, insets.top);
		}
		// define a square for the border
		if (r.width <= r.height) {
			r.y = r.y + (r.height - r.width) / 2;
			r.height = r.width;
		} else {
			r.x = r.x + (r.width - r.height) / 2;
			r.width = r.height;
		}
		g.setBackgroundColor(ColorConstants.white);
		g.fillRectangle(r);
//		@tag bug(151332-Colors(fix))
		g.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.LIGHT_BLUE_CYAN));
		g.drawRectangle(new Rectangle(r.x+1, r.y+1, r.width-1, r.height-1));
//		@tag bug(151332-Colors(fix))
		g.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.GREY_BLUE));
		g.drawRectangle(r);
		
		int xMid = r.x + r.width / 2;
		int yMid = r.y + r.height / 2;
//		@tag bug(151332-Colors(fix))
		g.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.LIGHT_BLUE_CYAN));
		g.drawLine(r.x + 3, yMid+1, r.right() - 2, yMid+1);
		if (!isSelected()) {
			g.drawLine(xMid+1, r.y + 3, xMid+1, r.bottom() - 2);
		}
//		@tag bug(151332-Colors(fix))
		g.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.DARK_BLUE));
		g.drawLine(r.x + 2, yMid, r.right() - 2, yMid);
		if (!isSelected()) {
			g.drawLine(xMid, r.y + 2, xMid, r.bottom() - 2);
		}
		
			
	}
	
}
