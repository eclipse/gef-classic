/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.commands;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.StickyNote;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ChangeTextCommand 
	extends Command
{
	
private StickyNote note;
private String newText, oldText;

public ChangeTextCommand(StickyNote note, String newText) {
	super("Edit Comment");
	this.note = note;
	this.newText = newText;
}

public boolean canExecute() {
	return note != null && newText != null;
}

public void execute() {
	oldText = note.getText();
	note.setText(newText);
}

public void undo() {
	note.setText(oldText);
}

}
