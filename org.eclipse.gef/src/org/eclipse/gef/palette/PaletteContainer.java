package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.Iterator;
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

public static final String
	PALETTE_TYPE_GROUP = "Palette_Group";//$NON-NLS-1$

/**
 * This container's contents
 */
protected List children = new ArrayList();

/**
 * Constructor that sets the container's label and small icon
 * 
 * @see org.eclipse.gef.palette.PaletteEntry#PaletteEntry(String)
 */
protected PaletteContainer(String label, String desc, ImageDescriptor icon, Object type) {
	super(label, desc, icon, null, type);
}

public void add(PaletteEntry entry) {
	add(-1, entry);
}

public void add(int index, PaletteEntry entry) {
	List oldChildren = new ArrayList(children);

	int actualIndex = index < 0 ? children.size() : index;
	children.add(actualIndex, entry);
	entry.setParent(this);
	listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
}

public void addAll(List list) {
	ArrayList oldChildren = new ArrayList(children);
	for (int i = 0; i < list.size(); i++) {
		PaletteEntry child = (PaletteEntry) list.get(i);
		children.add(child);
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
	int index = children.indexOf(entry);
	if (index < 0) {
		// This container does not contain the given palette entry
		return false;
	}
	index = up ? index - 1 : index + 1;
	if (index < 0 || index >= children.size()) {
		// Performing the move operation will give the child an invalid index
		return false;
	}
	List oldChildren = new ArrayList(children);
	children.remove(entry);
	children.add(index, entry);
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

public void remove(PaletteEntry entry) {
	List oldChildren = new ArrayList(children);
	if (children.remove(entry)) {
		entry.setParent(null);
		listeners.firePropertyChange(PROPERTY_CHILDREN,	oldChildren, getChildren());
	}
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "Palette Container (" + (getLabel() != null ? getLabel() : "") + ")"; //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}