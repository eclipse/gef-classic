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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane extends FreeformLayeredPane implements IScalablePane {

	private double scale = 1.0;

	private final boolean useScaledGraphics;

	public ScalableFreeformLayeredPane() {
		this(true);
	}

	/**
	 * Constructor which allows to configure if scaled graphics should be used.
	 *
	 * @since 3.13
	 */
	public ScalableFreeformLayeredPane(boolean useScaledGraphics) {
		this.useScaledGraphics = useScaledGraphics;
	}

	/** @see org.eclipse.draw2d.Figure#getClientArea() */
	@Override
	public Rectangle getClientArea(Rectangle rect) {
		return getScaledRect(super.getClientArea(rect));
	}

	/**
	 * Returns the current zoom scale level.
	 *
	 * @return the scale
	 */
	@Override
	public double getScale() {
		return scale;
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#isCoordinateSystem()
	 */
	@Override
	public boolean isCoordinateSystem() {
		return true;
	}

	/** @see org.eclipse.draw2d.Figure#paintClientArea(Graphics) */
	@Override
	protected void paintClientArea(final Graphics graphics) {
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
		superFireMoved(); // For AncestorListener compatibility
		getFreeformHelper().invalidate();
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

}
