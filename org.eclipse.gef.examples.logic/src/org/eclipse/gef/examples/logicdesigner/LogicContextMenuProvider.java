package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DirectEditAction;

public class LogicContextMenuProvider
	extends org.eclipse.gef.ui.parts.ContextMenuProvider
{

public LogicContextMenuProvider(IEditorPart editor, EditPartViewer viewer) {
	super(editor, viewer);
}

protected void addEditDomainContributions(IMenuManager menu) {
	ActionRegistry registry = (ActionRegistry)getEditor().getAdapter(ActionRegistry.class);
	menu.appendToGroup(GROUP_DOMAIN_CONTRIBUTIONS, 
						registry.getAction(IWorkbenchActionConstants.UNDO));
	menu.appendToGroup(GROUP_DOMAIN_CONTRIBUTIONS, 
						registry.getAction(IWorkbenchActionConstants.REDO));
}

protected void addEditorPartContributions(IMenuManager menu) {
	ActionRegistry registry = (ActionRegistry)getEditor().getAdapter(ActionRegistry.class);
	menu.appendToGroup(GROUP_EDITOR_CONTRIBUTIONS, 
						registry.getAction(IWorkbenchActionConstants.SAVE));
}

protected void addEditPartViewerContributions(IMenuManager menu) {
	ActionRegistry registry = (ActionRegistry)getEditor().getAdapter(ActionRegistry.class);
	IAction action;
	action = registry.getAction(IWorkbenchActionConstants.PASTE);
	if (action.isEnabled())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, action);
	action = registry.getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, action);
	action = registry.getAction(DirectEditAction.ID);
	if (action.isEnabled())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, action);
	action = registry.getAction(IncrementDecrementAction.INCREMENT);
	if (action.isEnabled())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, action);
	action = registry.getAction(IncrementDecrementAction.DECREMENT);
	if (action.isEnabled())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, action);
	
	// Alignment Actions
	MenuManager submenu = new MenuManager(LogicMessages.AlignmentAction_AlignSubmenu_ActionLabelText);

	action = registry.getAction(AlignmentAction.ID_ALIGN_LEFT);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_CENTER);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_RIGHT);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_TOP);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_MIDDLE);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_BOTTOM);
	if (action.isEnabled())
		submenu.add(action);

	if (!submenu.isEmpty())
		menu.appendToGroup(GROUP_VIEWER_CONTRIBUTIONS, submenu);
}

public void buildContextMenu(IMenuManager menu) {
	addEditDomainContributions(menu);
	addEditPartViewerContributions(menu);
	addEditorPartContributions(menu);
}

/**
 * @see org.eclipse.gef.ui.parts.ContextMenuProvider#registerContextMenu(MenuManager)
 */
protected void registerContextMenu(MenuManager manager) {
	getEditor().getSite().registerContextMenu("org.eclipse.gef.editor", //$NON-NLS-1$
								manager, getEditor().getSite().getSelectionProvider()); 
}

}