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

public class MultiStyleAction extends BooleanStyleAction {

	private Object value;

	public MultiStyleAction(StyleService service, String styleID,
			String property, Object value) {
		super(service, styleID, property);
		this.value = value;
	}

	public void run() {
		service.setStyle(property, isChecked() ? value : new Integer(0));
	}

	public void refresh() {
		setChecked(value.equals(service.getStyle(property)));
		setEnabled(service.getStyleState(property).equals(
				StyleService.STATE_EDITABLE));
	}

}