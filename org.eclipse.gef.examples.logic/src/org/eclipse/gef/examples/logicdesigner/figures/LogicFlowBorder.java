package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class LogicFlowBorder 
	extends org.eclipse.draw2d.LineBorder 
{

private int grabBarWidth = 20;
private Dimension grabBarSize = new Dimension(grabBarWidth, 18);

public LogicFlowBorder() {}

public LogicFlowBorder(int width) {
	setGrabBarWidth(width);
	grabBarSize = new Dimension(width, 18);
}

public Insets getInsets(IFigure figure){
	return new Insets(getWidth()+2, grabBarWidth+2, getWidth()+2, getWidth()+2);
}

public Dimension getPreferredSize() {
	return grabBarSize;
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	Rectangle bounds = figure.getBounds();
	tempRect.setBounds(new Rectangle(bounds.x, bounds.y, grabBarWidth, bounds.height));
	graphics.setBackgroundColor(LogicColorConstants.logicGreen);
	graphics.fillRectangle(tempRect);
	super.paint(figure, graphics, insets);
}

public void setGrabBarWidth(int width) {
	grabBarWidth = width;
}

}