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

import org.eclipse.emf.ecore.EPackage;

public class EPackagePropertySource 
	extends ENamedElementPropertySource
{
	
private final PropertyId idNsPrefix;
private final PropertyId isNsURI;

public EPackagePropertySource(String categoryName, EPackage model) {
	super(categoryName, model);
	idNsPrefix = new PropertyId(categoryName, "Namespace Prefix");
	isNsURI = new PropertyId(categoryName, "Namespace URI");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new StringPropertyDescriptor(idNsPrefix));
	list.add(new StringPropertyDescriptor(isNsURI));
}

protected EPackage getEPackage() {
	return (EPackage)getENamedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idNsPrefix)
		return StringPropertyDescriptor.fromModel(getEPackage().getNsPrefix());
	if (id == isNsURI)
		return StringPropertyDescriptor.fromModel(getEPackage().getNsURI());
	return super.getPropertyValue(id);
}

public boolean isPropertySet(Object id) {
	if (id == idNsPrefix)
		return true;
	if (id == isNsURI)
		return true;
	return super.isPropertySet(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idNsPrefix)
		getEPackage().setNsPrefix(StringPropertyDescriptor.toModel(value));
	else if (id == isNsURI)
		getEPackage().setNsURI(StringPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}