package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.FontData;

/**
 * Interface to the Object storing the viewer preferences
 * 
 * @author Pratik Shah
 */
public interface PaletteViewerPreferences {

/**
 * Constant for an Auto-Collapse Option
 * <p>
 * Indicates that containers should always auto-collapse
 * </p>
 */
public static final int
	COLLAPSE_ALWAYS = 2;
/**
 * Constant for an Auto-Collapse Option
 * <p>
 * Indicates that containers should never auto-collapse
 * </p>
 */
public static final int
	COLLAPSE_NEVER  = 1;
/**
 * Constant for an Auto-Collapse Option
 * <p>
 * Indicates that containers should auto-collapse as needed
 * This is the default auto-collapse setting.
 * </p>
 */
public static final int
	COLLAPSE_AS_NEEDED = 0;
/**
 * Constant for a Layout Option
 * <p>
 * Indicates that the palette should be displayed in the folder mode.
 * </p>
 */
public static final int
	LAYOUT_FOLDER   = 1;
/**
 * Constant for a Layout Option
 * <p>
 * Indicates that the palette should be displayed in the list mode.
 * This is the default layout setting.
 * </p>
 */
public static final int
	LAYOUT_LIST     = 0;
/**
 * Constant for a Layout Option
 * <p>
 * Indicates that the palette should be displayed in the icons only mode.
 * </p>
 */
public static final int
	LAYOUT_ICONS    = 2;
/**
 * Constant for a Layout Option
 * <p>
 * Indicates that the palette should be displayed in the details mode.
 * </p>
 */
public static final int
	LAYOUT_DETAILS   = 3;
/**
 * Property name for the layout setting.  If the PropertyChangeEvent fired
 * has this property name, it means that the layout setting was changed.
 */
public static final String
	PREFERENCE_LAYOUT             = "Layout Setting"; //$NON-NLS-1$
/**
 * Property name for the auto-collapse setting.  If the PropertyChangeEvent 
 * fired has this property name, it means that the auto-collapse setting 
 * was changed.
 */
public static final String
	PREFERENCE_AUTO_COLLAPSE      = "Auto-Collapse Setting"; //$NON-NLS-1$
/**
 * Property name for the large icon setting for folder layout.  If the PropertyChangeEvent
 * fired has this property name, it means that the large icon setting was changed for
 * folder layout.  Large icons are default.
 */
public static final String
	PREFERENCE_FOLDER_ICON_SIZE   = "Use Large Icons - Folder"; //$NON-NLS-1$
/**
 * Property name for the large icon setting for list layout.  If the PropertyChangeEvent
 * fired has this property name, it means that the large icon setting was changed for
 * list layout.  Small icons are default.
 */
public static final String
	PREFERENCE_LIST_ICON_SIZE     = "Use Large Icons - List"; //$NON-NLS-1$
/**
 * Property name for the large icon setting for icons only layout.  If the
 * PropertyChangeEvent fired has this property name, it means that the large icon setting
 * was changed for icons only layout.  Large icons are default.
 */
public static final String
	PREFERENCE_ICONS_ICON_SIZE    = "Use Large Icons - Icons"; //$NON-NLS-1$
/**
 * Property name for the large icon setting for details layout.  If the
 * PropertyChangeEvent fired has this property name, it means that the large icon setting
 * was changed for details layout.  Small icons are default.
 */
public static final String
	PREFERENCE_DETAILS_ICON_SIZE  = "Use Large Icons - Details"; //$NON-NLS-1$
/**
 * Property name for the palette font setting.  If the PropertyChangeEvent fired
 * has this property name, it means that the palette font was changed.
 */
public static final String
	PREFERENCE_FONT               = "Palette Font"; //$NON-NLS-1$ 

/**
 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener);

/**
 * Returns the current Auto-collapse settings flag.
 * <p> 
 * Possible values returned:
 * <ul>
 * 		<li>COLLAPSE_ALWAYS (Always collapse)</li>
 * 		<li>COLLAPSE_AS_NEEDED (Collapse when needed)</li> 
 * 		<li>COLLAPSE_NEVER (Never collapse)</li>
 * </ul>
 * 
 * @return int	Flag indicating what the setting is
 */
public int getAutoCollapseSetting();

/** * @return The FontData for the font to be used in the palette. */
public FontData getFontData();

/**
 * Returns the current Layout settings flag.
 * <p> 
 * Possible values returned:
 * <ul>
 * 		<li>LAYOUT_FOLDER (Folder View)</li>
 * 		<li>LAYOUT_LIST (List View)</li> 
 * 		<li>LAYOUT_ICONS (Icons Only View)</li>
 * 		<li>LAYOUT_DETAILS (Details View)</li>
 * </ul>
 * 
 * @return int	Flag indicating what the setting is
 */
public int getLayoutSetting();

/**
 * Returns the layout modes that are supported.  All four layout modes --
 * LAYOUT_FOLDER, LAYOUT_LIST, LAYOUT_ICONS, LAYOUT_DETAILS -- are supported by default. 
 * 
 * @return int[] * @see #setSupportedLayoutModes(int[])
 */
public int[] getSupportedLayoutModes();

/**
 * @param layout	LAYOUT_FOLDER, LAYOUT_LIST, LAYOUT_ICONS, or LAYOUT_DETAILS
 * @return <code>true</code> if the given layout is a supported mode */
public boolean isSupportedLayoutMode(int layout);

/**
 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Sets the auto-collapse setting.
 * <p>
 * Possible values:
 * <ul>
 * 		<li>COLLAPSE_ALWAYS (Always collapse)</li>
 * 		<li>COLLAPSE_AS_NEEDED (Collapse when needed)</li> 
 * 		<li>COLLAPSE_NEVER (Never collapse)</li>
 * </ul>
 * 
 * @param newVal	One of the above-mentioned flags
 */
public void setAutoCollapseSetting(int newVal);

/**
 * Sets the FontData for the palette.
 *  * @param data	The FontData for the font to be used in the palette. */
public void setFontData(FontData data);

/**
 * Sets the given setting as the current layout.
 * <p> 
 * Possible values:
 * <ul>
 * 		<li>LAYOUT_FOLDER (Folder View)</li>
 * 		<li>LAYOUT_LIST (List View)</li> 
 * 		<li>LAYOUT_ICONS (Icons Only View)</li>
 * 		<li>LAYOUT_DETAILS (Details View)</li>
 * </ul>
 * 
 * @param newVal	One of the above-mentioned flags
 */
public void setLayoutSetting(int newVal);

/**
 * Sets the "Use Large Icons" option for the currently active layout.
 *  * @param newVal <code>true</code> if large icons are to be used */
public void setCurrentUseLargeIcons(boolean newVal);

/**
 * The client can restrict the modes that the palette supports using this method.  By
 * default, the palette will support all layout modes: LAYOUT_ICONS, LAYOUT_DETAILS,
 * LAYOUT_FOLDER, LAYOUT_LIST.  Should the client wish to not support all these modes,
 * they can call this method with an array of the desired modes.  This method should be
 * called during set-up as soon as the preferences are created, and not later.
 * <p>
 * If the default layout mode and/or the current layout mode are not in the given array,
 * they will be updated to be the first mode in the given array.
 *  * @param modes	An array of layout modes desired.  The array must have at least one, and
 * 					is recommended to have at least two, of the recognized layout
 * 					modes.
 */
public void setSupportedLayoutModes(int[] modes);

/**
 * Sets the "Use Large Icons" option for the given layout.
 * <p>
 * The default is false for all layouts.
 * </p>
 * 
 * @param	layout	Indicates to change the icon setting associated with the given layout,
 * 					which could be any of LAYOUT_FOLDER, LAYOUT_LIST, LAYOUT_ICONS, and
 * 					LAYOUT_DETAILS.
 * @param newVal	<code>true</code> if large icons are to be used with the given layout
 */
public void setUseLargeIcons(int layout, boolean newVal);

/**
 * The defaults are as follows:
 * <ul>
 * 	<li>LAYOUT_FOLDER  - <code>true</code><li>
 * 	<li>LAYOUT_LIST    - <code>false</code><li>
 * 	<li>LAYOUT_ICONS   - <code>true</code><li>
 * 	<li>LAYOUT_DETAILS - <code>false</code><li>
 * </ul>
 * 
 * @param	layout	Indicates to get the icon setting associated with the given layout, 
 * 					which could be any of LAYOUT_FOLDER, LAYOUT_LIST, LAYOUT_ICONS, and
 * 					LAYOUT_DETAILS.
 * @return <code>true</code> if large icons are to be used with the given layout
 */
public boolean useLargeIcons(int layout);

/**
 * @return <code>true</code> if large icons are to be used with the currently active
 * layout
 */
public boolean useLargeIcons();

}