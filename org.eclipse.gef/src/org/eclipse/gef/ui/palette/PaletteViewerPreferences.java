package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeListener;

/**
 * Interface to the Object storing the viewer preferences
 * 
 * @author Pratik Shah
 */
public interface PaletteViewerPreferences {

/**
 * Auto-Collapse Option Flag
 * <p>
 * Indicates that containers should always auto-collapse
 * </p>
 */
public static final int
	COLLAPSE_ALWAYS = 2;
/**
 * Auto-Collapse Option Flag
 * <p>
 * Indicates that containers should never auto-collapse
 * </p>
 */
public static final int
	COLLAPSE_NEVER  = 1;
/**
 * Auto-Collapse Option Flag
 * <p>
 * Indicates that containers should auto-collapse as needed
 * </p>
 */
public static final int
	COLLAPSE_AS_NEEDED = 0;
/**
 * Layout Option Flag
 * <p>
 * Indicates that the palette should be displayed in the folder view.
 * </p>
 */
public static final int
	LAYOUT_FOLDER   = 1;
/**
 * Layout Option Flag
 * <p>
 * Indicates that the palette should be displayed in the list view
 * This is the default auto-collapse setting.
 * </p>
 */
public static final int
	LAYOUT_LIST     = 0;
/**
 * Layout Option Flag
 * <p>
 * Indicates that the palette should be displayed in the icons only view.
 * This is the default layotu setting.
 * </p>
 */
public static final int
	LAYOUT_ICONS    = 2;

/**
 * Property name for the layout setting.  If the PropertyChangeEvent fired
 * has this property name, it means that the layout setting was changed.
 */
public static final String
	PREFERENCE_LAYOUT           = "Layout Setting"; //$NON-NLS-1$
/**
 * Property name for the auto-collapse setting.  If the PropertyChangeEvent 
 * fired has this property name, it means that the auto-collapse setting 
 * was changed.
 */
public static final String
	PREFERENCE_AUTO_COLLAPSE    = "Auto-Collapse Setting"; //$NON-NLS-1$
/**
 * Property name for the large icon setting.  If the PropertyChangeEvent fired
 * has this property name, it means that the large icon setting was changed.
 */
public static final String
	PREFERENCE_LARGE_ICONS      = "Use Large Icons"; //$NON-NLS-1$

/**
 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener);

/**
 * Called when the preferences are being disposed.
 */
void dispose();

/**
 * Returns the current Auto-collapse settings flag.
 * <p> 
 * Possible flags:
 * </p>
 * <p>
 * 		COLLAPSE_ALWAYS (Always collapse)
 * </p>
 * <p>
 * 		COLLAPSE_AS_NEEDED (Collapse when needed)
 * </p>
 * <p>
 * 		COLLAPSE_NEVER (Never collapse)
 * </p>
 * 
 * @return int	Flag indicating what the setting is
 */
public int getAutoCollapseSetting();

/**
 * Returns the current Layout settings flag.
 * <p> 
 * Possible flags:
 * </p>
 * <p>
 * 		LAYOUT_FOLDER (Folder View)
 * </p>
 * <p>
 * 		LAYOUT_LIST (List View)
 * </p>
 * <p>
 * 		LAYOUT_ICONS (Icons Only View)
 * </p>
 * 
 * @return int	Flag indicating what the setting is
 */
public int getLayoutSetting();

/**
 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Sets the auto-collapse setting.
 * <p>
 * Possible flags:
 * </p>
 * <p>
 * 		COLLAPSE_ALWAYS (Always collapse)
 * </p>
 * <p>
 * 		COLLAPSE_AS_NEEDED (Collapse when needed)
 * </p>
 * <p>
 * 		COLLAPSE_NEVER (Never collapse)
 * </p>
 * 
 * @param newVal	One of the above-mentioned flags
 */
public void setAutoCollapseSetting(int newVal);

/**
 * Sets the layout setting.
 * <p> 
 * Possible flags:
 * </p>
 * <p>
 * 		LAYOUT_FOLDER (Folder View)
 * </p>
 * <p>
 * 		LAYOUT_LIST (List View)
 * </p>
 * <p>
 * 		LAYOUT_ICONS (Icons Only View)
 * </p>
 * 
 * @param newVal	One of the above-mentioned flags
 */
public void setLayoutSetting(int newVal);

/**
 * Sets the "Use Large Icons" setting.  The value is <code>false</code> by
 * default.
 * 
 * @param newVal	A boolean indicating whether to use large icons or not.
 */
public void setUseLargeIcons(boolean newVal);

/**
 * @return A boolean indicating whether or not large icons should be used
 * in the Palette
 */
public boolean useLargeIcons();

}
