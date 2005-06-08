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

import org.eclipse.emf.ecore.EEnumLiteral;

public class EEnumLiteralPropertySource 
	extends ENamedElementPropertySource
{
private final PropertyId idValue;

public EEnumLiteralPropertySource(String categoryName, EEnumLiteral model) {
	super(categoryName, model);
	idValue = new PropertyId(categoryName, "Value");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new IntegerPropertyDescriptor(idValue));
}

protected EEnumLiteral getEEnumLiteral() {
	return (EEnumLiteral)getENamedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idValue)
		return IntegerPropertyDescriptor.fromModel(getEEnumLiteral().getValue());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id) || (id == idValue);
}

public boolean isPropertySet(Object id) {
	if (id == idValue)
		return true;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idValue)
		getEEnumLiteral().setValue(0);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idValue)
		getEEnumLiteral().setValue(IntegerPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}