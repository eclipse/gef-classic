/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * @author danlee
 */
public class AndGateFigure extends GateFigure {

private static final Dimension SIZE = new Dimension(15, 17);

/**
 * Constructor for AndGateFigure.
 */
public AndGateFigure() {
	setBackgroundColor(LogicColorConstants.andGate);
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
	r.translate(2, 2);
	r.setSize(11, 9);

	//Draw terminals, 2 at top
	g.drawLine(r.x + 2, r.y, r.x + 2, r.y - 2);
	g.drawLine(r.right() - 3, r.y, r.right() - 3, r.y - 2);

	//draw main area
	g.fillRectangle(r);
	
	//outline main area
	g.drawLine(r.x, r.y, r.right() - 1, r.y);
	g.drawLine(r.right() - 1, r.y, r.right() - 1, r.bottom() - 1);
	g.drawLine(r.x, r.y, r.x, r.bottom() - 1);

	//draw and outline the arc
	r.height = 9;
	r.y += 4;
	g.fillArc(r, 180, 180);
	r.width--;
	r.height--;
	g.drawArc(r, 180, 190);
	g.drawLine(r.x + r.width / 2, r.bottom(), r.x + r.width / 2, r.bottom() + 2);
}

}
