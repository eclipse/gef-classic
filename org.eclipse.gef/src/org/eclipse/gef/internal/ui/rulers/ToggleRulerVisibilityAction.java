/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * This action requires that the given graphical viewer have the ruler visibility
 * property (with RulerProvider.RULER_VISIBILITY as the boolean). 
 * 
 * @author Pratik Shah
 */
public class ToggleRulerVisibilityAction 
	extends Action 
{

protected GraphicalViewer diagramViewer;
	
/**
 * Constructor
 */
public ToggleRulerVisibilityAction(GraphicalViewer diagramViewer) {
	super(GEFMessages.ToggleRulerVisibility_Label, AS_CHECK_BOX);
	this.diagramViewer = diagramViewer;
	setToolTipText(GEFMessages.ToggleRulerVisibility_Tooltip);
	setId(GEFActionConstants.TOGGLE_RULER_VISIBILITY);
	setActionDefinitionId(GEFActionConstants.TOGGLE_RULER_VISIBILITY);
	setChecked(isChecked());
}

public void run() {
	diagramViewer.setProperty(RulerProvider.RULER_VISIBILITY, new Boolean(!isChecked()));
}

public boolean isChecked() {
	return ((Boolean)diagramViewer.getProperty(RulerProvider.RULER_VISIBILITY))
			.booleanValue();
}

}