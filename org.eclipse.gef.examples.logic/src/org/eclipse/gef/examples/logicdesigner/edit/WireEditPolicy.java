/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.commands.ConnectionCommand;

public class WireEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionCommand c = new ConnectionCommand();
		c.setWire((Wire) getHost().getModel());
		return c;
	}

}
