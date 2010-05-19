/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * @since 3.1
 */
public class TreeBorder extends AbstractBorder {

	private final Image image;
	private final String text;
	private Insets insets;

	public TreeBorder(Image image, String text) {
		this.image = image;
		this.text = text;
	}

	public Insets getInsets(IFigure figure) {
		if (insets == null) {
			FigureUtilities.getTextExtents(text, figure.getFont(),
					Dimension.SINGLETON);
			insets = new Insets(Math.max(16, Dimension.SINGLETON.height), 9, 0,
					0);
		}
		return insets;
	}

	public void paint(IFigure figure, Graphics g, Insets insets) {
		Rectangle where = getPaintRectangle(figure, insets);

		g.translate(where.x, where.y);

		for (int i = 16; i < where.height - 10; i += 2)
			g.drawPoint(9, i);

		g.drawImage(image, 0, 0);
		int h = FigureUtilities.getFontMetrics(g.getFont()).getHeight();
		g.drawText(text, 19, 16 - h);
	}

}
