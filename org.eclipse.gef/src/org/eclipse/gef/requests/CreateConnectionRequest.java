package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

/**
 * A Request to create a new Connection.
 */
public class CreateConnectionRequest
	extends CreateRequest
	implements TargetRequest
{

private Command startCommand;
private EditPart targetEditPart;
private EditPart sourceEditPart;

/**
 * Returns the source EditPart.
 *
 * @return The source EditPart.
 */
public EditPart getSourceEditPart(){
	return sourceEditPart;
}

public EditPart getTargetEditPart(){
	return targetEditPart;
}

/**
 * Returns the Command.
 *
 * @return The Command.
 */
public Command getStartCommand(){
	return startCommand;
}

/**
 * Sets the source of the Connection to the given EditPart.
 *
 * @param part The source EditPart.
 */
public void setSourceEditPart(EditPart part){
	sourceEditPart = part;
}

public void setTargetEditPart(EditPart part){
	targetEditPart = part;
}


/**
 * Sets the Command to be executed.
 *
 * @param c The Command.
 */
public void setStartCommand(Command c){
	startCommand = c;
}

}