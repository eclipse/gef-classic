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

import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.requests.GroupRequest;

import org.eclipse.gef3.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef3.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef3.examples.logicdesigner.model.commands.DeleteCommand;

public class LogicElementEditPolicy extends
		org.eclipse.gef3.editpolicies.ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest request) {
		Object parent = getHost().getParent().getModel();
		DeleteCommand deleteCmd = new DeleteCommand();
		deleteCmd.setParent((LogicDiagram) parent);
		deleteCmd.setChild((LogicSubpart) getHost().getModel());
		return deleteCmd;
	}

}
