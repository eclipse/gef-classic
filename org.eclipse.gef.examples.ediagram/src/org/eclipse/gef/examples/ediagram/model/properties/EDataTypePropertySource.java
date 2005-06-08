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

import org.eclipse.emf.ecore.EDataType;

public class EDataTypePropertySource 
	extends EClassifierPropertySource
{
	
private final PropertyId idSerializable;

public EDataTypePropertySource(String categoryName, EDataType model) {
	super(categoryName, model);
	idSerializable = new PropertyId(categoryName, "Serializable");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new BooleanPropertyDescriptor(idSerializable));
}

protected EDataType getEDataType() {
	return (EDataType)getENamedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idSerializable)
		return BooleanPropertyDescriptor.fromModel(getEDataType().isSerializable());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id) || (id == idSerializable);
}

public boolean isPropertySet(Object id) {
	if (id == idSerializable)
		return true;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idSerializable)
		getEDataType().setSerializable(true);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idSerializable)
		getEDataType().setSerializable(BooleanPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}