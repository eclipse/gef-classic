/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.zoom;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

/**
 * A simple Figure that represents an 'Attributes' or 'Methods' compartment in a
 * UML Class Diagram.
 */
public class CompartmentFigure extends Figure {

	public CompartmentFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(OrderedLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBorder(new CompartmentFigureBorder());
	}

	public static class CompartmentFigureBorder extends AbstractBorder {
		@Override
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}

	}
}