package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * This is the default implementation for PaletteViewerPreferences.  It uses
 * a single IPreferenceStore to load and save the palette viewer settings.
 * 
 * @author Pratik Shah
 */
public class DefaultPaletteViewerPreferences
	implements PaletteViewerPreferences
{

private IPreferenceStore store;
private PreferenceStoreListener listener;

private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

/**
 * Constructor for DefaultPaletteViewerPreferences.
 * 
 * @param	store	The IPreferenceStore where the settings are stored.
 */
public DefaultPaletteViewerPreferences(IPreferenceStore store) {
	this.store = store;
	listener = new PreferenceStoreListener();
	store.addPropertyChangeListener(listener);
}

/**
 * NOTE: Whenever a listener is notified of a change, the oldValue of the 
 * PropertyChangeEvent will always be null.
 * 
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#addPropertyChangeListener(PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

/**
 * The oldValue of the PropertyChangeEvent that is fired will always be null.
 * 
 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
 */
protected void firePropertyChanged(String property, Object newVal) {
	listeners.firePropertyChange(property, null, newVal);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#getAutoCollapseSetting()
 */
public int getAutoCollapseSetting() {
	return store.getInt(PREFERENCE_AUTO_COLLAPSE);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#getLayoutSetting()
 */
public int getLayoutSetting() {
	return store.getInt(PREFERENCE_LAYOUT);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#removePropertyChangeListener(PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#setAutoCollapseSetting(int)
 */
public void setAutoCollapseSetting(int newVal) {
	store.setValue(PREFERENCE_AUTO_COLLAPSE, newVal);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#setLayoutSetting(int)
 */
public void setLayoutSetting(int newVal) {
	store.setValue(PREFERENCE_LAYOUT, newVal);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#setUseLargeIcons(boolean)
 */
public void setUseLargeIcons(boolean newVal) {
	store.setValue(PREFERENCE_LARGE_ICONS, newVal);
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteViewerPreferences#useLargeIcons()
 */
public boolean useLargeIcons() {
	return store.getBoolean(PREFERENCE_LARGE_ICONS);
}

private class PreferenceStoreListener implements IPropertyChangeListener {
	/**
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getProperty();
		if (property.equals(PREFERENCE_LAYOUT)) {
			firePropertyChanged(PREFERENCE_LAYOUT, 
				new Integer(getLayoutSetting()));
		} else if (property.equals(PREFERENCE_AUTO_COLLAPSE)) {
			firePropertyChanged(PREFERENCE_AUTO_COLLAPSE,
				new Integer(getAutoCollapseSetting()));
		} else if (property.equals(PREFERENCE_LARGE_ICONS)) {
			firePropertyChanged(PREFERENCE_LARGE_ICONS,
				new Boolean(useLargeIcons()));
		}
	}
}

}
