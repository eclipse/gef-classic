package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.AbstractCommand;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class DeleteCommand
	extends AbstractCommand
{

private LogicSubpart child;
private LogicDiagram parent;
private int index = -1;

public DeleteCommand() {
	super(LogicMessages.DeleteCommand_Label);
}

public void execute() {
	primExecute();
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
