/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.editor;

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

import org.eclipse.gef.EditPart;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class GEFPropertySourceProvider extends AdapterFactoryContentProvider
{

public GEFPropertySourceProvider(AdapterFactory adapterFactory) {
	super(adapterFactory);
}

public IPropertySource getPropertySource(Object object) {
	if (object instanceof EditPart)
		object = ((EditPart)object).getModel();
	return super.getPropertySource(object);
}

}
