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
package org.eclipse.gef;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

/**
 * Extends MenuManager to allow building the context menu directly.
 * 
 * @author hudsonr
 */

public abstract class ContextMenuProvider 
	extends MenuManager
	implements IMenuListener
{

private EditPartViewer viewer;

public ContextMenuProvider(EditPartViewer viewer) {
	setViewer(viewer);
	addMenuListener(this);
	setRemoveAllWhenShown(true);
}

public abstract void buildContextMenu(IMenuManager menu);

protected EditPartViewer getViewer() {
	return viewer;
}

public void menuAboutToShow(IMenuManager menu) {
	buildContextMenu(menu);
}

protected void setViewer(EditPartViewer viewer) {
	this.viewer = viewer;
}

}