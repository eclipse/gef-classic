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
package org.eclipse.gef.ui.palette;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.customize.DefaultEntryPage;
import org.eclipse.gef.ui.palette.customize.DrawerEntryPage;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gef.ui.palette.customize.PaletteDrawerFactory;
import org.eclipse.gef.ui.palette.customize.PaletteSeparatorFactory;

/**
 * <code>PaletteCustomizer</code> is the <code>PaletteCustomizerDialog</code>'s interface
 * to the model.  This class is responsible for propogating to the model changes made in
 * the dialog.
 * 
 * @author Pratik Shah
 */
public abstract class PaletteCustomizer {

/**
 * Indicates whether the given entry can be deleted from the model or not.  Whether or not
 * an entry can be deleted depends on its permsission 
 * ({@link PaletteEntry#getUserModificationPermission()}).
 * <p> 
 * This method will be invoked by <code>PaletteCustomizerDialog</code> to determine
 * whether or not to enable the "Delete" action.
 * </p>
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * @return	<code>true</code> if the given entry can be deleted
 * 
 * @see #performDelete(PaletteEntry)
 */
public boolean canDelete(PaletteEntry entry) {
	return entry.getUserModificationPermission() == PaletteEntry.PERMISSION_FULL_MODIFICATION;
}

/**
 * Indicates whether the given entry can be moved down or not.  Whether or not an entry
 * can be moved down or not is determined by its parent's user modification
 * permission ({@link PaletteEntry#getUserModificationPermission()}).
 * <p> 
 * Will be called by PaletteCustomizerDialog to determine whether or not to enable the
 * "Move Down" action.
 * </p>
 * 
 * @param	entry	The selected palette entry (it'll never be <code>null</code>)
 * @return	<code>true</code> if the given entry can be moved down
 * 
 * @see	#performMoveDown(PaletteEntry)
 */
public boolean canMoveDown(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	int parentPermission = parent.getUserModificationPermission();
	if (parentPermission < PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
		return false;
	}
	
	List children = parent.getChildren();
	if (children.indexOf(entry) + 1 != children.size()) {
		return true;
	} else {
		// The given entry is the last child in its parent.
		if (entry instanceof PaletteContainer 
					|| parentPermission != PaletteEntry.PERMISSION_FULL_MODIFICATION) {
			return false;
		}

		children = parent.getParent().getChildren();
		int parentIndex = children.indexOf(parent);
		for (int i = parentIndex + 1; i < children.size(); i++) {
			PaletteContainer parentSibling = (PaletteContainer)children.get(i);
			if (parentSibling.getUserModificationPermission()
						== PaletteEntry.PERMISSION_FULL_MODIFICATION) {
				return true;
			}
		}
		return false;
	}
}

/**
 * Indicates whether the given entry can be moved up or not.  Whether or not an entry can
 * be moved up or not is determined by its parent's user modification permission
 * ({@link PaletteEntry#getUserModificationPermission()}).
 * <p> 
 * Will be called by PaletteCustomizerDialog to determine whether or not to enable the
 * "Move Up" action.
 * </p>
 * 
 * @param	entry	The selected palette entry (it'll never be <code>null</code>)
 * @return	<code>true</code> if the given entry can be moved up
 * 
 * @see #performMoveUp(PaletteEntry)
 */
public boolean canMoveUp(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	int parentPermission = parent.getUserModificationPermission();
	if (parentPermission < PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
		return false;
	}
	
	List children = parent.getChildren();
	if (children.indexOf(entry) != 0) {
		return true;
	} else {
		// The given entry is the first child in its parent.
		if (entry instanceof PaletteContainer 
					|| parentPermission != PaletteEntry.PERMISSION_FULL_MODIFICATION) {
			return false;
		}

		children = parent.getParent().getChildren();
		int parentIndex = children.indexOf(parent);
		for (int i = parentIndex - 1; i >= 0; i--) {
			PaletteContainer parentSibling = (PaletteContainer)children.get(i);
			if (parentSibling.getUserModificationPermission()
						== PaletteEntry.PERMISSION_FULL_MODIFICATION) {
				return true;
			}
		}
		return false;
	}
}

/**
 * Returns the list of PaletteEntryFactories that can be used to create new palette
 * entries.  The String returned by the getText() method of each PaletteEntryFactory will
 * be used to populate the "New" drop-down.  getImageDescriptor() will be used to set the
 * icons on the drop down.  This method can return null if there are no
 * PaletteEntryFactories available.
 * 
 * @return	The List of PaletteEntryFactories
 */
public List getNewEntryFactories() {
	List list = new ArrayList(3);
	list.add(new PaletteSeparatorFactory());
	list.add(new PaletteDrawerFactory());
	return list;
}

/**
 * Returns an EntryPage that will display the custom properties of the
 * given entry.  Can return null if there are no custom properties.
 * 
 * @param	entry	The PaletteEntry whose properties page needs to be displayed (it'll 
 * 					never be <code>null</code>)
 * @return	The EntryPage to represent the given entry
 */
public EntryPage getPropertiesPage(PaletteEntry entry) {
	if (entry instanceof PaletteDrawer) {
		return new DrawerEntryPage();
	}
	return new DefaultEntryPage();
}

/**
 * Updates the model by deleting the given entry from it.
 * <br> 
 * Called when the "Delete" action in the PaletteCustomizerDialog is executed.
 * 
 * @param	entry	The selected palette entry (it'll never be <code>null</code>)
 * 
 * @see #canDelete(PaletteEntry)
 */
public void performDelete(PaletteEntry entry) {
	entry.getParent().remove(entry);
}

/**
 * Updates the model by moving the entry down.
 * <br> 
 * Called when the "Move Down" action in the PaletteCustomizerDialog is invoked.
 * 
 * @param	entry	The selected palette entry (it'll never be <code>null</code>)
 * 
 * @see #canMoveDown(PaletteEntry)
 */
public void performMoveDown(PaletteEntry entry) {
	PaletteContainer parent = (PaletteContainer)entry.getParent();
	if (!parent.moveDown(entry)) {
		// This is the case of a PaletteEntry that is its parent's last child
		parent.remove(entry);
		PaletteContainer grandparent = (PaletteContainer)parent.getParent();
		List parents = grandparent.getChildren();
		int index = parents.indexOf(parent);
		PaletteContainer parentSibling = null;
		for (int i = index + 1; i < parents.size(); i++) {
			parentSibling = (PaletteContainer) parents.get(i);
			if (parentSibling.getUserModificationPermission()
						== PaletteEntry.PERMISSION_FULL_MODIFICATION) {
				break;
			}
		}
		parentSibling.add(0, entry);
	}
}

/**
 * Updates the model by moving the entry up.
 * <br> 
 * Called when the "Move Up" action in the PaletteCustomizerDialog is invoked.
 * 
 * @param	entry	The selected palette entry (it'll never be <code>null</code>)
 * 
 * @see #canMoveUp(PaletteEntry)
 */
public void performMoveUp(PaletteEntry entry) {
	PaletteContainer parent = (PaletteContainer)entry.getParent();
	if (!parent.moveUp(entry)) {
		//This is the case of a PaletteEntry that is its parent's first child
		parent.remove(entry);
		PaletteContainer grandparent = (PaletteContainer)parent.getParent();
		List parents = grandparent.getChildren();
		int index = parents.indexOf(parent);
		PaletteContainer parentSibling = null;
		for (int i = index - 1; i >= 0; i--) {
			parentSibling = (PaletteContainer) parents.get(i);
			if (parentSibling.getUserModificationPermission()
						== PaletteEntry.PERMISSION_FULL_MODIFICATION) {
				break;
			}
		}
		parentSibling.add(entry);
	}
}

/**
 * Undoes the changes made to the model since the last save.  
 * <p> 
 * This method is invoked when the "Cancel" is selected in the
 * <code>PaletteCustomizerDialog</code>.
 * </p>
 */
public abstract void revertToSaved();

/**
 * Persists the changes made to the model.  
 * <p>
 * Called when "OK" or "Apply" are selected in the <code>PaletteCustomizerDialog</code>.
 * </p>
 */
public abstract void save();

}