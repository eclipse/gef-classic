package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.viewers.*;

import org.eclipse.gef.palette.*;

public interface PaletteViewer 
	extends org.eclipse.gef.GraphicalViewer
{

void addPaletteListener(PaletteListener listener);

org.eclipse.draw2d.ButtonGroup getButtonGroup();

PaletteToolEntry getSelectedEntry();

ISelection getSelection();

void setPaletteRoot(PaletteRoot root);

void setSelection(PaletteEntry entry);

}