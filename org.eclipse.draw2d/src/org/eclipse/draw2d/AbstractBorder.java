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
 */
public abstract class AbstractBorder
	implements Border
{

private static final Dimension EMPTY = new Dimension();

/** A temporary Rectangle*/
protected static Rectangle tempRect = new Rectangle();

/**
 * Returns the region of f with its border removed, 
 * I.E. the paintable region of f. 
 *
 * @param f  Figure for which the paintable rectangle is needed.
 * @param insets  f's Insets.
 * @return  The paintable region of f.
 * @since 2.0
 */
static protected final Rectangle getPaintRectangle(IFigure f, Insets insets) {
	tempRect.setBounds(f.getBounds());
	return tempRect.crop(insets);
}

/*
 * From Interface
 */
public Dimension getPreferredSize(IFigure f) {
	return EMPTY;
}

}