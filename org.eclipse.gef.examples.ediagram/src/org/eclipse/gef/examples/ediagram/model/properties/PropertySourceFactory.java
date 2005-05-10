/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

public class PropertySourceFactory
{
	
private PropertySourceFactory() {
	super();
}

public static IPropertySource getPropertySource(Object model) {
	if (model instanceof Node)
		return new NodePropertySource(model);
	if (model instanceof ReferenceView)
		return new ReferencePropertySource(model);
	if (model instanceof Link)
		return new LinkPropertySource(model);
	return null;
}

}