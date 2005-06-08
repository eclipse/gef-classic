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

import org.eclipse.emf.ecore.ENamedElement;

public class ENamedElementPropertySource 
	extends ModelPropertySource
{
	
protected final PropertyId idName;

public ENamedElementPropertySource(String categoryName, ENamedElement eNamedElement) {
	super(eNamedElement);
	idName = new PropertyId(categoryName, "Name");
}

protected void createPropertyDescriptors(List list) {
	list.add(new StringPropertyDescriptor(idName));
}

protected ENamedElement getENamedElement() {
	return (ENamedElement)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == idName)
		return StringPropertyDescriptor.fromModel(getENamedElement().getName());
	return null;
}

public boolean isPropertyResettable(Object id) {
	return id == idName;
}

public boolean isPropertySet(Object id) {
	if (id == idName)
		return getENamedElement().getName() != null;
	return false;
}

public void resetPropertyValue(Object id) {
	if (id == idName)
		getENamedElement().setName(null);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idName)
		getENamedElement().setName(StringPropertyDescriptor.toModel(value));
}

}