/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef3.examples.digraph1.policy;

import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef3.requests.CreateRequest;

/**
 * An XYLayoutEditPolicy for the Directed Graph Example Editor.
 * 
 * @author Anthony Hunter
 */
public class Digraph1XYLayoutEditPolicy extends XYLayoutEditPolicy {

	/*
	 * @see org.eclipse.gef3.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef3.EditPart,
	 *      java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return null;
	}

	/*
	 * @see org.eclipse.gef3.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef3.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

}
