/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.Wire;

public class DeleteCommand
	extends Command
{

private LogicSubpart child;
private LogicDiagram parent;
private LogicGuide vGuide, hGuide;
private int vAlign, hAlign;
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

private void detachFromGuides(LogicSubpart part) {
	if (part.getVerticalGuide() != null) {
		vGuide = part.getVerticalGuide();
		vAlign = vGuide.getAlignment(part);
		vGuide.detachPart(part);
	}
	if (part.getHorizontalGuide() != null) {
		hGuide = part.getHorizontalGuide();
		hAlign = hGuide.getAlignment(part);
		hGuide.detachPart(part);
	}
		
}
public void execute() {
	primExecute(); 
}

protected void primExecute() {
	deleteConnections(child);
	detachFromGuides(child);
	index = parent.getChildren().indexOf(child);
	parent.removeChild(child);
}

private void reattachToGuides(LogicSubpart part) {
	if (vGuide != null)
		vGuide.attachPart(part, vAlign);
	if (hGuide != null)
		hGuide.attachPart(part, hAlign);
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
	reattachToGuides(child);
}

}
