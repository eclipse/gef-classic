package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * A Border is a graphical decoration that is painted just inside the outer edge of a
 * Figure. 
 */
public interface Border {

/**
 * Returns the Insets for this Border for the given Figure.
 * @param figure The figure this border belongs to
 * @return The insets
 */
Insets getInsets(IFigure figure);

/**
 * Returns the preferred width and height that this border would like to display itself
 * properly.
 * @param figure The figure
 * @return The preferred size
 */
Dimension getPreferredSize(IFigure figure);

/**
 * Returns <code>true</code> if the Border completely fills the region defined in 
 * {@link #paint(IFigure, Graphics, Insets)}.
 * @return <code>true</code> if this border is opaque
 */
boolean isOpaque();

/**
 * Paints the border. The border should paint inside figure's {@link IFigure#getBounds()},
 * inset by the parameter <i>insets</i>.  The border generally should not paint inside its
 * own insets.  More specifically, Border <i>b</i> should paint inside the rectangle:
 * figure.getBounds().getCropped(insets) and outside of the rectangle:
 * figure.getBounds().getCropped(insets).getCropped(getInsets()) where <i>inside</i> is
 * defined as {@link Rectangle#contains(int, int)}.
 * 
 * @param figure The figure this border belongs to
 * @param graphics The graphics object used for painting
 * @param insets The insets
 */
void paint(IFigure figure, Graphics graphics, Insets insets);


}