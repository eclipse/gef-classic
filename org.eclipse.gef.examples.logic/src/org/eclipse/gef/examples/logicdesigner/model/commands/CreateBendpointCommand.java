/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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




public class CreateBendpointCommand 
	extends BendpointCommand 
{

public void execute() {
	WireBendpoint wbp = new WireBendpoint();
	wbp.setRelativeDimensions(getFirstRelativeDimension(), 
					getSecondRelativeDimension());
	getWire().insertBendpoint(getIndex(), wbp);
	super.execute();
}

public void undo() {
	super.undo();
	getWire().removeBendpoint(getIndex());
}

}


