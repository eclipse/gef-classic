package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.GEF;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

public class ReorderPartCommand extends AbstractCommand {
	
private boolean valuesInitialized;
private int oldIndex, newIndex;
private LogicSubpart child;
private LogicDiagram parent;

public ReorderPartCommand(LogicSubpart child, LogicDiagram parent, int oldIndex, int newIndex ) {
	super(LogicResources.getString("ReorderPartCommand.Label"));//$NON-NLS-1$
	this.child = child;
	this.parent = parent;
	this.oldIndex = oldIndex;
	this.newIndex = newIndex;
}

public void execute() {
	parent.removeChild(child);
	parent.addChild(child, newIndex);
}

public String getDescription() {
	return LogicResources.getString("ReorderPartCommand.Description") + child.getID() + " in " + parent.getID() + //$NON-NLS-2$//$NON-NLS-1$
			" from index " + oldIndex + " to " + newIndex;//$NON-NLS-2$//$NON-NLS-1$
}

public void undo() {
	parent.removeChild(child);
	parent.addChild(child, oldIndex);
}

}