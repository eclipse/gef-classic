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
package org.eclipse.gef.examples.ediagram.outline;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.TreeViewer;

import org.eclipse.gef.examples.ediagram.EDiagramImages;
import org.eclipse.gef.examples.ediagram.editor.OutlineDragSourceListener;
import org.eclipse.gef.examples.ediagram.model.Diagram;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EDiagramOutlinePage 
	extends Page 
	implements IContentOutlinePage
{

protected ActionRegistry registry;
protected TreeViewer viewer;
protected Diagram model;

public EDiagramOutlinePage(Diagram diagram, ActionRegistry sharedActions) {
	model = diagram;
	registry = sharedActions;
}

public void addSelectionChangedListener(ISelectionChangedListener listener) {
}

public void createControl(Composite parent) {
	viewer = new TreeViewer();
	viewer.createControl(parent);
	viewer.setEditDomain(new EditDomain());
	viewer.setEditPartFactory(EDiagramTreePartFactory.getInstance());
	viewer.setContents(model);
	viewer.addDragSourceListener(new OutlineDragSourceListener(viewer));
}

public Control getControl() {
	return viewer.getControl();
}

public ISelection getSelection() {
	return null;
}

public void init(IPageSite pageSite) {
	super.init(pageSite);

	IActionBars bars = pageSite.getActionBars();
	String id = IWorkbenchActionConstants.UNDO;
	bars.setGlobalActionHandler(id, registry.getAction(id));
	id = IWorkbenchActionConstants.REDO;
	bars.setGlobalActionHandler(id, registry.getAction(id));
	bars.updateActionBars();
	
	IToolBarManager manager = bars.getToolBarManager();
	ImageDescriptor img = EDiagramImages.getImageDescriptor(EDiagramImages.EXPAND_ALL);
	IAction action = new Action("Expand All", img) {
		public void run() {
			expand(((Tree)viewer.getControl()).getItems());
		}
		private void expand(TreeItem[] items) {
			for (int i = 0; i < items.length; i++) {
				expand(items[i].getItems());
				items[i].setExpanded(true);
			}
		}
	};
	action.setToolTipText("Expand All");
	manager.add(action);
}

public void removeSelectionChangedListener(ISelectionChangedListener listener) {
}

public void setFocus() {
	getControl().setFocus();
}

public void setSelection(ISelection selection) {
}

}