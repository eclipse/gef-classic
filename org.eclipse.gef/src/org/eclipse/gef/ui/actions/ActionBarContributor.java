package org.eclipse.gef.ui.actions;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Contributes actions to the workbench.
 * !!Warning:  This class is subject to change.
 */
public class ActionBarContributor 
	extends EditorActionBarContributor
{

public static final String SEPARATOR = "$separator";  //$NON-NLS-1$

/**
 * A list of global actions.  Any action that should be shared between more than one view
 * or editor should be put in this list.  
 */
protected List globalActions = new ArrayList();

/**
 * A list of actions that will be on the toolbar.  These should be actions that need to be
 * explicitly added to the toolbar.  For example, a print button is already on the toolbar
 * so we do not need to add one.  But there is no delete button by default, so the delete
 * action ID should get added to this list.
 */
protected List toolbarActions = new ArrayList();

/**
 * Contains the {@link RetargetAction}s that are registered as global action handlers.  We
 * need to hold on to these so that we can remove them as PartListeners in dispose().
 */
protected Map retargetActions = new HashMap();

/**
 * The active editor.
 */
private IEditorPart activeEditor;

/**
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
 */
public void contributeToToolBar(IToolBarManager tbm) {
	Iterator iter = toolbarActions.iterator();
	while (iter.hasNext()) {
		String id = (String)iter.next();
		if (id == SEPARATOR)
			tbm.add(new Separator());
		else
			tbm.add(getRetargetAction(id));
	}
	getActionBars().updateActionBars();
}

/**
 * Creates and initializes the {@link RetargetAction RetargetActions}.
 */
protected void createActions() {
	RetargetAction action;

	// Create undo action	
	action = new UndoRetargetAction();
	getPage().addPartListener(action);
	retargetActions.put(action.getId(), action);
	
	// Create redo action
	action = new RedoRetargetAction();
	getPage().addPartListener(action);
	retargetActions.put(action.getId(), action);
	
	// Create delete action
	action = new DeleteRetargetAction();
	getPage().addPartListener(action);
	retargetActions.put(action.getId(), action);
	
	// Create print action
	action = new PrintRetargetAction();
	getPage().addPartListener(action);
	retargetActions.put(action.getId(), action);
//
//	// Create copy action
//	action = new CopyRetargetAction();
//	getPage().addPartListener(action);
//	retargetActions.put(action.getId(), action);
//
//	// Create paste action
//	action = new PasteRetargetAction();
//	getPage().addPartListener(action);
//	retargetActions.put(action.getId(), action);
}

/**
 * Initializes the global and toolbar action lists.  
 *
 * @see #globalActions
 * @see #toolbarActions
 * @see #init(IActionBars)
 */
protected void declareActions() {
	globalActions.add(IWorkbenchActionConstants.UNDO);
	globalActions.add(IWorkbenchActionConstants.REDO);
	globalActions.add(IWorkbenchActionConstants.DELETE);
	globalActions.add(IWorkbenchActionConstants.PRINT);
//	globalActions.add(IWorkbenchActionConstants.COPY);
//	globalActions.add(IWorkbenchActionConstants.PASTE);
	
	toolbarActions.add(IWorkbenchActionConstants.UNDO);
	toolbarActions.add(IWorkbenchActionConstants.REDO);
	toolbarActions.add(IWorkbenchActionConstants.DELETE);
	toolbarActions.add(SEPARATOR);
//	toolbarActions.add(IWorkbenchActionConstants.COPY);
//	toolbarActions.add(IWorkbenchActionConstants.PASTE);
//	toolbarActions.add(SEPARATOR);
}

/**
 * Remove the {@link RetargetAction}s that are {@link org.eclipse.ui.IPartListener}s on
 * the {@link org.eclipse.ui.IWorkbenchPage}.
 * @see org.eclipse.ui.part.EditorActionBarContributor#dispose()
 */
public void dispose() {
	Iterator iter = retargetActions.values().iterator();
	while (iter.hasNext())
		getPage().removePartListener((RetargetAction)iter.next());
	retargetActions = null;
}

/**
 * Returns the active editor.
 */
protected IEditorPart getActiveEditor() {
	return activeEditor;
}

/**
 * Returns the RetargetAction for the given id.
 * @param id the action id
 * @return the RetargetAction for the given id
 */
protected RetargetAction getRetargetAction(String id) {
	return (RetargetAction)retargetActions.get(id);
}

/**
 * Initializes the contributor.
 *
 * @param bars The <code>IActionBars</code> for the workbench.
 */
public void init(IActionBars bars) {
	declareActions();
	createActions();
	super.init(bars);
}

/**
 * Sets the active editor and updates the global actions.
 */
public void setActiveEditor(IEditorPart editor) {
	activeEditor = editor;
	ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
	IActionBars bars = getActionBars();

	for (int i = 0; i < globalActions.size(); i++) {
		String id = (String)globalActions.get(i);
		bars.setGlobalActionHandler(id, registry.getAction(id));
	}
	
	getActionBars().updateActionBars();
}

}