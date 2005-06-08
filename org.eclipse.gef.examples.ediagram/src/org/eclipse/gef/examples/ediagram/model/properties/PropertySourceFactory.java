/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     E.D.Willink
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.StickyNote;

public class PropertySourceFactory
{
public static final String CATEGORY_DIAGRAM = "Diagram";
public static final String CATEGORY_ECORE = "Ecore";
public static final String CATEGORY_EOPPOSITE = "Ecore (EOpposite)";
	
private PropertySourceFactory() {
	super();
}

public static IPropertySource getPropertySource(Object model) {
	if (model instanceof NamedElementView)
		return new CompoundPropertySource(
				new NodePropertySource(CATEGORY_DIAGRAM, model), 
				getPropertySource(((NamedElementView)model).getENamedElement()), 
				null);
	if (model instanceof StickyNote)
		return new StickyNotePropertySource(CATEGORY_DIAGRAM, (StickyNote)model);
	if (model instanceof Node)
		return new NodePropertySource(CATEGORY_DIAGRAM, model);
	if (model instanceof ReferenceView) {
		EReference opp = ((ReferenceView)model).getEReference().getEOpposite();
		IPropertySource third = null;
		if (opp != null)
			third = new EStructuralFeaturePropertySource(
					CATEGORY_EOPPOSITE, opp, EClassifier.class);
		return new CompoundPropertySource(
				new ReferencePropertySource(CATEGORY_DIAGRAM, model),
				getPropertySource(((ReferenceView)model).getEReference()),
				third);
	}
	if (model instanceof Link)
		return new LinkPropertySource(CATEGORY_DIAGRAM, model);
	if (model instanceof EEnumLiteral)
		return new EEnumLiteralPropertySource(CATEGORY_ECORE, (EEnumLiteral)model);
	if (model instanceof EAttribute)
		return new EAttributePropertySource(CATEGORY_ECORE, (EAttribute)model);
	if (model instanceof EStructuralFeature)
		return new EStructuralFeaturePropertySource(
				CATEGORY_ECORE, (EStructuralFeature)model, EClassifier.class);
	if (model instanceof EClass)
		return new EClassPropertySource(CATEGORY_ECORE, (EClass)model);
	if (model instanceof EDataType)
		return new EDataTypePropertySource(CATEGORY_ECORE, (EDataType) model);
	if (model instanceof EPackage)
		return new EPackagePropertySource(CATEGORY_ECORE, (EPackage) model);
	if (model instanceof EClassifier)
		return new EClassifierPropertySource(CATEGORY_ECORE, (EClassifier) model);
	if (model instanceof ETypedElement)
		return new ETypedElementPropertySource(
				CATEGORY_ECORE, (ETypedElement)model, EClassifier.class);
	if (model instanceof ENamedElement)
		return new ENamedElementPropertySource(CATEGORY_ECORE, (ENamedElement)model);
	return null;
}

}