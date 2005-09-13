/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;


import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * A freeform layer figure which can be scaled.
 * 
 * @author irbull
 * @author Chris Callendar
 */
public class ScaledFigure extends Figure {

	private double scale = 1D;
	
	public ScaledFigure() {
		scale = 1D;
		setLayoutManager(new FreeformLayout());
		setOpaque(true);
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		revalidate();
		repaint();
	}
	
	public double getScale() {
		return this.scale;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */
	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= scale;
		rect.height /= scale;
		return rect;
	}

	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w,h);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#translateFromParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1/scale);		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#translateToParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateToParent(Translatable t) {
		t.performScale(scale);
		super.translateToParent(t);
	}

	protected boolean useLocalCoordinates() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		ScaledGraphics g = null;
		boolean disposeGraphics = false;
		if (graphics instanceof ScaledGraphics) {
			g = (ScaledGraphics)graphics;
		} else {
			g = new ScaledGraphics(graphics);
			disposeGraphics = true;
		}
		
		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
		if (!optimizeClip) {
			g.clipRect(getBounds().getCropped(getInsets()));
		}
		g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
		g.scale(scale);
		g.pushState();
		paintChildren(g);
		g.popState();
		if (disposeGraphics) {
			g.dispose();
			graphics.restoreState();
		}
	}
	
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);
	}	
}
