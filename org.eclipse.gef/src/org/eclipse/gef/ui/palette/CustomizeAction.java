package org.eclipse.gef.ui.palette;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.customize.*;
import org.eclipse.jface.action.Action;

/**
 * Action to launch the PaletteCustomizerDialog, which allows to customize the
 * given PaletteViewerImpl.
 * 
 * @author Pratik Shah
 */
public class CustomizeAction extends Action {

private PaletteViewerImpl paletteViewer;

/**
 * Constructor
 * 
 * @param palette	The Palette which has to be customized when this action is
 * run
 */
public CustomizeAction(PaletteViewerImpl palette) {
	super();
	setText(PaletteMessages.MENU_OPEN_CUSTOMIZE_DIALOG);
	this.paletteViewer = palette;
}

/**
 * Opens the Customizer Dialog for the palette
 * 
 * @see org.eclipse.jface.action.IAction#run()
 */
public void run() {
	PaletteCustomizerDialog dialog = paletteViewer.getCustomizerDialog();
	List list = paletteViewer.getSelectedEditParts();
	if (!list.isEmpty()) {
		PaletteEntry selection = (PaletteEntry)((EditPart)list.get(0)).getModel();
		if (!(selection instanceof PaletteRoot)) {
			dialog.setDefaultSelection(selection);
		}
	}
	dialog.open();
}

}
