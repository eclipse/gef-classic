/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * This action sets a Tool to be the active entry in the PaletteViewer.
 */
public class SetActivePaletteToolAction
	extends Action 
{

private PaletteViewer viewer;
private ToolEntry entry;

/**
 * Creates a new SetActivePaletteToolAction with the given entry to set, as well as a 
 * label, icon, and isChecked to be used in a menu.
 * 
 * @param viewer the PaletteViewer
 * @param label the label to show in the menu for this entry.
 * @param icon the icon to show in the menu for this entry.
 * @param isChecked whether or not this is the current active entry.
 * @param entry the entry to set if this action is invoked.
 */
public SetActivePaletteToolAction(PaletteViewer viewer, String label, ImageDescriptor icon, 
		boolean isChecked, ToolEntry entry) {
	super(label, icon);
	this.viewer = viewer;
	this.entry = entry;
	setChecked(isChecked);
}

/**
 * @see org.eclipse.jface.action.IAction#run()
 */
public void run() {
	if (viewer != null)
		viewer.setActiveTool(entry);
}

}