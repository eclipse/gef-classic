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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
import org.eclipse.gef.examples.logicdesigner.model.commands.LogicLabelCommand;

public class LogicLabelEditPolicy extends LogicElementEditPolicy {

	@Override
	public Command getCommand(Request request) {
		if (NativeDropRequest.ID.equals(request.getType()))
			return getDropTextCommand((NativeDropRequest) request);
		return super.getCommand(request);
	}

	protected Command getDropTextCommand(NativeDropRequest request) {
		return new LogicLabelCommand((LogicLabel) getHost().getModel(), (String) request.getData());
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (NativeDropRequest.ID.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

}
