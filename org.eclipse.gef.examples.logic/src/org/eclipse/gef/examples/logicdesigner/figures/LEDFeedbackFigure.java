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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


public class LEDFeedbackFigure
	extends LEDFigure
{

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	g.setXORMode(true);
	g.setForegroundColor(ColorConstants.white);
	
	Rectangle r = getBounds().getCopy();
	g.translate(r.getLocation());
	
	g.setBackgroundColor(LogicColorConstants.ghostFillColor);
	g.fillRectangle(0, 2, r.width, r.height - 4);
	
	int right = r.width - 1;
	g.drawLine(0, Y1, right, Y1);
	g.drawLine(0, Y1, 0, Y2);
	g.drawLine(0, Y2, right, Y2);
	g.drawLine(right, Y1, right, Y2);

	g.drawPoint(0, Y1);
	g.drawPoint(right, Y1);
	g.drawPoint(0, Y2);
	g.drawPoint(right, Y2);
	
	// Draw the gaps for the connectors
	for (int i = 0; i < 4; i++) {
		g.drawLine(GAP_CENTERS_X[i] - 2, Y1, GAP_CENTERS_X[i] + 3, Y1);
		g.drawLine(GAP_CENTERS_X[i] - 2, Y2, GAP_CENTERS_X[i] + 3, Y2);
	}
		
	// Draw the connectors
	for (int i = 0; i < 4; i++) {
		connector.translate(GAP_CENTERS_X[i], 0);
		g.drawPolygon(connector);
		connector.translate(-GAP_CENTERS_X[i], 0);
	
		bottomConnector.translate(GAP_CENTERS_X[i], r.height - 1);
		g.drawPolygon(bottomConnector);
		bottomConnector.translate(-GAP_CENTERS_X[i], -r.height + 1);
	}
}

}
