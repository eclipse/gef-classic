package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.EditPartFactory;
import com.ibm.etools.gef.examples.logicdesigner.model.LED;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicDiagram;

public class TreePartFactory
	implements EditPartFactory
{

public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof LED)
		return new LogicTreeEditPart(model);
	if (model instanceof LogicDiagram)
		return new LogicContainerTreeEditPart(model);
	return new LogicTreeEditPart(model);
}

}