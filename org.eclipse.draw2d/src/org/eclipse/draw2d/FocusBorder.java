package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Insets;

/**
 * A Border that looks like the system's focus rectangle.
 */
public class FocusBorder
	extends AbstractBorder
{

/**
 * Constructs a new FocusBorder. */
public FocusBorder() { }

/**
 * @see org.eclipse.draw2d.Border#getInsets(IFigure)
 */
public Insets getInsets(IFigure figure) {
	return new Insets(1);
}

/**
 * @see org.eclipse.draw2d.Border#isOpaque()
 */
public boolean isOpaque() {
	return true;
}

/**
 * Paints a focus rectangle.
 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
 */
public void paint(IFigure figure, Graphics graphics, Insets insets) {
	tempRect.setBounds(getPaintRectangle(figure, insets));
	tempRect.width--;
	tempRect.height--;
	graphics.setXORMode(true);
	graphics.setForegroundColor(ColorConstants.black);
	graphics.setBackgroundColor(ColorConstants.white);
	graphics.drawFocus(tempRect);
}

}