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

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.Node;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class TransferLinkCommand 
	extends Command
{
	
private Node src, target;
private Link link;

public TransferLinkCommand(Link link, Node src, Node target) {
	super("Create Link from Outline");
	this.link = link;
	this.src = src;
	this.target = target;
}

public boolean canExecute() {
	return src != null && target != null && link != null;
}

public void execute() {
	if (src == target) {
		link.getBendpoints().add(new AbsoluteBendpoint(
				src.getLocation().getTranslated(-10, 10)));
		link.getBendpoints().add(new AbsoluteBendpoint(
				src.getLocation().getTranslated(-10, -10)));
		link.getBendpoints().add(new AbsoluteBendpoint(
				src.getLocation().getTranslated(10, -10)));
	}
	link.setTarget(target);
	link.setSource(src);
}

public void undo() {
	link.setSource(null);
	link.setTarget(null);
	if (src == target) {
		link.getBendpoints().clear();
	}
}

}
