package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteSeparator PaletteSeparators}.
 * 
 * @author Pratik Shah
 */
public class PaletteSeparatorFactory 
	extends PaletteEntryFactory 
{

/**
 * Constructor
 */
public PaletteSeparatorFactory() {
	setLabel(PaletteMessages.MODEL_TYPE_SEPARATOR);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
public PaletteEntry createNewEntry(Shell shell) {
	PaletteSeparator separator = new PaletteSeparator();
	return separator;
}

}
