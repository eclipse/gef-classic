package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;

public interface PaletteViewer 
	extends org.eclipse.gef.GraphicalViewer
{

void addPaletteListener(PaletteListener listener);

ToolEntry getActiveTool();

void removePaletteListener(PaletteListener paletteListener);

void setActiveTool(ToolEntry entry);

void setPaletteRoot(PaletteRoot root);

}