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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.Node;

/**
 * Provides support for connections
 * @author Pratik Shah
 * @since 3.1
 */
public abstract class NodeEditPart 
	extends BaseEditPart
	implements org.eclipse.gef.NodeEditPart
{

public NodeEditPart(Node model) {
	super(model);
}

protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	// By default, can't add anything to a node
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutEditPolicy() {
		protected EditPolicy createChildEditPolicy(EditPart child) {
			return null;
		}
		protected Command getCreateCommand(CreateRequest request) {
			return null;
		}
		protected Command getDeleteDependantCommand(Request request) {
			return null;
		}
		protected Command getMoveChildrenCommand(Request request) {
			return null;
		}
	});
}

protected Node getNode() {
	return (Node)getModel();
}

protected List getModelSourceConnections() {
	return getNode().getOutgoingConnections();
}

protected List getModelTargetConnections() {
	return getNode().getIncomingConnections();
}

public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return new ChopboxAnchor(getFigure());
}

public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return new ChopboxAnchor(getFigure());
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(Node.class)) {
		case ModelPackage.NODE__LOCATION:
		case ModelPackage.NODE__WIDTH:
			refreshVisuals();
			break;
		case ModelPackage.NODE__OUTGOING_CONNECTIONS:
			refreshSourceConnections();
			break;
		case ModelPackage.NODE__INCOMING_CONNECTIONS:
			refreshTargetConnections();
			break;
	}
}

protected void refreshVisuals() {
	super.refreshVisuals();
	Rectangle constraint = new Rectangle(0, 0, -1, -1);
	constraint.setLocation(getNode().getLocation());
	constraint.width = getNode().getWidth();
	((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), constraint);
}

}