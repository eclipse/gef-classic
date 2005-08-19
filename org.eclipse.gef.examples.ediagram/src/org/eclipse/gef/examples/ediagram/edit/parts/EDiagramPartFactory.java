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
package org.eclipse.gef.examples.ediagram.edit.parts;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import org.eclipse.gef.examples.ediagram.edit.parts.ClassEditPart.PlaceHolderModel;
import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.StickyNote;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EDiagramPartFactory 
	implements EditPartFactory
{
	
private static EDiagramPartFactory FACTORY;
	
public static final EDiagramPartFactory getInstance() {
	if (FACTORY == null)
		FACTORY = new EDiagramPartFactory();
	return FACTORY;
}

private EDiagramPartFactory() {
	super();
}

public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof Diagram)
		return new DiagramEditPart((Diagram)model);
	else if (model instanceof StickyNote)
		return new StickyNoteEditPart((StickyNote)model);
	else if (model instanceof NamedElementView) {
		NamedElementView mdl = (NamedElementView)model;
		ENamedElement element = mdl.getENamedElement();
		if (element instanceof EClass)
			return new ClassEditPart(mdl);
		else if (element instanceof EEnum)
			return new EnumEditPart(mdl);
		else if (element instanceof EDataType)
			return new DataTypeEditPart(mdl);
		else if (element instanceof EPackage)
			return new PackageEditPart(mdl);
	} else if (model instanceof ReferenceView)
		return new ReferenceLinkEditPart((ReferenceView)model);
	else if (model instanceof EOperation)
		return new OperationEditPart((EOperation)model);
	else if (model instanceof EAttribute)
		return new AttributeEditPart((EAttribute)model);
	else if (model instanceof PlaceHolderModel)
		return new CompartmentEditPart((PlaceHolderModel)model);
	else if (model instanceof EEnumLiteral)
		return new EnumLiteralEditPart((EEnumLiteral)model);
	else if (model instanceof InheritanceView)
		return new InheritanceLinkEditPart((InheritanceView)model);
	else if (model instanceof Link)
		return new LinkEditPart((Link)model);
	else if (model instanceof String)
		return new JavaClassEditPart(model);
	return null;
}

}
