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

/**
 * The cached preferred size.
 */
protected Dimension preferredSize;

/**
 * This method is now {@link #calculatePreferredSize(IFigure, int, int)}.
 * @param container the figure
 */
protected final void calculatePreferredSize(IFigure container) { }

/**
 * Calculates the preferred size of the given figure, using width and height hints.
 * @param container The figure
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */
protected abstract Dimension calculatePreferredSize(IFigure container,
	int wHint, int hHint);

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
 * This method is now {@link #getMinimumSize(IFigure, int, int)}.
 * @param container the figure
 */
public final void getMinimumSize(IFigure container) { }

/** * @see org.eclipse.draw2d.LayoutManager#getMinimumSize(IFigure, int, int) */
public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
	return getPreferredSize(container, wHint, hHint);
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
 * This method is now {@link #getPreferredSize(IFigure, int, int)}.
 * @param container the figure
 */
public final void getPreferredSize(IFigure container) { }

/** * @see org.eclipse.draw2d.LayoutManager#invalidate() */
public void invalidate() {
	preferredSize = null;
}

/**
 * Removes any cached information about the given figure.
 * @param child the child that is invalidated
 */
protected void invalidate(IFigure child) {
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
 * @param child the child
 * @param constraint the child's new constraint
 */
public void setConstraint(IFigure child, Object constraint) {
	invalidate(child);
}

}