package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.gef.examples.logicdesigner.actions.IncrementDecrementAction;

public class LogicContextMenuProvider
	extends org.eclipse.gef.ContextMenuProvider
{

private ActionRegistry actionRegistry;

public LogicContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
	super(viewer);
	setActionRegistry(registry);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ContextMenuProvider#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
 */
public void buildContextMenu(IMenuManager manager) {
	manager.add(new Separator(GEFActionConstants.GROUP_UNDO));
	manager.add(new Separator(GEFActionConstants.GROUP_EDIT));
	manager.add(new Separator(GEFActionConstants.GROUP_REST));
	manager.add(new Separator(GEFActionConstants.MB_ADDITIONS));
	manager.add(new Separator(GEFActionConstants.GROUP_SAVE));

	IAction action;

	action = getActionRegistry().getAction(GEFActionConstants.UNDO);
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(GEFActionConstants.REDO);
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(IWorkbenchActionConstants.PASTE);
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	action = getActionRegistry().getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	action = getActionRegistry().getAction(IncrementDecrementAction.INCREMENT);
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_REST, action);

	action = getActionRegistry().getAction(IncrementDecrementAction.DECREMENT);
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_REST, action);
	
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
		manager.appendToGroup(GEFActionConstants.GROUP_REST, submenu);

	action = getActionRegistry().getAction(IWorkbenchActionConstants.SAVE);
	manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);

}

private ActionRegistry getActionRegistry() {
	return actionRegistry;
}

private void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
}

}