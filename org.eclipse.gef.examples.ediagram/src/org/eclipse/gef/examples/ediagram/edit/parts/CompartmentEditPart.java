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
package org.eclipse.gef.examples.ediagram.edit.parts;

import java.util.List;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import org.eclipse.gef.examples.ediagram.edit.parts.ClassEditPart.PlaceHolderModel;
import org.eclipse.gef.examples.ediagram.figures.CompartmentFigure;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class CompartmentEditPart 
	extends AbstractGraphicalEditPart
{
	
public CompartmentEditPart(PlaceHolderModel model) {
	super();
	setModel(model);
}

protected void createEditPolicies() {
}

protected IFigure createFigure() {
	return new CompartmentFigure();
}

protected List getModelChildren() {
	return ((PlaceHolderModel)getModel()).getChildren();
}

public boolean isSelectable() {
	return false;
}

}
