package org.eclipse.gef.ui.parts;
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
import org.eclipse.swt.widgets.Menu;

public abstract class ContextMenuProvider 
	implements IMenuListener 
{

private ActionRegistry registry;
private EditPartViewer viewer;
private MenuManager menuManager;

public ContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
	setViewer(viewer);
	setActionRegistry(registry);
	createContextMenu();
}

public abstract void buildContextMenu(IMenuManager menu);

public Menu createContextMenu() {
	menuManager = new MenuManager(); 
	menuManager.addMenuListener(this);
	menuManager.setRemoveAllWhenShown(true);

	Menu menu = menuManager.createContextMenu(getViewer().getControl());
	getViewer().getControl().setMenu(menu);
	return menu;
}

protected ActionRegistry getActionRegistry() {
	return registry;
}

public MenuManager getMenuManager() {
	return menuManager;
}

protected EditPartViewer getViewer() {
	return viewer;
}

public void menuAboutToShow(IMenuManager menu) {
	//Any async paints won't be dispatched while the menu is visible.
	getViewer().flush();
	// As long as setRemoveAllWhenShown is set to true, we must re-add these.
	menu.add(new Separator(GEFActionConstants.GROUP_UNDO));
	menu.add(new Separator(GEFActionConstants.GROUP_COPY));
	menu.add(new Separator(GEFActionConstants.GROUP_PRINT));
	menu.add(new Separator(GEFActionConstants.GROUP_EDIT));
	menu.add(new Separator(GEFActionConstants.GROUP_FIND));
	menu.add(new Separator(GEFActionConstants.GROUP_ADD));
	menu.add(new Separator(GEFActionConstants.GROUP_REST));
	menu.add(new Separator(GEFActionConstants.MB_ADDITIONS));
	menu.add(new Separator(GEFActionConstants.GROUP_SAVE));
	buildContextMenu(menu);
}

protected void setActionRegistry(ActionRegistry registry) {
	this.registry = registry;
}

protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

}