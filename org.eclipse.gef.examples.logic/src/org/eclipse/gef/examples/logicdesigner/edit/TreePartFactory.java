package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;

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