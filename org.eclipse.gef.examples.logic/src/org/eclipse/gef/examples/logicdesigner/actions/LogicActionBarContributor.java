package org.eclipse.gef.examples.logicdesigner.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.ui.*;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jface.action.*;

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;


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
	
//	addRetargetAction(new DirectEditRetargetAction());
}

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
	addGlobalActionKey(IWorkbenchActionConstants.PRINT);
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
	tbm.add(new ZoomComboContributionItem(getPage()));
}

/**
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(IMenuManager)
 */
public void contributeToMenu(IMenuManager menubar) {
	super.contributeToMenu(menubar);
	MenuManager viewMenu = new MenuManager("$View");
	viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
	viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
	menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
}

}
