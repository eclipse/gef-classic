package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.examples.logicdesigner.model.*;


/**
 * Provides support for Container EditParts.
 */
abstract public class LogicContainerEditPart
	extends LogicEditPart
{

/**
 * Installs the desired EditPolicies for this.
 */
protected void createEditPolicies() {
	super.createEditPolicies();
	installEditPolicy(EditPolicy.CONTAINER_ROLE, new LogicContainerEditPolicy());
	installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicXYLayoutEditPolicy());
}

/**
 * Returns the model of this as a LogicDiagram.
 *
 * @return  LogicDiagram of this.
 */
protected LogicDiagram getLogicDiagram() {
	return (LogicDiagram)getModel();
}

/**
 * Returns the children of this through the model.
 *
 * @return  Children of this as a List.
 */
protected List getModelChildren() {
	return getLogicDiagram().getChildren();
}

}
