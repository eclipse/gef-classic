/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     E.D.Willink
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertySource;

public class CompoundPropertySource 
	extends AbstractPropertySource
{
	
private List sources;

public CompoundPropertySource() { }

public CompoundPropertySource(IPropertySource one, IPropertySource two,
		IPropertySource three) {
	add(one);
	add(two);
	add(three);
}

public void add(IPropertySource propertySource) {
	if (propertySource instanceof AbstractPropertySource) {
		if (sources == null)
			sources = new ArrayList();
		sources.add(propertySource);
	}
}

protected void createPropertyDescriptors(List list) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			propertySource.createPropertyDescriptors(list);
		}
}

public Object getPropertyValue(Object id) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			Object result = propertySource.getPropertyValue(id);
			if (result != null)
				return result;
		}
	return null;
}

public boolean isPropertyResettable(Object id) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			if (propertySource.isPropertyResettable(id))
				return true;
		}
	return false;
}

public boolean isPropertySet(Object id) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			if (propertySource.isPropertySet(id))
				return true;
		}
	return false;
}

public void resetPropertyValue(Object id) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			propertySource.resetPropertyValue(id);
		}
}

public void setPropertyValue(Object id, Object value) {
	if (sources != null)
		for (Iterator i = sources.iterator(); i.hasNext(); ) {
			AbstractPropertySource propertySource = (AbstractPropertySource) i.next();
			propertySource.setPropertyValue(id, value);
		}
}

}