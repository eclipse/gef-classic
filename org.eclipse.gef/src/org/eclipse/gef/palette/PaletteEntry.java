package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Root class (statically) for the palette model.
 * 
 * @author Pratik Shah
 */
public class PaletteEntry
{

private PaletteContainer parent;
private String label;
private String shortDescription;
private ImageDescriptor iconSmall;
private ImageDescriptor iconLarge;
private boolean isDefault = false;
private boolean visible = true;
private Object type = PaletteEntry.PALETTE_TYPE_UNKNOWN;

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
public static final String PALETTE_TYPE_UNKNOWN = "Palette_type_Unknown";//$NON-NLS-1$

/**
 * PropertyChangeSupport
 */
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

/**
 * Constructor
 */
public PaletteEntry() {
	this(""); //$NON-NLS-1$
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param	label	The entry's name
 */
public PaletteEntry(String label) {
	this(label, "", null, null, null); //$NON-NLS-1$
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param label	The entry's name
 * @param shortDescription	The entry's description
 */
public PaletteEntry(String label, String shortDescription) {
	this(label, shortDescription, null, null, null);
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param label				Tbe entry's name
 * @param shortDescription		The entry's description
 * @param type					Tbe entry's name
 */
public PaletteEntry(String label,
							String shortDescription,
							Object type) {
	this(label, shortDescription, null, null, type);
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param label				Tbe entry's name
 * @param shortDescription		The entry's description
 * @param iconSmall			The small icon to represent this entry
 * @param iconLarge			The large icon to represent this entry
 */
public PaletteEntry(String label,
							String shortDescription,
							ImageDescriptor iconSmall,
							ImageDescriptor iconLarge) {
	this(label, shortDescription, iconSmall, iconLarge, null);
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param label				Tbe entry's name
 * @param shortDescription		The entry's description
 * @param iconSmall			The small icon to represent this entry
 * @param iconLarge			The large icon to represent this entry
 * @param type					The entry's type
 */
public PaletteEntry(String label,
							String shortDescription,
							ImageDescriptor iconSmall,
							ImageDescriptor iconLarge,
							Object type) {
	this(label, shortDescription, iconSmall, iconLarge, type, null);
}

/**
 * Constructor
 * <p>
 * Any parameter can be <code>null</code>
 * </p>
 * 
 * @param label				The entry's name
 * @param shortDescription		The entry's description
 * @param iconSmall			The small icon to represent this entry
 * @param iconLarge			The large icon to represent this entry
 * @param type					The entry's type
 * @param parent				The entry's parent
 */
public PaletteEntry(String label,
					String shortDescription,
					ImageDescriptor iconSmall,
					ImageDescriptor iconLarge,
					Object type,
					PaletteContainer parent) {
	setLabel(label);
	setDescription(shortDescription);
	setSmallIcon(iconSmall);
	setLargeIcon(iconLarge);
	setType(type);
	setParent(parent);
}

/**
 * A listener can only be added once.  Adding it more than once will do nothing.
 * 
 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
	listeners.addPropertyChangeListener(listener);
}

/**
 * @return a short desecription describing this entry.
 */
public String getDescription() {
	return shortDescription;
}

/**
 * @return the label for this entry.
 */
public String getLabel() {
	return label;
}

/**
 * @return a large icon representing this entry.
 */
public ImageDescriptor getLargeIcon() {
	return iconLarge;
}

/**
 * @return the parent container of this entry
 */
public PaletteContainer getParent() {
	return parent;
}

/**
 * @return a small icon representing the entry.
 */
public ImageDescriptor getSmallIcon() {
	return iconSmall;
}

/**
 * @return the type of this entry. Useful for different interpretations 
 * of the palette model.
 */
public Object getType() {
	return type;
}

/**
 * @return the default nature of the entry.
 */
public boolean isDefault() {
	return isDefault;
}

/**
 * @return whether or not this entry is visible.  An entry that is not visible is not
 * shown on the palette.
 */
public boolean isVisible() {
	return visible;
}

/**
 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

/**
 * Mutator method for default
 * 
 * @param newDefault	The new default value
 */
public void setDefault(boolean newDefault) {
	if (newDefault != isDefault) {
		isDefault = newDefault;
		listeners.firePropertyChange(PROPERTY_DEFAULT, !isDefault, isDefault);
	}
}

/**
 * Mutator method for description
 * 
 * @param s	The new description
 */
public void setDescription(String s) {
	if (s == null && shortDescription == null) {
		return;
	}

	if (s == null || !s.equals(shortDescription)) {
		String oldDescrption = shortDescription;
		shortDescription = s;
		listeners.firePropertyChange(PROPERTY_DESCRIPTION, oldDescrption, shortDescription);
	}
}

/**
 * Mutator method for label
 * 
 * @param s	The new name
 */
public void setLabel(String s) {
	if (s == null && label == null) {
		return;
	}

	if (s == null || !s.equals(label)) {
		String oldLabel = label;
		label = s;
		listeners.firePropertyChange(PROPERTY_LABEL, oldLabel, label);
	}
}

/**
 * Mutator method for large icon
 * 
 * @param 	icon	The large icon to represent this entry
 */
public void setLargeIcon(ImageDescriptor icon) {
	if (icon != iconLarge) {
		ImageDescriptor oldIcon = iconLarge;
		iconLarge = icon;
		listeners.firePropertyChange(PROPERTY_LARGE_ICON, oldIcon, iconLarge);
	}
}

/**
 * Sets the parent of this entry
 * 
 * @param newParent	The parent PaletteContainer
 */
public void setParent(PaletteContainer newParent) {
	if (parent != newParent) {
		PaletteContainer oldParent = parent;
		parent = newParent;
		listeners.firePropertyChange(PROPERTY_PARENT, oldParent, parent);
	}
}

/**
 * Mutator method for small icon
 * 
 * @param 	icon	The new small icon to represent this entry
 */
public void setSmallIcon(ImageDescriptor icon) {
	if (icon != iconSmall) {
		ImageDescriptor oldIcon = iconSmall;
		iconSmall = icon;
		listeners.firePropertyChange(PROPERTY_SMALL_ICON, oldIcon, icon);
	}
}

/**
 * Mutator method for type
 * 
 * @param newType	The new type
 */
public void setType(Object newType) {
	if (newType == null && type == null) {
		return;
	}

	if (type == null || !type.equals(newType)) {
		Object oldType = type;
		type = newType;
		listeners.firePropertyChange(PROPERTY_TYPE, oldType, type);
	}
}

/**
 * Makes this entry visible or invisible.  An invisible entry does not show up on the
 * palette.
 * 
 * @param newVal	The new boolean indicating whether the entry is visible or not
 */
public void setVisible(boolean newVal) {
	if (newVal != visible) {
		visible = newVal;
		listeners.firePropertyChange(PROPERTY_VISIBLE, !visible, visible);
	}
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "Palette Entry (" + (label != null ? label : "") + ")"; //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}
