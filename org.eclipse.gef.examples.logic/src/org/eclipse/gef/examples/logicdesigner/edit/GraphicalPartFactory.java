/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.logicdesigner.model.*;

public class GraphicalPartFactory
	implements EditPartFactory
{

public EditPart createEditPart(EditPart context, Object model) {
	EditPart child = null;
	
	if (model instanceof LogicFlowContainer)
		child = new LogicFlowContainerEditPart();
	else if (model instanceof Wire)
		child = new WireEditPart();
	else if (model instanceof LED)
		child = new LEDEditPart();
	else if (model instanceof LogicLabel)
		child = new LogicLabelEditPart();
	else if (model instanceof Circuit)
		child = new CircuitEditPart();
	else if (model instanceof Gate)
		child = new GateEditPart();
	else if (model instanceof SimpleOutput)
		child = new OutputEditPart();
	//Note that subclasses of LogicDiagram have already been matched above, like Circuit
	else if (model instanceof LogicDiagram)
		child = new LogicDiagramEditPart();
	child.setModel(model);
	return child;
}

}
