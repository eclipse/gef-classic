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

import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * A separator for the palette
 * <br><br>
 * Separators can also be used as markers.  Palettes that expect external code to add
 * entries to it can use such markers to indicate where those new entries should be added.
 * For this to happen, a separator must be uniquely identified.  Unless a separator
 * is not a marker, it is recommended that it be given a unique ID.  If a separator is not
 * a marker, <code>NOT_A_MARKER</code> can be used as the ID.
 * 
 * @author Pratik Shah
 */
public class PaletteSeparator extends PaletteEntry {

private String id;

/** Type identifier **/
public static final Object PALETTE_TYPE_SEPARATOR = "$Palette Separator";//$NON-NLS-1$

/**
 * Creates a new PaletteSeparator with an empty string as its identifier.
 */
public PaletteSeparator() {
	this ("");  //$NON-NLS-1$
}

/**
 * Constructor
 * 
 * @param	id	This Separator's unique ID
 */
public PaletteSeparator(String id) {
	super(PaletteMessages.NEW_SEPARATOR_LABEL, "", PALETTE_TYPE_SEPARATOR);//$NON-NLS-1$
	setId(id);
}

/**
 * Returns the id
 * @return String
 */
public String getId() {
	return id;
}

/**
 * Sets the id
 * @param id The new id to be set
 */
public void setId(String id) {
	this.id = id;
}

}
