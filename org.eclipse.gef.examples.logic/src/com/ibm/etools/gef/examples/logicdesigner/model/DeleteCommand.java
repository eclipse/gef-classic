package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

public class DeleteCommand
	extends AbstractCommand
{

private LogicSubpart child;
private LogicDiagram parent;
private int index = -1;

public DeleteCommand() {
	super(LogicResources.getString("DeleteCommand.Label"));//$NON-NLS-1$
}

public void execute() {
	primExecute();
}

public String getDescription() {
	String name = child.getClass().getName();
	name = name.substring(name.lastIndexOf(".")+1);//$NON-NLS-1$
	return LogicResources.getString("DeleteCommand.Description")+" " + name;//$NON-NLS-2$//$NON-NLS-1$
}

protected void primExecute() {
	index=parent.getChildren().indexOf(child);
	parent.removeChild(child);
}

public void redo() {
	primExecute();
}

public void setChild (LogicSubpart c) {
	child = c;
}

public void setParent(LogicDiagram p) {
	parent = p;
}

public void undo() {
	parent.addChild(child, index);
}

}
