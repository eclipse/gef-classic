/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class LEDEditPolicy extends LogicElementEditPolicy {

	private static final String INCREMENT_REQUEST = "Increment", //$NON-NLS-1$
			DECREMENT_REQUEST = "Decrement"; //$NON-NLS-1$

	public Command getCommand(Request request) {
		if (INCREMENT_REQUEST.equals(request.getType()))
			return getIncrementDecrementCommand(true);
		if (DECREMENT_REQUEST.equals(request.getType()))
			return getIncrementDecrementCommand(false);
		return super.getCommand(request);
	}

	protected Command getIncrementDecrementCommand(boolean type) {
		IncrementDecrementCommand command = new IncrementDecrementCommand(type);
		command.setChild((LogicSubpart) getHost().getModel());
		return command;
	}

	static class IncrementDecrementCommand extends
			org.eclipse.gef.commands.Command {

		boolean isIncrement = true;
		LED child = null;

		public IncrementDecrementCommand(boolean increment) {
			super(LogicMessages.IncrementDecrementCommand_LabelText);
			isIncrement = increment;
		}

		public void setChild(LogicSubpart child) {
			this.child = (LED) child;
		}

		public void execute() {
			int value = child.getValue();
			if (isIncrement) {
				if (value == 15)
					value = -1;
				child.setValue(value + 1);
			} else {
				if (value == 0)
					value = 16;
				child.setValue(value - 1);
			}
		}

		public void undo() {
			isIncrement = !isIncrement;
			execute();
			isIncrement = !isIncrement;
		}

		public void redo() {
			execute();
		}
	}

}
