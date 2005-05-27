/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.stackview;

import java.util.EventObject;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.commands.CompoundCommand;

/**
 * Internal class used for a stack inspector.
 * @deprecated this class will be deleted
 */
public class TreeContentProvider
	implements org.eclipse.jface.viewers.ITreeContentProvider,
		CommandStackListener
{

Viewer viewer;

/**
 * Creates a new TreeContentProvider with the given CommandStack
 * @param stack The CommandStack
 */
public TreeContentProvider(CommandStack stack) {
	stack.addCommandStackListener(this);

}

/**
 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
 */
public void dispose() {
}

/**
 * @see CommandStackListener#commandStackChanged(EventObject)
 */
public void commandStackChanged(EventObject event) {
	viewer.refresh();
}

/**
 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
 */
public Object[] getChildren(Object o) {
	if (o instanceof CompoundCommand) {
		return ((CompoundCommand)o).getChildren();
	}
	return null;
}

/**
 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
 */
public Object[] getElements(Object o) {
	if (o instanceof CommandStack) {
		return ((CommandStack)o).getCommands();
	}
//	if (o instanceof CompoundCommand) {
//	}
	return null;
}

/**
 * @see TreeContentProvider#getParent(Object)
 */
public Object getParent(Object child) { return null; }

/**
 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
 */
public boolean hasChildren(Object o) {
	return o instanceof CompoundCommand;
}

/**
 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
 */
public void inputChanged(Viewer v, Object o, Object n) {
	viewer = v;
}

}
