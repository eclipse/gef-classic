package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

class CategoryToolTipBorder 
	extends AbstractBorder
{

/**
 * Constructs a default black lined border of one
 * pixel width.
 */
public CategoryToolTipBorder(){
}

/**
 * Returns the space used by the border for the 
 * figure provided as input. In this border all 
 * sides always have equal width.
 *
 * @param figure  Figure for which this is the border.
 * @return  Insets for this border.
 */
public Insets getInsets(IFigure figure){
	return new Insets(1,3,5,3);
}

/**
 * Returns true to indicate whether this border is opaque or not.
 * Being opaque it is responsible to fill in the area within
 * its boundaries.
 * 
 * @return  Opaque state of this border.
 */
public boolean isOpaque(){
	return true;
}

/**
 * Paints the border based on the inputs given. 
 *
 * @param figure  Figure for which this is the border.
 * @param graphics  Graphics handle for drawing the border.
 * @param insets  Space to be taken up by this border.
 */
public void paint(IFigure figure, Graphics graphics, Insets insets){
	Rectangle r = getPaintRectangle(figure, insets);
	graphics.setLineWidth(1);

	r.width  -= 2;
	r.height -= 2;

	graphics.setForegroundColor(ColorConstants.black);
	graphics.drawRectangle(r);

	r.translate(1,1);
	graphics.drawLine(r.x      , r.bottom(), r.right(), r.bottom());
	graphics.drawLine(r.right(), r.y       , r.right(), r.bottom());
}

}