/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;

/**
 * Tree EditPart for the Container.
 */
public class LogicContainerTreeEditPart
	extends LogicTreeEditPart
{

/**
 * Constructor, which initializes this using the
 * model given as input.
 */
public LogicContainerTreeEditPart(Object model) {
	super(model);
}

/**
 * Creates and installs pertinent EditPolicies.
 */
protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new LogicContainerEditPolicy());
	installEditPolicy(EditPolicy.TREE_CONTAINER_ROLE, new LogicTreeContainerEditPolicy());
	//If this editpart is the contents of the viewer, then it is not deletable!
	if (getParent() instanceof RootEditPart)
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
}

/**
 * Returns the model of this as a LogicDiagram.
 *
 * @return  Model of this.
 */
protected LogicDiagram getLogicDiagram() {
	return (LogicDiagram)getModel();
}

/**
 * Returns the children of this from the model,
 * as this is capable enough of holding EditParts.
 *
 * @return  List of children.
 */
protected List getModelChildren() {
	return getLogicDiagram().getChildren();
}

}