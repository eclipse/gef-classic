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
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


public class GroundFeedbackFigure extends GroundFigure {


/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics g) {
	g.setXORMode(true);
	g.setForegroundColor(ColorConstants.white);
	g.setBackgroundColor(LogicColorConstants.ghostFillColor);
	Rectangle r = getBounds().getCopy();
	
	g.fillOval(r);
	r.height--;
	r.width--;
	g.drawOval(r);
	g.translate(r.getLocation());
		
	// Draw the "V"
	g.drawLine(3, 4, 5, 9);
	g.drawLine(5, 9, 7, 4);
	g.drawLine(5, 8, 5, 9);
		
	// Draw the "0"
	g.drawOval(7, 8, 3, 3);	
}

}