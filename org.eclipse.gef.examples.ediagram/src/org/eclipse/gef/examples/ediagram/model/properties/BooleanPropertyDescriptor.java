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

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

public class BooleanPropertyDescriptor extends ComboBoxPropertyDescriptor
{
public static final Integer falseInteger = new Integer(0);
public static final Integer trueInteger = new Integer(1);
public static final String[] booleanValues = new String[] {"false", "true" };

public BooleanPropertyDescriptor(PropertyId propertyId) {
	super(propertyId, propertyId.getDisplayName(), booleanValues);
	setCategory(propertyId.getCategoryName());
}

public static Integer fromModel(boolean b) {
	return b ? trueInteger : falseInteger;
}

public static boolean toModel(Object integer) {
	return ((Integer)integer).intValue() != 0;
}

}