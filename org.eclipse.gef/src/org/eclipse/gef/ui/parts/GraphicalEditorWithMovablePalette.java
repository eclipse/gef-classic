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
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
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
			getPaletteViewerProvider());
	splitter.setInitialState(getInitialPaletteState());
	super.createPartControl(splitter);
	splitter.setGraphicalControl(getGraphicalControl());
	if (page != null) {
		splitter.setExternalViewer(page.getPaletteViewer());
		page = null;
	}
	splitter.setFixedSize(getInitialPaletteSize());
	splitter.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(FlyoutPaletteComposite.PROPERTY_DEFAULT_STATE))
				handlePaletteDefaultStateChanged(((Integer)evt.getNewValue()).intValue());
			else
				handlePaletteResized(((Integer)evt.getNewValue()).intValue());
		}
	});
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
	return FlyoutPaletteComposite.DEFAULT_PALETTE_SIZE;
}

protected int getInitialPaletteState() {
	return FlyoutPaletteComposite.FLYOVER_COLLAPSED;
}

protected abstract void handlePaletteDefaultStateChanged(int newState);

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

protected class CustomPalettePage extends DefaultPalettePage {
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