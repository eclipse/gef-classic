package org.eclipse.gef.examples.logicdesigner.palette;

import java.util.List;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteToolEntry;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.customize.EntryPage;
import org.eclipse.gef.ui.palette.customize.ReadOnlyEntryPage;

/**
 * @author Pratik Shah
 */
public class LogicPaletteCustomizer 
	extends PaletteCustomizer 
{

/**
 * Tools cannot be deleted
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canDelete(PaletteEntry)
 */
public boolean canDelete(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	}
	
	return super.canDelete(entry);
}

/**
 * Tools cannot be moved down
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canMoveDown(PaletteEntry)
 */
public boolean canMoveDown(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	} else if( entry instanceof PaletteGroup ){
		if( entry.getLabel().equals("Control Group") ){
			return false;
		}
	}
	return super.canMoveDown(entry);
}

/**
 * Tools cannot be moved up
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#canMoveUp(PaletteEntry)
 */
public boolean canMoveUp(PaletteEntry entry) {
	if( entry instanceof PaletteToolEntry ){
		return false;
	} else if( entry instanceof PaletteGroup ){
		if( entry.getLabel().equals("Control Group") ){
			return false;
		}
	}
	
	if( entry instanceof PaletteContainer && entry.getParent().getChildren().indexOf(entry) == 1){
		return false;
	} else if( entry.getParent().getChildren().indexOf(entry) == 0 && 
		entry.getParent().getParent().getChildren().indexOf(entry.getParent()) == 1 ){
		return false;
	}
	return super.canMoveUp(entry);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#getPropertiesPage(PaletteEntry)
 */
public EntryPage getPropertiesPage(PaletteEntry entry) {
	if( entry instanceof PaletteSeparator || entry instanceof PaletteToolEntry || 
	    entry instanceof PaletteGroup ){
		return new ReadOnlyEntryPage();
	} else {
		return new LogicEntryPage();
	}
}

/**
 * Optimized version
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#performMoveDown(PaletteEntry)
 */
public void performMoveDown(PaletteEntry entry){
	PaletteContainer parent = (PaletteContainer)entry.getParent();
	if( !parent.moveDown(entry) ){
		// This is the case of a PaletteEntry that is its parent's last child
		parent.remove(entry);
		PaletteContainer grandparent = (PaletteContainer)parent.getParent();
		List parents = grandparent.getChildren();
		int index = parents.indexOf( parent );
		PaletteContainer sibling = (PaletteContainer)parents.get(index+1);
		sibling.add(0, entry);
	}
}

/**
 * Optimized version
 * 
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#performMoveUp(PaletteEntry)
 */
public void performMoveUp(PaletteEntry entry){
	PaletteContainer parent = (PaletteContainer)entry.getParent();
	if( !parent.moveUp(entry) ){
		//This is the case of a PaletteEntry that is its parent's first child
		parent.remove(entry);
		PaletteContainer grandparent = (PaletteContainer)parent.getParent();
		List parents = grandparent.getChildren();
		int index = parents.indexOf( parent );
		PaletteContainer sibling = (PaletteContainer)parents.get(index-1);
		sibling.add(entry);
	}
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#revertToSaved()
 */
public void revertToSaved() {
}


/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#dialogClosed(PaletteEntry)
 */
public void save() {
}

}
