package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

/**
 * @author Pratik Shah
 */
public class ChangeIconSizeAction 
	extends Action 
{

private PaletteViewerPreferences prefs;
private boolean currentSetting;

/**
 * Constructor
 *  * @param prefs	The <code>PaletteViewerPreferences</code> object that this action is
 * 					manipulating.
 */
public ChangeIconSizeAction(PaletteViewerPreferences prefs) {
	super(PaletteMessages.USE_LARGE_ICONS_LABEL);
	this.prefs = prefs;
	setChecked(prefs.useLargeIconsCurrently());
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	prefs.setCurrentUseLargeIcons(!currentSetting);
}

}
