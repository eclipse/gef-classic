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
 * @param child The figure
 * @return The constraint
 */
Object getConstraint(IFigure child);

/**
 * Returns the minimum size of the given figure.
 * @param container The Figure
 * @param wHint the width hint
 * @param hHint the height hint
 * @return The minimum size
 */
Dimension getMinimumSize(IFigure container, int wHint, int hHint);

/**
 * Returns the preferred size of the given figure, using width and height hints.
 * @param container The figure
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */
Dimension getPreferredSize(IFigure container, int wHint, int hHint);

/**
 * Tells the LayoutManager to throw away all cached information about the figures it is
 * resposible for. This method is called whenever the owning figure is invalidated.
 */
void invalidate();

/**
 * Lays out the given figure.
 * @param container The figure
 */
void layout(IFigure container);

/**
 * Removes the given child from this layout.
 * @param child the child being remoced
 */
void remove(IFigure child);

/**
 * Sets the constraint for the given child.
 * @param child The figure
 * @param constraint The constraint
 */
void setConstraint(IFigure child, Object constraint);

}