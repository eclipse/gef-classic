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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.util.Utilities;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DeleteCommand
	extends Command
{

private boolean isHardDelete;
private Command delegate;

public static final String KEY_PERM_DELETE = "gef.ediagram.$perm"; //$NON-NLS-1$

public DeleteCommand(boolean hardDelete) {
	super("Delete");
	isHardDelete = hardDelete;
}

public boolean canExecute() {
	return delegate != null && delegate.canExecute();
}

public void execute() {
	delegate.execute();
}

public Command setPartToBeDeleted(Object model) {
	if (model instanceof NamedElementView) {
		ENamedElement elem = ((NamedElementView)model).getENamedElement();
		if (elem instanceof EClassifier)
			delegate = new DeleteClassifierCommand(model, isHardDelete);
		else if (elem instanceof EPackage)
			delegate = new DeletePackageCommand(model, isHardDelete);
	} else if (model instanceof Node)
		delegate = new DeleteNodeCommand(model);
	else if (model instanceof EAttribute)
		delegate = new DeleteAttributeCommand(model);
	else if (model instanceof EOperation)
		delegate = new DeleteOperationCommand(model);
	else if (model instanceof EEnumLiteral)
		delegate = new DeleteEnumLiteralCommand(model);
	else
		delegate = null;
	return this;
}

public void undo() {
	delegate.undo();
}

private static class DeleteNodeCommand extends Command {
	protected Map incomingConnectionsToSource;
	protected Map outgoingConnectionsToTarget;
	protected Diagram diagram;
	protected Node node;
	protected int index;
	public DeleteNodeCommand(Object node) {
		this.node = (Node)node;
		diagram = this.node.getDiagram();
	}
	public void execute() {
		index = diagram.getContents().indexOf(node);
		// There's always the possibility that this node has already been deleted by the
		// time this command is executed (eg., when the package containing this class
		// is also selected).  Hence, we check to see if the node exists in the diagram.
		// If not, do nothing.
		if (index == -1)
			return;
		incomingConnectionsToSource = new HashMap();
		outgoingConnectionsToTarget = new HashMap();
		for (Iterator iter = node.getIncomingConnections().iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			incomingConnectionsToSource.put(link, link.getSource());
			link.setSource(null);
		}
		for (Iterator iter = node.getOutgoingConnections().iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			outgoingConnectionsToTarget.put(link, link.getTarget());
			link.setTarget(null);
		}
		node.getIncomingConnections().clear();
		node.getOutgoingConnections().clear();
		diagram.getContents().remove(index);
	}
	public void undo() {
		if (index == -1)
			return;
		diagram.getContents().add(index, node);
		for (Iterator iter = incomingConnectionsToSource.keySet().iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			link.setTarget(node);
			link.setSource((Node)incomingConnectionsToSource.get(link));
		}
		for (Iterator iter = outgoingConnectionsToTarget.keySet().iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			link.setTarget((Node)outgoingConnectionsToTarget.get(link));
			link.setSource(node);
		}
		outgoingConnectionsToTarget = null;
		incomingConnectionsToSource = null;
	}
}

private static class DeleteAttributeCommand extends Command {
	private EAttribute attribute;
	private EClass parent;
	private int index;
	public DeleteAttributeCommand(Object attrib) {
		attribute = (EAttribute)attrib;		
		parent = attribute.getEContainingClass();
	}
	public void execute() {
		index = parent.getEStructuralFeatures().indexOf(attribute);
		parent.getEStructuralFeatures().remove(index);
	}
	public void undo() {
		parent.getEStructuralFeatures().add(index, attribute);
	}
}

private static class DeleteEnumLiteralCommand extends Command {
	private EEnumLiteral literal;
	private EEnum parent;
	private int index;
	public DeleteEnumLiteralCommand(Object model) {
		literal = (EEnumLiteral)model;
		parent = literal.getEEnum();
	}
	public void execute() {
		index = parent.getELiterals().indexOf(literal);
		parent.getELiterals().remove(index);
	}
	public void undo() {
		parent.getELiterals().add(index, literal);
	}
}

private static class DeleteOperationCommand extends Command {
	private EOperation operation;
	private EClass container;
	private int index;
	public DeleteOperationCommand(Object oper) {
		operation = (EOperation)oper;
		container = operation.getEContainingClass();
	}
	public void execute() {
		index = container.getEOperations().indexOf(operation);
		container.getEOperations().remove(index);
	}
	public void undo() {
		container.getEOperations().add(index, operation);
	}
}

private static class DeletePackageCommand extends DeleteNodeCommand {
	protected NamedElementView view;
	protected EPackage pckg, superPackage;
	protected Resource eResource;
	protected int packageIndex, importsIndex;
	protected boolean isHardDelete;
	protected Command chain;
	public DeletePackageCommand(Object namedElementView, boolean isHardDelete) {
		super(namedElementView);
		this.view = (NamedElementView)namedElementView;
		this.isHardDelete = isHardDelete;
		pckg = (EPackage)this.view.getENamedElement();
	}
	protected Command createChainCommands() {
		CompoundCommand cmd = null;
		for (Iterator iter = pckg.getESubpackages().iterator(); iter.hasNext();) {
			EPackage subPackage = (EPackage) iter.next();
			NamedElementView packageView = 
					(NamedElementView)Utilities.findViewFor(subPackage, diagram);
			if (packageView != null) {
				if (cmd == null)
					cmd = new CompoundCommand();
				cmd.add(new DeletePackageCommand(packageView, true));
			}
		}
		for (Iterator iter = pckg.getEClassifiers().iterator(); iter.hasNext();) {
			NamedElementView classifierView = 
					(NamedElementView)Utilities.findViewFor(iter.next(), diagram);
			if (classifierView != null) {
				if (cmd == null)
					cmd = new CompoundCommand();
				cmd.add(new DeleteClassifierCommand(classifierView, false));
			}
		}
		return cmd;
	}
	public void execute() {
		super.execute();
		if (!isHardDelete || index == -1)
			return;
		importsIndex = diagram.getImports().indexOf(pckg);
		// If importsIndex is -1 that means that one of pckg's super packages is imported.
		// In that case, we just remove it from its superpackage.
		if (importsIndex != -1)
			diagram.getImports().remove(importsIndex);
		superPackage = pckg.getESuperPackage();
		if (superPackage != null) {
			packageIndex = superPackage.getESubpackages().indexOf(pckg);
			superPackage.getESubpackages().remove(packageIndex);
		} else {
			packageIndex = (eResource = pckg.eResource()).getContents().indexOf(pckg);
			eResource.getContents().remove(packageIndex);
		}
		chain = createChainCommands();
		if (chain != null)
			chain.execute();
	}
	public void undo() {
		if (index == -1)
			return;
		if (isHardDelete) {
			if (chain != null)
				chain.undo();
			if (superPackage != null)
				superPackage.getESubpackages().add(packageIndex, pckg);
			else
				eResource.getContents().add(packageIndex, pckg);
			if (importsIndex != -1)
				diagram.getImports().add(importsIndex, pckg);
		}
		super.undo();
	}
}

private static class DeleteClassifierCommand extends DeleteNodeCommand {
	protected NamedElementView view;
	protected EClassifier classifier;
	protected EPackage container;
	protected int classifierIndex;
	protected boolean isHardDelete;
	public DeleteClassifierCommand(Object namedElementView, boolean deletePerm) {
		super(namedElementView);
		view = (NamedElementView)namedElementView;
		isHardDelete = deletePerm;
		classifier = (EClassifier)view.getENamedElement();
		container = classifier.getEPackage();
	}
	public void execute() {
		super.execute();
		if (index == -1)
			return;
		if (!isHardDelete)
			return;
		classifierIndex = container.getEClassifiers().indexOf(classifier);
		container.getEClassifiers().remove(classifierIndex);
	}
	public void undo() {
		if (index == -1)
			return;
		super.undo();
		if (!isHardDelete)
			return;
		container.getEClassifiers().add(classifierIndex, classifier);
	}
}
	
}
