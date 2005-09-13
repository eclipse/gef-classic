/***********************************************************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 **********************************************************************************************************************/
package org.eclipse.mylar.zest.core.internal.viewers.figures;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A simple Figure that holds other figures.
 * 
 * @author Chris Callendar
 */
public class CompartmentFigure extends Figure {

	private ToolbarLayout layout = null;
	private Dimension minSize = null;
	
	public CompartmentFigure() {
		this.layout = new ToolbarLayout();
		this.minSize = new Dimension(0, 0);

		layout.setVertical(true);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(4);
		setLayoutManager(layout);
		setBorder(new CompartmentFigureBorder());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure figure, Object constraint, int index) {
		super.add(figure, constraint, index);
		if (constraint instanceof Rectangle) {
			Rectangle rect = (Rectangle) constraint;
			minSize.expand(Math.max(0, (rect.width - minSize.width)), rect.height);
			setMinimumSize(minSize);
		}
	}
	
	public class CompartmentFigureBorder extends AbstractBorder {

		public Insets getInsets(IFigure figure) {
			return new Insets(4);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}

	}
	
}