package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

public class DefaultPaletteEntry
	implements PaletteEntry
{

private String label;
private String shortDescription;
private Image iconSmall;
private Image iconLarge;	
private boolean isDefault = false;
private Object type = PaletteEntry.PALETTE_TYPE_UNKNOWN;

public DefaultPaletteEntry() {}

public DefaultPaletteEntry(String label) {
	setLabel(label);
}

public DefaultPaletteEntry(String label, String shortDescription) {
	setLabel(label);
	setDescription(shortDescription);
}

public DefaultPaletteEntry(String label, String shortDescription, Object type) {
	setLabel(label);
	setDescription(shortDescription);
	setType(type);
}

public DefaultPaletteEntry(String label, String shortDescription, Image iconSmall, Image iconLarge) {
	setLabel(label);
	setDescription(shortDescription);
	setSmallIcon(iconSmall);
	setLargeIcon(iconLarge);
}

public DefaultPaletteEntry(String label, String shortDescription, Object type, Image iconSmall,Image iconLarge){
	setLabel(label);
	setDescription(shortDescription);
	setType(type);
	setSmallIcon(iconSmall);
	setLargeIcon(iconLarge);
}

public DefaultPaletteEntry(String label, String shortDescription, Image iconSmall, Image iconLarge, Object type) {
	setLabel(label);
	setDescription(shortDescription);
	setSmallIcon(iconSmall);
	setLargeIcon(iconLarge);
	setType(type);
}

public Image getSmallIcon(){
	return iconSmall;
}

public Object getType(){
	return type;
}

public boolean isDefault(){
	return isDefault;
}

public Image getLargeIcon(){
	return iconLarge;
}

public String getLabel(){
	return label;
}

public String getDescription() {
	return shortDescription;
}

public void setSmallIcon(Image icon) {
	iconSmall = icon;
}

public void setType(Object type){
	this.type = type;
}

public void setLargeIcon(Image icon) {
	iconLarge = icon;
}

public void setLabel(String s) {
	label = s;
}

public void setDescription(String s) {
	shortDescription = s;
}

public void setDefault(boolean isDefault){
	this.isDefault = isDefault;
}

public String toString() {
	return  "Palette Entry (" + (label != null ? label : "") + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}
}
