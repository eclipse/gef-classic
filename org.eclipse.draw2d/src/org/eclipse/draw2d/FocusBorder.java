package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Insets;

/**
 * Provides for a simple border with all sides
 * of constant width.
 */
public class FocusBorder
	extends AbstractBorder
{

public FocusBorder(){
}

public Insets getInsets(IFigure figure){
	return new Insets(1);
}

public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics graphics, Insets insets){
	tempRect.setBounds(getPaintRectangle(figure, insets));
	tempRect.width--;
	tempRect.height--;
	graphics.setXORMode(true);
	graphics.setForegroundColor(ColorConstants.black);
	graphics.setBackgroundColor(ColorConstants.white);
	graphics.drawFocus(tempRect);
}

}