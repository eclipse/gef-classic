package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Tool;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * A class used to represent a GEF tool on the palette.
 */
public class PaletteToolEntry
	extends PaletteEntry
{

protected Tool tool;

public PaletteToolEntry(Tool tool, String label){
	setLabel(label);
	setTool(tool);
}

public PaletteToolEntry(Tool tool, String label, String shortDesc){
	setTool(tool);
	setLabel(label);
	setDescription(shortDesc);
}

public PaletteToolEntry(
	Tool tool,
	String label,
	String shortDesc,
	ImageDescriptor iconSmall,
	ImageDescriptor iconLarge)
{
	setTool(tool);
	setLabel(label);
	setDescription(shortDesc);
	setSmallIcon(iconSmall);
	setLargeIcon(iconLarge);
}

public Tool getTool(){
	return tool;
}

public void setTool(Tool tool){
	this.tool = tool;
}

}