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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class ReorderPartCommand extends Command {
	
private boolean valuesInitialized;
private int oldIndex, newIndex;
private LogicSubpart child;
private LogicDiagram parent;

public ReorderPartCommand(LogicSubpart child, LogicDiagram parent, int oldIndex, int newIndex ) {
	super(LogicMessages.ReorderPartCommand_Label);
	this.child = child;
	this.parent = parent;
	this.oldIndex = oldIndex;
	this.newIndex = newIndex;
}

public void execute() {
	parent.removeChild(child);
	parent.addChild(child, newIndex);
}

public void undo() {
	parent.removeChild(child);
	parent.addChild(child, oldIndex);
}

}