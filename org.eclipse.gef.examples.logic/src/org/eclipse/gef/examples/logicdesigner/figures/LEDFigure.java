/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.handles.HandleBounds;

import org.eclipse.gef.examples.logicdesigner.model.LED;

/**
 * @author danlee
 */
public class LEDFigure
	extends NodeFigure
	implements HandleBounds
{
protected static final Dimension SIZE = new Dimension(61, 47); 

/**
 * Color of the shadow around the LEDFigure's display
 */
public static final Color DISPLAY_SHADOW = new Color(null, 57, 117, 90); 

/**
 * Color of the LEDFigure's displayed value
 */
public static final Color DISPLAY_TEXT = new Color(null, 255, 199, 16);

protected static final Font DISPLAY_FONT = new Font(null, "", 19, 0); //$NON-NLS-1$
protected static PointList connector = new PointList();
protected static PointList bottomConnector = new PointList();
protected static Rectangle displayRectangle = new Rectangle(15, 11, 31, 25);
protected static Rectangle displayShadow = new Rectangle(14, 10, 32, 26);
protected static Rectangle displayHighlight = new Rectangle(15, 11, 32, 26);
protected static Point valuePoint = new Point(16, 10);

static {
	connector.addPoint(-2, 0);
	connector.addPoint(1, 0);
	connector.addPoint(2, 1);
	connector.addPoint(2, 5);
	connector.addPoint(-1, 5);
	connector.addPoint(-1, 1);

	bottomConnector.addPoint(-2, 0);
	bottomConnector.addPoint(1, 0);
	bottomConnector.addPoint(2, -1);
	bottomConnector.addPoint(2, -5);
	bottomConnector.addPoint(-1, -5);
	bottomConnector.addPoint(-1, -1);
}

protected static final int[] GAP_CENTERS_X = {8, 23, 38, 53};
protected static final int Y1 = 2;
protected static final int Y2 = 44;

protected String value;

/**
 * @see org.eclipse.draw2d.Figure#setBounds(Rectangle)
 */
public void setBounds(Rectangle rect) {
	super.setBounds(rect);
}

/**
 * @see org.eclipse.draw2d.Figure#setConstraint(IFigure, Object)
 */
public void setConstraint(IFigure child, Object constraint) {
	super.setConstraint(child, constraint);
}

/**
 * Creates a new LEDFigure
 */
public LEDFigure() {
	getBounds().width = 61;
	getBounds().height = 47;
	
	FixedConnectionAnchor c;
	c = new FixedConnectionAnchor(this);
	c.offsetH = 51;
	connectionAnchors.put(LED.TERMINAL_1_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 36;
	connectionAnchors.put(LED.TERMINAL_2_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 21;
	connectionAnchors.put(LED.TERMINAL_3_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 6;
	connectionAnchors.put(LED.TERMINAL_4_IN, c);
	inputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 51;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_1_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 36;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_2_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 21;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_3_OUT, c);
	outputConnectionAnchors.addElement(c);
	c = new FixedConnectionAnchor(this);
	c.offsetH = 6;
	c.topDown = false;
	connectionAnchors.put(LED.TERMINAL_4_OUT, c);
	outputConnectionAnchors.addElement(c);

}

/**
 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
 */
public Rectangle getHandleBounds() {
	return getBounds().getCropped(new Insets(2,0,2,0));
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return SIZE;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	Rectangle r = getBounds().getCopy();
	g.translate(r.getLocation());
	g.setBackgroundColor(LogicColorConstants.logicGreen);
	g.setForegroundColor(LogicColorConstants.connectorGreen);
	g.fillRectangle(0, 2, r.width, r.height - 4);	
	int right = r.width - 1;
	g.drawLine(0, Y1, right, Y1);
	g.drawLine(0, Y1, 0, Y2);
	
	g.setForegroundColor(LogicColorConstants.connectorGreen);
	g.drawLine(0, Y2, right, Y2);
	g.drawLine(right, Y1, right, Y2);

	// Draw the gaps for the connectors
	g.setForegroundColor(ColorConstants.listBackground);
	for (int i = 0; i < 4; i++) {
		g.drawLine(GAP_CENTERS_X[i] - 2, Y1, GAP_CENTERS_X[i] + 3, Y1);
		g.drawLine(GAP_CENTERS_X[i] - 2, Y2, GAP_CENTERS_X[i] + 3, Y2);
	}
			
	// Draw the connectors
	g.setForegroundColor(LogicColorConstants.connectorGreen);
	g.setBackgroundColor(LogicColorConstants.connectorGreen);
	for (int i = 0; i < 4; i++) {
		connector.translate(GAP_CENTERS_X[i], 0);
		g.fillPolygon(connector);
		g.drawPolygon(connector);
		connector.translate(-GAP_CENTERS_X[i], 0);
		
		bottomConnector.translate(GAP_CENTERS_X[i], r.height - 1);
		g.fillPolygon(bottomConnector);
		g.drawPolygon(bottomConnector);
		bottomConnector.translate(-GAP_CENTERS_X[i], -r.height + 1);
	}
	
	// Draw the display
	g.setBackgroundColor(LogicColorConstants.logicHighlight);
	g.fillRectangle(displayHighlight);
	g.setBackgroundColor(DISPLAY_SHADOW);
	g.fillRectangle(displayShadow);
	g.setBackgroundColor(ColorConstants.black);
	g.fillRectangle(displayRectangle);
	
	// Draw the value
	g.setFont(DISPLAY_FONT);
	g.setForegroundColor(DISPLAY_TEXT);
	g.drawText(value, valuePoint);
}

/**
 * Sets the value of the LEDFigure to val.
 * 
 * @param val The value to set on this LEDFigure */
public void setValue(int val) {
	value = String.valueOf(val);
	if (val < 10)
		value = "0" + value;	//$NON-NLS-1$
	repaint();
}

/**
 *  * @see java.lang.Object#toString() */
public String toString() {
	return "LEDFigure"; //$NON-NLS-1$
}

}