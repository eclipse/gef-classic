package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class OrphanChildCommand
	extends Command
{

private LogicDiagram diagram;
private LogicSubpart child;
private int index;

public OrphanChildCommand () {
	super(LogicMessages.OrphanChildCommand_Label);
}

public void execute() {
	List children = diagram.getChildren();
	index = children.indexOf(child);
	diagram.removeChild(child);
}

public void redo() {
	diagram.removeChild(child);
}

public void setChild(LogicSubpart child) {
	this.child = child;
}

public void setParent(LogicDiagram parent) { 
	diagram = parent;
}

public void undo() {
	diagram.addChild(child,index);
}

}
