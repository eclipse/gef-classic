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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gef.examples.ediagram.EDiagramImages;
import org.eclipse.gef.examples.ediagram.figures.UMLClassFigure;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.commands.CreateAttributeCommand;
import org.eclipse.gef.examples.ediagram.model.commands.CreateOperationCommand;
import org.eclipse.gef.examples.ediagram.model.commands.LinkCreationCommand;
import org.eclipse.gef.examples.ediagram.model.commands.ReconnectLinkCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ClassEditPart 
	extends NamedElementEditPart
{
	
private static Font ITALICS;
private List modelChildren;
	
public ClassEditPart(NamedElementView model) {
	super(model);
}

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutEditPolicy() {
		protected EditPolicy createChildEditPolicy(EditPart child) {
			return null;
		}
		protected Command getCreateCommand(CreateRequest request) {
			if (request.getNewObject() instanceof EAttribute)
				return new CreateAttributeCommand(
						(EAttribute)request.getNewObject(), 
						((ClassEditPart)getHost()).getEClass());
			else if (request.getNewObject() instanceof EOperation)
				return new CreateOperationCommand(
						((EOperation)request.getNewObject()), 
						((ClassEditPart)getHost()).getEClass());
			return UnexecutableCommand.INSTANCE;
		}
		protected Command getDeleteDependantCommand(Request request) {
			return null;
		}
		protected Command getMoveChildrenCommand(Request request) {
			return UnexecutableCommand.INSTANCE;
		}
	});
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {
		protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
			LinkCreationCommand cmd = (LinkCreationCommand)request.getStartCommand();
			cmd.setTarget((NamedElementView)getHost().getModel());
			return cmd;
		}
		protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
			Link link = (Link)request.getNewObject();
			if (link instanceof ReferenceView || link instanceof InheritanceView) {
				Command cmd = new LinkCreationCommand((Link)request.getNewObject(),
						(NamedElementView)getHost().getModel());
				request.setStartCommand(cmd);
				return cmd;
			}
			/*
			 * The disallow cursor will be shown IFF you return null.  If you return
			 * UnexecutableCommand.INSTANCE, the disallow cursor will not appear.  This is
			 * because since this is the first step of the Command it doesn't check to 
			 * see if it's executable or not (which it most likely isn't).
			 */
			return null;
		}
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			Link link = (Link)request.getConnectionEditPart().getModel();
			if (link instanceof InheritanceView || link instanceof ReferenceView)
				return new ReconnectLinkCommand(link, (Node)getHost().getModel(), true);
			return UnexecutableCommand.INSTANCE;
		}
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			return new ReconnectLinkCommand(
					(Link)request.getConnectionEditPart().getModel(),
					(Node)getHost().getModel(), false);
		}
	});
}

protected IFigure createFigure() {
	return new UMLClassFigure(new Label());
}

public IFigure getContentPane() {
	return ((UMLClassFigure)getFigure()).getContentPane();
}

IFigure getDirectEditFigure() {
	return ((UMLClassFigure)getFigure()).getHeader();
}

private EClass getEClass() {
	return (EClass)getView().getENamedElement();
}

protected List getModelChildren() {
	if (modelChildren == null) {
		modelChildren = new ArrayList();
		modelChildren.add(new PlaceHolderModel(getEClass(), true));
		modelChildren.add(new PlaceHolderModel(getEClass(), false));
	}
	return modelChildren;
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EClass.class)) {
		case EcorePackage.ECLASS__ESTRUCTURAL_FEATURES:
			((EditPart)getChildren().get(0)).refresh();
			return;
		case EcorePackage.ECLASS__EOPERATIONS:
			((EditPart)getChildren().get(1)).refresh();
			return;
		case EcorePackage.ECLASS__INTERFACE:
		case EcorePackage.ECLASS__ABSTRACT:
			refreshVisuals();
	}
	super.handlePropertyChanged(msg);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	Label header = (Label)((UMLClassFigure)getFigure()).getHeader();
	header.setText(getEClass().getName());
	header.setIcon(EDiagramImages.getImage(
			getEClass().isInterface() ? EDiagramImages.INTERFACE : EDiagramImages.CLASS));
	if (getEClass().isAbstract()) {
		if (ITALICS == null) {
			FontData[] fontDatas = header.getFont().getFontData();
			for (int i = 0; i < fontDatas.length; i++) {
				fontDatas[i].setStyle(fontDatas[i].getStyle() | SWT.ITALIC);
			}
			ITALICS = new Font(null, fontDatas);
		}
		header.setFont(ITALICS);
		getFigure().setBackgroundColor(ColorConstants.lightGreen);
	} else {
		header.setFont(null);
		getFigure().setBackgroundColor(UMLClassFigure.CLASS_COLOR);
	}
}

// can't use the previous solution of lists for children because two empty lists are
// considered equal (and hence the same child).
public static final class PlaceHolderModel {
	private EClass theClass;
	private boolean attributes;
	public PlaceHolderModel(EClass theClass, boolean attributes) {
		this.theClass = theClass;
		this.attributes = attributes;
	}
	public List getChildren() {
		if (attributes)
			return theClass.getEAttributes();
		return theClass.getEOperations();
	}
}

}
