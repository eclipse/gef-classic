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

public class EClassifierPropertySource 
	extends ENamedElementPropertySource
{
	
private final PropertyId idDefaultValue;
private final PropertyId idInstanceClassName;
private final PropertyId idPackage;

public EClassifierPropertySource(String categoryName, EClassifier model) {
	super(categoryName, model);
	idDefaultValue = new PropertyId(categoryName + READ_ONLY, "Default Value");
	idInstanceClassName = new PropertyId(categoryName, "Instance Class Name");
	idPackage = new PropertyId(categoryName + READ_ONLY, "Package");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new ReadOnlyPropertyDescriptor(idDefaultValue));
	list.add(new StringPropertyDescriptor(idInstanceClassName));
	list.add(new ReadOnlyPropertyDescriptor(idPackage));
}

protected EClassifier getEClassifier() {
	return (EClassifier)getENamedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idDefaultValue)
		return ReadOnlyPropertyDescriptor.fromModel(getEClassifier().getDefaultValue());
	if (id == idInstanceClassName)
		return StringPropertyDescriptor.fromModel(getEClassifier().getInstanceClassName());
	if (id == idPackage)
		return ReadOnlyPropertyDescriptor.fromModel(getEClassifier().getEPackage().getName());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id) || (id == idInstanceClassName);
}

public boolean isPropertySet(Object id) {
	if (id == idInstanceClassName)
		return getEClassifier().getInstanceClassName() != null;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idInstanceClassName)
		getEClassifier().setInstanceClassName(null);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idInstanceClassName)
		getEClassifier().setInstanceClassName(StringPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}