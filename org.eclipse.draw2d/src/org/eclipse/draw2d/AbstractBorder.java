package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * Provides generic support for borders.
 * @author hudsonr
 */
public abstract class AbstractBorder
	implements Border
{

private static final Dimension EMPTY = new Dimension();

/** A temporary Rectangle*/
protected static Rectangle tempRect = new Rectangle();

/**
 * Returns a temporary rectangle representing the figure's bounds cropped by the specified
 * inserts.  This method exists for convenience and performance; the method does not new
 * any Objects and returns a rectangle which the caller can manipulate.
 * @since 2.0
 * @param figure  Figure for which the paintable rectangle is needed
 * @param insets The insets
 * @return The paintable region on the Figure f
 */
protected static final Rectangle getPaintRectangle(IFigure figure, Insets insets) {
	tempRect.setBounds(figure.getBounds());
	return tempRect.crop(insets);
}

/**
 * @see org.eclipse.draw2d.Border#getPreferredSize(IFigure)
 */
public Dimension getPreferredSize(IFigure f) {
	return EMPTY;
}

/**
 * @see org.eclipse.draw2d.Border#isOpaque()
 */
public boolean isOpaque() {
	return false;
}

}