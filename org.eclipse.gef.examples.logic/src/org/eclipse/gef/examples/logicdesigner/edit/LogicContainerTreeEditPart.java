package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.gef.*;

import org.eclipse.gef.examples.logicdesigner.model.*;

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