package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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