package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.EditPartFactory;
import com.ibm.etools.gef.examples.logicdesigner.model.*;

public class GraphicalPartFactory
	implements EditPartFactory
{

public EditPart createEditPart(EditPart context, Object model) {
	EditPart child = null;
	
	if (model instanceof LogicFlowContainer)
		child = new LogicFlowContainerEditPart();
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
