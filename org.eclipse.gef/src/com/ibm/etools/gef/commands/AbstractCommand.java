package com.ibm.etools.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.common.command.Command;

abstract public class AbstractCommand
	implements Command
{

private String label;
private String description = null;

private String debugLabel;

public AbstractCommand (){}

public AbstractCommand (String label){
	setLabel(label);
}

public Collection getAffectedObjects(){
	return Collections.EMPTY_LIST;
}

public String getDebugLabel(){
	return debugLabel + ' ' + getLabel();
}

public String getDescription(){
	return description;
}

public String getLabel(){
	return label;
}

final public Collection getResult(){
	return Collections.EMPTY_LIST;
}

public boolean canExecute(){
	return true;
}

public boolean canUndo(){
	return true;
}

public Command chain(Command command){
	if (command == null)
		return this;
	class ChainedCompoundCommand
		extends CompoundCommand
	{
		public Command chain(Command c){
			add(c);
			return this;
		}
	}
	CompoundCommand result = new ChainedCompoundCommand();
	result.setDebugLabel("chained commands"); //$NON-NLS-1$
	result.add(this);
	result.add(command);
	return result;
}

public void dispose(){}

public void execute(){}

public void redo() {
	execute();
}

public void setDebugLabel(String label){
	debugLabel = label;
}

public void setDescription(String label) {
	this.label = label;
}

public void setLabel(String label) {
	this.label = label;
}

public void undo(){}

}