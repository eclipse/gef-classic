package org.eclipse.gef.commands;

/**
 * An Undo/Redo Stack containing {@link Command Commands}. A CommandStack is used to
 * manage the set of Commands that have been executed and redone. A CommandStack consists
 * of two stacks.  The <i>undo</i> stack contains Commands which have been either
 * executed or redone.  The <i>redo</i> stack contains the Commands which have been
 * undone.
 * 
 */
public interface CommandStack {

/**
 * Adds the specified listener. Multiple adds will result in multiple notifications.
 * @param listener the listener
 */
void addCommandStackListener(CommandStackListener listener);

/**
 * @return <code>true</code> if it is appropriate to call {@link #redo()}.
 */
boolean canRedo(); 

/**
 * @return <code>true</code> if {@link #undo()} can be called
 */
boolean canUndo();

/**
 * Executes the specified Command if it is executable. Flushes the redo stack.
 * @param command the Command to execute
 */
void execute(Command command); 

/**
 * This will dispose all the commands in both the undo and redo stack.
 */
void flush();

/**
 * Peeks at the top of the <i>redo</i> stack. This is useful for describing to the User
 * what will be redone. The returned <code>Command</code> has a label describing it.
 * @return the top of the <i>redo</i> stack, which may be <code>null</code>
 */
Command getRedoCommand();

/**
 * Peeks at the top of the <i>undo</i> stack. This is useful for describing to the User
 * what will be undone. The returned <code>Command</code> has a label describing it.
 * @return the top of the <i>undo</i> stack, which may be <code>null</code>
 */
Command getUndoCommand();

/**
 * Calls redo on the Command at the top of the <i>redo</i> stack, and pushes that Command
 * onto the <i>undo</i> stack.
 */
void redo();

/**
 * Removes the first occurance of the specified listener.
 * @param listener the listener
 */
void removeCommandStackListener(CommandStackListener listener);

/**
 * Undoes the most recently executed (or redone) Command. The Command is popped from the
 * undo stack to and pushed onto the redo stack. This method should only be called if
 * {@link #canUndo()} returns <code>true</code>.
 */
void undo();

}
