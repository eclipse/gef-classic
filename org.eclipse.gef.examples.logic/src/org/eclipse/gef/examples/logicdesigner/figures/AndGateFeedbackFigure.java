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
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

public class AndGateFeedbackFigure extends AndGateFigure {

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		g.setXORMode(true);
		g.setForegroundColor(ColorConstants.white);
		g.setBackgroundColor(LogicColorConstants.ghostFillColor);

		Rectangle r = getBounds().getCopy();
		r.translate(2, 2);
		r.setSize(11, 9);

		// Draw terminals, 2 at top
		g.drawLine(r.x + 2, r.y, r.x + 2, r.y - 2);
		g.drawLine(r.right() - 3, r.y, r.right() - 3, r.y - 2);
		g.drawPoint(r.x + 2, r.y);
		g.drawPoint(r.right() - 3, r.y);

		// outline main area
		g.drawLine(r.x, r.y, r.right() - 1, r.y);
		g.drawLine(r.right() - 1, r.y, r.right() - 1, r.bottom() - 1);
		g.drawLine(r.x, r.y, r.x, r.bottom() - 1);

		g.drawPoint(r.x, r.y);
		g.drawPoint(r.right() - 1, r.y);

		g.fillRectangle(r);

		// draw and outline the arc
		r.height = 9;
		r.y += 4;
		g.setForegroundColor(LogicColorConstants.ghostFillColor);
		g.drawLine(r.x, r.y + 4, r.x + 10, r.y + 4);
		g.setForegroundColor(ColorConstants.white);

		g.drawPoint(r.x, r.y + 4);
		g.fillArc(r, 180, 180);

		r.width--;
		r.height--;

		g.drawArc(r, 180, 180);
		g.drawLine(r.x + r.width / 2, r.bottom(), r.x + r.width / 2,
				r.bottom() + 2);

		g.drawPoint(r.x + r.width / 2, r.bottom());
	}

}
