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
import org.eclipse.jface.util.Assert;

import org.eclipse.gef.Disposable;

/**
 * A container for editor actions.  You must register the actions before they will be
 * available to the editor.
 */
public class ActionRegistry {

/*
 * A hashmap that contains the actions.
 */
private Map map = new HashMap(15);

/**
 * Calls dispose on all actions which implement the {@link Disposable} interface so they
 * can perform their own clean-up.
 */
public void dispose() {
	Iterator actions = getActions();
	while (actions.hasNext()) {
		IAction action = (IAction)actions.next();
		if (action instanceof Disposable)
			((Disposable)action).dispose();
	}
}

/**
 * Returns the action with the given key.
 */
public IAction getAction(Object key){
	return (IAction)map.get(key);
}

/**
 * Returns an {@link Iterator} of all the actions.
 */
public Iterator getActions() {
	return map.values().iterator();
}

/**
 * Register an action with this registry.  The action must have an id associated with it.
 */
public void registerAction(IAction action) {
	Assert.isNotNull(action.getId(),
		"action must have an ID in " +//$NON-NLS-1$
		getClass().getName() + " :registerAction(IAction)");//$NON-NLS-1$
	registerAction(action.getId(), action);
}

/**
 * Register an action with this registry using the given id.
 */
private void registerAction(String id, IAction action) {
	map.put(id, action);
}

public void removeAction(IAction action) {
	map.remove(action.getId());
}

}