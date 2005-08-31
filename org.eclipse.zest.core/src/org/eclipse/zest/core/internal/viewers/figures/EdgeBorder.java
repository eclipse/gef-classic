/***********************************************************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 **********************************************************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;


import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

/**
 * An extension of the LineBorder class
 * to allow for setting the color and width of each edge of the bounding rectangle.
 * 
 * @author ccallendar
 */
public class EdgeBorder extends LineBorder {
	
	/** The edge widths: top, left, bottom, right. */
	private int[] widths = new int[] { 1, 1, 1, 1};
	
	/** The edge colors: top, left, bottom, right. */
	private Color[] colors = new Color[4];

	/**
	 * Initializes this border to be like a LineBorder - one color and one width.
	 * @param color
	 * @param allsides
	 */
	public EdgeBorder(Color color, int allsides) {
		super();
		setColor(color);
		setWidth(allsides);
	}
	
	/**
	 * Initializes this border with all sides have the one color, and each side
	 * having a specific width.
	 */
	public EdgeBorder(Color color, int top, int left, int bottom, int right) {
		super();
		setColor(color);
		setWidths(top, left, bottom, right);
	}
	
	/**
	 * Sets the colors and widths for the border.<br>
	 * Array of length 1: all sides get the same values.
	 * Array of length 2: {top/bottom, left/right}
	 * Array of length 4: {top, left, bottom, right}
	 * @param colors array of colors: must be length 1, 2, or 4
	 * @param widths array of widths: must be length 1, 2, or 4
	 * @throws IllegalArgumentException if the two arrays 
	 */
	public EdgeBorder(Color[] colors, int[] widths) {
		super();
		setColors(colors);
		setWidths(widths);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LineBorder#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		int x1 = tempRect.x,
			x2 = tempRect.x + tempRect.width,
			y1 = tempRect.y,
			y2 = tempRect.y + tempRect.height;
		
		// top
		paintLine(graphics, widths[0], colors[0], new Point(x1, y1), new Point(x2-widths[0], y1));
		// left
		paintLine(graphics, widths[1], colors[1], new Point(x1, y1), new Point(x1, y2-widths[1]));
		// bottom
		paintLine(graphics, widths[2], colors[2], new Point(x1, y2-widths[2]), new Point(x2-widths[2], y2-widths[2]));
		// right
		paintLine(graphics, widths[3], colors[3], new Point(x2-widths[3], y1), new Point(x2-widths[3], y2-widths[3]));
	}
		
	public void paintLine(Graphics g, int width, Color color, Point p1, Point p2) {
		if (width > 0) {
			g.setLineWidth(width);
			g.setForegroundColor(color);
			g.drawLine(p1, p2);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LineBorder#setWidth(int)
	 */
	public void setWidth(int allsides) {
		super.setWidth(allsides);
		setWidths(allsides, allsides, allsides, allsides);
	}
	
	/**
	 * Sets the width for the edges.  The array must be of 
	 * length 1 (all sides get the same width), 2 (top/bottom and 
	 * left/right get same width), or 4.
	 * @param widths the widths: must be length 1, 2, or 4
	 */
	public void setWidths(int[] widths) {
		if (widths.length == 1) {
			setWidth(widths[0]);
		} else if (widths.length == 2) {
			setWidths(widths[0], widths[1], widths[0], widths[1]);
		} else if (widths.length == 4) {
			setWidths(widths[0], widths[1], widths[2], widths[3]);
		}
	}
	
	/**
	 * Sets the widths of the border's edges.
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 */
	public void setWidths(int top, int left, int bottom, int right) {
		super.setWidth(top);
		this.widths[0] = top;
		this.widths[1] = left;
		this.widths[2] = bottom;
		this.widths[3] = right;		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LineBorder#setColor(org.eclipse.swt.graphics.Color)
	 */
	public void setColor(Color color) {
		super.setColor(color);
		setColors(color, color, color, color);
	}
	
	/**
	 * Sets the colors for the edges.  The array must be of length
	 * 1 (all sides same color), 2 (top/bottom and left/right same colors) or 4.
	 * @param colors the colors array, must be of length 1, 2, 4
	 */
	public void setColors(Color[] colors) {
		if (colors.length == 1) {
			setColor(colors[0]);
		} else if (colors.length == 2) {
			setColors(colors[0], colors[1], colors[0], colors[1]);
		} else if (colors.length == 4) {
			setColors(colors[0], colors[1], colors[2], colors[3]);
		}
	}
	
	/**
	 * Sets the colors of the border's edges.
	 * @param topColor
	 * @param leftColor
	 * @param bottomColor
	 * @param rightColor
	 */
	public void setColors(Color topColor, Color leftColor, Color bottomColor, Color rightColor) {
		super.setColor(topColor);
		this.colors[0] = topColor;
		this.colors[1] = leftColor;
		this.colors[2] = bottomColor;
		this.colors[3] = rightColor;
	}
	
	/**
	 * Returns the color of the border.  If multiple edge colors are defined (for each edge)
	 * then this is just the top edge color.
	 * @see org.eclipse.draw2d.LineBorder#getColor()
	 */
	public Color getColor() {
		return colors[0];
	}

	/**
	 * Returns the width of the border.  If multiple edge widths are defined
	 * then this is just the top edge width.
	 * @see org.eclipse.draw2d.LineBorder#getWidth()
	 */
	public int getWidth() {
		return widths[0];
	}

	/** 
	 * Returns the insets.
	 * @return Inserts
	 */
	public Insets getInsets(IFigure figure) {
		Insets insets = new Insets(widths[0], widths[1], widths[2], widths[3]);
		return insets;
	}
}
