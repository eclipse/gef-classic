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

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;

import org.eclipse.gef.commands.Command;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class CreateEnumLiteralCommand 
	extends Command
{
	
private EEnumLiteral literal;
private EEnum parent;

public CreateEnumLiteralCommand(EEnumLiteral literal, EEnum target) {
	super("Create Enum Literal");
	this.literal = literal;
	parent = target;
}

public void execute() {
	literal.setName("New");
	literal.setValue(parent.getELiterals().size());
	parent.getELiterals().add(literal);
}

public void undo() {
	parent.getELiterals().remove(literal);
	literal.setName(null);
	literal.setValue(0);
}

}
