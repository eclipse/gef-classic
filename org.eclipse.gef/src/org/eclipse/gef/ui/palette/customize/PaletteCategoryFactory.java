package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.DefaultPaletteCategory;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * Factory to create {@link org.eclipse.gef.palette.DefaultPaletteCategory categories}
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
	Image img = new Image(shell.getDisplay(), 
	                      getImageDescriptor().getImageData());
	DefaultPaletteCategory category =
				new DefaultPaletteCategory(PaletteMessages.NEW_CATEGORY_LABEL, img);
	return category;
}

}
