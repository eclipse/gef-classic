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
package org.eclipse.gef.ui.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;

/**
 * <EM>IMPORTANT</EM>This class should only be used as a reference for creating your own
 * EditorPart implementation. This class will not suit everyone's needs, and may change in
 * the future. Clients may copy the implementation.
 * 
 * @author Pratik Shah
 */
public abstract class GraphicalEditorWithFlyoutPalette
	extends GraphicalEditor
{
	
private PaletteViewerProvider provider;
private FlyoutPaletteComposite splitter;
private CustomPalettePage page;

protected void initializeGraphicalViewer() {
	splitter.hookDropTargetListener(getGraphicalViewer());
}

protected PaletteViewerProvider createPaletteViewerProvider() {
	return new PaletteViewerProvider(getGraphicalViewer().getEditDomain());
}

public void createPartControl(Composite parent) {
	splitter = new FlyoutPaletteComposite(parent, SWT.NONE, getSite().getPage(),
			getPaletteViewerProvider(), getPalettePreferences());
	super.createPartControl(splitter);
	splitter.setGraphicalControl(getGraphicalControl());
	if (page != null) {
		splitter.setExternalViewer(page.getPaletteViewer());
		page = null;
	}
}

public Object getAdapter(Class type) {
	if (type == PalettePage.class) {
		if (splitter == null) {
			page = new CustomPalettePage(getPaletteViewerProvider());
			return page;
		}
		return new CustomPalettePage(getPaletteViewerProvider());
	}
	return super.getAdapter(type);
}

protected Control getGraphicalControl() {
	return getGraphicalViewer().getControl();
}

protected abstract FlyoutPreferences getPalettePreferences();
	
/**
 * Returns the PaletteRoot for the palette viewer.
 * @return the palette root
 */
protected abstract PaletteRoot getPaletteRoot();

protected final PaletteViewerProvider getPaletteViewerProvider() {
	if (provider == null)
		provider = createPaletteViewerProvider();
	return provider;
}

protected int getInitialPaletteState() {
	return FlyoutPaletteComposite.STATE_COLLAPSED;
}

protected void setEditDomain(DefaultEditDomain ed) {
	super.setEditDomain(ed);
	getEditDomain().setPaletteRoot(getPaletteRoot());
}

protected class CustomPalettePage extends PaletteViewerPage {
	public CustomPalettePage(PaletteViewerProvider provider) {
		super(provider);
	}
	public void createControl(Composite parent) {
		super.createControl(parent);
		if (splitter != null)
			splitter.setExternalViewer(viewer);
	}
	public void dispose() {
		if (splitter != null)
			splitter.setExternalViewer(null);
		super.dispose();
	}
	public PaletteViewer getPaletteViewer() {
		return viewer;
	}
}

}