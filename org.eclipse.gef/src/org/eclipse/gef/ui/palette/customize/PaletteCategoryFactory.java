package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteDrawer categories}
 * 
 * @author Pratik Shah
 */
public class PaletteCategoryFactory extends PaletteContainerFactory {

/**
 * Constructor
 */
public PaletteCategoryFactory() {
	setLabel(PaletteMessages.MODEL_TYPE_CATEGORY);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
protected PaletteEntry createNewEntry(Shell shell) {
	return new PaletteDrawer(PaletteMessages.NEW_CATEGORY_LABEL);
}

}
