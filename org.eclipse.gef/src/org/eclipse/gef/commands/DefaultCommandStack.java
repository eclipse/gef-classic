package org.eclipse.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

/**
 * A default implementation for the CommandStack interface.
 * @author hudsonr
 */
public class DefaultCommandStack
	implements CommandStack
{

private Stack undo = new Stack();
private Stack redo = new Stack();

/**
 * the list of {@link CommandStackListener}s
 */
protected List listeners = new ArrayList();

/**
 * Constructs a command stack.
 */
public DefaultCommandStack() { }

/** * @param listener CommandStackListener
 * @see CommandStack#addCommandStackListener(CommandStackListener) */
public void addCommandStackListener(CommandStackListener listener) {
	listeners.add(listener);
}

/** * @return boolean
 * @see org.eclipse.gef.commands.CommandStack#canRedo() */
public boolean canRedo() {
	return !redo.isEmpty();
}

/** * @return boolean
 * @see org.eclipse.gef.commands.CommandStack#canUndo() */
public boolean canUndo() {
	if (undo.size() == 0)
		return false;
	return ((Command)undo.lastElement()).canUndo();
}

/** * @param command Command
 * @see org.eclipse.gef.commands.CommandStack#execute(Command) */
public void execute(Command command) {
	if (command == null || !command.canExecute())
		return;
	flushRedo();
	try {
		command.execute();
		undo.push(command);
	} catch (RuntimeException exception) {
		command.dispose();
		flushUndo();
		undo.push(UnexecutableCommand.INSTANCE);
		throw exception;
	} finally {
		notifyListeners();
	}
}

/** * @see org.eclipse.gef.commands.CommandStack#flush() */
public void flush() {
	flushUndo();
	flushRedo();
	notifyListeners();
}

private void flushRedo() {
	while (!redo.isEmpty())
		((Command)redo.pop()).dispose();
}

private void flushUndo() {
	while (!undo.isEmpty())
		((Command)undo.pop()).dispose();
}

/**
 * @return an array containing all commands in the order they were executed
 */
public Object[] getCommands() { 
	List commands = new ArrayList(undo);
	for (int i = redo.size() - 1; i >= 0; i--) {
		commands.add(redo.get(i));
	}
	return commands.toArray();
}

/**
 * @return Command
 * @see org.eclipse.gef.commands.CommandStack#getRedoCommand()
 */
public Command getRedoCommand() {
	return redo.isEmpty() ? null : (Command)redo.peek();
}

/**
 * @return Command
 * @see org.eclipse.gef.commands.CommandStack#getUndoCommand()
 */
public Command getUndoCommand() {
	return undo.isEmpty() ? null : (Command)undo.peek();
}

/**
 * Sends notification to all {@link CommandStackListener}s.
 */
protected void notifyListeners() {
	EventObject event = new EventObject(this);
	for (int i = 0; i < listeners.size(); i++)
		((CommandStackListener)listeners.get(i))
			.commandStackChanged(event);
}

/**
 * @see org.eclipse.gef.commands.CommandStack#redo()
 */
public void redo() {
	//Assert.isTrue(canRedo())
	if (!canRedo())
		return;
	Command command = (Command)redo.pop();

	try {
		command.redo();
		undo.push(command);
	} catch (RuntimeException exception) {
		flushRedo();
		throw exception;
	} finally {
		notifyListeners();
	}
}

/**
 * @param listener CommandStackListener
 * @see org.eclipse.gef.commands.CommandStack#removeCommandStackListener(CommandStackListener)
 */
public void removeCommandStackListener(CommandStackListener listener) {
	listeners.remove(listener);
}

/**
 * @see org.eclipse.gef.commands.CommandStack#undo()
 */
public void undo() {
	//Assert.isTrue(canUndo());
	Command command = (Command)undo.pop();
	try {
		command.undo();
		redo.push(command);
	} catch (Exception exception) {
		flush();
	}
	notifyListeners();
}

}
