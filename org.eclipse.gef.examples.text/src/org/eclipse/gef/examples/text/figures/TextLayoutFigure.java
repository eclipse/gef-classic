/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.figures;

import org.eclipse.swt.graphics.TextLayout;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @since 3.1
 */
public class TextLayoutFigure extends Figure {

TextLayout tl;

private int beginOffset = -1;

private int endOffset = -1;

/**
 * @since 3.1
 */
public TextLayoutFigure(TextLayout layout) {
	tl = layout;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics graphics) {
	Rectangle client = getClientArea(Rectangle.SINGLETON);
	if (client.width == 0) return;
	tl.setWidth(client.width);
	graphics.drawTextLayout(tl, client.x, client.y, beginOffset, endOffset, null, null);
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	wHint = Math.max(1, wHint - getInsets().getWidth());
	tl.setWidth(wHint);
	Dimension d = new Dimension(tl.getBounds().width, tl.getBounds().height);
	Insets insets = getInsets();
	d.expand(insets.getWidth(), insets.getHeight());
	return d;
}

public void setSelectionRange(int beginOffset, int endOffset) {
	this.beginOffset = beginOffset;
	this.endOffset = endOffset;
	repaint();
}

}