package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides a two pixel wide constant sized border,
 * having an etched look.
 */
final public class SimpleEtchedBorder
	extends SchemeBorder
{

public static final Border singleton = new SimpleEtchedBorder();

protected static final Insets INSETS = new Insets(2);

/**
 * Constructs a default border having a two pixel wide
 * border.
 * 
 * @since 2.0 
 */
protected SimpleEtchedBorder(){}

/*
 * Returns the Insets used by this border. This is
 * a constant value of two pixels in each direction.
 */
public Insets getInsets(IFigure figure){
	return new Insets(INSETS);
}

/*
 * Returns the opaque state of this border. This border
 * is opaque and takes responsibility to fill the region 
 * it encloses.
 */
public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics g, Insets insets){
	Rectangle rect = getPaintRectangle(figure, insets);
	FigureUtilities.paintEtchedBorder(g, rect);
}

}