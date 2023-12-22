/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * A non-freeform, scalable layered pane.
 *
 * @author Eric Bordeau
 * @since 2.1.1
 */
public class ScalableLayeredPane extends LayeredPane implements IScalablePane {

	private double scale = 1.0;

	private final boolean useScaledGraphics;

	public ScalableLayeredPane() {
		this(true);
	}

	/**
	 * Constructor which allows to configure if scaled graphics should be used.
	 *
	 * @since 3.13
	 */
	public ScalableLayeredPane(boolean useScaledGraphics) {
		this.useScaledGraphics = useScaledGraphics;
	}

	/** @see IFigure#getClientArea(Rectangle) */
	@Override
	public Rectangle getClientArea(Rectangle rect) {
		return getScaledRect(super.getClientArea(rect));
	}

	/** @see Figure#getMinimumSize(int, int) */
	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		Dimension d = super.getMinimumSize(wHint != SWT.DEFAULT ? (int) (wHint / getScale()) : SWT.DEFAULT,
				hHint != SWT.DEFAULT ? (int) (hHint / getScale()) : SWT.DEFAULT);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w, h);
	}

	/** @see Figure#getPreferredSize(int, int) */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint != SWT.DEFAULT ? (int) (wHint / getScale()) : SWT.DEFAULT,
				hHint != SWT.DEFAULT ? (int) (hHint / getScale()) : SWT.DEFAULT);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w, h);
	}

	/**
	 * Returns the scale level, default is 1.0.
	 *
	 * @return the scale level
	 */
	@Override
	public double getScale() {
		return scale;
	}

	/** @see org.eclipse.draw2d.Figure#paintClientArea(Graphics) */
	@Override
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty()) {
			return;
		}

		if (scale == 1.0) {
			super.paintClientArea(graphics);
		} else {
			Graphics graphicsToUse = IScalablePaneHelper.prepareScaledGraphics(graphics, this);
			paintChildren(graphicsToUse);
			IScalablePaneHelper.cleanupScaledGraphics(graphics, graphicsToUse);
		}
	}

	/**
	 * Make this method publicly accessible for IScaleablePane.
	 *
	 * @since 3.13
	 */
	@Override
	public boolean optimizeClip() {
		return super.optimizeClip();
	}

	/**
	 * Sets the zoom level
	 *
	 * @param newZoom The new zoom level
	 */
	@Override
	public void setScale(double newZoom) {
		if (scale == newZoom) {
			return;
		}
		scale = newZoom;
		fireMoved(); // for AncestorListener compatibility
		revalidate();
		repaint();
	}

	/**
	 * @since 3.13
	 */
	@Override
	public boolean useScaledGraphics() {
		return useScaledGraphics;
	}

	/** @see org.eclipse.draw2d.Figure#translateToParent(Translatable) */
	@Override
	public void translateToParent(Translatable t) {
		t.performScale(getScale());
	}

	/** @see org.eclipse.draw2d.Figure#translateFromParent(Translatable) */
	@Override
	public void translateFromParent(Translatable t) {
		t.performScale(1 / getScale());
	}

	/** @see org.eclipse.draw2d.IFigure#isCoordinateSystem() */
	@Override
	public boolean isCoordinateSystem() {
		return true;
	}

}
