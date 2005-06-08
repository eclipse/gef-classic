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

import java.util.List;

import org.eclipse.gef.examples.ediagram.model.StickyNote;

public class StickyNotePropertySource 
	extends NodePropertySource
{
	
private final PropertyId idText;

public StickyNotePropertySource(String categoryName, StickyNote model) {
	super(categoryName, model);
	idText = new PropertyId(categoryName, "Text");
}

protected void createPropertyDescriptors(List list) {
	super.createPropertyDescriptors(list);
	list.add(new StringPropertyDescriptor(idText));
}

protected StickyNote getStickyNote() {
	return (StickyNote)getNode();
}

public Object getPropertyValue(Object id) {
	if (id == idText)
		return StringPropertyDescriptor.fromModel(getStickyNote().getText());
	return super.getPropertyValue(id);
}

public boolean isPropertyResettable(Object id) {
	return super.isPropertyResettable(id) || (id == idText);
}

public boolean isPropertySet(Object id) {
	if (id == idText)
		return getStickyNote().getText() != null;
	return super.isPropertySet(id);
}

public void resetPropertyValue(Object id) {
	if (id == idText)
		getStickyNote().setText(null);
	else
		super.resetPropertyValue(id);
}

public void setPropertyValue(Object id, Object value) {
	if (id == idText) {
		getStickyNote().setText(StringPropertyDescriptor.toModel(value));
	} else
		super.setPropertyValue(id, value);
}

}