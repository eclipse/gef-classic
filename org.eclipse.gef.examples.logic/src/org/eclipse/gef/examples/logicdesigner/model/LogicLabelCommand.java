package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.AbstractCommand;

public class LogicLabelCommand
	extends AbstractCommand
{

private String newName, oldName;
private LogicLabel label;

public LogicLabelCommand(LogicLabel l, String s) {
	label = l;
	if (s != null)
		newName = s;
	else
		newName = "";  //$NON-NLS-1$
}

public void execute() {
	oldName = label.getLabelContents();
	label.setLabelContents(newName);
}

public void undo() {
	label.setLabelContents(oldName);
}

}
