package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.ui.palette.PaletteMessages;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

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
//@TODO:Pratik
// Take care of the strings and image descriptors in this class
	setLabel(PaletteMessages.MODEL_TYPE_SEPARATOR);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_BUILD_EXEC_HOVER));
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
public PaletteEntry createNewEntry(Shell shell) {
	PaletteSeparator separator = new PaletteSeparator(PaletteSeparator.NOT_A_MARKER);
	return separator;
}

}
