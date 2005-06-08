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

import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class IntegerPropertyDescriptor extends TextPropertyDescriptor
{

public IntegerPropertyDescriptor(PropertyId propertyId) {
	super(propertyId, propertyId.getDisplayName());
	setCategory(propertyId.getCategoryName());
}

public static String fromModel(int i) {
	return String.valueOf(i);
}

public static int toModel(Object string) {
	try {
		return Integer.parseInt((String)string);
	} catch (NumberFormatException e) {	// FIXME logException
		return 0;
	}
}

}