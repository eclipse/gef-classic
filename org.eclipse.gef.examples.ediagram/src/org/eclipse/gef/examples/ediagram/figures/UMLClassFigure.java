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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;

/**
 * A simple Figure that represents a UML Class Diagram.
 * @author Dan Lee
 */
public class UMLClassFigure 
	extends Figure 
{
	
/** Background color of UMLFigure */
public static final Color CLASS_COLOR = new Color(null,255,255,206);

private IFigure body;
private IFigure header;

public UMLClassFigure(IFigure header) {
	this.header = header;
	ToolbarLayout layout = new ToolbarLayout();
	setLayoutManager(layout);	
	setBorder(new CompoundBorder(new LineBorder(ColorConstants.red,1), new MarginBorder(1)));
	setBackgroundColor(CLASS_COLOR);
	setOpaque(true);
	add(header);
	body = new Figure();
	body.setLayoutManager(new ToolbarLayout());
	add(body);
}

public IFigure getContentPane() {
	return body;
}

public IFigure getHeader() {
	return header;
}

}