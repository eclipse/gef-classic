package com.ibm.etools.gef.examples.logicdesigner.figures;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;

public class CircuitBorder  
	extends AbstractBorder
{

private static Image downTab = new Image(null,CircuitBorder.class.getResourceAsStream("icons/downtab.bmp"));//$NON-NLS-1$
private Color borderColor = ColorConstants.button;

private static Color inner = new Color(null,186,176,190);
private static Insets insets = new Insets(14,10,14,10);
private static Image upTab = new Image(null, CircuitBorder.class.getResourceAsStream("icons/uptab.bmp"));//$NON-NLS-1$
	
private void drawSoldering(Graphics g, Rectangle rec) {
	g.setForegroundColor(FigureUtilities.lighter(borderColor));
	int y1 = rec.y,
		y2 = y1 + insets.top - 5,
		y3 = y1 + rec.height - insets.bottom,
		y4 = y3 + insets.bottom - 5,
		width = rec.width,
		innerwidth = width - insets.right * 2,
		x1,x2;
	for (int i = 0; i < 4; i++){
		x1 = rec.x+(2*i+1) * width / 8;
		g.drawImage(upTab,x1-2,y1);
		g.drawImage(downTab, x1-2,y4);
		x2 = rec.x+insets.right + (2*i+1) * innerwidth / 8;
		g.drawImage(downTab, x2-2,y2);
		g.drawImage(upTab, x2-2,y3);
		g.drawLine( x1,y1+5,x2,y2);
		g.drawLine( x2,y3+5,x1,y4);
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
	
	Rectangle rec = r.getExpanded(4,0);
	g.setLineWidth(28);
	g.setForegroundColor(borderColor);
	g.drawRectangle(rec);
	g.setLineWidth(1);

	//Outer Highlight
	Color lighter = FigureUtilities.lighter(borderColor);
	Color darker = ColorConstants.black;
	g.setForegroundColor(lighter);
	g.drawLine(r.x, r.y, r.right(), r.y);
	g.drawLine(r.x, r.y, r.x, r.bottom());
	r.crop(new Insets(1,1,0,0));

	//Outer Shadow
	r.width--; r.height--;
	g.setForegroundColor(darker);
	g.drawLine(r.right(), r.bottom(), r.right(), r.y);
	g.drawLine(r.right(), r.bottom(), r.x, r.bottom());

	r.expand(1,1);
	r.crop(getInsets(figure));

	//Outer Inner Highlight
	g.setForegroundColor(darker);
	g.drawLine(r.x, r.y, r.right(), r.y);
	g.drawLine(r.x, r.y, r.x, r.bottom());
	r.crop(new Insets(1,1,0,0));

	//Outer Inner Shadow
	r.width--; r.height--;
	g.setForegroundColor(lighter);
	g.drawLine(r.right(), r.bottom(), r.right(), r.y);
	g.drawLine(r.right(), r.bottom(), r.x, r.bottom());

	g.setBackgroundColor(inner);
	drawSoldering(g,figure.getBounds().getCropped(in));
}

void setBorderColor(Color c){
	borderColor = c;
}

}