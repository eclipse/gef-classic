/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;



/**
 * @author danlee
 */
public class XOrGateFigure extends GateFigure {

protected static final Dimension SIZE = new Dimension(15, 17);
protected static final PointList GATE_OUTLINE = new PointList();
protected static final PointList GATE_TOP = new PointList();

static {
	//setup gate outline
	GATE_OUTLINE.addPoint(2, 10);
	GATE_OUTLINE.addPoint(2, 4);
	GATE_OUTLINE.addPoint(4, 6);
	GATE_OUTLINE.addPoint(6, 7);
	GATE_OUTLINE.addPoint(7, 7);
	GATE_OUTLINE.addPoint(8, 7);
	GATE_OUTLINE.addPoint(10, 6);
	GATE_OUTLINE.addPoint(12, 4);
	GATE_OUTLINE.addPoint(12, 10);

	//setup top curve of gate
	GATE_TOP.addPoint(2, 2);
	GATE_TOP.addPoint(4, 4);
	GATE_TOP.addPoint(6, 5);
	GATE_TOP.addPoint(7, 5);
	GATE_TOP.addPoint(8, 5);
	GATE_TOP.addPoint(10, 4);
	GATE_TOP.addPoint(12, 2);
}	

/**
  * Constructor for XOrGateFigure.
  */
public XOrGateFigure() {
	setBackgroundColor(LogicColorConstants.xorGate);
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
	g.drawLine(r.x + 2, r.y + 2, r.x + 2, r.y - 2);
	g.drawLine(r.right() - 3, r.y + 2, r.right() - 3, r.y - 2);

	//Draw an oval that represents the bottom arc
	r.y += 4;
	
	/* 
	 * Draw the bottom gate arc.
	 * This is done with an oval. The oval overlaps the top
	 * arc of the gate, so this region is clipped.
	 */
	g.pushState();
	r.y++;
	g.clipRect(r);
	r.y--;
	g.fillOval(r);
	r.width--;
	r.height--;
	g.drawOval(r);
	g.popState();
	g.drawLine(r.x + r.width / 2, r.bottom(), r.x + r.width / 2, r.bottom() + 2);

	//Draw the gate outline and top curve
	g.translate(getLocation());
	g.drawPolyline(GATE_TOP);
	g.fillPolygon(GATE_OUTLINE);
	g.drawPolyline(GATE_OUTLINE);
	g.translate(getLocation().negate());
}

}
