package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.ui.palette.DefaultPaletteViewerPreferences;
import org.eclipse.jface.preference.IPreferenceStore;

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
	this(LogicPlugin.getDefault().getPreferenceStore());
}

/**
 * Constructor for LogicPaletteViewerPreferences.
 * @param store
 */
public LogicPaletteViewerPreferences(IPreferenceStore store) {
	super(store);
	store.setDefault(PREFERENCE_PALETTE_SIZE, DEFAULT_PALETTE_SIZE);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#getPaletteSize()
 */
public int getPaletteSize() {
	return getPreferenceStore().getInt(PREFERENCE_PALETTE_SIZE);
}

/**
 * @see org.eclipse.gef.ui.palette.DefaultPaletteViewerPreferences#handlePreferenceStorePropertyChanged(String)
 */
protected void handlePreferenceStorePropertyChanged(String property) {
	if (property.equals(PREFERENCE_PALETTE_SIZE)) {
		firePropertyChanged(property, new Integer(getPaletteSize()));
	} else {
		super.handlePreferenceStorePropertyChanged(property);
	}
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#setPaletteSize(int)
 */
public void setPaletteSize(int newSize) {
	getPreferenceStore().setValue(PREFERENCE_PALETTE_SIZE, newSize);
}

}
