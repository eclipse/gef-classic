package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * A border that provides blank padding.
 */
public class MarginBorder
	extends AbstractBorder
{

/**
 * This border's insets.
 */
protected Insets insets;

/**
 * Constructs a MarginBorder with dimensions specified by <i>insets</i>.
 *
 * @param insets The Insets for the border
 * @since 2.0
 */
public MarginBorder(Insets insets) {
	this.insets = insets;
}

/**
 * Constructs a MarginBorder with padding specified by the passed values.
 *
 * @param t Top padding
 * @param l Left padding
 * @param b Bottom padding
 * @param r Right padding
 * @since 2.0
 */
public MarginBorder(int t, int l, int b, int r) {
	this(new Insets(t, l, b, r));
}

/**
 * Constructs a MarginBorder with equal padding on all sides.
 *
 * @param allsides Padding size for all sides of the border.
 * @since 2.0
 */
public MarginBorder(int allsides) {
	this(new Insets(allsides));
}
/**
 * @see org.eclipse.draw2d.Border#getInsets(IFigure)
 */
public Insets getInsets(IFigure figure) {
	return insets;
}

/**
 * Returns <code>false</code>, forcing the figure of this border to fill up the occupying
 * region by itself.
 * @return <code>false</code> since this border is transparent
 */
public boolean isOpaque() {
	return false;
}

/**
 * This method does nothing, since this border is just for spacing.
 * @see org.eclipse.draw2d.Border#paint(IFigure, Graphics, Insets)
 */
public void paint(IFigure figure, Graphics graphics, Insets insets) { }

}