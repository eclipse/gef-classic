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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gef.examples.ediagram.figures.StickyNoteFigure;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.StickyNote;
import org.eclipse.gef.examples.ediagram.model.commands.ChangeTextCommand;
import org.eclipse.gef.examples.ediagram.model.commands.LinkCreationCommand;
import org.eclipse.gef.examples.ediagram.model.commands.ReconnectLinkCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class StickyNoteEditPart 
	extends NodeEditPart
{

public StickyNoteEditPart(StickyNote model) {
	super(model);
}

protected DirectEditPolicy createDirectEditPolicy() {
	return new DirectEditPolicy() {
		protected Command getDirectEditCommand(DirectEditRequest request) {
			return new ChangeTextCommand((StickyNote)getModel(), 
					(String)request.getCellEditor().getValue());
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
			((StickyNoteFigure)getFigure()).setText(
					(String)request.getCellEditor().getValue());
			getFigure().getUpdateManager().performUpdate();
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
			Link link = (Link)request.getNewObject();
			if (link instanceof ReferenceView || link instanceof InheritanceView)
				return null;
			Command cmd = new LinkCreationCommand((Link)request.getNewObject(),
					(Node)getHost().getModel());
			request.setStartCommand(cmd);
			return cmd;
		}
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			Link link = (Link)request.getConnectionEditPart().getModel();
			if (link instanceof ReferenceView || link instanceof InheritanceView)
				return UnexecutableCommand.INSTANCE;
			return new ReconnectLinkCommand(link, (Node)getHost().getModel(), true);
		}
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			Link link = (Link)request.getConnectionEditPart().getModel();
			if (link instanceof ReferenceView || link instanceof InheritanceView)
				return UnexecutableCommand.INSTANCE;
			return new ReconnectLinkCommand(link, (Node)getHost().getModel(), false);
		}
	});
}

protected IFigure createFigure() {
	return new StickyNoteFigure();
}

String getDirectEditText() {
	return ((StickyNoteFigure)getFigure()).getText();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(StickyNote.class)) {
		case ModelPackage.STICKY_NOTE__TEXT:
			refreshVisuals();
			return;
	}
	super.handlePropertyChanged(msg);
}

protected void refreshVisuals() {
	super.refreshVisuals();
	((StickyNoteFigure)getFigure()).setText(((StickyNote)getModel()).getText());
}

}
