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
 * @author Pratik Shah
 * @since 3.1
 */
public class CreateBendpointCommand 
	extends Command
{
	
private Link link;
private Bendpoint point;
private int index = -1;

public CreateBendpointCommand(Link link, Point location, int index) {
	super("Create Bendpoint");
	this.link = link;
	point = new AbsoluteBendpoint(location);
	this.index = index;
}

public boolean canExecute() {
	return link != null && point != null;
}

public void execute() {
	link.getBendpoints().add(index, point);
}

public void undo() {
	link.getBendpoints().remove(index);
}

}
