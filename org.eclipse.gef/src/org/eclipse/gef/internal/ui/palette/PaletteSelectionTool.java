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
package org.eclipse.gef.internal.ui.palette;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * Selection Tool to be used in the Palette.
 */
public class PaletteSelectionTool 
	extends SelectionTool
{

private boolean handleAbort(KeyEvent e) {
	if (e.keyCode == SWT.ESC) {
		return (getCurrentViewer() instanceof PaletteViewer 
				&& ((PaletteViewer)getCurrentViewer()).getPaletteRoot().getDefaultEntry() != null);
	}
	return false;
}

protected boolean handleKeyDown(KeyEvent e) {
	if (handleAbort(e)) {
		loadDefaultTool();
		return true;
	}
	return super.handleKeyDown(e);
}

private void loadDefaultTool() {
	((PaletteViewer)getCurrentViewer()).setActiveTool(
			((PaletteViewer)getCurrentViewer()).getPaletteRoot().getDefaultEntry());
}

}