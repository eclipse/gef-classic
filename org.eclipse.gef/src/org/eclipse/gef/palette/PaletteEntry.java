package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Image;

/**
 * Interface for a PaletteEntry
 * 
 * @author Pratik Shah */
public interface PaletteEntry {

/**
 * Property name for the entry's small icon 
 */
public static final String
	PROPERTY_SMALL_ICON                      = "Small Icon";    //$NON-NLS-1$
	
/**
 * Property name for the entry's type 
 */
public static final String
	PROPERTY_TYPE                            = "Type";          //$NON-NLS-1$
	
/**
 * Property name for the entry's large icon 
 */
public static final String
	PROPERTY_LARGE_ICON                      = "Large Icon";    //$NON-NLS-1$
	
/**
 * Property name for the entry's label (name) 
 */
public static final String
	PROPERTY_LABEL                           = "Name";          //$NON-NLS-1$
	
/**
 * Property name for the entry's description 
 */
public static final String
	PROPERTY_DESCRIPTION                     = "Description";   //$NON-NLS-1$
	
/**
 * Property name for the entry's hidden status 
 */
public static final String
	PROPERTY_VISIBLE                          = "Visible";      //$NON-NLS-1$
	
/**
 * Property name for the entry's default staus 
 */
public static final String
	PROPERTY_DEFAULT                         = "Default";       //$NON-NLS-1$
	
/**
 * Property name for the entry's parent
 */
public static final String
	PROPERTY_PARENT                          = "Parent";        //$NON-NLS-1$

/**
 * Type unknown
 */
public static String PALETTE_TYPE_UNKNOWN = "Palette_type_Unknown";//$NON-NLS-1$

/**
 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
void addPropertyChangeListener(PropertyChangeListener listener);

/**
 * @return the label for this entry.
 */
String getLabel();

/**
 * @return a large icon representing this entry.
 */
Image getLargeIcon();

/**
 * @return a short desecription describing this entry.
 */
String getDescription();

/**
 * @return the parent container of this entry
 */
PaletteContainer getParent();

/**
 * @return a small icon representing the entry.
 */
Image getSmallIcon();

/**
 * @return the type of this entry. Useful for different interpretations 
 * of the palette model.
 */
Object getType();

/**
 * @return the default nature of the entry.
 */
boolean isDefault();

/**
 * @return whether or not this entry is visible.  An entry that is not visible is not
 * shown on the palette.
 */
boolean isVisible();

/**
 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Sets the parent of this entry
 * 
 * @param parent	The parent PaletteContainer
 */
void setParent(PaletteContainer parent);
}
