package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.gef.examples.logicdesigner.LogicColorConstants;

public class LogicFlowBorder 
	extends org.eclipse.draw2d.LineBorder 
{

private int grabBarWidth = 20;
private Dimension grabBarSize = new Dimension(grabBarWidth, 18);
private Color grabBarColor = LogicColorConstants.logicGreen;

public LogicFlowBorder() {}

public LogicFlowBorder(int width) {
	setGrabBarWidth(width);
	grabBarSize = new Dimension(width, 18);
}

public LogicFlowBorder(int width, Color color) {
	this(width);
	setGrabBarColor(color);
}

public Insets getInsets(IFigure figure){
	return new Insets(width+2, grabBarWidth+2, width+2, width+2);
}

public Dimension getPreferredSize() {
	return grabBarSize;
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	Rectangle bounds = figure.getBounds();
	tempRect.setBounds(new Rectangle(bounds.x, bounds.y, grabBarWidth, bounds.height));
	graphics.setBackgroundColor(grabBarColor);
	graphics.fillRectangle(tempRect);
	super.paint(figure, graphics, insets);
}

public void setGrabBarColor(Color color) {
	grabBarColor = color;
}

public void setGrabBarWidth(int width) {
	grabBarWidth = width;
}

public void setOutlineColor(Color c) {
	color = c;
}

public void setOutlineWidth(int w) {
	width = w;
}

}