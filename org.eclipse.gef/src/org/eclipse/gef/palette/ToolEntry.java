/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.palette;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Tool;

/**
 * A factory for returning Tools.
 */
public abstract class ToolEntry
	extends PaletteEntry
{

/** Type Identifier **/
public static final Object PALETTE_TYPE_TOOL = "$Palette Tool";//$NON-NLS-1$

/** 
 * Creates a new ToolEntry.  Any parameter can be <code>null</code>.
 * @param label the entry's name
 * @param shortDesc the entry's description
 * @param iconSmall the entry's small icon
 * @param iconLarge the entry's large icon
 */
public ToolEntry(
	String label,
	String shortDesc,
	ImageDescriptor iconSmall,
	ImageDescriptor iconLarge) {
	super(label, shortDesc, iconSmall, iconLarge, PALETTE_TYPE_TOOL);
}

/**
 * Handles the creation of a new Tool. Subclasses must implement.
 * @return the new Tool
 */
public abstract Tool createTool();

}