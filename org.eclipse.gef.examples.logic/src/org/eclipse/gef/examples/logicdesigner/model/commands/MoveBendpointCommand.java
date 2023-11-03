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
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.gef.examples.logicdesigner.model.WireBendpoint;

public class MoveBendpointCommand extends BendpointCommand {

	private WireBendpoint oldBendpoint;

	@Override
	public void execute() {
		WireBendpoint bp = new WireBendpoint();
		bp.setRelativeDimensions(getFirstRelativeDimension(), getSecondRelativeDimension());
		setOldBendpoint(getWire().getBendpoints().get(getIndex()));
		getWire().setBendpoint(getIndex(), bp);
		super.execute();
	}

	protected WireBendpoint getOldBendpoint() {
		return oldBendpoint;
	}

	public void setOldBendpoint(WireBendpoint bp) {
		oldBendpoint = bp;
	}

	@Override
	public void undo() {
		super.undo();
		getWire().setBendpoint(getIndex(), getOldBendpoint());
	}

}
