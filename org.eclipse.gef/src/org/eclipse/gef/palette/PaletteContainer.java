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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Default implementation of PaletteContainer
 * 
 * @author Pratik Shah
 */
public class PaletteContainer
	extends PaletteEntry
{

/**
 * Property name indicating that this PaletteContainer's children have changed
 */
public static final String
	PROPERTY_CHILDREN = "Children Changed"; //$NON-NLS-1$

/**
 * This container's contents
 */
protected List children = new ArrayList();

/**
 * Constructor that sets the container's label and small icon
 * 
 * @see org.eclipse.gef.palette.PaletteEntry#PaletteEntry(String, String, 
 *      ImageDescriptor, ImageDescriptor, Object)
 */
protected PaletteContainer(String label, String desc, ImageDescriptor icon, Object type) {
	super(label, desc, icon, null, type);
}

/**
 * Returns true if this type can be a child of this container, also checks permissions.
 * 
 * @param type the type being requested
 * @return true if this can be a child of this container
 */
public boolean acceptsType(Object type) {
	return getUserModificationPermission() == PERMISSION_FULL_MODIFICATION;
}

/**
 * Adds the given entry to the end of this PaletteContainer
 * @param entry the PaletteEntry to add
 */
public void add(PaletteEntry entry) {
	add(-1, entry);
}

/**
 * Adds the given PaletteEntry at position <code>index</code>.
 * @param index position to add the PaletteEntry
 * @param entry the PaletteEntry to add
 */
public void add(int index, PaletteEntry entry) {
	List oldChildren = new ArrayList(getChildren());

	int actualIndex = index < 0 ? getChildren().size() : index;
	getChildren().add(actualIndex, entry);
	entry.setParent(this);
	listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
}

/**
 * Adds the list of {@link PaletteEntry} objects to this PaletteContainer.
 * @param list a list of PaletteEntry objects to add to this PaletteContainer
 */
public void addAll(List list) {
	ArrayList oldChildren = new ArrayList(getChildren());
	for (int i = 0; i < list.size(); i++) {
		PaletteEntry child = (PaletteEntry) list.get(i);
			getChildren().add(child);
			child.setParent(this);
	}
	listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
}

/**
 * @return the children of this container
 */
public List getChildren() {
	return children;
}

private boolean move(PaletteEntry entry, boolean up) {
	int index = getChildren().indexOf(entry);
	if (index < 0) {
		// This container does not contain the given palette entry
		return false;
	}
	index = up ? index - 1 : index + 1;
	if (index < 0 || index >= getChildren().size()) {
		// Performing the move operation will give the child an invalid index
		return false;
	}
	if (getChildren().get(index) instanceof PaletteContainer 
			&& getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION) {
		// move it into a container if we have full permission
		PaletteContainer container = (PaletteContainer)getChildren().get(index);
		if (container.acceptsType(entry.getType())) {
			remove(entry);
			if (up)
				container.add(entry);
			else 
				container.add(0, entry);
			return true;
		}
	}
	List oldChildren = new ArrayList(getChildren());
	getChildren().remove(entry);
	getChildren().add(index, entry);
	listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
	return true;
}

/**
 * Moves the given entry down, if possible.  This method only handles moving the child
 * within this container.
 * 
 * @param entry	The entry to be moved
 * @return <code>true</code> if the given entry was successfully moved down
 */
public boolean moveDown(PaletteEntry entry) {
	return move(entry, false);
}

/**
 * Moves the given entry up, if possible.  This method only handles moving the child
 * within this container.
 * 
 * @param entry	The entry to be moved
 * @return <code>true</code> if the given entry was successfully moved up
 */
public boolean moveUp(PaletteEntry entry) {
	return move(entry, true);
}

/**
 * Removes the given PaletteEntry from this PaletteContainer
 * @param entry the PaletteEntry to remove
 */
public void remove(PaletteEntry entry) {
	List oldChildren = new ArrayList(getChildren());
	if (getChildren().remove(entry)) {
		entry.setParent(null);
		listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
	}
}

/**
 * Sets the children of this PaletteContainer to the given list of
 * {@link PaletteEntry} objects.
 * @param list the list of children
 */
public void setChildren(List list) {
	List oldChildren = children;
	children = list;
	listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "Palette Container (" //$NON-NLS-1$
				+ (getLabel() != null ? getLabel() : "") //$NON-NLS-1$
				+ ")"; //$NON-NLS-1$
}

}