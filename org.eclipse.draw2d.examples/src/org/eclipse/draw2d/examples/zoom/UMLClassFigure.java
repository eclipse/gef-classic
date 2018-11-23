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
package org.eclipse.draw2d.examples.zoom;
import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;

/**
 * A simple Figure that represents a UML Class Diagram.
 */
public class UMLClassFigure extends Figure {

/** Background color of UMLFigure */
public static Color classColor = new Color(null,255,255,206);

/** CompartmentFigures */
private CompartmentFigure attributeFigure = new CompartmentFigure();
private CompartmentFigure methodFigure = new CompartmentFigure();

public UMLClassFigure(Label name) {
	ToolbarLayout layout = new ToolbarLayout();
	setLayoutManager(layout);	
	setBorder(new LineBorder(ColorConstants.black,1));
	setBackgroundColor(classColor);
	setOpaque(true);
	
	add(name);	
	add(attributeFigure);
	add(methodFigure);
}

/**
 * Returns the "attributes" figure
 */
public CompartmentFigure getAttributesCompartment() {
	return attributeFigure;
}

/**
 * Returns the "methods" figure
 */
public CompartmentFigure getMethodsCompartment() {
	return methodFigure;
}

}