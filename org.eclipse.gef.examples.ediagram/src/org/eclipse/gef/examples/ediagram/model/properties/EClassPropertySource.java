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

import org.eclipse.emf.ecore.EClass;

public class EClassPropertySource 
	extends EClassifierPropertySource
{
	
private final PropertyId idAbstract;
private final PropertyId idInterface;

public EClassPropertySource(String categoryName, EClass model) {
	super(categoryName, model);
	idAbstract = new PropertyId(categoryName, "Abstract");
	idInterface = new PropertyId(categoryName, "Interface");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new BooleanPropertyDescriptor(idAbstract));
	list.add(new BooleanPropertyDescriptor(idInterface));
}

protected EClass getEClass() {
	return (EClass)getEClassifier();
}

public Object getPropertyValue(Object id) {
	if (id == idAbstract)
		return BooleanPropertyDescriptor.fromModel(getEClass().isAbstract()); //$NON-NLS-1$
	if (id == idInterface)
		return BooleanPropertyDescriptor.fromModel(getEClass().isInterface()); //$NON-NLS-1$
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id) || (id == idAbstract) || (id == idInterface);
}

public boolean isPropertySet(Object id) {
	if (id == idAbstract)
		return true;
	if (id == idInterface)
		return true;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
if (id == idAbstract)
		getEClass().setAbstract(false);
	else if (id == idInterface)
		getEClass().setInterface(false);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
if (id == idAbstract)
		getEClass().setAbstract(BooleanPropertyDescriptor.toModel(value));
	else if (id == idInterface)
		getEClass().setInterface(BooleanPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}