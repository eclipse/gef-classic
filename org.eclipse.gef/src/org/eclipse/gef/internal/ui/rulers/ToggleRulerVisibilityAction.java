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

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.RulerComposite;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Pratik Shah
 */
public class ToggleRulerVisibilityAction 
	extends Action 
{

protected int orientation;
protected RulerComposite rulerComp;
protected GraphicalViewer diagramViewer;
	
/**
 * Constructor
 * 
 * @param text
 */
public ToggleRulerVisibilityAction(RulerComposite rulerComp, int orientation, 
                                   GraphicalViewer diagramViewer) {
	super();
	this.rulerComp = rulerComp;
	this.orientation = orientation;
	this.diagramViewer = diagramViewer;
	String id = null;
	if (isHorizontalRuler()) {
		id = GEFActionConstants.TOGGLE_HORIZONTAL_RULER_VISIBILITY;
		setText(GEFMessages.ToggleHRulerVisibility_Label);
		setToolTipText(GEFMessages.ToggleHRulerVisibility_Tooltip);
	} else {
		id = GEFActionConstants.TOGGLE_VERTICAL_RULER_VISIBILITY;
		setText(GEFMessages.ToggleVRulerVisibility_Label);
		setToolTipText(GEFMessages.ToggleVRulerVisibility_Tooltip);
	}
	setId(id);
	setActionDefinitionId(id);
}

public void run() {
	rulerComp.setRulerVisibility(orientation, !rulerComp.isRulerVisible(orientation));
}

public boolean isEnabled() {
	if (isHorizontalRuler()) {
		return diagramViewer.getProperty(RulerProvider.HORIZONTAL) != null;
	} else {
		return diagramViewer.getProperty(RulerProvider.VERTICAL) != null;
	}
}

protected boolean isHorizontalRuler() {
	return orientation == PositionConstants.NORTH;
}

}