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

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.flow.FlowImages;

/**
 * @author hudsonr Created on Jul 21, 2003
 */
public class StartTag extends Label {

	static final Border BORDER = new MarginBorder(2, 0, 2, 9);

	/**
	 * Creates a new StartTag
	 * 
	 * @param name
	 *            the text to display in this StartTag
	 */
	public StartTag(String name) {
		setIconTextGap(4);
		setText(name);
		setIcon(FlowImages.GEAR);
		setBorder(new MarginBorder(2, 0, 2, 9));
	}

	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getTextBounds();

		r.resize(-1, -1);
		r.expand(1, 1);
		r.width -= 1;
		r.x -= 2;
		g.drawLine(r.x, r.y, r.right(), r.y); // Top line
		g.drawLine(r.x, r.bottom(), r.right(), r.bottom()); // Bottom line
		g.drawLine(r.x, r.bottom(), r.x, r.y); // left line

		g.drawLine(r.right() + 7, r.y + r.height / 2, r.right(), r.y);
		g.drawLine(r.right() + 7, r.y + r.height / 2, r.right(), r.bottom());
	}

}
