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

import java.util.List;

public class CompoundPropertySource 
	extends BasePropertySource
{
	
private BasePropertySource prop1, prop2;

public CompoundPropertySource(BasePropertySource ediagramPropSrc, 
		BasePropertySource ecorePropSrc) {
	super(null);
	prop1 = ediagramPropSrc;
	prop2 = ecorePropSrc;
}

protected void createPropertyDescriptors(List list) {
	prop1.createPropertyDescriptors(list);
	prop2.createPropertyDescriptors(list);
}

public Object getPropertyValue(Object id) {
	Object result = prop1.getPropertyValue(id);
	if (result == null)
		result = prop2.getPropertyValue(id);
	return result;
}

public boolean isPropertyResettable(Object id) {
	return prop1.isPropertyResettable(id) || prop2.isPropertyResettable(id);
}

public boolean isPropertySet(Object id) {
	return prop1.isPropertySet(id) || prop2.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	prop1.resetPropertyValue(id);
	prop2.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	prop1.setPropertyValue(id, value);
	prop2.setPropertyValue(id, value);
}

}