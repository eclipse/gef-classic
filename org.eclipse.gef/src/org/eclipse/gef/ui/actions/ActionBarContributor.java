package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.*;

/**
 * Contributes actions to the workbench.
 * !!Warning:  This class is subject to change.
 */
public class ActionBarContributor 
	extends org.eclipse.ui.part.EditorActionBarContributor
{

public static final String SEPARATOR = "$separator";  //$NON-NLS-1$

/**
 * A list of global actions.
 */
protected List globalActions = new ArrayList();

/**
 * A list of actions that will be on the toolbar.
 */
protected List toolbarActions = new ArrayList();

/**
 * The active editor.
 */
private IEditorPart activeEditor;

/**
 * Initializes the global and toolbar action lists.
 *
 * @see #init(IActionBars)
 */
protected void declareActions() {
	globalActions.add(IWorkbenchActionConstants.UNDO);
	globalActions.add(IWorkbenchActionConstants.REDO);
	globalActions.add(IWorkbenchActionConstants.DELETE);
	globalActions.add(IWorkbenchActionConstants.PRINT);
	
	toolbarActions.add(IWorkbenchActionConstants.UNDO);
	toolbarActions.add(IWorkbenchActionConstants.REDO);
	toolbarActions.add(IWorkbenchActionConstants.DELETE);
	toolbarActions.add(SEPARATOR);
}

/**
 * Returns the active editor.
 */
protected IEditorPart getActiveEditor() {
	return activeEditor;
}

/**
 * Initializes the contributor.
 *
 * @param bars The <code>IActionBars</code> for the workbench.
 */
public void init(IActionBars bars) {
	super.init(bars);
	declareActions();
}

/**
 * Sets the active editor and updates the global actions.
 */
public void setActiveEditor(IEditorPart editor) {
	ActionRegistry registry;
	registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
	activeEditor = editor;
	IActionBars bars = getActionBars();
	String id;

	for (int i=0; i<globalActions.size(); i++) {
		id = (String)globalActions.get(i);
		bars.setGlobalActionHandler(id,registry.getAction(id));
	}

	IToolBarManager tbm = bars.getToolBarManager();

	for (int i=0; i<toolbarActions.size(); i++) {
		id = (String)toolbarActions.get(i);
		if (id == SEPARATOR) {
			tbm.add(new Separator());
			continue;
		}
		if (tbm.find(id) != null)
			tbm.remove(id);
		tbm.add(registry.getAction(id));
	}
	
	tbm.update(false);
}

}