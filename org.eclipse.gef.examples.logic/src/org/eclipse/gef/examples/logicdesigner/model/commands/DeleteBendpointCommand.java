/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.gef.examples.logicdesigner.model.WireBendpoint;

public class DeleteBendpointCommand extends BendpointCommand {

	private WireBendpoint bendpoint;

	@Override
	public void execute() {
		bendpoint = getWire().getBendpoints().get(getIndex());
		getWire().removeBendpoint(getIndex());
		super.execute();
	}

	@Override
	public void undo() {
		super.undo();
		getWire().insertBendpoint(getIndex(), bendpoint);
	}

}
