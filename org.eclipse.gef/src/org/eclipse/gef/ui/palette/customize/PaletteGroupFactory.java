package org.eclipse.gef.ui.palette.customize;

import org.eclipse.gef.palette.DefaultPaletteGroup;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;

/**
 * Factory to create {@link org.eclipse.gef.palette.DefaultPaletteGroup groups}
 * 
 * @author Pratik Shah
 */
public class PaletteGroupFactory 
	extends PaletteContainerFactory
{
	
/**
 * Constructor
 */
public PaletteGroupFactory() {
	//@TODO:Pratik
	// Take care of image descriptors in this class
	setLabel(PaletteMessages.MODEL_TYPE_GROUP);
	setImageDescriptor(WorkbenchImages.getImageDescriptor(
			IWorkbenchGraphicConstants.IMG_CTOOL_SAVEALL_EDIT_HOVER));
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
protected PaletteEntry createNewEntry(Shell shell) {
	Image img = new Image(shell.getDisplay(), 
	                      getImageDescriptor().getImageData());
	DefaultPaletteGroup group =
				new DefaultPaletteGroup(PaletteMessages.NEW_GROUP_LABEL);
	//@TODO:Pratik
	// You also need to set the large icons in all the factories
	group.setSmallIcon(img);
	return group;
}

}
