/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane extends FreeformLayeredPane implements IScalablePane {

	private double scale = 1.0;

	private boolean useScaledGraphics = true;

	public ScalableFreeformLayeredPane() {
	}

	public ScalableFreeformLayeredPane(boolean useScaledGraphics) {
		this.useScaledGraphics = useScaledGraphics;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */
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
	 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
	 */
	@Override
	protected void paintClientArea(final Graphics graphics) {
		if (getChildren().isEmpty())
			return;

		Graphics graphicsToUse = getScaledGraphics(graphics);
		super.paintClientArea(graphicsToUse);
		if (graphicsToUse != graphics) {
			graphicsToUse.dispose();
		}
		graphics.restoreState();
	}

	/**
	 * Sets the zoom level
	 * 
	 * @param newZoom The new zoom level
	 */
	@Override
	public void setScale(double newZoom) {
		if (scale == newZoom)
			return;
		scale = newZoom;
		superFireMoved(); // For AncestorListener compatibility
		getFreeformHelper().invalidate();
		repaint();
	}

	@Override
	public boolean useScaledGraphics() {
		return useScaledGraphics;
	}

}
