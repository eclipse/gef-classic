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
package org.eclipse.gef.examples.ediagram.outline;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import org.eclipse.gef.examples.ediagram.model.Diagram;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EDiagramTreePartFactory 
	implements EditPartFactory
{
	
private static EDiagramTreePartFactory factory;

private EDiagramTreePartFactory() {
	super();
}

public static final EDiagramTreePartFactory getInstance() {
	if (factory == null)
		factory = new EDiagramTreePartFactory();
	return factory;
}

public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof Diagram)
		return new DiagramTreeEditPart((Diagram)model);
	else if (model instanceof EPackage)
		return new PackageTreeEditPart((EPackage)model);
	else if (model instanceof EClassifier)
		return new ClassifierTreeEditPart((EClassifier)model);
	else if (model instanceof EReference)
		return new ReferenceTreeEditPart((EReference)model);
	else if (model instanceof InheritanceModel)
		return new InheritanceTreeEditPart((InheritanceModel)model);
	return null;
}

}