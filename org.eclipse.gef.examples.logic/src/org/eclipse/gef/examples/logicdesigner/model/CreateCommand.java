package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class CreateCommand
	extends org.eclipse.gef.commands.AbstractCommand
{

private LogicSubpart child;
private Rectangle rect;
private LogicDiagram parent;
private int index = -1;

public CreateCommand() {
	super(LogicMessages.CreateCommand_Label);
}

public void execute() {
	if (rect != null) {
		child.setLocation(rect.getLocation());
		if (!rect.isEmpty())
			child.setSize(rect.getSize());
	}
	if( index < 0 )
		parent.addChild(child);
	else
		parent.addChild(child,index);
}

public String getDescription() {
	String name = child.getClass().getName();
	name = name.substring(name.lastIndexOf(".")+1);//$NON-NLS-1$
	return LogicMessages.CreateCommand_Description + " " + name;  //$NON-NLS-1$
}

public LogicDiagram getParent() {
	return parent;
}

public void redo() {
	if (rect != null) {
		child.setLocation(rect.getLocation());
		child.setSize    (rect.getSize());
	}
	if( index < 0 )
		parent.addChild(child);
	else
		parent.addChild(child,index);
}

public void setChild(LogicSubpart subpart) {
	child = subpart;
}

public void setIndex( int index ){
	this.index = index;
}

public void setLocation (Rectangle r) {
	rect = r;
}

public void setParent(LogicDiagram newParent) {
	parent = newParent;
}

public void undo() {
	parent.removeChild(child);
}

}
