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

protected Insets insets;

/**
 * Constructs a MarginBorder with dimensions specified 
 * by insets.
 *
 * @param insets  Insets for the border.
 * @since 2.0
 */
public MarginBorder(Insets insets){
	this.insets = insets;
}

/**
 * Constructs a MarginBorder with padding specified 
 * by the passed values.
 *
 * @param t Top padding
 * @param l Left padding
 * @param b Bottom padding
 * @param t Top padding
 * @since 2.0
 */
public MarginBorder(int t, int l, int b, int r){
	this(new Insets(t,l,b,r));
}

/**
 * Constructs a MarginBorder with equal padding on all sides.
 *
 * @param allsides Padding size for all sides of the border.
 * @since 2.0
 */
public MarginBorder(int allsides){
	this(new Insets(allsides));
}

public Insets getInsets(IFigure figure){
	return insets;
}

/*
 * Opaque state of this border. Returns false forcing
 * the figure of this border to fill up the occupying
 * region by itself.
 */
public boolean isOpaque(){
	return false;
}

public void paint(IFigure figure, Graphics graphics, Insets insets){
}

}