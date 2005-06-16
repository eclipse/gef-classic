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
package org.eclipse.gef.examples.text.model.commands;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.Style;

public class ApplyMultiStyle
	extends MiniEdit
{

private int oldValue, newValue = -1;
private String styleID;
private Style style;

public ApplyMultiStyle(Container c, String property, Object value) {
	style = c.getStyle();
	styleID = property;
	newValue = ((Integer)value).intValue();
}

public boolean canApply() {
	return newValue != -1;
}

public void apply() {
	if (Style.PROPERTY_ALIGNMENT.equals(styleID)) {
		oldValue = style.getAlignment(); 
		style.setAlignment(newValue);
	} else if (Style.PROPERTY_ORIENTATION.equals(styleID)) {
		oldValue = style.getOrientation();
		style.setOrientation(newValue);
	}
}

public ModelLocation getResultingLocation() {
	return null;
}

public void rollback() {
	if (Style.PROPERTY_ALIGNMENT.equals(styleID))
		style.setAlignment(oldValue);
	else if (Style.PROPERTY_ORIENTATION.equals(styleID))
		style.setOrientation(oldValue);
}

}