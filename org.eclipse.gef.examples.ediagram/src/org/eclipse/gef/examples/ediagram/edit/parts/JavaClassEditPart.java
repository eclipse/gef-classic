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
package org.eclipse.gef.examples.ediagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.ediagram.figures.CompartmentFigure.CompartmentFigureBorder;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class JavaClassEditPart 
	extends AbstractGraphicalEditPart
{

public JavaClassEditPart(Object className) {
	super();
	setModel(className);
}

protected void createEditPolicies() {
}

protected IFigure createFigure() {
	Label label = new Label();
	label.setBorder(new CompartmentFigureBorder());
	return label;
}

public boolean isSelectable() {
	return false;
}

protected void refreshVisuals() {
	((Label)getFigure()).setText("<<javaclass>> " + getModel());
}

}