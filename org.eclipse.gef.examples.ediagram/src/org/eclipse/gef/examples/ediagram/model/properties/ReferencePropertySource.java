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

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

import org.eclipse.gef.examples.ediagram.model.ReferenceView;

public class ReferencePropertySource extends LinkPropertySource
{
	
private static final String ID_OPP = "Opposite Shown";

public ReferencePropertySource(Object model) {
	super(model);
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new ComboBoxPropertyDescriptor(ID_OPP, ID_OPP, new String[] {"true", "false"}));
}

protected ReferenceView getReference() {
	return (ReferenceView)getModel();
}

public Object getPropertyValue(Object id) {
	if (id == ID_OPP)
		return getReference().isOppositeShown() ? new Integer(0) : new Integer(1);
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id);
}

public boolean isPropertySet(Object id) {
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == ID_OPP)
		getReference().setOppositeShown(((Integer)value).intValue() == 0);
	else
		super.setPropertyValue(id, value);
}

}