package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;

/**
 * Provides for a line border with sides of equal widths.
 */
public class LineBorder
	extends AbstractBorder
{

protected int width = 1;
protected Color color;

/**
 * Constructs a LineBorder with the specified color, and
 * of the specified width.
 *
 * @param c  Color of the border.
 * @param width  Width in pixels of the border.
 * @since 2.0
 */
public LineBorder(Color c, int width){
	color = c;
	this.width = width;
}

/**
 * Constructs a LineBorder with the specified color, and
 * a width of 1 pixel.
 *
 * @param c  Color of the border.
 * @since 2.0
 */
public LineBorder(Color c){
	this(c, 1);
}

/**
 * Constructs a black LineBorder with the specified width.
 *
 * @param width width of the LineBorder in pixels.
 * @since 2.0
 */
public LineBorder(int width){
	this(null, width);
}

/**
 * Constructs a default black LineBorder with a width of
 * one pixel.
 * 
 * @since 2.0
 */
public LineBorder(){}

/*
 * Returns the space used by the border for the 
 * figure provided as input. In this border all 
 * sides always have equal width.
 */
public Insets getInsets(IFigure figure){
	return new Insets(width);
}

/*
 * Returns true to indicate whether this border is opaque or not.
 * Being opaque it is responsible to fill in the area within
 * its boundaries.
 */
public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics graphics, Insets insets){
	tempRect.setBounds(getPaintRectangle(figure, insets));
	if (width%2 == 1){
		tempRect.width--;
		tempRect.height--;
	}
	tempRect.shrink(width/2,width/2);
	graphics.setLineWidth(width);
	if (color != null)
		graphics.setForegroundColor(color);
	graphics.drawRectangle(tempRect);
}

}