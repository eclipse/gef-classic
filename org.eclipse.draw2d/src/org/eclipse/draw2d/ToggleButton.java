package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Toggle that appears like a 3-dimensional button.
 */
public class ToggleButton
	extends Toggle
{

protected Label label = null;

public ToggleButton() {
}

/**
 * Constructs a ToggleButton with the passed
 * IFigure as its contents.
 * 
 * @since 2.0
 */
public ToggleButton(IFigure contents) {
	super(contents);
}

/**
 * Constructs a ToggleButton with the passed
 * string as its text.
 * 
 * @since 2.0
 */
public ToggleButton(String text) {
	this(text, null);
}

/**
 * Constructs a ToggleButton with a Label
 * containing the passed text and icon.
 * 
 * @since 2.0
 */
public ToggleButton(String text, Image normalIcon) {
	super(text, normalIcon);
}

/**
 * Initializes this Clickable by setting a default model
 * and adding a clickable event handler for that model.
 * 
 * @since 2.0
 */
protected void init() {
	setStyle(STYLE_BUTTON);
	super.init();
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	if (isSelected() && isOpaque()) {
		fillCheckeredRectangle(graphics);
	} else {
		super.paintFigure(graphics);
	}
}

/**
 * Helper method that can fill a rectangle of given dimensions with a checkered pattern
 * using the foreground and background colors of the given Graphics object.
 *  * @param graphics	The Graphics object used to paint * @param x		The x location starting from where the pattern has to be drawn * @param y		The y location starting from where the pattern has to be drawn * @param width	The width of the pattern * @param height	The height of the pattern */
protected void fillCheckeredRectangle(Graphics graphics) {

	graphics.setBackgroundColor(ColorConstants.button);
	graphics.setForegroundColor(ColorConstants.buttonLightest);
	Rectangle rect = getClientArea(Rectangle.SINGLETON).crop(new Insets(1, 1, 0, 0));
	graphics.fillRectangle(rect.x, rect.y, rect.width, rect.height);
	
	graphics.clipRect(rect);
	graphics.translate(rect.x, rect.y);
	int n = rect.width + rect.height;
	for (int i = 1; i < n; i += 2) {
		graphics.drawLine(0, i, i, 0);
	}
	graphics.restoreState();
}

}