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
package org.eclipse.gef.examples.flow.policies;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.model.Transition;
import org.eclipse.gef.examples.flow.model.commands.DeleteConnectionCommand;
import org.eclipse.gef.examples.flow.model.commands.SplitTransitionCommand;
import org.eclipse.gef.examples.flow.parts.TransitionPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

/**
 * EditPolicy for Transitions. Supports deletion and "splitting", i.e. adding an
 * Activity that splits the transition into an incoming and outgoing connection
 * to the new Activity.
 * 
 * @author Daniel Lee
 */
public class TransitionEditPolicy extends ConnectionEditPolicy {

	/**
	 * @see org.eclipse.gef.editpolicies.ConnectionEditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			return getSplitTransitionCommand(request);
		return super.getCommand(request);
	}

	private PolylineConnection getConnectionFigure() {
		return ((PolylineConnection) ((TransitionPart) getHost()).getFigure());
	}

	/**
	 * @see ConnectionEditPolicy#getDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command getDeleteCommand(GroupRequest request) {
		DeleteConnectionCommand cmd = new DeleteConnectionCommand();
		Transition t = (Transition) getHost().getModel();
		cmd.setTransition(t);
		cmd.setSource(t.source);
		cmd.setTarget(t.target);
		return cmd;
	}

	protected Command getSplitTransitionCommand(Request request) {
		SplitTransitionCommand cmd = new SplitTransitionCommand();
		cmd.setTransition(((Transition) getHost().getModel()));
		cmd.setParent(((StructuredActivity) ((TransitionPart) getHost())
				.getSource().getParent().getModel()));
		cmd.setNewActivity(((Activity) ((CreateRequest) request).getNewObject()));
		return cmd;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			return getHost();
		return null;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			getConnectionFigure().setLineWidth(1);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			getConnectionFigure().setLineWidth(2);
	}

}
