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
package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.jface.action.*;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.*;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LogicActionBarContributor
	extends org.eclipse.gef.ui.actions.ActionBarContributor
{

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#createActions()
 */
protected void buildActions() {
	addRetargetAction(new UndoRetargetAction());
	addRetargetAction(new RedoRetargetAction());
	addRetargetAction(new DeleteRetargetAction());
	
	addRetargetAction(new IncrementRetargetAction());
	addRetargetAction(new DecrementRetargetAction());
	
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
	addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
	
	addRetargetAction(new ZoomInRetargetAction());
	addRetargetAction(new ZoomOutRetargetAction());
	
	addRetargetAction(new RetargetAction(
			GEFActionConstants.TOGGLE_RULER_VISIBILITY, 
			GEFMessages.ToggleRulerVisibility_Label, IAction.AS_CHECK_BOX));
	
	addRetargetAction(new RetargetAction(
			GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, 
			GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));

	addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
			GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX));

//	addRetargetAction(new DirectEditRetargetAction());
}

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
	addGlobalActionKey(IWorkbenchActionConstants.PRINT);
	addGlobalActionKey(IWorkbenchActionConstants.SELECT_ALL);
}

/**
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
 */
public void contributeToToolBar(IToolBarManager tbm) {
	tbm.add(getAction(IWorkbenchActionConstants.UNDO));
	tbm.add(getAction(IWorkbenchActionConstants.REDO));
	
	tbm.add(new Separator());
	tbm.add(getAction(IncrementDecrementAction.DECREMENT));
	tbm.add(getAction(IncrementDecrementAction.INCREMENT));
	
	tbm.add(new Separator());
	tbm.add(getAction(GEFActionConstants.ALIGN_LEFT));
	tbm.add(getAction(GEFActionConstants.ALIGN_CENTER));
	tbm.add(getAction(GEFActionConstants.ALIGN_RIGHT));
	tbm.add(new Separator());
	tbm.add(getAction(GEFActionConstants.ALIGN_TOP));
	tbm.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
	tbm.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
	
	tbm.add(new Separator());
	String[] zoomStrings = new String[] {	ZoomManager.FIT_ALL, 
											ZoomManager.FIT_HEIGHT, 
											ZoomManager.FIT_WIDTH	};
	tbm.add(new ZoomComboContributionItem(getPage(), zoomStrings));
}

/**
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(IMenuManager)
 */
public void contributeToMenu(IMenuManager menubar) {
	super.contributeToMenu(menubar);
	MenuManager viewMenu = new MenuManager(LogicMessages.ViewMenu_LabelText);
	viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
	viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
	viewMenu.add(new Separator());
	viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
	viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
	viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
	menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
}

}
