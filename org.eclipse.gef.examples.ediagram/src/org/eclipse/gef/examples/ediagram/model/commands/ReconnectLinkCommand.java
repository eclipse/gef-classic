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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ReconnectLinkCommand 
	extends Command
{
	
private boolean isSource;
private Link link;
private Node newNode, oldNode;
private int oldIndex, viewIndex;
private EReference ref;
private EClass oldClass, newClass, superClass;

public ReconnectLinkCommand(Link link, Node newNode, boolean isSource) {
	super("Reconnect Link");
	this.link = link;
	this.newNode = newNode;
	this.isSource = isSource;
}

public boolean canExecute() {
	return link != null && newNode != null;
}

public void execute() {
	// Inheritance
	if (link instanceof InheritanceView) {
		superClass = (EClass)((NamedElementView)link.getTarget()).getENamedElement();
		oldClass = (EClass)((NamedElementView)link.getSource()).getENamedElement();
		newClass = (EClass)((NamedElementView)newNode).getENamedElement();
		oldIndex = oldClass.getESuperTypes().indexOf(superClass);
		oldClass.getESuperTypes().remove(oldIndex);
		if (isSource)
			newClass.getESuperTypes().add(superClass);
		else
			oldClass.getESuperTypes().add(oldIndex, newClass);
	}	
	
	// Link
	if (isSource) {
		oldNode = link.getSource();
		viewIndex = oldNode.getOutgoingConnections().indexOf(link);
		link.setSource(newNode);
	} else {
		oldNode = link.getTarget();
		viewIndex = oldNode.getIncomingConnections().indexOf(link);
		link.setTarget(newNode);
	}
	
	// Reference
	if (link instanceof ReferenceView) {
		ref = ((ReferenceView)link).getEReference();
		oldClass = (EClass)((NamedElementView)oldNode).getENamedElement();
		newClass = (EClass)((NamedElementView)newNode).getENamedElement();
		if (isSource) {
			oldIndex = oldClass.getEStructuralFeatures().indexOf(ref);
			oldClass.getEStructuralFeatures().remove(oldIndex);
			newClass.getEStructuralFeatures().add(ref);
		} else {
			ref.setEType(newClass);
		}
	}
}

public void undo() {
	if (link instanceof ReferenceView) {
		if (isSource) {
			newClass.getEStructuralFeatures().remove(ref);
			oldClass.getEStructuralFeatures().add(oldIndex, ref);
		} else
			ref.setEType(oldClass);
	}
	if (isSource) {
		newNode.getOutgoingConnections().remove(link);
		oldNode.getOutgoingConnections().add(viewIndex, link);
	} else {
		newNode.getIncomingConnections().remove(link);
		oldNode.getIncomingConnections().add(viewIndex, link);
	}
	oldNode = null;
	if (link instanceof InheritanceView) {
		if (isSource)
			newClass.getESuperTypes().remove(superClass);
		else
			oldClass.getESuperTypes().remove(newClass);
		oldClass.getESuperTypes().add(oldIndex, superClass);
	}
}

}
