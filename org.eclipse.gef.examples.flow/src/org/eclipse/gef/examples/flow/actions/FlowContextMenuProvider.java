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
package org.eclipse.gef.examples.flow.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * Provides a context menu for the flow editor.
 * @author Daniel Lee
 */
public class FlowContextMenuProvider extends ContextMenuProvider {

private ActionRegistry actionRegistry;

/**
 * Creates a new FlowContextMenuProvider assoicated with the given viewer and 
 * action registry.
 * @param viewer the viewer
 * @param registry the action registry
 */
public FlowContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
	super(viewer);
	setActionRegistry(registry);
}

/**
 * @see ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
 */
public void buildContextMenu(IMenuManager menu) {
	GEFActionConstants.addStandardActionGroups(menu);
	
	IAction action;
	action = getActionRegistry().getAction(GEFActionConstants.UNDO);
	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

	action = getActionRegistry().getAction(GEFActionConstants.REDO);
	menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
	
	action = getActionRegistry().getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);


}

private ActionRegistry getActionRegistry() {
	return actionRegistry;
}

/**
 * Sets the action registry
 * @param registry the action registry
 */
public void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
}

}
