package org.eclipse.gef.ui.palette;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gef.ui.palette.customize.PaletteCategoryFactory;
import org.eclipse.gef.ui.palette.customize.PaletteGroupFactory;
import org.eclipse.gef.ui.palette.customize.PaletteSeparatorFactory;
import org.eclipse.gef.ui.palette.customize.ReadOnlyEntryPage;

/**
 * <code>PaletteCustomizer</code> is the <code>PaletteCustomizerDialog</code>'s interface
 * to the model.  This class is responsible for transferring changes to the model.
 * 
 * @author Pratik Shah
 */
public abstract class PaletteCustomizer {

/**
 * Indicates whether the given entry can be deleted from the model or not.
 * <p>
 * This default implementation returns <code>true</code> for all entries.  If the given
 * entry is a <code>PaletteContainer</code>, it returns <code>true</code> IFF all its
 * children can be deleted as well.
 * </p>
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
	if (entry instanceof PaletteContainer) {
		List children = ((PaletteContainer) entry).getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			PaletteEntry child = (PaletteEntry) iter.next();
			if (!canDelete(child)) {
				return false;
			}
		}
	}
	return true;
}

/**
 * Returns whether the given entry can be moved down or not.
 * Will be called by PaletteCustomizerDialog to determine whether or not to
 * enable the "Move Down" action.
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * @return	<code>true</code> if the given entry can be moved down
 * 
 * @see	#performMoveDown(PaletteEntry)
 */
public boolean canMoveDown(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	List children = parent.getChildren();
	if (children.indexOf(entry) + 1 != children.size()) {
		return true;
	} else {
		// if the given entry's parent is not the last child of its parent,
		// you can still move down
		if (entry instanceof PaletteContainer)
			return false;

		children = parent.getParent().getChildren();
		return (children.indexOf(parent) + 1 != children.size());
	}
}

/**
 * Returns whether the given entry can be moved up or not.
 * Will be called by PaletteCustomizerDialog to determine whether or not to
 * enable the "Move Up" action.
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * @return	<code>true</code> if the given entry can be moved up
 * 
 * @see #performMoveUp(PaletteEntry)
 */
public boolean canMoveUp(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	if (parent.getChildren().indexOf(entry) != 0) {
		return true;
	} else {
		if (entry instanceof PaletteContainer)
			return false;
		return (parent.getParent().getChildren().indexOf(parent) != 0);
	}
}

/**
 * Returns the list of PaletteEntryFactories that can be used to create new palette
 * entries.  The String returned by the getText() method of each PaletteEntryFactory will
 * be used to populate the "New" ToolItem drop down.  getImageDescriptor() will be used to
 * set the icons on the drop down.  Clicking on the "New" ToolItem will launch the first
 * action in the list.  The rest have to be picked from the drop down.  This method can
 * return null if there are no PaletteEntryFactories available.
 * 
 * @return	The List of PaletteEntryFactories
 */
public List getNewEntryFactories() {
	List list = new ArrayList(3);
	list.add(new PaletteSeparatorFactory());
	list.add(new PaletteCategoryFactory());
	list.add(new PaletteGroupFactory());
	return list;
}

/**
 * Returns an EntryPage that will display the custom properties of the
 * given entry.  Can return null if there are no custom properties.
 * 
 * @param	entry	The PaletteEntry whose properties page needs to be displayed
 * @return	The EntryPage to represent the given entry
 */
public EntryPage getPropertiesPage(PaletteEntry entry) {
	return new ReadOnlyEntryPage();
}

/**
 * Updates the model by deleting the given entry from it.
 * Called when the "Delete" ToolItem in the PaletteCustomizerDialog is clicked.
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * 
 * @see #canDelete(PaletteEntry)
 */
public void performDelete(PaletteEntry entry) {
	entry.getParent().remove(entry);
}

/**
 * Updates the model by moving the entry down.
 * Called when the "Move Down" ToolItem in the PaletteCustomizerDialog is 
 * clicked.
 * 
 * <p>
 * NOTE: It is recommended that sub-classes override this method and optimize it.  The way
 * it is implemented right now, it causes two changes to be fired (one for removing the
 * entry from its parent, and one for re-inserting it at the new index).  This causes the 
 * TreeViewer in the PaletteCustomizerDialog to refresh twice, which produces an effect
 * similar to flickering.
 * </p>
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * 
 * @see #canMoveDown(PaletteEntry)
 */
public void performMoveDown(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	List children = parent.getChildren();
	int index = children.indexOf(entry) + 1;
	parent.remove(entry);
	
	if (index == children.size()) {
		PaletteContainer grandparent = parent.getParent();
		int parentIndex = grandparent.getChildren().indexOf(parent) + 1;
		parent = (PaletteContainer)grandparent.getChildren().get(parentIndex);
		index = 0;
	}
	parent.add(index, entry);
}

/**
 * Updates the model by moving the entry up.
 * Called when the "Move Up" ToolItem in the PaletteCustomizerDialog is clicked.
 * 
 * <p>
 * NOTE: It is recommended that sub-classes override this method and optimize it.  The way
 * it is implemented right now, it causes two changes to be fired (one for removing the
 * entry from its parent, and one for re-inserting it at the new index). This causes the
 * TreeViewer in the PaletteCustomizerDialog to refresh twice, which produces an effect
 * similar to flickering.
 * </p>
 * 
 * @param	entry	The selected palette entry.  It'll never be <code>null</code>.
 * 
 * @see #canMoveUp(PaletteEntry)
 */
public void performMoveUp(PaletteEntry entry) {
	PaletteContainer parent = entry.getParent();
	List children = parent.getChildren();
	int index = children.indexOf(entry) - 1;
	parent.remove(entry);
	
	if (index < 0) {
		PaletteContainer grandparent = parent.getParent();
		int parentIndex = grandparent.getChildren().indexOf(parent) - 1;
		parent = (PaletteContainer)grandparent.getChildren().get(parentIndex);
		index = -1;
	}
	parent.add(index, entry);
}

/**
 * Undoes the changes made to the model since the last save.  
 * <p> 
 * This method is invoked when the "Cancel" button is hit on the
 * <code>PaletteCustomizerDialog</code>.  
 * </p>
 */
public abstract void revertToSaved();

/**
 * Persists the changes made to the model.  
 * <p>
 * Called when <code>PaletteCustomizerDialog</code> is closed.
 * </p>
 */
public abstract void save();

}