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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.ediagram.edit.policies.LinkBendpointEditPolicy;
import org.eclipse.gef.examples.ediagram.edit.policies.LinkEndpointEditPolicy;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.commands.DeleteCommand;
import org.eclipse.gef.examples.ediagram.model.commands.DeleteLinkCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class LinkEditPart 
	extends AbstractConnectionEditPart
{
	
protected Adapter modelListener = new AdapterImpl() {
	public void notifyChanged(Notification msg) {
		handlePropertyChanged(msg);
	}
};

public LinkEditPart(Link conn) {
	super();
	setModel(conn);
}

public void activate() {
	super.activate();
	getLink().eAdapters().add(modelListener);
}

protected void createEditPolicies() {
	installEditPolicy(
			EditPolicy.CONNECTION_ENDPOINTS_ROLE, new LinkEndpointEditPolicy());
	installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
		protected Command getDeleteCommand(GroupRequest request) {
			Boolean bool = (Boolean)request.getExtendedData()
					.get(DeleteCommand.KEY_PERM_DELETE);
			boolean permDelete = bool == null ? false : bool.booleanValue();
			return new DeleteLinkCommand((Link)getHost().getModel(), permDelete);
		}
	});
	installEditPolicy(
			EditPolicy.CONNECTION_BENDPOINTS_ROLE, new LinkBendpointEditPolicy());
}

protected IFigure createFigure() {
	PolylineConnection conn = new PolylineConnection();
	conn.setLineStyle(Graphics.LINE_DASHDOT);
	return conn;
}

public void deactivate() {
	getLink().eAdapters().remove(modelListener);
	super.deactivate();
}

protected Link getLink() {
	return (Link)getModel();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(Link.class)) {
		case ModelPackage.LINK__BENDPOINTS:
			refreshVisuals();
			break;
//		case ModelPackage.LINK__SOURCE:
//			refreshSourceAnchor();
//			break;
//		case ModelPackage.LINK__TARGET:
//			refreshTargetAnchor();
//			break;
	}
}

protected void refreshVisuals() {
	getConnectionFigure().setRoutingConstraint(getLink().getBendpoints());
}

}
