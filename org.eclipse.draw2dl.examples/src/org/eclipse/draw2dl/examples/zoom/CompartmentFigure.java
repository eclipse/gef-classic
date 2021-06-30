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
import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.geometry.Insets;


/**
 * A simple Figure that represents an 'Attributes' or 'Methods' compartment in a UML
 * Class Diagram.
 */
public class CompartmentFigure extends Figure {

public CompartmentFigure() {
	org.eclipse.draw2dl.ToolbarLayout layout = new org.eclipse.draw2dl.ToolbarLayout();
	layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	layout.setStretchMinorAxis(false);
	layout.setSpacing(2);
	setLayoutManager(layout);
	setBorder(new CompartmentFigureBorder());
}
	public class CompartmentFigureBorder extends AbstractBorder {
		public org.eclipse.draw2dl.geometry.Insets getInsets(org.eclipse.draw2dl.IFigure figure) {
			return new org.eclipse.draw2dl.geometry.Insets(1,0,0,0);
		}
		
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), 
								tempRect.getTopRight());
		}

	}
}