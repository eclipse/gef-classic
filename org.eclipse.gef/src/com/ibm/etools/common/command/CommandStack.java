/**
 * <copyright> 
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 *
 * plugins/com.ibm.etools.common.command/src/org/eclipse/emf/common/command/CommandStack.java, emf.common, org.eclipse.dev, 20020529_1802
 * @version 1.1 5/29/02
 */
package com.ibm.etools.common.command;



/**
 * This is a simple and obvious interface for an undoable stack of commands with a listener.
 * See {@link Command} for more details about the command methods that this implementation uses
 * and {@link CommandStackListener} for details about the listener.
 */
public interface CommandStack 
{
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";

  /**
   * This will clear any redoable commands not yet redone, add the command, and then execute the command.
   */
  void execute(Command command); 

  /**
   * This returns whether the top command on the stack can be undone.
   */
  boolean canUndo();

  /**
   * This moves the top of the stack down, undoing what was formerly the top command.
   */
  void undo();

  /**
   * This returns whether there are commands past the top of the stack that can be redone.
   */
  boolean canRedo(); 

  /**
   * This returns the command that will be undone if {@link #undo} is called.
   */
  public Command getUndoCommand();
  
  /**
   * This returns the command that will be redone if {@link #redo} is called.
   */
  public Command getRedoCommand();
  
  /**
   * This returns the command most recently executed, undone, or redone.
   */
  public Command getMostRecentCommand();

  /**
   * This moves the top of the stack up, redoing the new top command.
   */
  void redo();

  /**
   * This will dispose all the commands in the stack.
   */
  void flush();

  /**
   * This adds a listener to the command stack, which will be notified whenever a command has been processed on the stack.
   */
  void addCommandStackListener(CommandStackListener listener);

  /**
   * This removes a previously added listener.
   */
  void removeCommandStackListener(CommandStackListener listener);
}
