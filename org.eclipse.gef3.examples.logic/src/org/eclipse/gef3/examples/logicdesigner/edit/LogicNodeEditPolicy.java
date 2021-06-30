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

import org.eclipse.draw2dl.Connection;
import org.eclipse.draw2dl.ConnectionAnchor;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PolylineConnection;

import org.eclipse.gef3.GraphicalEditPart;
import org.eclipse.gef3.LayerConstants;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.requests.CreateConnectionRequest;
import org.eclipse.gef3.requests.ReconnectRequest;

import org.eclipse.gef3.examples.logicdesigner.figures.FigureFactory;
import org.eclipse.gef3.examples.logicdesigner.figures.NodeFigure;
import org.eclipse.gef3.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef3.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef3.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef3.examples.logicdesigner.model.Wire;
import org.eclipse.gef3.examples.logicdesigner.model.commands.ConnectionCommand;

public class LogicNodeEditPolicy extends
		org.eclipse.gef3.editpolicies.GraphicalNodeEditPolicy {

	protected Connection createDummyConnection(Request req) {
		PolylineConnection conn = FigureFactory.createNewWire(null);
		return conn;
	}

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		ConnectionCommand command = (ConnectionCommand) request
				.getStartCommand();
		command.setTarget(getLogicSubpart());
		ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(
				request);
		if (ctor == null)
			return null;
		command.setTargetTerminal(getLogicEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		return command;
	}

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		ConnectionCommand command = new ConnectionCommand();
		command.setWire(new Wire());
		command.setSource(getLogicSubpart());
		ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(
				request);
		command.setSourceTerminal(getLogicEditPart()
				.mapConnectionAnchorToTerminal(ctor));
		request.setStartCommand(command);
		return command;
	}

	/**
	 * Feedback should be added to the scaled feedback layer.
	 * 
	 * @see org.eclipse.gef3.editpolicies.GraphicalEditPolicy#getFeedbackLayer()
	 */
	protected IFigure getFeedbackLayer() {
		/*
		 * Fix for Bug# 66590 Feedback needs to be added to the scaled feedback
		 * layer
		 */
		return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
	}

	protected LogicEditPart getLogicEditPart() {
		return (LogicEditPart) getHost();
	}

	protected LogicSubpart getLogicSubpart() {
		return (LogicSubpart) getHost().getModel();
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		if (getLogicSubpart() instanceof LiveOutput
				|| getLogicSubpart() instanceof GroundOutput)
			return null;

		ConnectionCommand cmd = new ConnectionCommand();
		cmd.setWire((Wire) request.getConnectionEditPart().getModel());

		ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(
				request);
		cmd.setTarget(getLogicSubpart());
		cmd.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(
				ctor));
		return cmd;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ConnectionCommand cmd = new ConnectionCommand();
		cmd.setWire((Wire) request.getConnectionEditPart().getModel());

		ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(
				request);
		cmd.setSource(getLogicSubpart());
		cmd.setSourceTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(
				ctor));
		return cmd;
	}

	protected NodeFigure getNodeFigure() {
		return (NodeFigure) ((GraphicalEditPart) getHost()).getFigure();
	}

}
