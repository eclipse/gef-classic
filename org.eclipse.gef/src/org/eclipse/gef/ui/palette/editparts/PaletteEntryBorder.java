package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

final public class PaletteEntryBorder 
	extends org.eclipse.draw2d.AbstractBorder 
{

private static Insets insets = new Insets(1);

public PaletteEntryBorder() {}

public Insets getInsets(IFigure figure) {
	return insets;
}

public boolean isOpaque(){
	return true;
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	ButtonModel model = ((Clickable)figure).getModel();
	boolean shouldPaint =
		(model.isSelected() || model.isArmed() || model.isMouseOver());
	if (model.isMouseOver())
		graphics.setForegroundColor(ColorConstants.button);

	if (model.isSelected())
		graphics.setForegroundColor(ColorConstants.menuBackgroundSelected);

	if (model.isArmed())
		graphics.setForegroundColor(ColorConstants.buttonDarker);

	if (shouldPaint){
		Rectangle r = getPaintRectangle(figure, insets);
		graphics.drawLine(r.x+1, r.y, r.right()-2, r.y);
		graphics.drawLine(r.x+1, r.bottom()-1, r.right()-2, r.bottom()-1);
		graphics.drawLine(r.x, r.y+1, r.x, r.bottom()-2);
		graphics.drawLine(r.right()-1, r.y+1, r.right()-1, r.bottom()-2);

//		graphics.drawRoundRectangle(figure.getBounds(),5,5);
	}
}

}