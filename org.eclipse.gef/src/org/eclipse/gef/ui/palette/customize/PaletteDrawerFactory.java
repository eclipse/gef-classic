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
public class PaletteDrawerFactory extends PaletteContainerFactory {

/**
 * Constructor
 */
public PaletteDrawerFactory() {
	setLabel(PaletteMessages.MODEL_TYPE_DRAWER);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
protected PaletteEntry createNewEntry(Shell shell) {
	return new PaletteDrawer(PaletteMessages.NEW_DRAWER_LABEL);
}

}
