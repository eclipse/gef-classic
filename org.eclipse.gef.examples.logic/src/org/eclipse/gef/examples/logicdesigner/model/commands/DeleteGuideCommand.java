/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicRuler;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

/**
 * @author Pratik Shah
 */
public class DeleteGuideCommand 
	extends Command 
{

private LogicRuler parent;
private LogicGuide guide;
private Map oldParts;

public DeleteGuideCommand(LogicGuide guide, LogicRuler parent) {
	super(LogicMessages.DeleteGuideCommand_Label);
	this.guide = guide;
	this.parent = parent;
}

public boolean canUndo() {
	return true;
}

public void execute() {
	oldParts = new HashMap(guide.getMap());
	Iterator iter = oldParts.keySet().iterator();
	while (iter.hasNext()) {
		guide.detachPart((LogicSubpart)iter.next());
	}
	parent.removeGuide(guide);
}
public void undo() {
	parent.addGuide(guide);
	Iterator iter = oldParts.keySet().iterator();
	while (iter.hasNext()) {
		LogicSubpart part = (LogicSubpart)iter.next();
		guide.attachPart(part, ((Integer)oldParts.get(part)).intValue());
	}
}
}
