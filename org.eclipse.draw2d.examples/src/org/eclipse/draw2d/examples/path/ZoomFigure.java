/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephane Lizeray slizeray@ilog.fr - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.path;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

public class ZoomFigure extends Figure implements ScalableFigure {
	private double scale = 1.0;

	/*
	 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;
		if (scale == 1.0) {
			super.paintClientArea(graphics);
		} else {
			ScaledGraphics g = new ScaledGraphics(graphics);
			boolean optimizeClip = getBorder() == null
					|| getBorder().isOpaque();
			if (!optimizeClip)
				g.clipRect(getBounds().getCropped(getInsets()));
			g.translate(getBounds().x + getInsets().left, getBounds().y
					+ getInsets().top);
			g.scale(scale);
			g.pushState();
			paintChildren(g);
			g.dispose();
			graphics.restoreState();
		}

	}

	/*
	 * @see org.eclipse.draw2d.ScalableFigure#getScale()
	 */
	public double getScale() {
		return scale;
	}

	/*
	 * @see org.eclipse.draw2d.ScalableFigure#setScale(double)
	 */
	public void setScale(double newZoom) {
		if (scale == newZoom)
			return;
		scale = newZoom;
		repaint();
	}

	/*
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */
	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= scale;
		rect.height /= scale;
		return rect;
	}

	/*
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w, h);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
	 */
	public void translateToParent(Translatable t) {
		t.performScale(scale);
		super.translateToParent(t);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
	 */
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1 / scale);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}

}
