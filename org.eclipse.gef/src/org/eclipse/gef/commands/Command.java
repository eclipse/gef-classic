/**
 * <copyright> 
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 */
package org.eclipse.gef.commands;

/**
 * Encapsulates execute, undo, and redo of an operation. A Command has a <i>label</i>
 * which is displayed in the UI.
 */
public interface Command {

/**
 * @return <code>true</code> if the command can be executed
 */
boolean canExecute();

/**
 * @return <code>true</code> if the command can be undone. This method should only be
 * called after <code>execute()</code> or <code>redo()</code> has been called.
 */
boolean canUndo();

/**
 * Returns a Command that represents the chaining of a specified Command to this
 * Command. The Command being chained will <code>execute()</code> after this command has
 * executed, and it will <code>undo()</code> before this Command is undone.
 * @param command <code>null</code> or the Command being chained
 * @return a Command representing the union
 */
Command chain(Command command);

/**
 * This is called to indicate that the <code>Command</code> will not be used again. The
 * Command may be in any state (executed, undone or redone) when dispose is called. The
 * Command should not be referenced in any way after it has been disposed.
 */
void dispose();

/**
 * executes the Command. This method should not be called if the Command is not
 * executable.
 */
void execute();

/**
 * @return a String used to describe this command to the Eser
 */
String getLabel();

/**
 * re-executes the Command. This method should only be called after <code>undo()</code>
 * has been called.
 */
void redo();

/**
 * Undoes the changes performed during <code>execute()</code>. This method should only be
 * called after <code>execute</code> has been called, and iff <code>canUndo()</code>
 * returns <code>true</code>.
 * @see #canUndo()
 */
void undo();

}
