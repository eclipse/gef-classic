package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.figures.NodeFigure;
import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class LogicNodeEditPolicy
	extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
{

protected Connection createDummyConnection(Request req) {
	PolylineConnection conn = FigureFactory.createNewWire(null);
	return conn;
}

protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {	
	ConnectionCommand command = (ConnectionCommand)request.getStartCommand();
	command.setTarget(getLogicSubpart());
	ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(request);
	if (ctor == null)
		return null;
	command.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
	return command;
}

protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
	ConnectionCommand command = new ConnectionCommand();
	command.setWire(new Wire());
	command.setSource(getLogicSubpart());
	ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(request);
	command.setSourceTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
	request.setStartCommand(command);
	return command;
}

protected LogicEditPart getLogicEditPart() {
	return (LogicEditPart) getHost();
}

protected LogicSubpart getLogicSubpart() {
	return (LogicSubpart) getHost().getModel();
}

protected Command getReconnectTargetCommand(ReconnectRequest request) {
	if (getLogicSubpart() instanceof LiveOutput || 
		getLogicSubpart() instanceof GroundOutput)
			return null;
	
	ConnectionCommand cmd = new ConnectionCommand();
	cmd.setWire((Wire)request.getConnectionEditPart().getModel());

	ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(request);
	cmd.setTarget(getLogicSubpart());
	cmd.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
	return cmd;
}

protected Command getReconnectSourceCommand(ReconnectRequest request) {
	ConnectionCommand cmd = new ConnectionCommand();
	cmd.setWire((Wire)request.getConnectionEditPart().getModel());

	ConnectionAnchor ctor = getLogicEditPart().getSourceConnectionAnchor(request);
	cmd.setSource(getLogicSubpart());
	cmd.setSourceTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
	return cmd;
}

protected NodeFigure getNodeFigure() {
	return (NodeFigure)((GraphicalEditPart)getHost()).getFigure();
}

}