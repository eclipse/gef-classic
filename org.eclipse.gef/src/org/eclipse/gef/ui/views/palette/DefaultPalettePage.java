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

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TransferDragSourceListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.palette.customize.ToolbarDropdownContributionItem;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

/**
 * @author Pratik Shah
 */
public class DefaultPalettePage 
	extends Page
	implements IPalettePage, IAdaptable
{

protected GraphicalViewer diagramViewer;
protected PaletteRoot root;
private PaletteViewer paletteViewer;
private TransferDragSourceListener listener;

public DefaultPalettePage(PaletteRoot model, GraphicalViewer primaryViewer) {
	super();
	root = model;
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
	//$TODO uncomment when NPE bug is fixed (50022)
	//paletteViewer.removeDragSourceListener(listener);
}

public Object getAdapter(Class type) {
	if (type == ZoomManager.class)
		return diagramViewer.getProperty(ZoomManager.class.toString());
	return null;
}

public Control getControl() {
	return paletteViewer.getControl();
}

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
System.out.println( "init");
	super.init(pageSite);
	pageSite.getActionBars().getToolBarManager().add(new ToolbarDropdownContributionItem(
			new LayoutAction(getPaletteViewer().getPaletteViewerPreferences(), true)));
}

/**
 * Called to populate the palette viewer.
 */
protected void initializePaletteViewer() {
	diagramViewer.getEditDomain().setPaletteRoot(root);
	paletteViewer.addDragSourceListener(
			listener = new TemplateTransferDragSourceListener(paletteViewer));
}

public void setFocus() {
	getControl().setFocus();
}

protected void setPaletteViewer(PaletteViewer viewer) {
	paletteViewer = viewer;
}

}