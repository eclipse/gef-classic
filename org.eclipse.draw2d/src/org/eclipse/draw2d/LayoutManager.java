package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Lays out figures, using a constraint if necessary.
 */
public interface LayoutManager {

/**
 * Returns the constraint for the given figure.
 * @param figure The figure
 * @return The constraint
 */
Object getConstraint(IFigure figure);

/**
 * Returns the minimum size of the given figure.
 * @param figure The Figure
 * @return The minimum size
 */
Dimension getMinimumSize(IFigure figure);

/**
 * Returns the preferred size of the given figure.
 * @param figure The figure
 * @return The preferred size
 */
Dimension getPreferredSize(IFigure figure);

/**
 * Returns the preferred size of the given figure, using width and height hints.
 * @param figure The figure
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */
Dimension getPreferredSize(IFigure figure, int wHint, int hHint);

/**
 * Tells the LayoutManager to throw away all cached information about the figures it is
 * resposible for.
 */
void invalidate();

/**
 * Tells the LayoutManager to throw away any cached information about the given figure.
 * @param figure The figure
 */
void invalidate(IFigure figure);

/**
 * Lays out the given figure.
 * @param figure The figure
 */
void layout(IFigure figure);

/**
 * Removes the given figure from this LayoutManager.
 * @param figure The figure
 */
void remove(IFigure figure);

/**
 * Sets the constraint for the given figure.
 * @param figure The figure
 * @param constraint The constraint
 */
void setConstraint(IFigure figure, Object constraint);

}