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
package org.eclipse.gef.ui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;

public abstract class GraphicalEditorWithPalette 
	extends GraphicalEditor 
{

private static final int PALETTE_SIZE = 125;

private PaletteViewer paletteViewer;

protected void configurePaletteViewer() { }

private void createPaletteViewer(Composite parent) {
	PaletteViewer viewer = new PaletteViewer();
	viewer.createControl(parent);
	setPaletteViewer(viewer);
	configurePaletteViewer();
	hookPaletteViewer();
	initializePaletteViewer();
}

public void createPartControl(Composite parent) {
	Splitter splitter = new Splitter(parent, SWT.HORIZONTAL);
	createGraphicalViewer(splitter);
	createPaletteViewer(splitter);
	splitter.maintainSize(getPaletteViewer().getControl());
	splitter.setFixedSize(getInitialPaletteSize());
	splitter.addFixedSizeChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			handlePaletteResized(((Splitter)evt.getSource()).getFixedSize());
		}
	});
}

/**
 * Returns the model that is used in the PaletteViewer.
 */
protected abstract PaletteRoot getPaletteRoot();

protected int getInitialPaletteSize() {
	return PALETTE_SIZE;
}

/**
 * Returns the PaletteViewer.
 */
protected PaletteViewer getPaletteViewer() {
	return paletteViewer;
}

protected void handlePaletteResized(int newSize) {
}

protected void hookPaletteViewer() {
	getEditDomain().setPaletteViewer(paletteViewer);
}

protected void initializePaletteViewer() {
	getEditDomain().setPaletteRoot(getPaletteRoot());
}

protected void setPaletteViewer(PaletteViewer paletteViewer) {
	this.paletteViewer = paletteViewer;
}

}
