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

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.Link;

/**
 * Bendpoint manipulation commands cannot be chained or compounded.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class MoveBendpointCommand 
	extends Command
{
	
private Link link;
private int index;
private Bendpoint oldBendpoint, newBendpoint;
	
public MoveBendpointCommand(Link link, Point location, int index) {
	super("Move Bendpoint");
	this.link = link;
	this.index = index;
	newBendpoint = new AbsoluteBendpoint(location);
	oldBendpoint = (Bendpoint)link.getBendpoints().get(index);
}

public boolean canExecute() {
	return link != null && index >= 0 && newBendpoint != null && oldBendpoint != null;
}
	
public void execute() {
	link.getBendpoints().set(index, newBendpoint);
}

public void undo() {
	link.getBendpoints().set(index, oldBendpoint);
}

}
