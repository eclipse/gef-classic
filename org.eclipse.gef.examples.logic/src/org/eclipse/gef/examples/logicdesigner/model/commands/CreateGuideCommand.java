/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
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
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicRuler;

/**
 * @author Pratik Shah
 */
public class CreateGuideCommand extends Command {

private LogicGuide guide;
private LogicRuler parent;
private int position;

public CreateGuideCommand(LogicRuler parent, int position) {
	super(LogicMessages.CreateGuideCommand_Label);
	this.parent = parent;
	this.position = position;
}

public boolean canUndo() {
	return true;
}
public void execute() {
	guide = new LogicGuide(!parent.isHorizontal());
	guide.setPosition(position);
	parent.addGuide(guide);
}
public void undo() {
	parent.removeGuide(guide);
}

}