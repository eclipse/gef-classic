/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.figures;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.handles.HandleBounds;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class PackageFigure 
	extends Figure
	implements HandleBounds
{

private static Color
	brown      = new Color(null, 205, 133,  63),
	darkBrown  = new Color(null, 139,  69,  19),
	lightBrown = new Color(null, 244, 164,  96);
protected Box fPackage;
protected SelectableLabel label;

public PackageFigure() {
	ToolbarLayout layout = new ToolbarLayout();
	layout.setStretchMinorAxis(false);
	layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
	layout.setSpacing(2);
	setLayoutManager(layout);
	add(fPackage = new Box());
	add(label = new SelectableLabel());
	label.setBorder(new MarginBorder(0, 1, 0, 0));
}

public Rectangle getHandleBounds() {
	return fPackage.getBounds();
}

public SelectableLabel getLabel() {
	return label;
}

public void setText(String s) {
	label.setText(s);
}

protected static class Box extends Figure {
	public Box() {
		setSize(51, 51);
		setBackgroundColor(lightBrown);
		setForegroundColor(darkBrown);
		setOpaque(true);
		setBorder(new LineBorder(brown));
	}
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.drawLine(bounds.getTop(), bounds.getBottom());
		graphics.drawLine(bounds.getLeft(), bounds.getRight());
	}
}

}