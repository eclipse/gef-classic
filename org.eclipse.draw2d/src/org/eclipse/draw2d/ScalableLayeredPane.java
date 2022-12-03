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

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A non-freeform, scalable layered pane.
 * 
 * @author Eric Bordeau
 * @since 2.1.1
 */
public class ScalableLayeredPane extends LayeredPane implements IScalablePane {

	private double scale = 1.0;

	private boolean useScaledGraphics = true;

	public ScalableLayeredPane() {
	}

	public ScalableLayeredPane(boolean useScaledGraphics) {
		this.useScaledGraphics = useScaledGraphics;
	}

	/**
	 * @see IFigure#getClientArea(Rectangle)
	 */
	@Override
	public Rectangle getClientArea(Rectangle rect) {
		return getScaledRect(super.getClientArea(rect));
	}

	/**
	 * @see Figure#getMinimumSize(int, int)
	 */
	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {
		Dimension d = super.getMinimumSize(wHint != SWT.DEFAULT ? (int) (wHint / getScale()) : SWT.DEFAULT,
				hHint != SWT.DEFAULT ? (int) (hHint / getScale()) : SWT.DEFAULT);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(scale).expand(w, h);
	}

	/**
	 * @see Figure#getPreferredSize(int, int)
	 */
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

	/**
	 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
	 */
	@Override
	protected void paintClientArea(Graphics graphics) {
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
		fireMoved(); // for AncestorListener compatibility
		revalidate();
		repaint();
	}

	@Override
	public boolean useScaledGraphics() {
		return useScaledGraphics;
	}

}
