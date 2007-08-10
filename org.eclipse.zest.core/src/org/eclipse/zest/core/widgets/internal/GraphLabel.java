/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets.internal;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Overrides the Draw2D Label Figure class to ensure that the text is never
 * truncated. Also draws a rounded rectangle border.
 * 
 * @author Chris Callendar
 */
public class GraphLabel extends CachedLabel {

	private Color borderColor;
	private int borderWidth;
	private int arcWidth;

	private boolean painting = false;

	/**
	 * Creates a GraphLabel
	 * 
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the text is not as clear
	 */
	public GraphLabel(boolean cacheLabel) {
		this("", cacheLabel);
	}

	/**
	 * Creates a graph label with text
	 * 
	 * @param text
	 *            The text
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(String text, boolean cacheLabel) {
		this("", null, cacheLabel);
	}

	/**
	 * Creates the graph label with an image
	 * 
	 * @param i
	 *            The Image
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(Image i, boolean cacheLabel) {
		this("", i, cacheLabel);
	}

	/**
	 * Creates a graph label with an image and text
	 * 
	 * @param text
	 *            The text
	 * @param i
	 *            The Image
	 * @param cacheLabel
	 *            Determine if the text should be cached. This will make it
	 *            faster, but the
	 */
	public GraphLabel(String text, Image i, boolean cacheLabel) {
		super(cacheLabel);
		initLabel();
		setText(text);
		setIcon(i);
		adjustBoundsToFit();
	}

	/**
	 * Initialises the border colour, border width, and sets the layout manager.
	 * Also sets the font to be the default system font.
	 */
	protected void initLabel() {
		super.setFont(Display.getDefault().getSystemFont());
		this.borderColor = ColorConstants.black;
		this.borderWidth = 1;
		this.arcWidth = 8;
		this.setLayoutManager(new StackLayout());
		this.setBorder(new MarginBorder(1));
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if ((text != null)) {
			Font font = getFont();
			if (font != null) {
				Dimension minSize = FigureUtilities.getTextExtents(text, font);
				if (getIcon() != null) {
					org.eclipse.swt.graphics.Rectangle imageRect = getIcon().getBounds();
					int expandHeight = Math.max(imageRect.height - minSize.height, 0);
					minSize.expand(imageRect.width + 4, expandHeight);
				}
				minSize.expand(10 + (2 * borderWidth), 4 + (2 * borderWidth));
				setBounds(new Rectangle(getLocation(), minSize));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	public void paint(Graphics graphics) {
		int blue = getBackgroundColor().getBlue();
		blue = (int) (blue - (blue * 0.20));
		blue = blue > 0 ? blue : 0;

		int red = getBackgroundColor().getRed();
		red = (int) (red - (red * 0.20));
		red = red > 0 ? red : 0;

		int green = getBackgroundColor().getGreen();
		green = (int) (green - (green * 0.20));
		green = green > 0 ? green : 0;

		Color lightenColor = new Color(Display.getCurrent(), new RGB(red, green, blue));
		graphics.setForegroundColor(lightenColor);
		graphics.setBackgroundColor(getBackgroundColor());

		graphics.pushState();

		// fill in the background
		Rectangle bounds = getBounds().getCopy();
		Rectangle r = bounds.getCopy();
		//r.x += arcWidth / 2;
		r.y += arcWidth / 2;
		//r.width -= arcWidth;
		r.height -= arcWidth;

		Rectangle top = bounds.getCopy();
		top.height /= 2;
		//graphics.setForegroundColor(lightenColor);
		//graphics.setBackgroundColor(lightenColor);
		graphics.setForegroundColor(getBackgroundColor());
		graphics.setBackgroundColor(getBackgroundColor());
		graphics.fillRoundRectangle(top, arcWidth, arcWidth);

		top.y = top.y + top.height;
		//graphics.setForegroundColor(getBackgroundColor());
		//graphics.setBackgroundColor(getBackgroundColor());
		graphics.setForegroundColor(lightenColor);
		graphics.setBackgroundColor(lightenColor);
		graphics.fillRoundRectangle(top, arcWidth, arcWidth);

		//graphics.setForegroundColor(lightenColor);
		//graphics.setBackgroundColor(getBackgroundColor());
		graphics.setBackgroundColor(lightenColor);
		graphics.setForegroundColor(getBackgroundColor());
		graphics.fillGradient(r, true);

		super.paint(graphics);
		graphics.popState();
		graphics.setForegroundColor(lightenColor);
		graphics.setBackgroundColor(lightenColor);
		// paint the border
		bounds.setSize(bounds.width - 1, bounds.height - 1);
		graphics.drawRoundRectangle(bounds, arcWidth, arcWidth);
		lightenColor.dispose();
		/*
		graphics.setForegroundColor(getForegroundColor());
		graphics.setBackgroundColor(getBackgroundColor());

		// fill in the background
		Rectangle bounds = getBounds().getCopy();
		graphics.fillRoundRectangle(bounds, arcWidth, arcWidth);

		super.paint(graphics);
		// paint the border
		graphics.setForegroundColor(getBorderColor());
		graphics.setLineWidth(getBorderWidth());
		bounds.setSize(bounds.width - 1, bounds.height - 1);
		graphics.drawRoundRectangle(bounds, arcWidth, arcWidth);
		*/
	}

	protected Color getBackgroundTextColor() {
		return getBackgroundColor();
	}

	/**
	 * This method is overridden to ensure that it doesn't get called while the
	 * super.paintFigure() is being called. Otherwise NullPointerExceptions can
	 * occur because the icon or text locations are cleared *after* they were
	 * calculated.
	 * 
	 * @see org.eclipse.draw2d.Label#invalidate()
	 */
	public void invalidate() {
		if (!painting) {
			super.invalidate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#setText(java.lang.String)
	 */
	public void setText(String s) {
		if (!s.equals("")) {
			super.setText(s);

		} else {
			super.setText("");
		}
		adjustBoundsToFit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Label#setIcon(org.eclipse.swt.graphics.Image)
	 */
	public void setIcon(Image image) {
		super.setIcon(image);
		//adjustBoundsToFit();
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

	public void setBounds(Rectangle rect) {
		// TODO Auto-generated method stub
		super.setBounds(rect);
	}

}
