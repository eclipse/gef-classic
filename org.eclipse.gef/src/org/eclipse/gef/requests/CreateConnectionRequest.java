/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.requests;


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