package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

public class AddCommand extends
	com.ibm.etools.gef.commands.AbstractCommand
{

private LogicSubpart child;
private LogicDiagram parent;
private int index = -1;

public AddCommand() {
	super(LogicResources.getString("AddCommand.Label"));//$NON-NLS-1$
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
	return LogicResources.getString("AddCommand.Description") + name;//$NON-NLS-1$
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