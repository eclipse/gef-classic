/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.views.palette;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.util.Assert;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import org.eclipse.gef.internal.ui.palette.ToolbarDropdownContributionItem;
import org.eclipse.gef.ui.palette.LayoutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;

/**
 * @author Pratik Shah
 */
public class DefaultPalettePage 
	extends Page
	implements PalettePage {
	
private PaletteViewerProvider provider;
private PaletteViewer viewer;
	
public DefaultPalettePage(PaletteViewerProvider pvProvider) {
	Assert.isNotNull(pvProvider);
	provider = pvProvider;
}

public void createControl(Composite parent) {
	viewer = provider.createPaletteViewer(parent);
}

public void dispose() {
	if (viewer != null)
		provider.destroyPaletteViewer(viewer);
	viewer = null;
}

public Control getControl() {
	return viewer.getControl();
}

/**
 * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
 */
public void init(IPageSite pageSite) {
	super.init(pageSite);
	pageSite.getActionBars().getToolBarManager().add(new ToolbarDropdownContributionItem(
			new LayoutAction(viewer.getPaletteViewerPreferences(), true)));
}

public void setFocus() {
	getControl().setFocus();
}

}