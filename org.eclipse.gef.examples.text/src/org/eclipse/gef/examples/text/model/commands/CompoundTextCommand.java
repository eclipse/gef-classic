/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.text.model.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.text.AppendableCommand;
import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextCommand;

public class CompoundTextCommand
	extends Command
	implements TextCommand, AppendableCommand
{

private List pending = new ArrayList();
private List applied = new ArrayList();
	
public CompoundTextCommand() {
	super();
}

public CompoundTextCommand(String label) {
	super(label);
}

public void add(TextCommand command) {
	if (command != null)
		pending.add(command);
}

public boolean canExecute() {
	return canExecutePending();
}

public boolean canExecutePending() {
	if (pending.size() == 0)
		return false;
	for (int i = 0; i < pending.size(); i++) {
		Command cmd = (Command) pending.get(i);
		if (cmd == null)
			return false;
		if (!cmd.canExecute())
			return false;
	}
	return true;
}

public void dispose() {
	for (int i = 0; i < applied.size(); i++)
		((Command) applied.get(i)).dispose();
	flushPending();
}

public void execute() {
	executePending();
}

public void executePending() {
	for (int i = 0; i < pending.size(); i++) {
		Command cmd = (Command) pending.get(i);
		cmd.execute();
		applied.add(cmd);
	}
	flushPending();
}

public void flushPending() {
	pending.clear();
}

public SelectionRange getExecuteSelectionRange(GraphicalTextViewer viewer) {
	if (applied.isEmpty())
		return null;
	return ((TextCommand)applied.get(applied.size() - 1))
			.getExecuteSelectionRange(viewer);
}

public SelectionRange getRedoSelectionRange(GraphicalTextViewer viewer) {
	if (applied.isEmpty())
		return null;
	return ((TextCommand)applied.get(applied.size() - 1))
			.getExecuteSelectionRange(viewer);
}

public SelectionRange getUndoSelectionRange(GraphicalTextViewer viewer) {
	if (applied.isEmpty())
		return null;
	return ((TextCommand)applied.get(0)).getUndoSelectionRange(viewer);
}

public void redo() {
	for (int i = 0; i < applied.size(); i++)
		((Command) applied.get(i)).redo();
}

public void undo() {
	for (int i = applied.size() - 1; i >= 0; i--)
		((Command) applied.get(i)).undo();
}

}