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
package org.eclipse.draw2dl.examples.zoom;

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.ScaledGraphics;
import org.eclipse.draw2dl.StackLayout;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.geometry.Translatable;

public class ZoomContainer
	extends org.eclipse.draw2dl.Figure
{

{
	setLayoutManager(new StackLayout());
}

private float zoom;

/**
 * @see org.eclipse.draw2dl.Figure#getClientArea()
 */
public org.eclipse.draw2dl.geometry.Rectangle getClientArea(Rectangle rect) {
	super.getClientArea(rect);
	rect.width /= zoom;
	rect.height /= zoom;
	return rect;
}

public org.eclipse.draw2dl.geometry.Dimension getPreferredSize(int wHint, int hHint) {
	Dimension d = super.getPreferredSize(wHint, hHint);
	int w = getInsets().getWidth();
	int h = getInsets().getHeight();
	return d.getExpanded(-w, -h)
		.scale(zoom)
		.expand(w,h);
}

/**
 * @see org.eclipse.draw2dl.Figure#paintClientArea(org.eclipse.draw2dl.Graphics)
 */
protected void paintClientArea(Graphics graphics) {
	if (getChildren().isEmpty())
		return;

	boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

	org.eclipse.draw2dl.ScaledGraphics g = new ScaledGraphics(graphics);

	if (!optimizeClip)
		g.clipRect(getBounds().getCropped(getInsets()));
	g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
	g.scale(zoom);
	g.pushState();
	paintChildren(g);
	g.popState();
	g.dispose();
	graphics.restoreState();
}

public void setZoom(float zoom) {
	this.zoom = zoom;
	revalidate();
	repaint();
}

/**
 * @see org.eclipse.draw2dl.Figure#translateToParent(org.eclipse.draw2dl.geometry.Translatable)
 */
public void translateToParent(org.eclipse.draw2dl.geometry.Translatable t) {
	t.performScale(zoom);
	super.translateToParent(t);
}

/**
 * @see org.eclipse.draw2dl.Figure#translateFromParent(org.eclipse.draw2dl.geometry.Translatable)
 */
public void translateFromParent(Translatable t) {
	super.translateFromParent(t);
	t.performScale(1/zoom);
}

/**
 * @see Figure#useLocalCoordinates()
 */
protected boolean useLocalCoordinates() {
	return true;
}

}
