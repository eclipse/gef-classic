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
package org.eclipse.gef.ui.palette;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.util.Assert;

import org.eclipse.gef.EditDomain;

/**
 * @author Pratik Shah
 */
public class PaletteViewerProvider {
	
private EditDomain graphicalViewerEditDomain;

public PaletteViewerProvider(EditDomain graphicalViewerDomain) {
	Assert.isNotNull(graphicalViewerDomain);
	graphicalViewerEditDomain = graphicalViewerDomain;
}

protected void configurePaletteViewer(PaletteViewer viewer) {
	viewer.setContextMenu(new PaletteContextMenuProvider(viewer));
}

public PaletteViewer createPaletteViewer(Composite parent) {
	PaletteViewer pViewer = new PaletteViewer();
	pViewer.createControl(parent);
	configurePaletteViewer(pViewer);
	hookPaletteViewer(pViewer);
	return pViewer;
}

public final EditDomain getEditDomain() {
	return graphicalViewerEditDomain;
}

protected void hookPaletteViewer(PaletteViewer viewer) {
	getEditDomain().setPaletteViewer(viewer);
}

}