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

import org.eclipse.gef.examples.ediagram.figures.PackageFigure;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class PackageEditPart 
	extends NamedElementEditPart
{

public PackageEditPart(NamedElementView model) {
	super(model);
}

protected IFigure createFigure() {
	return new PackageFigure();
}

IFigure getDirectEditFigure() {
	return ((PackageFigure)getFigure()).getLabel();
}

protected void refreshVisuals() {
	super.refreshVisuals();
	((PackageFigure)getFigure()).setText(getView().getENamedElement().getName());
}

}