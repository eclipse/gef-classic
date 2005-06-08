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

/**
 * A PropertyId combines the category name and display name for a
 * IPropertyDescriptor in a single object. The same set of diaplayNames
 * may therefore appear more than once in a property list as is necessary
 * to display both ends of an EReference.
 */
public class PropertyId
{
private final String categoryName;
private final String displayName;

public PropertyId(String categoryName, String displayName) {
	this.categoryName = categoryName;
	this.displayName = displayName;
}

public String getCategoryName() { return categoryName; }
public String getDisplayName() { return displayName; }

public String toString() { return categoryName + " : " + displayName; }

}