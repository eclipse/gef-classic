package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

/**
 * @author Pratik Shah
 */
public class DetailsLayoutAction 
	extends Action 
{

private PaletteViewerPreferences prefs;

/**
 * Constructor
 * 
 * @param	prefs	The PaletteViewerPreferences where the preference is stored
 */
public DetailsLayoutAction(PaletteViewerPreferences prefs) {
	super(PaletteMessages.SETTINGS_DETAILS_VIEW_LABEL);
	this.prefs = prefs;
	if (prefs.getLayoutSetting() == PaletteViewerPreferences.LAYOUT_DETAILS) {
		setChecked(true);
	}
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	prefs.setLayoutSetting(PaletteViewerPreferences.LAYOUT_DETAILS);
}

}