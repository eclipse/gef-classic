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

import org.eclipse.emf.ecore.ENamedElement;

import org.eclipse.gef.commands.Command;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ChangeNameCommand 
	extends Command
{
	
private ENamedElement element;
private String newName, oldName;

public ChangeNameCommand(ENamedElement elem, String newName) {
	super("Change Name");
	element = elem;
	oldName = elem.getName();
	this.newName = newName.trim();
}

public boolean canExecute() {
	return element != null;
}

public void execute() {
	element.setName(newName);
}

public void undo() {
	element.setName(oldName);
}

}
