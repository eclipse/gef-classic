package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.graphics.Image;

/**
 * Default implementation of PaletteEntry
 * 
 * @author Pratik Shah
 */
public class DefaultPaletteEntry 
	implements PaletteEntry 
{

private PaletteContainer parent;
private String label;
private String shortDescription;
private Image iconSmall;
private Image iconLarge;
private boolean isDefault = false;
private boolean visible = true;
private Object type = PaletteEntry.PALETTE_TYPE_UNKNOWN;

/**
 * PropertyChangeSupport
 */
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

/**
 * Constructor
 */
public DefaultPaletteEntry() {
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
public DefaultPaletteEntry(String label) {
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
public DefaultPaletteEntry(String label, String shortDescription) {
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
public DefaultPaletteEntry(
	String label,
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
public DefaultPaletteEntry(
	String label,
	String shortDescription,
	Image iconSmall,
	Image iconLarge) {
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
public DefaultPaletteEntry(
	String label,
	String shortDescription,
	Image iconSmall,
	Image iconLarge,
	Object type) {
	this(label, shortDescription, iconSmall, iconLarge, type, null);
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
 * @param parent				The entry's parent
 */
public DefaultPaletteEntry(
	String label,
	String shortDescription,
	Image iconSmall,
	Image iconLarge,
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
 * @see org.eclipse.gef.palette.PaletteEntry#addPropertyChangeListener(PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getParent()
 */
public PaletteContainer getParent() {
	return parent;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getSmallIcon()
 */
public Image getSmallIcon() {
	return iconSmall;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getType()
 */
public Object getType() {
	return type;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#isDefault()
 */
public boolean isDefault() {
	return isDefault;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#isVisible()
 */
public boolean isVisible() {
	return visible;
}

/**
 * @see PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
 */
protected void firePropertyChange(
	String property,
	Object oldVal,
	Object newVal) {
	listeners.firePropertyChange(property, oldVal, newVal);
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getLargeIcon()
 */
public Image getLargeIcon() {
	return iconLarge;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getLabel()
 */
public String getLabel() {
	return label;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getDescription()
 */
public String getDescription() {
	return shortDescription;
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#removePropertyChangeListener(PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

/**
 * Mutator method for small icon
 * 
 * @param 	icon	The new small icon to represent this entry
 */
public void setSmallIcon(Image icon) {
	if (icon != iconSmall) {
		Image oldIcon = iconSmall;
		iconSmall = icon;
		firePropertyChange(PROPERTY_SMALL_ICON, oldIcon, icon);
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
		firePropertyChange(PROPERTY_TYPE, oldType, type);
	}
}

/**
 * Mutator method for large icon
 * 
 * @param 	icon	The large icon to represent this entry
 */
public void setLargeIcon(Image icon) {
	if (icon != iconLarge) {
		Image oldIcon = iconLarge;
		iconLarge = icon;
		firePropertyChange(PROPERTY_LARGE_ICON, oldIcon, iconLarge);
	}
}

/**
 * Mutator method for default
 * 
 * @param newDefault	The new default value
 */
public void setDefault(boolean newDefault) {
	if (newDefault != isDefault) {
		isDefault = newDefault;
		firePropertyChange(
			PROPERTY_DEFAULT,
			new Boolean(!isDefault),
			new Boolean(isDefault));
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
		firePropertyChange(
			PROPERTY_DESCRIPTION,
			oldDescrption,
			shortDescription);
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
		firePropertyChange(PROPERTY_LABEL, oldLabel, label);
	}
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#setParent(PaletteContainer)
 */
public void setParent(PaletteContainer newParent) {
	if (parent != newParent) {
		PaletteContainer oldParent = parent;
		parent = newParent;
		firePropertyChange(PROPERTY_PARENT, oldParent, parent);
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
		firePropertyChange(
			PROPERTY_VISIBLE,
			new Boolean(!visible),
			new Boolean(visible));
	}
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	return "Palette Entry (" + (label != null ? label : "") + ")"; //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}
