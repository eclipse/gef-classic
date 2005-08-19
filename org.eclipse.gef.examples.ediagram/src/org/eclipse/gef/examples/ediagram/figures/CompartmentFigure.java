/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.figures;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

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
    
public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension size = super.getPreferredSize(wHint, hHint);
	size.height = Math.max(size.height, 10);
	return size;
}

}
