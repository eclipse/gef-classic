/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.palette.ToolbarDropdownContributionItem;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.*;

/**
 * A PalettePage which uses the PaletteViewer do display a palette.
 * @author Pratik Shah
 */
public class PaletteViewerPage 
	extends Page
	implements PalettePage, IAdaptable
{

private GraphicalViewer diagramViewer;
private PaletteRoot root;
private PaletteViewer paletteViewer;
private TransferDragSourceListener listener;

public PaletteViewerPage(PaletteRoot model, GraphicalViewer primaryViewer) {
	super();
	setRoot(model);
	diagramViewer = primaryViewer;
}

/**
 * Called to configure the viewer before it receives its contents.
 */
protected void configurePaletteViewer() {
	ContextMenuProvider provider = new PaletteContextMenuProvider(paletteViewer);
	getPaletteViewer().setContextMenu(provider);
}

/**
 * Creates the palette on the given composite.
 * @param	parent	the parent composite
 */
public void createControl(Composite parent) {
	PaletteViewer viewer = new PaletteViewer();
	setPaletteViewer(viewer);
	viewer.createControl(parent);
	configurePaletteViewer();
	hookPaletteViewer();
	initializePaletteViewer();
}

public void dispose() {
	paletteViewer.removeDragSourceListener(listener);
}

public Object getAdapter(Class type) {
	if (type == ZoomManager.class)
		return diagramViewer.getProperty(ZoomManager.class.toString());
	return null;
}

/**
 * @see org.eclipse.ui.part.IPage#getControl()
 */
public Control getControl() {
	return paletteViewer.getControl();
}

/**
 * Returns the palette viewer
 * @since 3.0
 * @return
 */
public PaletteViewer getPaletteViewer() {
	return paletteViewer;
}

/**
 * Called when the palette viewer is set. By default, the EditDomain is given the palette
 * viewer.
 */
protected void hookPaletteViewer() {
	diagramViewer.getEditDomain().setPaletteViewer(paletteViewer);
}

/**
 * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
 */
public void init(IPageSite pageSite) {
	super.init(pageSite);
	pageSite.getActionBars().getToolBarManager().add(new ToolbarDropdownContributionItem(
			new LayoutAction(getPaletteViewer().getPaletteViewerPreferences(), true)));
}

/**
 * Called to populate the palette viewer.
 */
protected void initializePaletteViewer() {
	diagramViewer.getEditDomain().setPaletteRoot(getRoot());
	paletteViewer.addDragSourceListener(
		listener = new TemplateTransferDragSourceListener(paletteViewer));
}

public void setFocus() {
	getControl().setFocus();
}

protected void setPaletteViewer(PaletteViewer viewer) {
	paletteViewer = viewer;
}

/**
 * @return Returns the root.
 */
protected PaletteRoot getRoot() {
	return root;
}
/**
 * @param root The root to set.
 */
protected void setRoot(PaletteRoot root) {
	this.root = root;
}
}