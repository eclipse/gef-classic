/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     E.D.Willink - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ETypedElement;

public class ETypedElementPropertySource 
	extends ENamedElementPropertySource
{
	
private final PropertyId idLowerBound;
private final PropertyId idOrdered;
private final PropertyId idType;
private final PropertyId idUnique;
private final PropertyId idUpperBound;

protected final ENamedElementPropertyDescriptor typeDescriptor;

public ETypedElementPropertySource(String categoryName, ETypedElement model, Class typeClass) {
	super(categoryName, model);
	idLowerBound = new PropertyId(categoryName, "LowerBound");
	idOrdered = new PropertyId(categoryName, "Ordered");
	idType = new PropertyId(categoryName, "Type");
	idUnique = new PropertyId(categoryName, "Unique");
	idUpperBound = new PropertyId(categoryName, "Upper Bound");
	typeDescriptor = new ENamedElementPropertyDescriptor(idType, model.eResource(), typeClass);
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new IntegerPropertyDescriptor(idLowerBound));
	list.add(new BooleanPropertyDescriptor(idOrdered));
	list.add(typeDescriptor);
	list.add(new BooleanPropertyDescriptor(idUnique));
	list.add(new IntegerPropertyDescriptor(idUpperBound));
}

protected ETypedElement getETypedElement() {
	return (ETypedElement)getENamedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idLowerBound)
		return IntegerPropertyDescriptor.fromModel(getETypedElement().getLowerBound());
	if (id == idOrdered)
		return BooleanPropertyDescriptor.fromModel(getETypedElement().isOrdered());
	if (id == idType)
		return typeDescriptor.fromModel(getETypedElement().getEType());
	if (id == idUnique)
		return BooleanPropertyDescriptor.fromModel(getETypedElement().isUnique());
	if (id == idUpperBound)
		return IntegerPropertyDescriptor.fromModel(getETypedElement().getUpperBound());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id)
	|| (id == idLowerBound) || (id == idOrdered)
	|| (id == idUnique) || (id == idUpperBound);
}

public boolean isPropertySet(Object id) {
	if (id == idLowerBound)
		return true;
	if (id == idOrdered)
		return true;
	if (id == idType)
		return true;
	if (id == idUnique)
		return true;
	if (id == idUpperBound)
		return true;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idLowerBound)
		getETypedElement().setLowerBound(0);
	else if (id == idOrdered)
		getETypedElement().setOrdered(false);
	else if (id == idUnique)
		getETypedElement().setUnique(false);
	else if (id == idUpperBound)
		getETypedElement().setUpperBound(1);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idLowerBound)
		getETypedElement().setLowerBound(IntegerPropertyDescriptor.toModel(value));
	else if (id == idOrdered)
		getETypedElement().setOrdered(BooleanPropertyDescriptor.toModel(value));
	else if (id == idType)
		getETypedElement().setEType((EClassifier)typeDescriptor.toModel(value));
	else if (id == idUnique)
		getETypedElement().setUnique(BooleanPropertyDescriptor.toModel(value));
	else if (id == idUpperBound)
		getETypedElement().setUpperBound(IntegerPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}