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

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Tool;

/**
 * A factory for returning Tools.
 */
public abstract class ToolEntry
	extends PaletteEntry
{

public static final Object PALETTE_TYPE_TOOL = "$Palette Tool";//$NON-NLS-1$

public ToolEntry(
	String label,
	String shortDesc,
	ImageDescriptor iconSmall,
	ImageDescriptor iconLarge)
{
	super(label,shortDesc, iconSmall, iconLarge, PALETTE_TYPE_TOOL);
}

public abstract Tool createTool();

}