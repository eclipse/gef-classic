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

import org.eclipse.draw2d.Bendpoint;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.Link;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DeleteBendpointCommand 
	extends Command
{

private Link link;
private Bendpoint bendpoint;
private int index;

public DeleteBendpointCommand(Link link, int index) {
	super("Delete Bendpoint");
	this.link = link;
	this.index = index;
	bendpoint = (Bendpoint)link.getBendpoints().get(index);
}

public boolean canExecute() {
	return index >= 0 && bendpoint != null && link != null;
}
	
public void execute() {
	link.getBendpoints().remove(index);
}

public void undo() {
	link.getBendpoints().add(index, bendpoint);
}

}
