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
package org.eclipse.gef;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @author Pratik Shah
 */
public class ToggleGridAction
	extends Action 
{
	
private GraphicalViewer diagramViewer;

public ToggleGridAction(GraphicalViewer diagramViewer) {
	super(GEFMessages.ToggleGrid_Label, AS_CHECK_BOX);
	this.diagramViewer = diagramViewer;
	setToolTipText(GEFMessages.ToggleGrid_Tooltip);
	setId(GEFActionConstants.TOGGLE_GRID);
	setActionDefinitionId(GEFActionConstants.TOGGLE_GRID);
	setChecked(isChecked());
}

public boolean isChecked() {
	Boolean val = (Boolean)diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
	if (val != null)
		return val.booleanValue();
	return false;
}

public void run() {
	diagramViewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, 
			new Boolean(!isChecked()));
}

}