package com.ibm.etools.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.common.command.UnexecutableCommand;

/**
 * A command that can be used to combine multple commands
 * into a single undoable action.
 */
public class CompoundCommand
	extends AbstractCommand
{

protected List commandList = new ArrayList();

public CompoundCommand(){}

public CompoundCommand(String desc){
	super(desc);
}

public void add(Command command){
	if (command != null)
		commandList.add(command);
}

/**
 * canExecute method comment.
 */
public boolean canExecute() {
	if (commandList.size() == 0) return false;
	for (int i = 0; i < commandList.size(); i++){
		Command cmd = (Command) commandList.get(i);
		if (cmd == null)
			return false;
		if (!cmd.canExecute())
			return false;
	}
	return super.canExecute();
}

/**
 * canExecute method comment.
 */
public boolean canUndo() {
	if (commandList.size() == 0) return false;
	for (int i = 0; i < commandList.size(); i++){
		Command cmd = (Command) commandList.get(i);
		if (cmd == null)
			return false;
		if (!cmd.canUndo())
			return false;
	}
	return true;
}

/**
 * cancels the command.  For a compound command this
 * means cancelling all of the commands that it contains.
 */
public void dispose() {
	for (int i = 0; i < commandList.size(); i++){
		Command c = (Command) commandList.get(i);
		if (c != null) c.dispose();
	}
}

/**
 * Execute the command.  For a compound command this
 * means executing all of the commands that it contains.
 */
public void execute() {
	for (int i = 0; i < commandList.size(); i++){
		Command cmd = (Command) commandList.get(i);
		cmd.execute();
	}
}

public Collection getAffectedObjects(){
	List list = new ArrayList();
	for (int i=0; i<commandList.size(); i++)
		list.addAll(((Command)commandList.get(i)).getAffectedObjects());
	return list;
}

public Object [] getChildren(){
	return commandList.toArray();
}

/**
 * Return a vector of child commands
 * @return java.util.List  The child commands
 */
public List getCommands() {
	return commandList;
}

public String getLabel(){
	String label = super.getLabel();
	if (label == null)
		if (commandList.isEmpty())
			return null;
	if (label != null)
		return label;
	return ((Command)commandList.get(0)).getLabel();
}

public boolean isEmpty(){
	return commandList.isEmpty();
}

/**
 * redo the command.  For a compound command this
 * means redoing all of the commands that it contains.
 */
public void redo() {
	for (int i = 0; i < commandList.size(); i++)
		((Command) commandList.get(i)).redo();
}

public int size(){
	return commandList.size();
}

/**
 * Undo the command.  For a compound command this
 * means undoing all of the commands that it contains.
 */
public void undo() {
	for (int i = commandList.size()-1; i >= 0; i--)
		((Command) commandList.get(i)).undo();
}

  /**
   * This returns one of three things: 
   * {@link com.ibm.etools.common.command.UnexecutableCommand#INSTANCE}, if there are no commands,
   * the one command, if there is exactly one command,
   * or <tt>this</tt>, if there are multiple commands;
   * this command is {@link #dispose}d in the first two cases.
   * You should only unwrap a compound command if you created it for that purpose, e.g.,
   * <pre>
   *   CompoundCommand subcommands = new CompoundCommand();
   *   subcommands.append(x);
   *   if (condition) subcommands.append(y);
   *   Command result = subcommands.unwrap();
   * </pre>
   * is a good way to create an efficient accumulated result.
   */
  public Command unwrap()
  {
    switch (commandList.size())
    {
      case 0:
      {
        dispose();
        return UnexecutableCommand.INSTANCE;
      }
      case 1:
      {
        Command result = (Command)commandList.remove(0);
        dispose();
        return result;
      }
      default:
      {
        return this;
      }
    }
  }
}
