/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.geometry.*;

/**
 * Border provides a title bar on the Figure for which
 * this is the border of. Generally used in
 * conjunction with other borders to create window-like
 * effects. Also provides for alignment of the text in
 * the bar.
 * 
 * @see  FrameBorder
 */
public class TitleBarBorder
	extends AbstractLabeledBorder
{

private static Color defaultColor = ColorConstants.menuBackgroundSelected;

private int textAlignment = PositionConstants.LEFT;
private Insets padding = new Insets(1,3,2,2);
private Color fillColor = defaultColor;

/**
 * Constructs a TitleBarBorder with its label
 * set to the name of this class.
 * 
 * @since 2.0
 */
public TitleBarBorder(){}

/**
 * Constructs a TitleBarBorder with its label set to the
 * passed String.
 * 
 * @param s  Text of the label.
 * @since 2.0
 */
public TitleBarBorder(String s){
	setLabel(s);
}

/**
 * Calculates and returns the Insets for this border. 
 * 
 * @param figure  Figure on which Insets calculations are
 *                 based.
 * @return  The calculated Insets.
 * @since 2.0
 */
protected Insets calculateInsets(IFigure figure) {
	return new Insets(getTextExtents(figure).height + padding.getHeight(),0,0,0);
}

/**
 * Returns the background Color of this TitleBarBorder.
 * @since 2.0
 */
protected Color getBackgroundColor(){
	return fillColor;
}

/**
 * Returns this TitleBarBorder's padding. Padding 
 * provides spacing along the sides of the TitleBarBorder.
 * 
 * @return The Insets representing the space along the
 *          sides of the TitleBarBorder.
 *          Default value is no padding along all sides.
 * @since 2.0 
 */
protected Insets getPadding(){
	return padding;
}

/**
 * Returns the alignment of the text in the title bar.
 * Values permitted are
 * <ul>
 * 	<li> Left
 * 	<li> Center
 * 	<li> Right
 * </ul> 
 * as defined in the {@link PositionConstants} interface.
 * 
 * @return  Alignment of the text as an integer.
 * @see  PositionConstants
 * @see  #setTextAlignment(int)
 * @since 2.0
 */
public int getTextAlignment() {
	return textAlignment;
}

/*
 * Returns the opaque state of the border. It returns 
 * true thereby filling up all the contents within its
 * boundaries, eleminating the need by the figure to
 * clip the boundaries and do the same.
 */
public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics g, Insets insets) {
	tempRect.setBounds(getPaintRectangle(figure, insets));
	Rectangle rec = tempRect;
	rec.height = Math.min(rec.height,getTextExtents(figure).height + padding.getHeight());
	g.clipRect(rec);
	g.setBackgroundColor(fillColor);
	g.fillRectangle(rec);
	
	int x = rec.x + padding.left;
	int y = rec.y + padding.top;

	int textWidth = getTextExtents(figure).width;
	int freeSpace = rec.width - padding.getWidth() - textWidth;

	if (getTextAlignment() == PositionConstants.CENTER)
		freeSpace /= 2;
	if (getTextAlignment() != PositionConstants.LEFT)
		x += freeSpace;

	g.setFont(getFont(figure));
	g.setForegroundColor(getTextColor());
	g.drawString(getLabel(), x, y);
}

/**
 * Sets the background color of the area within the
 * boundaries of this border. This is required as this
 * border takes responsibility for filling up the
 * region, as TitleBarBorders are always opaque.
 *
 * @param color  Color of the backround.
 * @see  #isOpaque()
 * @since 2.0
 */
public void setBackgroundColor(Color color){
	fillColor = color;
}

/**
 * Sets the padding space to be applied on all sides of
 * the border.
 * Default value is no padding on all sides.
 *
 * @param all  Value of the padding on all sides.
 * @since 2.0
 */
public void setPadding(int all){
	padding = new Insets(all);
	invalidate();
}

/**
 * Sets the padding space of this TitleBarBorder to the passed
 * value.
 * Default value is no padding on all sides.
 *
 * @param pad  Specific values of padding set to specific 
 *              sides.
 * @since 2.0
 */
public void setPadding(Insets pad){
	padding = pad; invalidate();
}

/**
 * Sets the alignment of the text in the title bar. Values
 * can be <ul>
 * 	<li> Left
 * 	<li> Center
 * 	<li> Right
 * </ul>
 * as defined in the {@link PositionConstants} interface.
 *
 * @param align  Value of the alignment.
 * @see PositionConstants
 * @see  #getTextAlignment()
 * @since 2.0
 */
public void setTextAlignment(int align){
	textAlignment = align;
}

}