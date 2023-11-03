/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
package org.eclipse.gef.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;

/**
 * The <i>root</i> component cannot be removed from its parent. This EditPolicy
 * is typically installed on the Viewer's
 * {@link org.eclipse.gef.EditPartViewer#getContents() contents}.
 */
public class RootComponentEditPolicy extends ComponentEditPolicy {

	/**
	 * Overridden to prevent the host from being deleted.
	 *
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(GroupRequest)
	 */
	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		return UnexecutableCommand.INSTANCE;
	}

}
