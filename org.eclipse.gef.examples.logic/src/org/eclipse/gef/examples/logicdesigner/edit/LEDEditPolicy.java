package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class LEDEditPolicy
	extends LogicElementEditPolicy
{

private static final String
	INCREMENT_REQUEST = "Increment", //$NON-NLS-1$
	DECREMENT_REQUEST = "Decrement"; //$NON-NLS-1$

public Command getCommand(Request request) {
	if (INCREMENT_REQUEST.equals(request.getType()))
		return getIncrementDecrementCommand(true);
	if (DECREMENT_REQUEST.equals(request.getType()))
		return getIncrementDecrementCommand(false);
	return super.getCommand(request);
}

protected Command getIncrementDecrementCommand(boolean type){
	IncrementDecrementCommand command = new IncrementDecrementCommand(type);
	command.setChild((LogicSubpart)getHost().getModel());
	return command;
}

static class IncrementDecrementCommand 
	extends org.eclipse.gef.commands.AbstractCommand{
	
	boolean isIncrement = true;
	LED child = null;
	
	public IncrementDecrementCommand(boolean increment){
		super(LogicMessages.IncrementDecrementCommand_LabelText);
		isIncrement=increment;
	}
	
	public void setChild(LogicSubpart child){
		this.child=(LED)child;
	}
	
	public void execute(){
		int value = child.getValue();
		if(isIncrement){
			if(value==15)value=-1;
			child.setValue(value+1);
		}else{
			if(value==0)value=16;
			child.setValue(value-1);
		}
	}
	
	public void undo(){
		isIncrement=!isIncrement;
		execute();
		isIncrement=!isIncrement;
	}
	
	public void redo(){
		execute();
	}
}
	
}