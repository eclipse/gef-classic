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

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.internal.GEFMessages;

/**
 * @author Pratik Shah
 */
public class ToggleSnapToGeometryAction 
	extends Action 
{
	
private GraphicalViewer diagramViewer;

public ToggleSnapToGeometryAction(GraphicalViewer diagramViewer) {
	super(GEFMessages.ToggleSnapToGeometry_Label, AS_CHECK_BOX);
	this.diagramViewer = diagramViewer;
	setToolTipText(GEFMessages.ToggleSnapToGeometry_Tooltip);
	setId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
	setActionDefinitionId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
	setChecked(isChecked());
}

public boolean isChecked() {
	Boolean val = (Boolean)diagramViewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
	if (val != null)
		return val.booleanValue();
	return false;
}

public void run() {
	diagramViewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, 
			new Boolean(!isChecked()));
}

}