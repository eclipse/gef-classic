/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.commands;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class InheritanceLinkCreationCommand 
	extends Command
{

private InheritanceView newView;
private NamedElementView source, target;

public InheritanceLinkCreationCommand(InheritanceView view, NamedElementView src) {
	super("Inherit");
	newView = view;
	source = src;
}

public boolean canExecute() {
	return source != null && target != null && newView != null 
			&& source.getENamedElement() instanceof EClass 
			&& target.getENamedElement() instanceof EClass;
}

public void execute() {
	super.execute();
}



}