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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.commands.ChangeNameCommand;
import org.eclipse.gef.examples.ediagram.model.commands.LinkCreationCommand;
import org.eclipse.gef.examples.ediagram.model.commands.ReconnectLinkCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public abstract class NamedElementEditPart 
	extends NodeEditPart
{

public NamedElementEditPart(NamedElementView model) {
	super(model);
}

public void activate() {
	super.activate();
	getView().getENamedElement().eAdapters().add(modelListener);
}

protected DirectEditPolicy createDirectEditPolicy() {
	return new DirectEditPolicy() {
		protected Command getDirectEditCommand(DirectEditRequest request) {
			return new ChangeNameCommand(getView().getENamedElement(), 
					(String)request.getCellEditor().getValue());
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
			IFigure fig = getDirectEditFigure();
			if (fig instanceof Label) {
				((Label)fig).setText((String)request.getCellEditor().getValue());
				fig.getUpdateManager().performUpdate();
			}
		}
	};
}

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {
		protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
			Link link = (Link)request.getNewObject();
			if (link instanceof ReferenceView || link instanceof InheritanceView)
				return null;
			LinkCreationCommand cmd = (LinkCreationCommand)request.getStartCommand();
			cmd.setTarget((Node)getHost().getModel());
			return cmd;
		}
		protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
			return null;
		}
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			return UnexecutableCommand.INSTANCE;
		}
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			Link link = (Link)request.getConnectionEditPart().getModel();
			if (link instanceof ReferenceView || link instanceof InheritanceView)
				return UnexecutableCommand.INSTANCE;
			return new ReconnectLinkCommand(link, (Node)getHost().getModel(), false);
		}
	});
}

public void deactivate() {
	getView().getENamedElement().eAdapters().remove(modelListener);
	super.deactivate();
}

protected NamedElementView getView() {
	return (NamedElementView)getModel();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(ENamedElement.class)) {
		case EcorePackage.ENAMED_ELEMENT__NAME:
			refreshVisuals();
			return;
	}
	super.handlePropertyChanged(msg);
}

}