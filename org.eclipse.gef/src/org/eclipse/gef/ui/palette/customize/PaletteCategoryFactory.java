package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.PaletteCategory;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteCategory categories}
 * 
 * @author Pratik Shah
 */
public class PaletteCategoryFactory extends PaletteContainerFactory {
	
/**
 * Constructor
 */
public PaletteCategoryFactory() {
//@TODO:Pratik
// Take care of image descriptors in this class
	setLabel(PaletteMessages.MODEL_TYPE_CATEGORY);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_NEW_PAGE_HOVER));
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
protected PaletteEntry createNewEntry(Shell shell) {
	// @TODO:Pratik
	// A new image is created here everytime.  Should this image be static?
	Image img = new Image(shell.getDisplay(), getImageDescriptor().getImageData());
	PaletteCategory category = 
					new PaletteCategory(PaletteMessages.NEW_CATEGORY_LABEL, img);
	return category;
}

}
