package com.ibm.etools.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

public interface PaletteEntry {

static String PALETTE_TYPE_UNKNOWN = "Palette_type_Unknown";//$NON-NLS-1$

/**
 * returns the label for this entry.
 */
String getLabel();

/**
 * returns a large icon representing this entry.
 */
Image getLargeIcon();

/**
 * Returns a short desecription describing this entry.
 */
String getDescription();

/**
 * Returns a small icon representing the entry.
 */
Image getSmallIcon();

/**
 * Returns the type of this entry. Useful for different interpretations 
 * of the palette model.
 */
Object getType();

/**
 * Returns the default nature of the entry.
 */
boolean isDefault();

}
