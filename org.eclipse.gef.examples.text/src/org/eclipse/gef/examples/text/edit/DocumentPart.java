/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class DocumentPart
	extends BlockTextualPart
	implements TextStyleManager
{

/**
 * @since 3.1
 */
public DocumentPart(Object model) {
	super(model);
}

/**
 * @see AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	installEditPolicy("Text Editing", new BlockEditPolicy());
}

public Object getAdapter(Class key) {
	if (key == TextStyleManager.class)
		return this;
	return super.getAdapter(key);
}

public TextLocation getLocation(Point absolute) {
	return null;
}

public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	return null;
}

public Object getStyleState(String styleID, SelectionRange range) {
	if (styleID.equals(GEFActionConstants.STYLE_BOLD))
		return StyleService.STATE_EDITABLE;
	return StyleService.STATE_READ_ONLY;
}

public Object getStyleValue(String styleID, SelectionRange range) {
	if (styleID.equals(GEFActionConstants.STYLE_BOLD)) {
		TextRun run = (TextRun)range.begin.part.getModel();
		Container container = run.getContainer();
		while (container != null) {
			if (container.getStyle().isBold())
				return Boolean.TRUE;
			container = container.getContainer();
		}
		return Boolean.FALSE;
	}
	return Boolean.FALSE;
}

}