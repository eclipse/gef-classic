package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

public abstract class ContextMenuProvider 
	implements IMenuListener, DisposeListener
{

private EditPartViewer viewer;
private MenuManager menuManager;

public ContextMenuProvider(EditPartViewer viewer) {
	setViewer(viewer);
	createContextMenu();
}

public abstract void buildContextMenu(IMenuManager menu);

public Menu createContextMenu() {
	menuManager = new MenuManager();
	menuManager.addMenuListener(this);
	menuManager.setRemoveAllWhenShown(true);

	Menu menu = menuManager.createContextMenu(getViewer().getControl());
	getViewer().getControl().setMenu(menu);
	menu.addDisposeListener(this);
	return menu;
}

public void dispose() {}

public MenuManager getMenuManager() {
	return menuManager;
}

protected EditPartViewer getViewer() {
	return viewer;
}

public void menuAboutToShow(IMenuManager menu) {
	//Any async paints won't be dispatched while the menu is visible.
	getViewer().flush();
	buildContextMenu(menu);
}

protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

/**
 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
 */
public void widgetDisposed(DisposeEvent e) {
	dispose();
	menuManager.getMenu().removeDisposeListener(this);
	menuManager = null;
}

}