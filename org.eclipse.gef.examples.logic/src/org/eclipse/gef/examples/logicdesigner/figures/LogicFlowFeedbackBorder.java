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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public class LogicFlowFeedbackBorder 
	extends LogicFlowBorder
{

public LogicFlowFeedbackBorder() {}

public LogicFlowFeedbackBorder(int width) {
	super(width);
}

public void paint(IFigure figure, Graphics graphics, Insets insets) {
	graphics.setForegroundColor(ColorConstants.white);	
	graphics.setBackgroundColor(LogicColorConstants.ghostFillColor);
	graphics.setXORMode(true);
	
	Rectangle r = figure.getBounds();
	
	graphics.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
//	graphics.drawLine(r.x, r.y + 1, r.right() - 1, r.y + 1);
//	graphics.drawLine(r.x, r.bottom() - 1, r.right() - 1, r.bottom() - 1);
//	graphics.drawLine(r.x, r.y + 1, r.x, r.bottom() - 1);
//	graphics.drawLine(r.right() - 1, r.bottom() - 1, r.right() - 1, r.y + 1);
	
	tempRect.setBounds(new Rectangle(r.x, r.y, grabBarWidth, r.height));
	
	graphics.fillRectangle(tempRect);
}
}