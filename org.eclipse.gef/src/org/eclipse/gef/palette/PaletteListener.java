package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.gef.ui.palette.PaletteViewer;

public interface PaletteListener
	extends EventListener
{

/**
 * A new tool was activated in the palette.
 * @param palette the source of the change
 * @param tool the new tool that was activated
 */
void activeToolChanged(PaletteViewer palette, ToolEntry tool);

}
