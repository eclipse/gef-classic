/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;

import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Pratik Shah
 */
public class ToggleGridVisibilityAction
	extends Action 
{
	
private GraphicalViewer diagramViewer;

public ToggleGridVisibilityAction(GraphicalViewer diagramViewer) {
	super(GEFMessages.ToggleGrid_Label, AS_CHECK_BOX);
	this.diagramViewer = diagramViewer;
	setToolTipText(GEFMessages.ToggleGrid_Tooltip);
	setId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
	setActionDefinitionId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
	setChecked(isChecked());
}

public boolean isChecked() {
	Boolean val = (Boolean)diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
	if (val != null)
		return val.booleanValue();
	return false;
}

public boolean isEnabled() {
	boolean enabled = true;
	Dimension spacing = (Dimension)diagramViewer.getProperty(
			SnapToGrid.PROPERTY_GRID_SPACING);
	if (spacing != null)
		enabled = spacing.width >= 0 || spacing.height >= 0;
	return enabled;
}

public void run() {
	diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, 
			new Boolean(!isChecked()));
}

}