package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class AddCommand extends
	org.eclipse.gef.commands.AbstractCommand
{

private LogicSubpart child;
private LogicDiagram parent;
private int index = -1;

public AddCommand() {
	super(LogicMessages.AddCommand_Label);
}

public void execute() {
	if( index < 0 )
		parent.addChild(child);
	else
		parent.addChild(child,index);
}

public String getDescription() {
	String name = child.getClass().getName();
	name = name.substring(name.lastIndexOf(".")+1);//$NON-NLS-1$
	return LogicMessages.AddCommand_Description + name;
}

public LogicDiagram getParent() {
	return parent;
}

public void redo() {
	if( index < 0 )
		parent.addChild(child);
	else
		parent.addChild(child,index);
}

public void setChild(LogicSubpart subpart) {
	child = subpart;
}

public void setIndex(int i){
	index = i;
}

public void setParent(LogicDiagram newParent) {
	parent = newParent;
}

public void undo() {
	parent.removeChild(child);
}

}