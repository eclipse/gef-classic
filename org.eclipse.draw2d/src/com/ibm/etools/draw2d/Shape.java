package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Provides abstract support for a variety of shapes.
 */
public abstract class Shape
	extends Figure
{

protected int
	lineWidth = 1,
	lineStyle = Graphics.LINE_SOLID;

private boolean
	fill = true,
	outline = true,
	xorFill,
	xorOutline;

/**
 * Default constructor.
 * 
 * @since 2.0
 */
public Shape(){}

abstract protected void fillShape(Graphics graphics);

public int getLineStyle(){
	return lineStyle;
}

public int getLineWidth(){
	return lineWidth;
}

/**
 * Returns the opaque state of this shape. 
 *
 * @return  Returns <code>false</code> as shapes only
 *          draw themselves onto other figures, and 
 *          generally dont have rectangular dimensions.
 * 
 * @since 2.0
 */
public boolean isOpaque(){return false;}

abstract protected void outlineShape(Graphics graphics);

/*
 * Paints the shape with the help of the graphics handle
 * given as input. Each shape has an outline to draw, and
 * a region to fill within that outline. Disabled shapes
 * must visually depict the disabled state. 
 */
public void paintFigure(Graphics graphics){
	if( !isEnabled() ){
		graphics.translate(1,1);
		graphics.setBackgroundColor(ColorConstants.buttonLightest);
		graphics.setForegroundColor(ColorConstants.buttonLightest);
		if (fill){
			graphics.setXORMode(xorFill);
			fillShape(graphics);
		}
		if (outline){
			graphics.setXORMode(xorOutline);
			graphics.setLineStyle(lineStyle);
			graphics.setLineWidth(lineWidth);
			outlineShape(graphics);
		}
		graphics.setBackgroundColor(ColorConstants.buttonDarker);
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.translate(-1,-1);
	}
	if (fill){
		graphics.setXORMode(xorFill);
		fillShape(graphics);
	}
	if (outline){
		graphics.setXORMode(xorOutline);
		graphics.setLineStyle(lineStyle);
		graphics.setLineWidth(lineWidth);
		outlineShape(graphics);
	}
}

/**
 * Sets whether this shape should fill its
 * region or not. It repaints this figure.
 *
 * @param b  Fill state.
 * @since 2.0
 */
public void setFill(boolean b){
	if (fill == b) return;
	fill = b;
	repaint();
}

/**
 * Sets whether XOR based fill should be used by the shape.
 * It repaints this figure.
 *
 * @param  XOR fill state.
 * @since 2.0
 */
public void setFillXOR(boolean b){
	if (xorFill == b) return;
	xorFill = b;
	repaint();
}

/**
 * Sets the line width to be used by the shape.
 *
 * @param w  Width of the line to be used.
 * @since 2.0
 */
public void setLineWidth(int w){
	if (lineWidth == w) return;
	lineWidth = w;
	repaint();
}

/**
 * Sets the style of line to be used by this shape.
 *
 * @param s  Line style.
 * @since 2.0
 */
public void setLineStyle(int s){
	if (lineStyle == s) return;
	lineStyle = s;
	repaint();
}

/**
 * Sets whether the outline should be drawn for this
 * shape.
 *
 * @param b  Outline state of the shape.
 * @since 2.0
 */
public void setOutline(boolean b){
	if (outline == b) return;
	outline = b;
	repaint();
}

/**
 * Sets whether XOR based outline should be used for this shape.
 *
 * @param b  XOR based outline usage state.
 * @since 2.0
 */
public void setOutlineXOR(boolean b){
	if (xorOutline == b) return;
	xorOutline = b;
	repaint();
}

/**
 * Sets whether XOR based fill and XOR based outline 
 * should be used for this shape.
 *
 * @param b  XOR based usage state.
 * @since 2.0
 */
public void setXOR(boolean b){
	xorOutline = xorFill = b;
	repaint();
}

}