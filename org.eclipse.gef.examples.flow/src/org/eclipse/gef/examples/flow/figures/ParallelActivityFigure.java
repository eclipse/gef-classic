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
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author hudsonr
 */
public class ParallelActivityFigure extends SubgraphFigure {

	boolean selected;

	/**
	 * @param header
	 * @param footer
	 */
	public ParallelActivityFigure() {
		super(new Label(""), new Label(""));
		setBorder(new MarginBorder(3, 5, 3, 0));
		setOpaque(true);
	}

	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getBounds();
		g.setBackgroundColor(ColorConstants.button);
		if (selected) {
			g.setBackgroundColor(ColorConstants.menuBackgroundSelected);
			g.setForegroundColor(ColorConstants.menuForegroundSelected);
		}

		g.fillRectangle(r.x, r.y, 3, r.height);
		g.fillRectangle(r.right() - 3, r.y, 3, r.height);
		g.fillRectangle(r.x, r.bottom() - 18, r.width, 18);
		g.fillRectangle(r.x, r.y, r.width, 18);
	}

	public void setSelected(boolean selected) {
		if (this.selected == selected)
			return;
		this.selected = selected;
		if (!selected) {
			getHeader().setForegroundColor(null);
			getFooter().setForegroundColor(null);
		} else {
			getHeader().setForegroundColor(
					ColorConstants.menuForegroundSelected);
			getFooter().setForegroundColor(
					ColorConstants.menuForegroundSelected);
		}

		repaint();
	}

}
