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
package org.eclipse.gef.ui.actions;

import java.util.*;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Contributes actions to the workbench.
 * !!Warning:  This class is subject to change.
 */
public abstract class ActionBarContributor 
	extends EditorActionBarContributor
{

private ActionRegistry registry = new ActionRegistry();

/**
 * Contains the {@link RetargetAction}s that are registered as global action handlers.  We
 * need to hold on to these so that we can remove them as PartListeners in dispose().
 */
private List retargetActions = new ArrayList();
private List globalActionKeys = new ArrayList();

protected void addAction(IAction action) {
	getActionRegistry().registerAction(action);
}

protected void addGlobalActionKey(String key) {
	globalActionKeys.add(key);
}

protected void addRetargetAction(RetargetAction action) {
	addAction(action);
	retargetActions.add(action);
	getPage().addPartListener(action);
	addGlobalActionKey(action.getId());
}

/**
 * Creates and initializes Actions managed by this contributor.
 */
protected abstract void buildActions();

protected abstract void declareGlobalActionKeys();

/**
 * Remove the {@link RetargetAction}s that are {@link org.eclipse.ui.IPartListener}s on
 * the {@link org.eclipse.ui.IWorkbenchPage}.
 * @see org.eclipse.ui.part.EditorActionBarContributor#dispose()
 */
public void dispose() {
	for (int i = 0; i < retargetActions.size(); i++) {
		RetargetAction action = (RetargetAction)retargetActions.get(i);
		getPage().removePartListener(action);
	}
	registry.dispose();
	retargetActions = null;
	registry = null;
}

protected IAction getAction(String id) {
	return getActionRegistry().getAction(id);
}

/**
 * returns the ActionRegistry.
 * @return the ActionRegistry */
protected ActionRegistry getActionRegistry() {
	return registry;
}

/** * @see org.eclipse.ui.part.EditorActionBarContributor#init(IActionBars) */
public void init(IActionBars bars) {
	buildActions();
	declareGlobalActionKeys();
	super.init(bars);
}

/** * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(IEditorPart) */
public void setActiveEditor(IEditorPart editor) {
	ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
	IActionBars bars = getActionBars();
	for (int i = 0; i < globalActionKeys.size(); i++) {
		String id = (String)globalActionKeys.get(i);
		bars.setGlobalActionHandler(id, registry.getAction(id));
	}
	getActionBars().updateActionBars();
}

}