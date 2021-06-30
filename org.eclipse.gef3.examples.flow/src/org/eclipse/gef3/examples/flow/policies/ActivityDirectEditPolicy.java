/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.policies;

import org.eclipse.draw2dl.Label;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.editpolicies.DirectEditPolicy;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.commands.RenameActivityCommand;
import org.eclipse.gef3.requests.DirectEditRequest;

/**
 * EditPolicy for the direct editing of Activity names.
 * 
 * @author Daniel Lee
 */
public class ActivityDirectEditPolicy extends DirectEditPolicy {

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest request) {
		RenameActivityCommand cmd = new RenameActivityCommand();
		cmd.setSource((Activity) getHost().getModel());
		cmd.setOldName(((Activity) getHost().getModel()).getName());
		cmd.setName((String) request.getCellEditor().getValue());
		return cmd;
	}

	/**
	 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((Label) getHostFigure()).setText(value);
		// hack to prevent async layout from placing the cell editor twice.
		// getHostFigure().getUpdateManager().performUpdate();
	}

}
