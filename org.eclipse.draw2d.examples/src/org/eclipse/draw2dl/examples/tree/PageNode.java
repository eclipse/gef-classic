/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.tree;

import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * 
 * @author hudsonr
 * Created on Apr 22, 2003
 */
public class PageNode extends org.eclipse.draw2dl.Figure {

private boolean selected;
	static final Color gradient1 = new Color(null, 232,232,240);
static final Color gradient2 = new Color(null, 176,184,216);
static final Color corner1 = new Color(null, 200, 208, 223);
static final Color corner2 = new Color(null, 160, 172, 200);
static final Color blue = new Color(null, 152,168,200);
static final Color shadow = new Color(null, 202,202,202);

static final int CORNER_SIZE = 10;
static final Border BORDER = new CompoundBorder(
	new FoldedPageBorder(),
	new MarginBorder(4,4,8,3));

static class FoldedPageBorder extends AbstractBorder {
	static final org.eclipse.draw2dl.geometry.PointList CORNER_ERASE;
	static final org.eclipse.draw2dl.geometry.PointList CORNER_PAINT;
	static {
		CORNER_ERASE = new org.eclipse.draw2dl.geometry.PointList(3);
		CORNER_ERASE.addPoint(1,0);
		CORNER_ERASE.addPoint(1, CORNER_SIZE + 1);
		CORNER_ERASE.addPoint(-CORNER_SIZE, 0);
		CORNER_PAINT = new PointList(3);
		CORNER_PAINT.addPoint(-CORNER_SIZE, 0);
		CORNER_PAINT.addPoint(0, CORNER_SIZE);
		CORNER_PAINT.addPoint(-CORNER_SIZE, CORNER_SIZE);
	}
	
	static final org.eclipse.draw2dl.geometry.Insets insets = new org.eclipse.draw2dl.geometry.Insets(CORNER_SIZE,2,4,4);

	public org.eclipse.draw2dl.geometry.Insets getInsets(org.eclipse.draw2dl.IFigure figure) {
		return insets;
	}

	public void paint(IFigure figure, org.eclipse.draw2dl.Graphics g, Insets insets) {
		Rectangle r = getPaintRectangle(figure, insets);

		g.setLineWidth(4);
		r.resize(-2, -2);
		g.setForegroundColor(shadow);
		g.drawLine(r.x+3, r.bottom(), r.right() - 1, r.bottom());
		g.drawLine(r.right(), r.y + 3 + CORNER_SIZE, r.right(), r.bottom() - 1);
		
		g.restoreState();
		r.resize(-1, -1);
		g.drawRectangle(r);
		g.setForegroundColor(blue);
		g.drawRectangle(r.x + 1, r.y+1, r.width - 2, r.height - 2);
		g.translate(r.getTopRight());
		g.fillPolygon(CORNER_ERASE);
		g.setBackgroundColor(corner1);
		g.fillPolygon(CORNER_PAINT);
		g.setForegroundColor(figure.getForegroundColor());
		g.drawPolygon(CORNER_PAINT);
		g.restoreState();
		g.setForegroundColor(corner2);
		g.drawLine(
			r.right() - CORNER_SIZE + 1,
			r.y + 2,
			r.right() - 2,
			r.y + CORNER_SIZE-1);
	}
}

private org.eclipse.draw2dl.Label label = new org.eclipse.draw2dl.Label();

public PageNode(String text) {
	this();
	label.setText(text);
}

public PageNode() {
	setBorder(BORDER);
	setLayoutManager(new StackLayout());
	add(label);
}

/**
 * @see Figure#paintFigure(org.eclipse.draw2dl.Graphics)
 */
protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	if (selected) {
		g.setForegroundColor(org.eclipse.draw2dl.ColorConstants.menuBackgroundSelected);
		g.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.titleGradient);
	} else {
		g.setForegroundColor(gradient1);
		g.setBackgroundColor(gradient2);
	}
	g.fillGradient(getBounds().getResized(-3, -3), true);
}

public void setSelected(boolean value) {
	this.selected = value;
	if (selected)
		label.setForegroundColor(ColorConstants.white);
	else
		label.setForegroundColor(null);
	repaint();
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return ((Label)getChildren().get(0)).getText();
}

}
