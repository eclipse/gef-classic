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
package org.eclipse.gef.ui.parts;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.SimpleObjectTransfer;

/**
 * Used to move EditParts around in a single viewer.  
 */
class TreeViewerTransfer extends SimpleObjectTransfer {

private static final TreeViewerTransfer instance = new TreeViewerTransfer();
private static final String TYPE_NAME = "Local Transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

private static EditPartViewer viewer;

/**
 * Returns the singleton instance.
 * @return The singleton instance */
public static TreeViewerTransfer getInstance() {
	return instance;
}

private TreeViewerTransfer() { }

/**
 * @see Transfer#getTypeIds()
 */
protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

/**
 * @see Transfer#getTypeNames()
 */
protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

/**
 * Returns the viewer where the drag started.
 * @return The viewer where the drag started */
public EditPartViewer getViewer() {
	return viewer;
}

/**
 * Sets the viewer where the drag started.
 * @param epv The viewer */
public void setViewer(EditPartViewer epv) {
	viewer = epv;
}

}
