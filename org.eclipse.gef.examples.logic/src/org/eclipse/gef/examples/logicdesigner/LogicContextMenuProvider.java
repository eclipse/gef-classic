package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.*;
import org.eclipse.ui.IWorkbenchActionConstants;

public class LogicContextMenuProvider
	extends org.eclipse.gef.ContextMenuProvider
{

public LogicContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
	super(viewer, registry);
}

public void buildContextMenu(IMenuManager menu) {
	IAction action;

	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, 
						getActionRegistry().getAction(GEFActionConstants.UNDO));
	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, 
						getActionRegistry().getAction(GEFActionConstants.REDO));
	
	action = getActionRegistry().getAction(IWorkbenchActionConstants.PASTE);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	action = getActionRegistry().getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
	action = getActionRegistry().getAction(IncrementDecrementAction.INCREMENT);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
	action = getActionRegistry().getAction(IncrementDecrementAction.DECREMENT);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
	
	// Alignment Actions
	MenuManager submenu = new MenuManager(LogicMessages.AlignmentAction_AlignSubmenu_ActionLabelText);

	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT);
	if (action.isEnabled())
		submenu.add(action);
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER);
	if (action.isEnabled())
		submenu.add(action);
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT);
	if (action.isEnabled())
		submenu.add(action);
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP);
	if (action.isEnabled())
		submenu.add(action);
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE);
	if (action.isEnabled())
		submenu.add(action);
	action = getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM);
	if (action.isEnabled())
		submenu.add(action);

	if (!submenu.isEmpty())
		menu.appendToGroup(GEFActionConstants.GROUP_REST, submenu);

	menu.appendToGroup(GEFActionConstants.GROUP_SAVE, 
						getActionRegistry().getAction(IWorkbenchActionConstants.SAVE));
}

}