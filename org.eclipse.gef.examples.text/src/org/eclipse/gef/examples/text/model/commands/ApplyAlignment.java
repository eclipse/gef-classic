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

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.ModelLocation;
import org.eclipse.gef.examples.text.model.Style;

public class ApplyAlignment 
	extends MiniEdit
{

private int alignment = -1;
private int oldAlignment = -1;
private Style style;
private ModelLocation resultingLocation;

public ApplyAlignment(Container c, String styleID, Object value, ModelLocation result) {
	style = c.getStyle();
	if (Boolean.FALSE.equals(value))
		alignment = PositionConstants.NONE;
	else if (GEFActionConstants.BLOCK_ALIGN_LEFT.equals(styleID))
		alignment = PositionConstants.LEFT;
	else if (GEFActionConstants.BLOCK_ALIGN_RIGHT.equals(styleID))
		alignment = PositionConstants.RIGHT;
	else if (GEFActionConstants.BLOCK_ALIGN_CENTER.equals(styleID))
		alignment = PositionConstants.CENTER;
	resultingLocation = result;
}

public boolean canApply() {
	return alignment != -1;
}

public void apply() {
	oldAlignment = style.getAlignment(); 
	style.setAlignment(alignment);
}

public ModelLocation getResultingLocation() {
	return resultingLocation;
}

public void rollback() {
	style.setAlignment(oldAlignment);
}

}