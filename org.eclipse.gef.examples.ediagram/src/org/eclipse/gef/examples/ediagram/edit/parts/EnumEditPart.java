/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.ediagram.figures.UMLClassFigure;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.commands.CreateEnumLiteralCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EnumEditPart 
	extends NamedElementEditPart
{

private Label header;
	
public EnumEditPart(NamedElementView model) {
	super(model);
}

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutEditPolicy() {
		protected EditPolicy createChildEditPolicy(EditPart child) {
			return null;
		}
		protected Command getCreateCommand(CreateRequest request) {
			if (request.getNewObject() instanceof EEnumLiteral)
				return new CreateEnumLiteralCommand(
						(EEnumLiteral)request.getNewObject(), 
						((EnumEditPart)getHost()).getEEnum());
			return UnexecutableCommand.INSTANCE;
		}
		protected Command getDeleteDependantCommand(Request request) {
			return null;
		}
		protected Command getMoveChildrenCommand(Request request) {
			return UnexecutableCommand.INSTANCE;
		}
	});
}

protected IFigure createFigure() {
	IFigure fig = new Figure();
	fig.setLayoutManager(new ToolbarLayout());
	fig.add(new Label("<<enumeration>>"));
	fig.add(header = new Label());
	return new UMLClassFigure(fig);
}

public IFigure getContentPane() {
	return ((UMLClassFigure)getFigure()).getContentPane();
}

IFigure getDirectEditFigure() {
	return header;
}

protected EEnum getEEnum() {
	return (EEnum)getView().getENamedElement();
}

protected List getModelChildren() {
	List children = new ArrayList();
	children.add(getEEnum().getELiterals());
	return children;
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EEnum.class)) {
		case EcorePackage.EENUM__ELITERALS:
			((EditPart)getChildren().get(0)).refresh();
			return;
	}
	super.handlePropertyChanged(msg);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	header.setText(getEEnum().getName());
}

}
