package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;

/**
 * Provides for a line border with sides of equal widths.
 */
public class LineBorder
	extends AbstractBorder
{

private int width = 1;
private Color color;

/**
 * Constructs a LineBorder with the specified color and of the specified width.
 *
 * @param color The color of the border.
 * @param width The width of the border in pixels.
 * @since 2.0
 */
public LineBorder(Color color, int width) {
	setColor(color);
	setWidth(width);
}

/**
 * Constructs a LineBorder with the specified color and a width of 1 pixel.
 *
 * @param color The color of the border.
 * @since 2.0
 */
public LineBorder(Color color) {
	this(color, 1);
}

/**
 * Constructs a black LineBorder with the specified width.
 *
 * @param width The width of the border in pixels.
 * @since 2.0
 */
public LineBorder(int width) {
	this(null, width);
}

/**
 * Constructs a default black LineBorder with a width of one pixel.
 * 
 * @since 2.0
 */
public LineBorder() { }

/**
 * Returns the line color of this border.
 * @return The line color of this border */
public Color getColor() {
	return color;
}

/**
 * Returns the space used by the border for the figure provided as input. In this border
 * all sides always have equal width.
 * @param figure The figure this border belongs to
 * @return This border's insets
 */
public Insets getInsets(IFigure figure) {
	return new Insets(getWidth());
}

/**
 * Returns the line width of this border.
 * @return The line width of this border */
public int getWidth() {
	return width;
}

/**
 * Returns <code>true</code> since this border is opaque. Being opaque it is responsible 
 * to fill in the area within its boundaries.
 * @return <code>true</code> since this border is opaque
 */
public boolean isOpaque() {
	return true;
}

/**
 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
 */
public void paint(IFigure figure, Graphics graphics, Insets insets) {
	tempRect.setBounds(getPaintRectangle(figure, insets));
	if (getWidth() % 2 == 1) {
		tempRect.width--;
		tempRect.height--;
	}
	tempRect.shrink(getWidth() / 2, getWidth() / 2);
	graphics.setLineWidth(getWidth());
	if (getColor() != null)
		graphics.setForegroundColor(getColor());
	graphics.drawRectangle(tempRect);
}

/**
 * Sets the line color for this border.
 * @param color The line color */
public void setColor(Color color) {
	this.color = color;
}

/**
 * Sets the line width for this border.
 * @param width The line width */
public void setWidth(int width) {
	this.width = width;
}

}