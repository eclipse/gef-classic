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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.ui.*;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.palette.ToolbarDropdownContributionItem;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteView;

/**
 * @author Pratik Shah
 */
public abstract class GraphicalEditorWithMovablePalette
	extends GraphicalEditorWithPalette 
{
	
/*
 * @TODO:Pratik    maybe this class shouldn't inherit from graphicaleditorw/palette
 * since it overrides most of its methods.
 */
	
private boolean paletteInEditor;
private Splitter splitter;
private TransferDragSourceListener listener;
private PropertyChangeListener sizeListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		handlePaletteResized(((Splitter)evt.getSource()).getFixedSize());
	}
};
	
private IPartListener2 partListener = new IPartListener2() {
	public void partActivated(IWorkbenchPartReference ref) {
	}
	public void partBroughtToTop(IWorkbenchPartReference ref) {
	}
	public void partClosed(IWorkbenchPartReference ref) {
		if (ref.getId().equals(PaletteView.ID) && !splitter.isDisposed())
			createPaletteInEditor(false);
	}
	public void partDeactivated(IWorkbenchPartReference ref) {
	}
	public void partOpened(IWorkbenchPartReference ref) {
		if (ref.getId().equals(PaletteView.ID) && !splitter.isDisposed())
			closePaletteInEditor(false);
	}
	public void partHidden(IWorkbenchPartReference ref) {
	}
	public void partVisible(IWorkbenchPartReference ref) {
	}
	public void partInputChanged(IWorkbenchPartReference ref) {
	}
};

protected void closePaletteInEditor(boolean force) {
	if (paletteInEditor || force) {
		// the palette is disposed when the new one is set
		splitter.setMaximizedControl(splitter.getChildren()[0]);
		paletteInEditor = false;
	}
}

protected void configurePaletteViewer() {
	super.configurePaletteViewer();
	ContextMenuProvider provider = new PaletteContextMenuProvider(getPaletteViewer());
	getPaletteViewer().setContextMenu(provider);
}

protected void createPaletteInEditor(boolean force) {
	if (!paletteInEditor || force) {
		/*
		 * @TODO:Pratik  use setRedraw() at other places in this class to reduce 
		 * flickering.  See if you can also prevent resize.
		 */
		splitter.setRedraw(false);
		createPaletteViewer(splitter);
		splitter.maintainSize(getPaletteViewer().getControl());
		getPaletteViewer().getControl().moveAbove(splitter.getChildren()[0]);
		splitter.setMaximizedControl(null);
		splitter.setRedraw(true);
		paletteInEditor = true;
	}
}

protected void createPaletteViewer(Composite parent) {
	PaletteViewer viewer = new PaletteViewer();
	viewer.createControl(parent);
	setPaletteViewer(viewer);
}

public void createPartControl(Composite parent) {
	splitter = new Splitter(parent, SWT.HORIZONTAL);
	createGraphicalViewer(splitter);
	splitter.addFixedSizeChangeListener(sizeListener);
	splitter.setFixedSize(getInitialPaletteSize());

	paletteInEditor = getSite().getWorkbenchWindow().getActivePage()
			.findView(PaletteView.ID) == null;
	if (paletteInEditor) {
		createPaletteInEditor(true);
	} else {
		closePaletteInEditor(true);
	}
}

public void dispose() {
	splitter.removeFixedSizeChangeListener(sizeListener);
	getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
	super.dispose();
}

public Object getAdapter(Class type) {
	if (type == PalettePage.class)
		return new GraphicalEditorPalettePage();
	return super.getAdapter(type);
}

protected void hookPaletteViewer() {
	super.hookPaletteViewer();
	getPaletteViewer().addDragSourceListener(
			listener = new TemplateTransferDragSourceListener(getPaletteViewer()));		
}

protected void setPaletteViewer(PaletteViewer paletteViewer) {
	if (getPaletteViewer() == paletteViewer)
		return;
	if (getPaletteViewer() != null) {
		unhookPaletteViewer();
		if (getPaletteViewer().getControl() != null 
				&& !getPaletteViewer().getControl().isDisposed()) {
			getPaletteViewer().getControl().dispose();
		}
	}
	super.setPaletteViewer(paletteViewer);
	if (getPaletteViewer() != null) {
		configurePaletteViewer();
		hookPaletteViewer();
		initializePaletteViewer();
	}
}

protected void setSite(IWorkbenchPartSite site) {
	super.setSite(site);
	getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
}

protected void unhookPaletteViewer() {
	getPaletteViewer().removeDragSourceListener(listener);
	listener = null;
}

protected class GraphicalEditorPalettePage
		extends Page
		implements PalettePage, IAdaptable {
	protected PaletteViewer viewer;
	public void init(IPageSite site) {
		super.init(site);
		getSite().getActionBars().getToolBarManager().add(
				new ToolbarDropdownContributionItem(new LayoutAction(
				getPaletteViewer().getPaletteViewerPreferences(), true)));
	}
	public void createControl(Composite parent) {
		createPaletteViewer(parent);
		viewer = GraphicalEditorWithMovablePalette.this.getPaletteViewer();
	}
	public void dispose() {
		if (getControl() != null && !getControl().isDisposed())
			getControl().dispose();
		viewer = null;
	}
	public Object getAdapter(Class adapter) {
		if (adapter == ZoomManager.class)
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		return null;
	}
	public Control getControl() {
		return getPaletteViewer().getControl();
	}
	public PaletteViewer getPaletteViewer() {
		return viewer;
	}
	public void setFocus() {
		getControl().setFocus();
	}
}

}