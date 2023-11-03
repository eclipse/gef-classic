/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
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
