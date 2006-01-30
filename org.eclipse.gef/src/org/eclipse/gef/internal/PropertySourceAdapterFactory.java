/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.editparts.AbstractEditPart;

public class PropertySourceAdapterFactory implements IAdapterFactory {

public Object getAdapter(Object adaptableObject, Class adapterType) {
	AbstractEditPart part = (AbstractEditPart) adaptableObject;
	Object model = part.getModel();
	if (model instanceof IPropertySource)
		return model;
	if (model instanceof IAdaptable)
		return ((IAdaptable)model).getAdapter(adapterType);
	return null;
}

public Class[] getAdapterList() {
	return new Class[]{IPropertySource.class};
}

}
