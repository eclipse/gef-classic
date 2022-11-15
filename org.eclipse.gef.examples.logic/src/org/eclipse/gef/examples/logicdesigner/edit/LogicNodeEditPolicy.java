/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gef.examples.logicdesigner.figures.FigureFactory;
import org.eclipse.gef.examples.logicdesigner.figures.NodeFigure;
import org.eclipse.gef.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.commands.ConnectionCommand;

public class LogicNodeEditPolicy extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {

	@Override
	protected Connection createDummyConnection(Request req) {
		return FigureFactory.createNewWire(null);
	}

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCommand command = (ConnectionCommand) request.getStartCommand();
		command.setTarget(getLogicSubpart());
		ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(request);
		if (ctor == null)
			return null;
		command.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCommand command = new ConnectionCommand();
		command.setWire(new Wire());
		command.setSource(getLogicSubpart());
		ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(request);
		command.setSourceTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
		request.setStartCommand(command);
		return command;
	}

	/**
	 * Feedback should be added to the scaled feedback layer.
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalEditPolicy#getFeedbackLayer()
	 */
	@Override
	protected IFigure getFeedbackLayer() {
		/*
		 * Fix for Bug# 66590 Feedback needs to be added to the scaled feedback layer
		 */
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	protected LogicEditPart getLogicEditPart() {
		return (LogicEditPart) getHost();
	}

	protected LogicSubpart getLogicSubpart() {
		return (LogicSubpart) getHost().getModel();
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		if (getLogicSubpart() instanceof LiveOutput || getLogicSubpart() instanceof GroundOutput)
			return null;

		ConnectionCommand cmd = new ConnectionCommand();
		cmd.setWire((Wire) request.getConnectionEditPart().getModel());

		ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(request);
		cmd.setTarget(getLogicSubpart());
		cmd.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ConnectionCommand cmd = new ConnectionCommand();
		cmd.setWire((Wire) request.getConnectionEditPart().getModel());

		ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(request);
		cmd.setSource(getLogicSubpart());
		cmd.setSourceTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
		return cmd;
	}

	protected NodeFigure getNodeFigure() {
		return (NodeFigure) ((GraphicalEditPart) getHost()).getFigure();
	}

}
