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
package org.eclipse.gef.examples.text.actions;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.PositionConstants;

public class MultiStyleAction 
	extends BooleanStyleAction
{
	
private Object value;

public MultiStyleAction(StyleService service, String styleID, String property, 
		Object value) {
	this(service, styleID, property, value, SWT.CHECK);
}

public MultiStyleAction(StyleService service, String styleID, String property, 
		Object value, int style) {
	super(service, styleID, property, style);
	this.value = value;
}

public void run() {
	service.setStyle(property, isChecked() ? value : new Integer(PositionConstants.NONE));
}

public void refresh() {
	setChecked(value.equals(service.getStyle(property)));
	setEnabled(service.getStyleState(property).equals(StyleService.STATE_EDITABLE));
}

}