package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

public class PaletteRoot
	extends PaletteContainer
{

public static String PALETTE_TYPE_ROOT = "Palette_Root";//$NON-NLS-1$
private ToolEntry defaultEntry;

public PaletteRoot() {
	super(null, null, null, PALETTE_TYPE_ROOT);
}

public ToolEntry getDefaultEntry() {
	return defaultEntry;
}

public void setDefaultEntry(ToolEntry entry) {
	this.defaultEntry = entry;
}

public String toString() {
	return "Palette Root"; //$NON-NLS-1$
}

}
