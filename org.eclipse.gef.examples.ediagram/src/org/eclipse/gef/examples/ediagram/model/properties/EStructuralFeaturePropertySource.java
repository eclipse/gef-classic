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

import org.eclipse.emf.ecore.EStructuralFeature;

public class EStructuralFeaturePropertySource 
	extends ETypedElementPropertySource
{
	
private final PropertyId idChangeable;
private final PropertyId idDefaultValueLiteral;
private final PropertyId idDerived;
private final PropertyId idTransient;
private final PropertyId idUnsettable;
private final PropertyId idVolatile;

public EStructuralFeaturePropertySource(String categoryName, EStructuralFeature model, Class typeClass) {
	super(categoryName, model, typeClass);
	idChangeable = new PropertyId(categoryName, "Changeable");
	idDefaultValueLiteral = new PropertyId(categoryName, "Default Value Literal");
	idDerived = new PropertyId(categoryName, "Derived");
	idTransient = new PropertyId(categoryName, "Transient");
	idUnsettable = new PropertyId(categoryName, "Unsettable");
	idVolatile = new PropertyId(categoryName, "Volatile");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new BooleanPropertyDescriptor(idChangeable));
	list.add(new StringPropertyDescriptor(idDefaultValueLiteral));
	list.add(new BooleanPropertyDescriptor(idDerived));
	list.add(new BooleanPropertyDescriptor(idTransient));
	list.add(new BooleanPropertyDescriptor(idUnsettable));
	list.add(new BooleanPropertyDescriptor(idVolatile));
}

protected EStructuralFeature getEStructuralFeature() {
	return (EStructuralFeature)getETypedElement();
}

public Object getPropertyValue(Object id) {
	if (id == idChangeable)
		return BooleanPropertyDescriptor.fromModel(getEStructuralFeature().isChangeable());
	if (id == idDefaultValueLiteral)
		return StringPropertyDescriptor.fromModel(getEStructuralFeature().getDefaultValueLiteral());
	if (id == idDerived)
		return BooleanPropertyDescriptor.fromModel(getEStructuralFeature().isDerived());
	if (id == idTransient)
		return BooleanPropertyDescriptor.fromModel(getEStructuralFeature().isTransient());
	if (id == idUnsettable)
		return BooleanPropertyDescriptor.fromModel(getEStructuralFeature().isUnsettable());
	if (id == idVolatile)
		return BooleanPropertyDescriptor.fromModel(getEStructuralFeature().isVolatile());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id)
	|| (id == idChangeable) || (id == idDerived) || (id == idDefaultValueLiteral)
	|| (id == idTransient) || (id == idUnsettable) || (id == idVolatile);
}

public boolean isPropertySet(Object id) {
	if (id == idChangeable)
		return true;
	if (id == idDefaultValueLiteral)
		return true;
	if (id == idDerived)
		return true;
	if (id == idTransient)
		return true;
	if (id == idUnsettable)
		return true;
	if (id == idVolatile)
		return true;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idChangeable)
		getEStructuralFeature().setChangeable(false);
	else if (id == idDefaultValueLiteral)
		getEStructuralFeature().setDefaultValueLiteral(null);
	else if (id == idDerived)
		getEStructuralFeature().setDerived(false);
	else if (id == idTransient)
		getEStructuralFeature().setTransient(false);
	else if (id == idUnsettable)
		getEStructuralFeature().setUnsettable(false);
	else if (id == idVolatile)
		getEStructuralFeature().setVolatile(false);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idDerived)
		getEStructuralFeature().setChangeable(BooleanPropertyDescriptor.toModel(value));
	else if (id == idDefaultValueLiteral)
		getEStructuralFeature().setDefaultValueLiteral(StringPropertyDescriptor.toModel(value));
	else if (id == idDerived)
		getEStructuralFeature().setDerived(BooleanPropertyDescriptor.toModel(value));
	else if (id == idTransient)
		getEStructuralFeature().setTransient(BooleanPropertyDescriptor.toModel(value));
	else if (id == idUnsettable)
		getEStructuralFeature().setUnsettable(BooleanPropertyDescriptor.toModel(value));
	else if (id == idUnsettable)
		getEStructuralFeature().setVolatile(BooleanPropertyDescriptor.toModel(value));
	else
		super.setPropertyValue(id, value);
}

}