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
package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

/**
 * @author Pratik Shah
 */
public class ColumnsLayoutAction 
	extends Action 
{

private PaletteViewerPreferences prefs;

/**
 * Constructor
 * 
 * @param	prefs	The PaletteViewerPreferences where the preference is stored
 */
public ColumnsLayoutAction(PaletteViewerPreferences prefs) {
	super(PaletteMessages.SETTINGS_COLUMNS_VIEW_LABEL);
	this.prefs = prefs;
	if (prefs.getLayoutSetting() == PaletteViewerPreferences.LAYOUT_COLUMNS) {
		setChecked(true);
	}	
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	prefs.setLayoutSetting(PaletteViewerPreferences.LAYOUT_COLUMNS);
}

}