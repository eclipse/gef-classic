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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

import org.eclipse.gef.examples.ediagram.figures.UMLClassFigure;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DataTypeEditPart 
	extends NamedElementEditPart
{

private Label header;

public DataTypeEditPart(NamedElementView model) {
	super(model);
}

protected IFigure createFigure() {
	IFigure fig = new Figure();
	fig.setLayoutManager(new ToolbarLayout());
	fig.add(new Label("<<datatype>>"));
	fig.add(header = new Label());
	return new UMLClassFigure(fig);
}

public IFigure getContentPane() {
	return ((UMLClassFigure)getFigure()).getContentPane();
}

IFigure getDirectEditFigure() {
	return header;
}

protected EDataType getEDataType() {
	return (EDataType)getView().getENamedElement();
}

protected List getModelChildren() {
	List children = new ArrayList();
	String child = getEDataType().getInstanceClassName();
	if (child == null)
		child = "null";
	children.add(child);
	return children;
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EClassifier.class)) {
		case EcorePackage.ECLASSIFIER__INSTANCE_CLASS_NAME:
			refreshChildren();
			return;
	}
	super.handlePropertyChanged(msg);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	header.setText(getEDataType().getName());
}

}