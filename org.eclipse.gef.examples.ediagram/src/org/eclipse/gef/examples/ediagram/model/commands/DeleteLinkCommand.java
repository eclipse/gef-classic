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
public class DeleteLinkCommand 
	extends Command
{
	
private Link link;
private Node src, target;
private int srcIndex, targetIndex, superIndex, parentIndex;
private boolean isHardDelete;
private EClass superClass, subClass;
private EReference reference;
private EClass parent;

public DeleteLinkCommand(Link link, boolean isHardDelete) {
	super("Delete Connection");
	this.link = link;
	this.isHardDelete = isHardDelete;
	src = link.getSource();
	target = link.getTarget();
}

public boolean canExecute() {
	return link != null && src != null && target != null;
}

/*
 * This should work even if the link (view) was deleted.  Eg., user selects a class and
 * one of its references, and hits Ctrl+Del.  The class will be deleted first and it
 * will delete all its references.  When this command is executed, it should do the
 * hard-delete part.
 */
public void execute() {
	if (isHardDelete && link instanceof InheritanceView) {
		subClass = (EClass)((NamedElementView)src).getENamedElement();
		superClass = (EClass)((NamedElementView)target).getENamedElement();		
		superIndex = subClass.getESuperTypes().indexOf(superClass);
		if (superIndex != -1)
			subClass.getESuperTypes().remove(superIndex);
	}
	srcIndex = src.getOutgoingConnections().indexOf(link);
	targetIndex = target.getIncomingConnections().indexOf(link);
	if (srcIndex != -1 && targetIndex != -1) {
		src.getOutgoingConnections().remove(srcIndex);
		target.getIncomingConnections().remove(targetIndex);
	}
	if (isHardDelete && link instanceof ReferenceView) {
		reference = ((ReferenceView)link).getEReference();
		parent = reference.getEContainingClass();
		parentIndex = parent.getEStructuralFeatures().indexOf(reference);
		if (parentIndex != -1)
			parent.getEStructuralFeatures().remove(parentIndex);
	}
}

public void undo() {
	if (isHardDelete && link instanceof ReferenceView) {
		if (parentIndex != -1)
			parent.getEStructuralFeatures().add(parentIndex, reference);
	}
	if (srcIndex != -1 && targetIndex != -1) {
		src.getOutgoingConnections().add(srcIndex, link);
		target.getIncomingConnections().add(targetIndex, link);
	}
	if (isHardDelete && link instanceof InheritanceView) {
		if (superIndex != -1)
			subClass.getESuperTypes().add(superIndex, superClass);
	}
}

}