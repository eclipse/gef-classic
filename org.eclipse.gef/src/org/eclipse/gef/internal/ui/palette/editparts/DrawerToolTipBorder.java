/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

class DrawerToolTipBorder 
	extends AbstractBorder
{

/**
 * Constructs a default black lined border of one
 * pixel width.
 */
public DrawerToolTipBorder(){
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