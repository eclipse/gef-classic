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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.palette.FlyoverPaletteAutomaton;
import org.eclipse.gef.ui.views.palette.DefaultPalettePage;
import org.eclipse.gef.ui.views.palette.PalettePage;

/**
 * <EM>IMPORTANT</EM>This class should only be used as a reference for creating your own
 * EditorPart implementation. This class will not suit everyone's needs, and may change in
 * the future. Clients may copy the implementation.
 * 
 * @author Pratik Shah
 */
public abstract class GraphicalEditorWithMovablePalette
	extends GraphicalEditor
{
	
private PaletteViewerProvider provider;

protected PaletteViewerProvider createPaletteViewerProvider() {
	return new PaletteViewerProvider(getGraphicalViewer().getEditDomain());
}

public void createPartControl(Composite parent) {
	FlyoverPaletteAutomaton splitter = new FlyoverPaletteAutomaton(parent, SWT.NONE, 
			getSite().getPage());
	super.createPartControl(splitter);
	splitter.setPaletteViewerProvider(getPaletteViewerProvider());
	splitter.setGraphicalControl(getGraphicalControl());
	splitter.setFixedSize(getInitialPaletteSize());
	splitter.addFixedSizeChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			handlePaletteResized(((Integer)evt.getNewValue()).intValue());
		}
	});
}

public Object getAdapter(Class type) {
	if (type == PalettePage.class)
		return new DefaultPalettePage(getPaletteViewerProvider());
	return super.getAdapter(type);
}

protected Control getGraphicalControl() {
	return getGraphicalViewer().getControl();
}

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

/**
 * Returns the initial palette size in pixels. Subclasses may override this method to
 * return a persisted value.
 * @see #handlePaletteResized(int)
 * @return the initial size of the palette in pixels.
 */
protected int getInitialPaletteSize() {
	return FlyoverPaletteAutomaton.DEFAULT_PALETTE_SIZE;
}

/**
 * Called whenever the user resizes the palette.  Sub-classes can store the new palette
 * size.
 * @param newSize the new size in pixels
 */
protected abstract void handlePaletteResized(int newSize);

protected void setEditDomain(DefaultEditDomain ed) {
	super.setEditDomain(ed);
	getEditDomain().setPaletteRoot(getPaletteRoot());
}

}