/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/** 
 * An action that matches the size of all selected EditPart's Figures to the size
 * of the Primary Selection EditPart's Figure.
 */
public class MatchSizeAction
	extends Action
{

private IWorkbenchPart part;

/**
 * Constructs a <code>MatchSizeAction</code> and associates it with the given
 * part.
 * @param part The workbench part associated with this MatchSizeAction
 */
public MatchSizeAction(IWorkbenchPart part) {
	this.part = part;
	setText(GEFMessages.MatchSizeAction_Label);
	setToolTipText(GEFMessages.MatchSizeAction_Tooltip);
	setId(GEFActionConstants.MATCH_SIZE);
}

/**
 * Returns the height delta between the two bounds. Separated into a method so that 
 * it can be overriden to return 0 in the case of a width-only action.
 * 
 * @param precisePartBounds the precise bounds of the EditPart's Figure to be matched
 * @param precisePrimaryBounds the precise bounds of the Primary Selection EditPart's Figure
 * @return the delta between the two heights to be used in the Request.
 */
protected double getPreciseHeightDelta(PrecisionRectangle precisePartBounds, 
		PrecisionRectangle precisePrimaryBounds) {	
	return precisePrimaryBounds.preciseHeight - precisePartBounds.preciseHeight;
}

private GraphicalEditPart getPrimarySelectionEditPart(List editParts) {
	GraphicalEditPart part = null;
	for (int i = 0; i < editParts.size(); i++) {
		part = (GraphicalEditPart)editParts.get(i);
		if (part.getSelected() == EditPart.SELECTED_PRIMARY)
			return part;
	}
	return null;
}

/**
 * Returns the width delta between the two bounds. Separated into a method so that 
 * it can be overriden to return 0 in the case of a height-only action.
 * 
 * @param precisePartBounds the precise bounds of the EditPart's Figure to be matched
 * @param precisePrimaryBounds the precise bounds of the Primary Selection EditPart's Figure
 * @return the delta between the two widths to be used in the Request.
 */
protected double getPreciseWidthDelta(PrecisionRectangle precisePartBounds, 
		PrecisionRectangle precisePrimaryBounds) {
	return precisePrimaryBounds.preciseWidth - precisePartBounds.preciseWidth;
}

/**
 * Executes this action, cycling through the selected EditParts in the Action's viewer,
 * and matching the size of the selected EditPart's Figures to that of the Primary
 * Selection's Figure.
 */
public void run() {
	GraphicalViewer viewer = (GraphicalViewer)part.getAdapter(GraphicalViewer.class);
	if (viewer != null) {
		
		List editParts = viewer.getSelectedEditParts();
		
		GraphicalEditPart primarySelection = getPrimarySelectionEditPart(editParts);

		GraphicalEditPart part = null;
		ChangeBoundsRequest request = null;
		Command command = null;
		PrecisionDimension preciseDimension = null;
		PrecisionRectangle precisePartBounds = null;
		
		PrecisionRectangle precisePrimaryBounds = new PrecisionRectangle(primarySelection
				.getFigure().getBounds().getCopy());
		primarySelection.getFigure().translateToAbsolute(precisePrimaryBounds);
		
		
		for (int i = 0; i < editParts.size(); i++) {
			part = (GraphicalEditPart)editParts.get(i);
			if (!part.equals(primarySelection)) {
				request = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
				
				precisePartBounds = new PrecisionRectangle(part.getFigure().getBounds().getCopy());
				part.getFigure().translateToAbsolute(precisePartBounds);
				
				preciseDimension = new PrecisionDimension();
				preciseDimension.preciseWidth = getPreciseWidthDelta(precisePartBounds, 
						precisePrimaryBounds);
				preciseDimension.preciseHeight = getPreciseHeightDelta(precisePartBounds, 
						precisePrimaryBounds);
				preciseDimension.updateInts();
				
				request.setSizeDelta(preciseDimension);
				
				command = part.getCommand(request);
				if (command != null && command.canExecute())
					viewer.getEditDomain().getCommandStack().execute(command);
			}
		}
		
	}
}

}