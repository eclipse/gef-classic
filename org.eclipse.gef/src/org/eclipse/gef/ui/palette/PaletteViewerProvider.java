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

import java.util.NoSuchElementException;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.util.Assert;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;

/**
 * @author Pratik Shah
 */
public class PaletteViewerProvider {
	
protected PaletteViewer newViewer;
private PaletteViewer viewer;
private EditDomain graphicalViewerEditDomain;
private Memento state;

public PaletteViewerProvider(EditDomain graphicalViewerDomain) {
	Assert.isNotNull(graphicalViewerDomain);
	graphicalViewerEditDomain = graphicalViewerDomain;
}

// Fails silently
protected boolean applyState(PaletteViewer viewer) {
	if (state == null)
		return false;
	try {
		((MementoOriginator)viewer.getEditPartRegistry().get(viewer.getPaletteRoot()))
				.setMemento(state);
	} catch (NoSuchElementException nsee) {
		 // thrown by iterators if hierarchies don't match
		return false;
	} catch (ClassCastException cce) {
		// memento mismatch
		return false;
	} finally {
		state = null;
	}
	return true;
}

protected void configurePaletteViewer() {
	getPaletteViewer().setContextMenu(new PaletteContextMenuProvider(getPaletteViewer()));
}

protected void captureState(PaletteViewer viewer) {
	state = ((MementoOriginator)viewer.getEditPartRegistry().get(viewer.getPaletteRoot()))
					.createMemento();
	if (newViewer != null && newViewer != viewer) {
		applyState(newViewer);
		newViewer = null;
	}
}
	
public final PaletteViewer createPaletteViewer(Composite parent) {
	PaletteViewer pViewer = new PaletteViewer();
	setPaletteViewer(pViewer);
	getPaletteViewer().createControl(parent);
	configurePaletteViewer();
	hookPaletteViewer();
	initializePaletteViewer();
	if (!applyState(getPaletteViewer()))
		newViewer = getPaletteViewer();
	setPaletteViewer(null);
	return pViewer;
}

public final void destroyPaletteViewer(PaletteViewer viewer) {
	Assert.isNotNull(viewer);
	setPaletteViewer(viewer);
	captureState(getPaletteViewer());
	unhookPaletteViewer();
	if (getPaletteViewer().getControl() != null 
			&& !getPaletteViewer().getControl().isDisposed())
		getPaletteViewer().getControl().dispose();
	setPaletteViewer(null);
}

protected final EditDomain getEditDomain() {
	return graphicalViewerEditDomain;
}

protected final PaletteViewer getPaletteViewer() {
	return viewer;
}

protected void hookPaletteViewer() {
	getEditDomain().setPaletteViewer(getPaletteViewer());
	 // note that this listener is never removed.  however, it shouldn't be a problem.
	 // it'll be garbage collected along with the viewer.
	getPaletteViewer().addDragSourceListener(
			new TemplateTransferDragSourceListener(getPaletteViewer()));
}

protected void initializePaletteViewer() {
}

protected final void setPaletteViewer(PaletteViewer pViewer) {
	viewer = pViewer;
}

protected void unhookPaletteViewer() {
}
	
}