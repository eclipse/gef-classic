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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Overrides the Draw2D Label Figure class to ensure that the text is never truncated.
 * Also draws a rounded rectangle border.
 * 
 * @author Chris Callendar
 */
public class GraphLabel extends Label {
		
	private Color borderColor;
	private int borderWidth;
	private int arcWidth;

	private boolean painting = false;
	
	public GraphLabel() {
		this("");
	}
	
	public GraphLabel(String text) {
		initLabel();
		setText(text);
	}
	
	public GraphLabel(Image i) {
		initLabel();
		setIcon(i);
	}
	
	public GraphLabel(String text, Image i) {
		initLabel();
		setText(text);
		setIcon(i);
	}
	
	/**
	 * Initializes the border color, border width, and sets the layout manager.
	 * Also sets the font to be the default system font.
	 */
	protected void initLabel() {
		super.setFont(Display.getDefault().getSystemFont());
		this.borderColor = ColorConstants.black;
		this.borderWidth = 1;
		this.arcWidth = 8;
		super.setOpaque(false);
		this.setLayoutManager(new StackLayout());
		this.setBorder(new MarginBorder(1));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setOpaque(boolean)
	 */
	public void setOpaque(boolean opaque) {
		// do nothing, we are forcing non-opaqueness
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		super.setFont(f);
		adjustBoundsToFit();
	}
	
	/**
	 * Adjust the bounds to make the text fit without truncation.
	 */
	protected void adjustBoundsToFit() {
		String text = getText();
		if ((text != null) && (text.length() > 0)) {
			Font font = getFont();
			if (font != null) {
				Dimension minSize = FigureUtilities.getTextExtents(text, font);
				if (getIcon() != null) {
					org.eclipse.swt.graphics.Rectangle imageRect = getIcon().getBounds();
					int expandHeight = Math.max(imageRect.height - minSize.height, 0);
					minSize.expand(imageRect.width + 4, expandHeight);
				}
				minSize.expand(10 + (2*borderWidth), 4 + (2*borderWidth));
				setBounds(new Rectangle(getLocation(), minSize));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		
		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());
		
		// fill in the background
		Rectangle bounds = getBounds().getCopy();
		graphics.fillRoundRectangle(bounds, arcWidth, arcWidth);
		
		// draw the text
		this.painting = true;
		super.paintFigure(graphics);
		this.painting = false;

		// paint the border
		graphics.setForegroundColor(getBorderColor());
		graphics.setLineWidth(getBorderWidth());
		bounds.setSize(bounds.width-1, bounds.height-1);
		graphics.drawRoundRectangle(bounds, arcWidth, arcWidth);
	}
	
	/**
	 * This method is overridden to ensure that it doesn't get called while
	 * the super.paintFigure() is being called.  Otherwise NullPointerExceptions
	 * can occur because the icon or text locations are cleared *after* they were calculated.
	 * 
	 * @see org.eclipse.draw2d.Label#invalidate()
	 */
	public void invalidate() {
		if (!painting) {
			super.invalidate();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Label#setText(java.lang.String)
	 */
	public void setText(String s) {
		super.setText(s);
		adjustBoundsToFit();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Label#setIcon(org.eclipse.swt.graphics.Image)
	 */
	public void setIcon(Image image) {
		super.setIcon(image);
		adjustBoundsToFit();
	}
	
	public Color getBorderColor() {
		return borderColor;
	}
	
	public void setBorderColor(Color c) {
		this.borderColor = c;
	}
	
	public int getBorderWidth() {
		return borderWidth;
	}
	
	public void setBorderWidth(int width) {
		this.borderWidth = width;
	}

	
	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}
}
