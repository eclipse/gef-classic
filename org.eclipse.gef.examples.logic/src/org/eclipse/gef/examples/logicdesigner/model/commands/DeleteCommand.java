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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.Wire;

public class DeleteCommand
	extends Command
{

private LogicSubpart child;
private LogicDiagram parent;
private int index = -1;
private List sourceConnections = new ArrayList();
private List targetConnections = new ArrayList();

public DeleteCommand() {
	super(LogicMessages.DeleteCommand_Label);
}

private void deleteConnections(LogicSubpart part) {
	if (part instanceof LogicDiagram) {
		List children = ((LogicDiagram)part).getChildren();
		for (int i = 0; i < children.size(); i++)
			deleteConnections((LogicSubpart)children.get(i));
	}
	sourceConnections.addAll(part.getSourceConnections());
	for (int i = 0; i < sourceConnections.size(); i++) {
		Wire wire = (Wire)sourceConnections.get(i);
		wire.detachSource();
		wire.detachTarget();
	}
	targetConnections.addAll(part.getTargetConnections());
	for (int i = 0; i < targetConnections.size(); i++) {
		Wire wire = (Wire)targetConnections.get(i);
		wire.detachSource();
		wire.detachTarget();
	}
}

public void execute() {
	primExecute();
}

protected void primExecute() {
	deleteConnections(child);
	index = parent.getChildren().indexOf(child);
	parent.removeChild(child);
}

public void redo() {
	primExecute();
}

private void restoreConnections() {
	for (int i = 0; i < sourceConnections.size(); i++) {
		Wire wire = (Wire)sourceConnections.get(i);
		wire.attachSource();
		wire.attachTarget();
	}
	sourceConnections.clear();
	for (int i = 0; i < targetConnections.size(); i++) {
		Wire wire = (Wire)targetConnections.get(i);
		wire.attachSource();
		wire.attachTarget();
	}
	targetConnections.clear();
}

public void setChild (LogicSubpart c) {
	child = c;
}

public void setParent(LogicDiagram p) {
	parent = p;
}

public void undo() {
	parent.addChild(child, index);
	restoreConnections();
}

}
