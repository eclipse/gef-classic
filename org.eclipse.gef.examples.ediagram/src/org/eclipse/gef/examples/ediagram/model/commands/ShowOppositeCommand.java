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

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ShowOppositeCommand 
	extends Command
{
	
private ReferenceView refView;

public ShowOppositeCommand(ReferenceView refView) {
	super("Show Opposite");
	this.refView = refView;
}

public boolean canExecute() {
	return refView != null;
}

public void execute() {
	refView.setOppositeShown(true);
}

public void undo() {
	refView.setOppositeShown(false);
}

}
