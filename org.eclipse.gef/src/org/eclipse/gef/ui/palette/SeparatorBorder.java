package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

final class SeparatorBorder 
	extends AbstractBorder {

protected static final Insets insets = new Insets(1,0,0,0);

public SeparatorBorder(){}
public Insets getInsets(IFigure figure){return insets;}
public boolean isOpaque(){return true;}

public void paint(IFigure figure, Graphics graphics, Insets insets){
	Rectangle r = getPaintRectangle(figure, insets);	
	graphics.setLineWidth(1);
	graphics.setForegroundColor(ColorConstants.button);
	graphics.drawLine(r.x+3, r.y, r.right()-4, r.y);
}

}