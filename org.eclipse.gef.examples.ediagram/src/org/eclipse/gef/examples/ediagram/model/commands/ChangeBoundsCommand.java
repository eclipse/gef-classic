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

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.Node;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ChangeBoundsCommand
	extends Command
{
	
private Node node;
private Point newLocation, oldLocation;
private int newWidth, oldWidth;
	
public ChangeBoundsCommand(Node node, Point newLocation, int newWidth) {
	super("Change Bounds");
	setNode(node);
	setNewLocation(newLocation);
	this.newWidth = newWidth;
}

public boolean canExecute() {
	return node != null && newLocation != null 
			&& (newWidth != oldWidth || !(node.getLocation().equals(newLocation)));
}

public void execute() {
	oldLocation = node.getLocation();
	oldWidth = node.getWidth();
	redo();
}

public void redo() {
	node.setLocation(newLocation);
	node.setWidth(newWidth);
}

public void setNode(Node node) {
	this.node = node;
}

public void setNewLocation(Point loc) {
	newLocation = loc;
}

public void undo() {
	node.setWidth(oldWidth);
	node.setLocation(oldLocation);
}

}