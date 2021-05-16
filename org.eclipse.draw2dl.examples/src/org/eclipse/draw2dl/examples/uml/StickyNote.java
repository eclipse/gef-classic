/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2dl.examples.uml;

import org.eclipse.draw2dl.AbstractBorder;
import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.draw2dl.text.FlowFigure;
import org.eclipse.draw2dl.text.TextFlow;

/**
 * since 3.0
 */
public class StickyNote extends FlowPage {

static  class DogEar extends AbstractBorder {
	private static final Insets INSETS = new Insets(3, 3, 3, 3);
	/**
	 * @see org.eclipse.draw2dl.Border#getInsets(org.eclipse.draw2dl.IFigure)
	 */
	public Insets getInsets(org.eclipse.draw2dl.IFigure figure) {
		return INSETS;
	}
	
	/**
	 * @see org.eclipse.draw2dl.Border#paint(org.eclipse.draw2dl.IFigure, org.eclipse.draw2dl.Graphics, Insets)
	 */
	public void paint(org.eclipse.draw2dl.IFigure figure, org.eclipse.draw2dl.Graphics g, Insets insets) {
		Rectangle r = getPaintRectangle(figure, insets);
		r.resize(-1, -1);
		org.eclipse.draw2dl.geometry.PointList pl = new org.eclipse.draw2dl.geometry.PointList(new int[] { -5, 5, 0, 5, -5, 0});
		pl.translate(r.getTopRight());
		g.drawPolygon(pl);
		g.drawLine(r.getTopLeft(), r.getTopRight().translate( -5, 0));
		g.drawLine(r.getTopLeft(), r.getTopLeft());
		g.drawLine(r.getBottomLeft(), r.getBottomRight());
		g.drawLine(r.getTopRight().translate(0, 5), r.getBottomRight());
		g.drawLine(r.getTopLeft(), r.getBottomLeft());
	}
}

private TextFlow text = new TextFlow();

public StickyNote() {
	setBorder(new DogEar());
	add(text);
	text.setText("This is a sticky note.  It wraps text based on its " +
			"width.");
	setBackgroundColor(ColorConstants.tooltipBackground);
	setOpaque(true);
}

/**
 * @see FlowFigure#paintFigure(org.eclipse.draw2dl.Graphics)
 */
protected void paintFigure(org.eclipse.draw2dl.Graphics g) {
	Rectangle r = getBounds();
	org.eclipse.draw2dl.geometry.PointList pl = new PointList(5);
	pl.addPoint(r.getTopLeft());
	pl.addPoint(r.getTopRight().translate( - 6, 0));
	pl.addPoint(r. getTopRight().translate(0, 6));
	pl.addPoint(r.getBottomRight());
	pl.addPoint(r.getBottomLeft());
	g.fillPolygon(pl);
}

}
