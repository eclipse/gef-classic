/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.commands;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;

import org.eclipse.gef.commands.Command;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class CreateOperationCommand 
	extends Command
{
	
protected EOperation op;
protected EClass parent;

public CreateOperationCommand(EOperation operation, EClass parent) {
	super("Create Operation");
	op = operation;
	this.parent = parent;
}

public boolean canExecute() {
	return op != null && parent != null;
}

public void execute() {
	op.setName("newOperation");
//	op.setEType();
	parent.getEOperations().add(op);
}

public void undo() {
	parent.getEOperations().remove(op);
	op.setName(null);
}

}
