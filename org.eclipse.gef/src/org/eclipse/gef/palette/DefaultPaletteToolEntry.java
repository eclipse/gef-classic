package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.tools.*;
import org.eclipse.gef.Tool;
import org.eclipse.swt.graphics.Image;

/**
 * A class used to represent a GEF tool on the palette.
 */
public class DefaultPaletteToolEntry
	extends DefaultPaletteEntry
	implements PaletteToolEntry
{

protected Tool tool;

public DefaultPaletteToolEntry(Tool tool, String label){
	setLabel(label);
	setTool(tool);
}

public DefaultPaletteToolEntry(Tool tool, String label, String shortDesc){
	setTool(tool);
	setLabel(label);
	setDescription(shortDesc);
}

public DefaultPaletteToolEntry(
	Tool tool,
	String label,
	String shortDesc,
	Image iconSmall,
	Image iconLarge)
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