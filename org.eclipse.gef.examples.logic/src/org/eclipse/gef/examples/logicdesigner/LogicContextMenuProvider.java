package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.jface.action.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

public class LogicContextMenuProvider
	implements org.eclipse.gef.ui.parts.ContextMenuProvider
{

private IEditorPart editorPart;

public LogicContextMenuProvider(IEditorPart editor){
	editorPart = editor;
}

protected void addEditorPartContributions(
	IMenuManager menu,
	IEditorPart editor,
	ActionRegistry registry)
{
	menu.add(registry.getAction(IWorkbenchActionConstants.SAVE));
}

protected void addEditDomainContributions(
	IMenuManager menu,
	EditDomain editor,
	ActionRegistry registry)
{
	menu.add(registry.getAction(IWorkbenchActionConstants.UNDO));
	menu.add(registry.getAction(IWorkbenchActionConstants.REDO));
}

protected void addEditPartViewerContributions(
	IMenuManager menu,
	EditPartViewer viewer,
	ActionRegistry registry)
{
	IAction action;
	action = registry.getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		menu.add(action);
	action = registry.getAction(DirectEditAction.ID);
	if (action.isEnabled())
		menu.add(action);
	action = registry.getAction(IncrementDecrementAction.INCREMENT);
	if (action.isEnabled())
		menu.add(action);
	action = registry.getAction(IncrementDecrementAction.DECREMENT);
	if (action.isEnabled())
		menu.add(action);
	
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
		menu.add(submenu);
}

public void buildContextMenu(IMenuManager menu, EditPartViewer viewer){
	EditDomain domain = viewer.getEditDomain();
	IEditorPart editorpart = getEditorPart();
	ActionRegistry registry = (ActionRegistry)editorpart.getAdapter(ActionRegistry.class);
	addEditDomainContributions(menu, domain, registry);
	menu.add(new Separator());
	addEditPartViewerContributions(menu, viewer, registry);
	menu.add(new Separator());
	addEditorPartContributions(menu, editorpart, registry);
}

protected IEditorPart getEditorPart(){
	return editorPart;
}

}