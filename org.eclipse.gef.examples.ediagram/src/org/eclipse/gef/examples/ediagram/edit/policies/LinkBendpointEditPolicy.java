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
package org.eclipse.gef.examples.ediagram.edit.policies;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.commands.CreateBendpointCommand;
import org.eclipse.gef.examples.ediagram.model.commands.DeleteBendpointCommand;
import org.eclipse.gef.examples.ediagram.model.commands.MoveBendpointCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class LinkBendpointEditPolicy 
	extends BendpointEditPolicy
{

protected Command getCreateBendpointCommand(BendpointRequest request) {
	Point loc = request.getLocation();
	getConnection().translateToRelative(loc);
	return new CreateBendpointCommand((Link)request.getSource().getModel(), 
			loc, request.getIndex());
}

protected Command getDeleteBendpointCommand(BendpointRequest request) {
	return new DeleteBendpointCommand((Link)getHost().getModel(), request.getIndex());
}

protected Command getMoveBendpointCommand(BendpointRequest request) {
	Point loc = request.getLocation();
	getConnection().translateToRelative(loc);
	return new MoveBendpointCommand((Link)request.getSource().getModel(),
			loc, request.getIndex());
}

}
