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
 */
public class DefaultCommandStack
	implements CommandStack
{

private Stack undo = new Stack();
private Stack redo = new Stack();

protected Command mostRecentCommand;

protected List listeners = new ArrayList();

protected int saveIndex = -1;

/**
 * Default constructor.
 */
public DefaultCommandStack() {}

/**
 * Adds a listener to this CommandStack.
 *
 * @param listener The Object listening to this CommandStack.
 */
public void addCommandStackListener(CommandStackListener listener){
	listeners.add(listener);
}

/**
 * Returns <code>true</code> if there is a Command to redo.
 *
 * @return <code>true</code> if there is a Command to redo.
 */
public boolean canRedo(){
	return !redo.isEmpty();
}

/**
 * Returns <code>true</code> if given Command can be redone.
 *
 * @return <code>true</code> if given Command can be redone.
 */
public boolean canRedoCommand(Command command){
	return redo.contains(command);
}

/**
 * Returns <code>true</code> if the last Command executed can be undone.
 *
 * @return <code>true</code> if the last Command executed can be undone.
 */
public boolean canUndo(){
	if (undo.size() == 0)
		return false;
	return ((Command)undo.lastElement()).canUndo();
}

/**
 * Returns <code>true</code> if given Command can be undone.
 *
 * @return <code>true</code> if given Command can be undone.
 */
public boolean canUndoCommand(Command command){
	return undo.contains(command);
}

/**
 * Executes the given Command if it can execute.
 *
 * @param command The Command to execute.
 */
public void execute(Command command) {
	if (command == null || !command.canExecute())
		return;
	flushRedo();
	try{
		mostRecentCommand = null;
		command.execute();
		mostRecentCommand = command;
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


/**
 * Clears both the undo and redo stacks, then sends a notification
 * to any object listening to the CommandStack.
 */
public void flush(){
	flushUndo();
	flushRedo();
	notifyListeners();
}

/**
 * Clears the redo stack.
 */
protected void flushRedo(){
	while (!redo.isEmpty())
		((Command)redo.pop()).dispose();
}

/**
 * Clears the undo stack.
 */
protected void flushUndo(){
	while (!undo.isEmpty())
		((Command)undo.pop()).dispose();
}

/**
 * Returns an array containing the commands in both stacks.
 *
 * @return An Object array containing the commands in the stacks.
 */
public Object[] getCommands(){
	List commands = new ArrayList(undo);
	for (int i = (redo.size()-1); i >= 0; i--){
		commands.add(redo.get(i));
	}
	return commands.toArray();
}

/**
 * Returns the most recently executed command.
 *
 * @return The most recently executed command.
 */
public Command getMostRecentCommand(){
	return mostRecentCommand;
}

/**
 * Returns the command at the top of the redo stack.
 *
 * @return The next command to be redone.
 */
public Command getRedoCommand(){
	return redo.isEmpty() ? null : (Command)redo.peek();
}

/**
 * Returns the next command to be undone.
 */
public Command getUndoCommand(){
	return undo.isEmpty() ? null : (Command)undo.peek();
}

/**
 * Returns <code>true</code> if the model needs to be saved.
 *
 * @return <code>true</code> if the model needs to be saved.
 */
public boolean isSaveNeeded(){
	return !undo.isEmpty();
}

/**
 * Sends a notification to any object listening to this CommandStack.
 */
protected void notifyListeners(){
	EventObject event = new EventObject(this);
	for (int i=0; i<listeners.size(); i++)
		((CommandStackListener)listeners.get(i)).commandStackChanged(event);
}

/**
 * Executes the last undone Command.
 */
public void redo(){
	//Assert.isTrue(canRedo())
	if (!canRedo())
		return;
	Command command = (Command)redo.pop();

	try {
		mostRecentCommand = null;
		command.redo();
		undo.push(command);
		mostRecentCommand = command;
	} catch (RuntimeException exception) {
		flushRedo();
		throw exception;
	} finally {
		notifyListeners();
	}
}

/**
 * Removes the given CommandStackListener.
 *
 * @param listener The object to be removed from the list of listeners.
 */
public void removeCommandStackListener(CommandStackListener listener){
	listeners.remove(listener);
}

/**
 * Flushes the undo and redo stacks.
 */
public void saveIsDone(){
	flush();
}

/**
 * Undoes the last executed Command.
 */
public void undo(){
	//Assert.isTrue(canUndo());
	Command command = (Command)undo.pop();
	try{
		command.undo();
		mostRecentCommand = command;
		redo.push(command);
	} catch (Exception exception){
		mostRecentCommand = null;
		flush();
	}
	notifyListeners();
}

}


