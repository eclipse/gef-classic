package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * 
 */
public interface Border {

/**
 * Returns the Insets for this Border for the given Figure.
 */
Insets getInsets(IFigure figure);

/**
 * Returns the preferred width and height that this border
 * would like to display itself properly.
 */
Dimension getPreferredSize(IFigure figure);

/**
 * Returns true if the Border completely fills the region defined
 * above in paint.
 */
boolean isOpaque();

/**
 * paint the Border.  The border should paint inside figure's
 * getBounds(), inset by the parameter <B>insets</B>.  The border
 * generally should not paint inside its own insets.
 *
 * More specifically, Border b should paint inside the rectangle:
 * 	figure.getBounds().getCropped(insets)
 * and outside of the rectangle:
 *    figure.getBounds().getCropped(insets).getCropped(getInsets())
 * where <I>inside</I> is defined as rectangle.contains(x,y);
 */
void paint(IFigure figure, Graphics graphics, Insets insets);


}