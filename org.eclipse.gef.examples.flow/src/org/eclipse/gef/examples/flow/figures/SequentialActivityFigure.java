/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 */
public class SequentialActivityFigure extends SubgraphFigure {

	static final MarginBorder MARGIN_BORDER = new MarginBorder(0, 8, 0, 0);

	static final PointList ARROW = new PointList(3);
	{
		ARROW.addPoint(0, 0);
		ARROW.addPoint(10, 0);
		ARROW.addPoint(5, 5);
	}

	/**
	 * @param header
	 * @param footer
	 */
	public SequentialActivityFigure() {
		super(new StartTag(""), new EndTag(""));
		setBorder(MARGIN_BORDER);
		setOpaque(true);
	}

	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.setBackgroundColor(ColorConstants.button);
		Rectangle r = getBounds();
		graphics.fillRectangle(r.x + 13, r.y + 10, 8, r.height - 18);
		// graphics.fillPolygon(ARROW);
		// graphics.drawPolygon(ARROW);
	}

}
