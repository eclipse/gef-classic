package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Provides generic support for LayoutManagers.
 */
public abstract class AbstractLayout
	implements LayoutManager
{

private Dimension preferredSize;

/**
 * Calculates the preferred size of the given figure.
 * @param container The figure
 * @return The preferred size
 */
protected abstract Dimension calculatePreferredSize(IFigure container);

/**
 * Calculates the preferred size of the given figure, using width and height hints.
 * @param container The figure
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */
protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
	return calculatePreferredSize(container);
}

/**
 * Returns the preferred size of the figure's border.
 * @param container The figure that the border is on
 * @return The border's preferred size
 */
protected Dimension getBorderPreferredSize(IFigure container) {
	if (container.getBorder() == null)
		return new Dimension();
	return container.getBorder().getPreferredSize(container);
}

/**
 * Returns the constraint for the given figure.
 * @param child The figure
 * @return The constraint
 */
public Object getConstraint(IFigure child) {
	return null;
}

/**
 * Returns the minimum size of the given figure.
 * @param container The figure
 * @return The minimum size
 */
public Dimension getMinimumSize(IFigure container) {
	return getPreferredSize(container);
}

/**
 * Returns the preferred size of the given figure, using width and height hints.  If the
 * preferred size is cached, that size  is returned.  Otherwise, {@link
 * #calculatePreferredSize(IFigure, int, int)} is called.
 * @param container The figure
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */
public Dimension getPreferredSize(IFigure container, int wHint, int hHint) {
	if (preferredSize == null)
		preferredSize = calculatePreferredSize(container, wHint, hHint);
	return preferredSize;
}

/**
 * Returns the preferred size of the given figure.
 * @param container The figure
 * @return The preferred size
 */
public Dimension getPreferredSize(IFigure container) {
	return getPreferredSize(container, -1, -1);
}

/**
 * Removes all cached information for all figures this LayoutManager is responsible for.
 */
public void invalidate() {
	preferredSize = null;
}

/**
 * Removes any cached information about the given figure.
 * @param figure The figure to invalidate
 */
public void invalidate(IFigure figure) {
	invalidate();
}

/**
 * Removes the given figure from this LayoutManager's list of figures.
 * @param child The figure to remove
 */
public void remove(IFigure child) {
	invalidate();
}

/**
 * Sets the constraint for the given figure.
 * @param figure The figure
 * @param constraint The contstraint
 */
public void setConstraint(IFigure figure, Object constraint) {
	invalidate(figure);
}

}