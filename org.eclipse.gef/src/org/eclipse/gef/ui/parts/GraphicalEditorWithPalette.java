package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerImpl;

public abstract class GraphicalEditorWithPalette 
	extends GraphicalEditor 
{

private static final int PALETTE_SIZE = 125;

private PaletteViewer paletteViewer;

protected void configurePaletteViewer() { }

private void createPaletteViewer(Composite parent) {
	PaletteViewer viewer = new PaletteViewerImpl();
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
