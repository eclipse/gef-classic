package org.eclipse.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.gef.examples.logicdesigner.LogicColorConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;

public class CircuitBorder  
	extends AbstractBorder
{

private Color borderColor = ColorConstants.button;
private static Insets insets = new Insets(6,6,6,6);
private static PointList connector = new PointList();
private static PointList bottomConnector = new PointList();

static {
	connector.addPoint(-2, 0);
	connector.addPoint(1, 0);
	connector.addPoint(2, 1);
	connector.addPoint(2, 5);
	connector.addPoint(-1, 5);
	connector.addPoint(-1, 1);

	bottomConnector.addPoint(-2, -1);
	bottomConnector.addPoint(1, -1);
	bottomConnector.addPoint(2, -2);
	bottomConnector.addPoint(2, -6);
	bottomConnector.addPoint(-1, -6);
	bottomConnector.addPoint(-1, -2);
}
	
private void drawConnectors(Graphics g, Rectangle rec) {
	int y1 = rec.y,  
		width = rec.width,x1,
		bottom = y1 + rec.height;
	g.setBackgroundColor(LogicColorConstants.connectorGreen);
	for (int i = 0; i < 4; i++) {
		x1 = rec.x + (2 * i + 1) * width / 8;
		
		//Draw the "gap" for the connector
		g.setForegroundColor(ColorConstants.white);
		g.drawLine(x1 - 2, y1 + 2, x1 + 3, y1 + 2);
		
		//Draw the connectors
		g.setForegroundColor(LogicColorConstants.connectorGreen);
		connector.translate(x1, y1);
		g.fillPolygon(connector);
		g.drawPolygon(connector);
		connector.translate(-x1, -y1);
		g.setForegroundColor(ColorConstants.white);
		g.drawLine(x1 - 2, bottom - 3, x1 + 3, bottom - 3);
		g.setForegroundColor(LogicColorConstants.connectorGreen);
		bottomConnector.translate(x1, bottom);
		g.fillPolygon(bottomConnector);
		g.drawPolygon(bottomConnector);
		bottomConnector.translate(-x1, -bottom);
	}
}

public Insets getInsets(IFigure figure) {
	return insets;
}

Color getBorderColor(){
	return borderColor;
}

public boolean isOpaque() {
	return true;
}
 
public void paint(IFigure figure, Graphics g, Insets in) {
	Rectangle r = figure.getBounds().getCropped(in);
	
	g.setForegroundColor(borderColor);
	g.setBackgroundColor(borderColor);
	
	//Draw the sides of the border
	g.fillRectangle(r.x, r.y + 2, r.width, 6);
	g.fillRectangle(r.x, r.bottom() - 8, r.width, 6);
	g.fillRectangle(r.x, r.y + 2, 6, r.height - 4);
	g.fillRectangle(r.right() - 6, r.y + 2, 6, r.height - 4);

	//Outline the border
	g.setForegroundColor(LogicColorConstants.connectorGreen);
	g.drawLine(r.x, r.y + 2, r.right() - 1, r.y + 2);
	g.drawLine(r.x, r.bottom() - 3, r.right() - 1, r.bottom() - 3);
	g.drawLine(r.x, r.y + 2, r.x, r.bottom() - 3);
	g.drawLine(r.right() - 1, r.bottom() - 3, r.right() - 1, r.y + 2);
	
	r.crop(new Insets(1, 1, 0, 0));
	r.expand(1, 1);
	r.crop(getInsets(figure));
	drawConnectors(g, figure.getBounds().getCropped(in));
}

void setBorderColor(Color c){
	borderColor = c;
}

}