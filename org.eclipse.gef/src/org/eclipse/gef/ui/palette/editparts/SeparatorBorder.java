package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.swt.graphics.Color;

final class SeparatorBorder 
	extends AbstractBorder 
{

public static final int TOP = 0;
public static final int BOTTOM = 1;

private static final Insets TOP_INSETS = new Insets(1, 0, 0, 0);
private static final Insets BOTTOM_INSETS = new Insets(0, 0, 1, 0);

private Color color;
private int location;
private int padding;

public SeparatorBorder() {
	setColor(ColorConstants.buttonDarker);
}

public Insets getInsets(IFigure figure) {
	if (location == TOP) {
		return TOP_INSETS;
	} else {
		return BOTTOM_INSETS;
	}
}

public boolean isOpaque() {
	return true;
}

/**
 * Sets the color of the separator.  Default is <code>ColorConstants.buttonDarker</code>.
 * 
 * @param color	The new Color */
public void setColor(Color color) {
	this.color = color;
}

/**
 * Determines whether the separator should be drawn at the top of the given figure
 * or the bottom.  Default is <code>TOP</code>.
 *  * @param location Must be either <code>TOP</code> or <code>BOTTOM</code>. */
public void setLocation(int location) {
	if (location != TOP && location != BOTTOM) {
		return;
	}
	this.location = location;
}

/**
 * Default is 0.  Padding will be ignored if it is larger than the width of the figure.
 *  * @param padding */
public void setSidePadding(int padding) {
	if (padding < 0) {
		return;
	}
	this.padding = padding;
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	Rectangle r = getPaintRectangle(figure, insets);
	graphics.setLineWidth(1);
	graphics.setLineStyle(Graphics.LINE_SOLID);
	graphics.setForegroundColor(color);
	int space = padding;
	if (padding * 2 + 1 > r.width) {
		space = 0;
	}
	int y;
	if (location == TOP) {
		y = r.y;
	} else {
		y = r.y + r.height - 1;
	}
	graphics.drawLine(r.x + space, y, r.right() - (space + 1), y);
}

}