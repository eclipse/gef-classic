package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.ui.palette.PaletteMessages;

import org.eclipse.swt.widgets.Shell;

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
 * Separators cannot be inside groups.
 * 
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#canCreate(PaletteEntry)
 */
public boolean canCreate(PaletteEntry selected) {
	return super.canCreate(selected) && !(selected instanceof PaletteGroup) &&
			!(selected.getParent() instanceof PaletteGroup);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
public PaletteEntry createNewEntry(Shell shell) {
	PaletteSeparator separator = new PaletteSeparator(PaletteSeparator.NOT_A_MARKER);
	return separator;
}

}
