package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.ui.palette.DefaultPaletteViewerPreferences;

/**
 * @author Pratik Shah
 */
public class LogicPaletteViewerPreferences extends DefaultPaletteViewerPreferences {

private static final int DEFAULT_PALETTE_SIZE = 130;

/**
 * Property name for the palette size setting.  If the PropertyChangeEvent fired
 * has this property name, it means that the palette size was changed.
 */
public static final String
	PREFERENCE_PALETTE_SIZE       = "Palette Size"; //$NON-NLS-1$ 

/**
 * Constructor for LogicPaletteViewerPreferences.
 */
public LogicPaletteViewerPreferences() {
	super(LogicPlugin.getDefault().getPreferenceStore());
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#getPaletteSize()
 */
public int getPaletteSize() {
	return getPreferenceStore().getInt(PREFERENCE_PALETTE_SIZE);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#setPaletteSize(int)
 */
public void setPaletteSize(int newSize) {
	getPreferenceStore().setValue(PREFERENCE_PALETTE_SIZE, newSize);
}

}
