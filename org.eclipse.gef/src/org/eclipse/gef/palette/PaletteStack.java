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
package org.eclipse.gef.palette;

import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The model object for a PaletteStack - A stack of tools. A stack should contain only 
 * tools and should have permissions that are less than or equal to its parent.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStack 
	extends PaletteContainer 
{

/** Type identifier **/
public static final String PALETTE_TYPE_STACK = "$PaletteStack";  //$NON-NLS-1$

/** Property name for the active entry **/
public static final String PROPERTY_ACTIVE_ENTRY = "Active Entry"; //$NON-NLS-1$

private PaletteEntry activeEntry;

/**
 * Creates a new PaletteStack with the given name, description, and icon. These will 
 * be shown only in the customize menu.
 * 
 * @see PaletteEntry#PaletteEntry(String, String, ImageDescriptor, String)
 */
public PaletteStack(String name, String desc, ImageDescriptor icon) {
	super(name, desc, icon, PALETTE_TYPE_STACK);
	setUserModificationPermission(PERMISSION_LIMITED_MODIFICATION);
}

/**
 * Returns true if this type can be a child of this container
 * Only accepts ToolEntry's.
 * 
 * @param type the type being requested
 * @return true if this can be a child of this container
 */
public boolean acceptsType(Object type) {
	if (!type.equals(ToolEntry.PALETTE_TYPE_TOOL))
		return false;
	return super.acceptsType(type);
}

/**
 * @see org.eclipse.gef.palette.PaletteContainer#add(int, org.eclipse.gef.palette.PaletteEntry)
 */
public void add(int index, PaletteEntry entry) {
	super.add(index, entry);
	checkActiveEntry();
}

/**
 * @see org.eclipse.gef.palette.PaletteContainer#addAll(java.util.List)
 */
public void addAll(List list) {
	super.addAll(list);
	checkActiveEntry();
}

/**
 * Checks to make sure the active entry is up-to-date and sets it to the first
 * child if its null
 */
private void checkActiveEntry() {
	PaletteEntry currEntry = activeEntry;
	if (!getChildren().contains(activeEntry))
		activeEntry = null;
	if (activeEntry == null && getChildren().size() > 0)
		activeEntry = (PaletteEntry)getChildren().get(0);
	listeners.firePropertyChange(PROPERTY_ACTIVE_ENTRY, currEntry, activeEntry);
}

/**
 * Returns the PaletteEntry referring to the active entry that should be shown in the
 * palette.
 * 
 * @return active entry to be shown in the palette.
 */
public PaletteEntry getActiveEntry() {
	checkActiveEntry();
	return activeEntry;
}

/**
 * @see org.eclipse.gef.palette.PaletteContainer#remove(org.eclipse.gef.palette.PaletteEntry)
 */
public void remove(PaletteEntry entry) {
	super.remove(entry);
	checkActiveEntry();
}

/**
 * Sets the "active" child entry to the given PaletteEntry. This entry will be shown on
 * the palette and will be checked in the menu.
 * 
 * @param entry the entry to show on the palette. 
 */
public void setActiveEntry(PaletteEntry entry) {
	PaletteEntry oldEntry = activeEntry;
	if (activeEntry != null 
			&& (activeEntry.equals(entry) || !getChildren().contains(entry)))
		return;
	activeEntry = entry;
	listeners.firePropertyChange(PROPERTY_ACTIVE_ENTRY, oldEntry, activeEntry);
}

}