package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.EditPartViewer;

public abstract class ContextMenuProvider 
	implements IMenuListener 
{

public static final String GROUP_DOMAIN_CONTRIBUTIONS = "$domain menu group"; //$NON-NLS-1$
public static final String GROUP_EDITOR_CONTRIBUTIONS = "$editor menu group"; //$NON-NLS-1$
public static final String GROUP_VIEWER_CONTRIBUTIONS = "$viewer menu group"; //$NON-NLS-1$

private IEditorPart editor;
private EditPartViewer viewer;

public ContextMenuProvider(IEditorPart editor, EditPartViewer viewer) {
	setViewer(viewer);
	setEditor(editor);
	createContextMenu();
}

public abstract void buildContextMenu(IMenuManager menu);

public Menu createContextMenu() {
	MenuManager manager = new MenuManager(); 
	manager.addMenuListener(this);
	manager.setRemoveAllWhenShown(true);

	Menu menu = manager.createContextMenu(getViewer().getControl());
	getViewer().getControl().setMenu(menu);
	registerContextMenu(manager);
	return menu;
}

protected IEditorPart getEditor() {
	return editor;
}

protected EditPartViewer getViewer() {
	return viewer;
}

public void menuAboutToShow(IMenuManager menu) {
	//Any async paints won't be dispatched while the menu is visible.
	getViewer().flush();
	// As long as setRemoveAllWhenShown is set to true, we must re-add these.
	menu.add(new Separator(GROUP_DOMAIN_CONTRIBUTIONS));
	menu.add(new Separator(GROUP_VIEWER_CONTRIBUTIONS));
	menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	menu.add(new Separator(GROUP_EDITOR_CONTRIBUTIONS));
	buildContextMenu(menu);
}

protected abstract void registerContextMenu(MenuManager manager);

protected void setEditor(IEditorPart editor) {
	this.editor = editor;
}

protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

}