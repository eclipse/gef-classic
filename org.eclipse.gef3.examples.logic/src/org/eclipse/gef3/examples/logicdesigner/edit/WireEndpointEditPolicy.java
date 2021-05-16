/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.logicdesigner.edit;

import org.eclipse.draw2dl.PolylineConnection;

import org.eclipse.gef3.GraphicalEditPart;

public class WireEndpointEditPolicy extends
		org.eclipse.gef3.editpolicies.ConnectionEndpointEditPolicy {

	protected void addSelectionHandles() {
		super.addSelectionHandles();
		getConnectionFigure().setLineWidth(2);
	}

	protected PolylineConnection getConnectionFigure() {
		return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
	}

	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
		getConnectionFigure().setLineWidth(0);
	}

}
