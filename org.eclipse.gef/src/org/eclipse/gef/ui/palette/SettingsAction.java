package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.gef.ui.palette.customize.PaletteSettingsDialog;

/**
 * @author Pratik Shah
 */
public class SettingsAction extends Action {

private PaletteViewer paletteViewer;

/**
 * Constructor for SettingsAction.
 * 
 * @param palette	The Palette which has to be customized when this action is run
 */
public SettingsAction(PaletteViewer palette) {
	super();
	setText(PaletteMessages.MENU_OPEN_SETTINGS_DIALOG);
	paletteViewer = palette;
}

/**
 * Opens the Settings dialog
 * 
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	Dialog settings = new PaletteSettingsDialog(paletteViewer.getControl().getShell(), 
			((PaletteViewerImpl)paletteViewer).getPaletteViewerPreferences());
	settings.open();
}

}
