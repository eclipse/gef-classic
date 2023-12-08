/*******************************************************************************
 * Copyright (c) 2008, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	public static final int NATIVE_SCALING = 0;
	public static final int EMULATED_SCALING = 1;

	private double scale = 1.0;
	private int scaleMethod = EMULATED_SCALING;

	/*
	 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
	 */
	@Override
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty()) {
			return;
		}
		if ((scale == 1.0) && (scaleMethod != EMULATED_SCALING)) {
			super.paintClientArea(graphics);
		} else {
			Graphics g = graphics;
			if (EMULATED_SCALING == scaleMethod) {
				g = new ScaledGraphics(graphics);
			}
			if (!optimizeClip()) {
				g.clipRect(getBounds().getShrinked(getInsets()));
			}
			g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
			g.scale(scale);
			g.pushState();
			paintChildren(g);
			if (EMULATED_SCALING == scaleMethod) {
				g.dispose();
			} else {
				g.popState();
			}
			graphics.restoreState();
		}
	}

	/*
	 * @see org.eclipse.draw2d.ScalableFigure#getScale()
	 */
	@Override
	public double getScale() {
		return scale;
	}

	/*
	 * @see org.eclipse.draw2d.ScalableFigure#setScale(double)
	 */
	@Override
	public void setScale(double newZoom) {
		if (scale == newZoom) {
			return;
		}
		scale = newZoom;
		repaint();
	}

	/*
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */
	@Override
	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= scale;
		rect.height /= scale;
		return rect;
	}

	/*
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w, h);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
	 */
	@Override
	public void translateToParent(Translatable t) {
		t.performScale(scale);
		super.translateToParent(t);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
	 */
	@Override
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		t.performScale(1 / scale);
	}

	/*
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	public void setScaleMethod(int scaleMethod) {
		this.scaleMethod = scaleMethod;
	}

}
