package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.ui.*;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.internal.GEFMessages;
import com.ibm.etools.gef.ui.actions.*;
import com.ibm.etools.gef.ui.actions.ActionRegistry;

public class LogicContextMenuProvider
	implements com.ibm.etools.gef.ui.parts.ContextMenuProvider
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
	action = registry.getAction(IncrementDecrementAction.INCREMENT);
	if (action.isEnabled())
		menu.add(action);
	action = registry.getAction(IncrementDecrementAction.DECREMENT);
	if (action.isEnabled())
		menu.add(action);
	action = registry.getAction(IWorkbenchActionConstants.DELETE);
	if (action.isEnabled())
		menu.add(action);
	
	// Alignment Actions
	MenuManager submenu = new MenuManager(LogicResources.getString(
			"AlignmentAction.AlignSubmenu.ActionLabelText"));//$NON-NLS-1$
	
	action = registry.getAction(AlignmentAction.ID_ALIGN_TOP);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_BOTTOM);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_LEFT);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_RIGHT);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_CENTER);
	if (action.isEnabled())
		submenu.add(action);
	action = registry.getAction(AlignmentAction.ID_ALIGN_MIDDLE);
	if (action.isEnabled())
		submenu.add(action);

	menu.add(new Separator());
	menu.add(submenu);
}

public void buildContextMenu(IMenuManager menu, EditPartViewer viewer){
	EditDomain domain = viewer.getEditDomain();
	IEditorPart editorpart = getEditorPart();
	ActionRegistry registry = (ActionRegistry)editorpart.getAdapter(ActionRegistry.class);
	addEditorPartContributions(menu, editorpart, registry);
	addEditDomainContributions(menu, domain, registry);
	addEditPartViewerContributions(menu, viewer, registry);
}

protected IEditorPart getEditorPart(){
	return editorPart;
}

}