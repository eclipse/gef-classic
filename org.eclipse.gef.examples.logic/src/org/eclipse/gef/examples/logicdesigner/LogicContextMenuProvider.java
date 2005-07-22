/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

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
	GEFActionConstants.addStandardActionGroups(manager);

	IAction action;

	action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(ActionFactory.REDO.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
	if (action.isEnabled())
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
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
		
	submenu.add(new Separator());
	
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

	action = getActionRegistry().getAction(ActionFactory.SAVE.getId());
	manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);

}

private ActionRegistry getActionRegistry() {
	return actionRegistry;
}

private void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
}

}
