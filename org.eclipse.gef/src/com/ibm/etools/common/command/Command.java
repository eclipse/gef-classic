/**
 * <copyright> 
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 *
 * plugins/com.ibm.etools.common.command/src/org/eclipse/emf/common/command/Command.java, emf.common, org.eclipse.dev, 20020529_1802
 * @version 1.1 5/29/02
 */
package com.ibm.etools.common.command;


import java.util.Collection;


/**
 * This interface represents basic behaviour that every command is expected to support.
 * A command can be tested for executability, 
 * it can be executed, 
 * it can be tested for undoability, 
 * it can be undone, 
 * and can then be redone.
 * A comand also provides access to a result collection, an affected-objects collection,
 * a label, and a description.
 *
 * <p>
 * There are important constraints on the valid order in which the various methods may be invoked,
 * e.g., you cannot ask for the result before you've executed the command.
 * These constraints are documented with the various methods.
 */
public interface Command 
{
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";

  /**
   * This indicates whether the comamad is valid to execute.
   * The {@link UnexecutableCommand#INSTANCE}.canExecute() always returns false.
   * This <bf>must</bf> be called before calling execute.
   */
  boolean canExecute();

  /**
   * This will perform the command activity required for the effect.
   * The effect of calling execute when canExecute returns false, or when canExecute hasn't been called, is undefined.
   */
  void execute();

  /**
   * This returns whether the command can be undone.
   * The result of calling this before execute is well defined,
   * but the result of calling this before calling canExecute is undefined, i.e.,
   * a command that retuns false for canExecute may return true for canUndo, 
   * even though that is a contradiction.
   */
  boolean canUndo();

  /**
   * This will perform the command activity required to undo the effects of a preceding execute (or redo).
   * The effect, if any, of calling undo before execute or redo have been called, or when canUndo returns false, is undefined.
   */
  void undo();

  /**
   * This will again perform the command activity required to redo the effect after undoing the effect.
   * The effect, if any, of calling redo before undo is called is undefined.
   * Note that if you implement redo to call execute then any derived class will be restricted to by that decision also.
   */
  void redo();

  /**
   * This returns collection of things which this command wishes to present as it's result.
   * The result of calling this before an execute or redo, or after an undo, is undefined.
   */
  Collection getResult();

  /**
   * This returns the collection of things which this command wishes to present as the objects affected by the command.
   * Typically should could be used as the selection that should be highlighted to best illustrate the effect of the command.
   * The result of calling this before an execute, redo, or undo is undefined.
   * The result may be different after an undo than it is after an execute or redo,
   * but the result should be the same (equivalent) after either an execute or redo.
   */
  Collection getAffectedObjects();

  /**
   * This returns a string suitable to represent the label that identifies this command.
   */
  String getLabel();

  /**
   * This returns a string suitable to help describe the effect of this command.
   */
  String getDescription();

  /**
   * This is called to indicate that the command will never be used again.
   * Calling any other method after this one has undefined results.
   * 
   */
  void dispose();

  /**
   * This logically chains the given command to this command, by returning a command that represents the composition.
   * The resulting command may just be this, if this command is capabable of composition.
   * Otherwise, it will be a new command created to compose the two.
   * <p>
   * Instead of the following pattern of usage
   * <pre>
   *   Command result = x;
   *   if (condition) result = result.chain(y);
   * </pre>
   * you should consider using a {@link com.ibm.etools.common.command.CompoundCommand} 
   * and using {@link com.ibm.etools.common.command.CompoundCommand#unwrap()} to optimize the result:
   * <pre>
   *   CompoundCommand subcommands = new CompoundCommand();
   *   subcommands.append(x);
   *   if (condition) subcommands.append(y);
   *   Command result = subcommands.unwrap();
   * </pre>
   * This gives you more control over how the compound command composes it's result and affected objects.
   */
  Command chain(Command command);
}
