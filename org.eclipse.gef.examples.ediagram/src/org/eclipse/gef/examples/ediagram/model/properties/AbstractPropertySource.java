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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;

public abstract class AbstractPropertySource 
	implements IPropertySource2
{
public static final String READ_ONLY = " (Read-Only)";
	
private IPropertyDescriptor[] descriptors;

public AbstractPropertySource() {
}

protected abstract void createPropertyDescriptors(List list);

public Object getEditableValue() {
	return this;
}

public IPropertyDescriptor[] getPropertyDescriptors() {
	if (descriptors == null) {
		ArrayList list = new ArrayList();
		createPropertyDescriptors(list);
		descriptors = new IPropertyDescriptor[list.size()];
		list.toArray(descriptors);
		return descriptors;
	}
	return descriptors;
}

}