/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.palette;

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
