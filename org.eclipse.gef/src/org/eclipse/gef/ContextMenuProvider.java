package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

/**
 * Extends MenuManager to allow building the context menu directly.
 * 
 * @author hudsonr
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
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