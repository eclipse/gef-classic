/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.text.GraphicalTextViewer;
import org.eclipse.gef.examples.text.TextCommand;
import org.eclipse.gef.examples.text.edit.TextualEditPart;

/**
 * @since 3.1
 */
public abstract class ExampleTextCommand
	extends Command
	implements TextCommand
{

public ExampleTextCommand(String label) {
	super(label);
}

public boolean canExecutePending() {
	return false;
}

public void executePending() {}

protected static TextualEditPart lookupModel(GraphicalTextViewer viewer,
		ModelElement model) {
	return (TextualEditPart)viewer.getEditPartRegistry().get(model);
}

}