/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.commands;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.gef.commands.Command;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class CreateAttributeCommand 
	extends Command
{
	
private EAttribute attribute;
private EClass parent;
private String oldName;

public CreateAttributeCommand(EAttribute attrib, EClass parent) {
	super("Create Attribute");
	attribute = attrib;
	this.parent = parent;
}

public boolean canExecute() {
	return attribute != null && parent != null;
}

public void execute() {
	if (attribute.getName() == null || attribute.getName().trim().equals("")) {
		oldName =  attribute.getName();
		attribute.setName("newAttribute");
	}
	parent.getEStructuralFeatures().add(attribute);
}

public void undo() {
	parent.getEStructuralFeatures().remove(attribute);
	if (oldName != null) {
		attribute.setName(oldName);
	}
}

}
