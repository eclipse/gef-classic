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

public class CreateBendpointCommand extends BendpointCommand {

	@Override
	public void execute() {
		WireBendpoint wbp = new WireBendpoint();
		wbp.setRelativeDimensions(getFirstRelativeDimension(), getSecondRelativeDimension());
		getWire().insertBendpoint(getIndex(), wbp);
		super.execute();
	}

	@Override
	public void undo() {
		super.undo();
		getWire().removeBendpoint(getIndex());
	}

}
