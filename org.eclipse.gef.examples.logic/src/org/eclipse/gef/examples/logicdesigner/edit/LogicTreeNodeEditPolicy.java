package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.editpolicies.NodeEditPolicy;
import org.eclipse.gef.requests.DeleteRequest;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.examples.logicdesigner.model.*;
import java.util.List;

public class LogicTreeNodeEditPolicy extends NodeEditPolicy {

protected Command getDeleteCommand(DeleteRequest delRequest) {
	CompoundCommand cc = new CompoundCommand();
	cc.setDebugLabel("delete in NodeEditPolicy");//$NON-NLS-1$
	List connections = ((LogicSubpart)getHost().getModel()).getSourceConnections();

	DeleteRequest fwdRequest = new DeleteRequest(REQ_SOURCE_DELETED);
	fwdRequest.setContributions(delRequest.getContributions());

	for (int i=0; i<connections.size(); i++) {
		ConnectionCommand command = new ConnectionCommand();
		command.setWire((Wire)connections.get(i));
		cc.add(command);
	}

	connections = ((LogicSubpart)getHost().getModel()).getTargetConnections();
	fwdRequest.setType(REQ_TARGET_DELETED);

	for (int i=0; i<connections.size(); i++) {
		ConnectionCommand command = new ConnectionCommand();
		command.setWire((Wire)connections.get(i));
		cc.add(command);
	}

	return cc.isEmpty() ? null : cc;
}

}