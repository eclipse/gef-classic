package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.draw2d.geometry.*;

/**
 * Lays out figures, using a constraint if necessary.
 */
public interface LayoutManager {

/**
 * Returns the constraint for the given figure.
 */
Object getConstraint(IFigure figure);

/**
 * Returns the minimum size of the given figure.
 */
Dimension getMinimumSize(IFigure parent);

/**
 * Returns the preferred size of the given figure.
 */
Dimension getPreferredSize(IFigure parent);

/**
 * Returns the preferred size of the given figure, using
 * width and height hints.
 */
Dimension getPreferredSize(IFigure parent, int wHint, int hHint);

/**
 * Tells the LayoutManager to throw away all cached information
 * about the figures it is resposible for.
 */
void invalidate();

/**
 * Tells the LayoutManager to throw away any cached information
 * about the given figure.
 */
void invalidate(IFigure child);

/**
 * Lays out the given figure.
 */
void layout(IFigure parent);

/**
 * Removes the given figure from this LayoutManager.
 */
void remove(IFigure figure);

/**
 * Sets the constraint for the given figure.
 */
void setConstraint(IFigure figure, Object constraint);

}