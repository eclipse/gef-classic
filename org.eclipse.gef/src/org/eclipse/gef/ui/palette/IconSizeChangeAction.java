package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.Action;

/**
 * @author Pratik Shah
 */
public class IconSizeChangeAction extends Action {

private PaletteViewerPreferences prefs;
private boolean currentSetting;

public IconSizeChangeAction(PaletteViewerPreferences prefs) {
	setText(PaletteMessages.USE_LARGE_ICONS_LABEL);
	this.prefs = prefs;
	prefs.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			updateSetting();
		}
	});
	updateSetting();
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	prefs.setUseLargeIcons(!currentSetting);
}

private void updateSetting() {
	currentSetting = prefs.useLargeIcons();
	setChecked(currentSetting);
}

}
