/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class CreateCommand
	extends org.eclipse.gef.commands.Command
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
