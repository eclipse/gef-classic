/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

public class CompartmentFigure 
	extends Figure 
{

public CompartmentFigure() {
	ToolbarLayout layout = new ToolbarLayout();
	layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	layout.setStretchMinorAxis(false);
	layout.setSpacing(2);
	setLayoutManager(layout);
	setBorder(new CompoundBorder(new CompartmentFigureBorder(), new MarginBorder(1)));
}
    
public static class CompartmentFigureBorder extends AbstractBorder {
	private static final Insets INSETS = new Insets(2,0,0,1);
	public Insets getInsets(IFigure figure) {
		return INSETS;
	}
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.setForegroundColor(ColorConstants.red);
		graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
				tempRect.getTopRight());
	}
}

public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension size = super.getPreferredSize(wHint, hHint);
	size.height = Math.max(size.height, 10);
	return size;
}

}
