package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

/**
 * @author Pratik Shah
 */
public class ChangeIconSizeAction 
	extends Action 
{

private PaletteViewerPreferences prefs;

/**
 * Constructor
 *  * @param prefs	The <code>PaletteViewerPreferences</code> object that this action is
 * 					manipulating.
 */
public ChangeIconSizeAction(PaletteViewerPreferences prefs) {
	super(PaletteMessages.SETTINGS_USE_LARGE_ICONS_LABEL_CAPS);
	this.prefs = prefs;
	setChecked(prefs.useLargeIcons());
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	prefs.setCurrentUseLargeIcons(!prefs.useLargeIcons());
}

}
