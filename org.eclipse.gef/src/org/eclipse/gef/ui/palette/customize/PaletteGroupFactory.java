package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteGroup groups}
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
	setLabel(PaletteMessages.MODEL_TYPE_GROUP);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
 */
protected PaletteEntry createNewEntry(Shell shell) {
	PaletteGroup group = new PaletteGroup(PaletteMessages.NEW_GROUP_LABEL);
	group.setUserModificationPermission(group.PERMISSION_FULL_MODIFICATION);
	return group;
}

}
