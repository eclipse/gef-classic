/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.messages.ZestUIMessages;
import org.eclipse.mylar.zest.core.viewers.INestedGraphContentProvider;
import org.eclipse.mylar.zest.core.viewers.INestedGraphEntityContentProvider;
import org.eclipse.mylar.zest.core.viewers.INestedGraphFocusChangedListener;
import org.eclipse.mylar.zest.core.viewers.NestedGraphViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

/**
 * A convenience class for hooking a BreadCrumbBar to a NestedGraphViewer.
 * @author Del Myers
 */
//@tag bug(151889-ViewCoupling)
public class BreadCrumbHook implements INestedGraphFocusChangedListener, DisposeListener, IBreadCrumbListener {

	private BreadCrumbBar bar;
	private NestedGraphViewer viewer;
	//for convenient access.
	private IContentProvider content;
	private IBaseLabelProvider label; 

	/**
	 * Creates a new hook for the given BreadCrumbBar and NestedGraphViewer.
	 * Expects that the content provider and the label provider for the viewer have
	 * already been set.
	 * @param bar the BreadCrumbBar to hook to.
	 * @param viewer the NestedGraphViewer to hook to.
	 */
	public BreadCrumbHook(BreadCrumbBar bar, NestedGraphViewer viewer) {
		this.bar = bar;
		this.viewer = viewer;
		this.content = viewer.getContentProvider();
		this.label = viewer.getLabelProvider();
		viewer.getControl().addDisposeListener(this);
		bar.addDisposeListener(this);
		hook();
	}
	
	
	/**
	 * Unhooks the BreadCrumbHook from the BreadCrumbBar and NestedGraphViewer. Offered
	 * for robust hooking/unhooking functionality of a BreadCrumbBar. Clients need not
	 * worry about unhooking by default. The BreadCrumbHook will automatically unhook at
	 * the end of the viewer and/or bar's lifecycle.
	 *
	 */
	public final void unHook() {
		if (checkBar()) bar.removeBreadCrumbListener(this);
		if (checkViewer()) viewer.removeFocusListener(this);
		
	}
	
	private boolean checkViewer() {
		return (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed());
	}
	
	private boolean checkBar() {
		return (bar != null && !bar.isDisposed());
	}
	/**
	 * Hooks a previously unhooked BreadCrumbHook. Clients need not worry about hooking
	 * by default. The BreadCrumbHook is automatically hooked at creation.
	 *
	 */
	public final void hook() {
		if (!(checkBar() && checkViewer())) return;
		viewer.addFocusListener(this);
		bar.addBreadCrumbListener(this);
		focusChanged(null, viewer.getFocus());
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.INestedGraphFocusChangedListener#focusChanged(java.lang.Object, java.lang.Object)
	 */
	public void focusChanged(Object oldNode, Object newNode) {
		bar.clearItems();
		Object node = newNode;
		while (node != null) {
			String text = (label instanceof ILabelProvider) ? ((ILabelProvider)label).getText(node) : "Node";
			new BreadCrumbItem(bar,0,text, node);
			node = getParent(node);
		}
		String text = ZestUIMessages.VIEW_NESTED_TOP_NODE;
		new BreadCrumbItem(bar, 0, text, null);

		bar.setBackEnabled(viewer.canGoBackward());
		bar.setForwardEnabled(viewer.canGoForward());
		bar.setUpEnabled(viewer.focusHasParent());
		bar.layout();

	}

	/**
	 * @param node
	 * @return
	 */
	private Object getParent(Object node) {
		if (content instanceof INestedGraphContentProvider) {
			return ((INestedGraphContentProvider)content).getParent(node);
		} else if (content instanceof INestedGraphEntityContentProvider) {
			return ((INestedGraphEntityContentProvider)content).getParent(node);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		if (e.widget == viewer.getControl()) {
			if (checkBar()) bar.removeBreadCrumbListener(this);
		} else if (e.widget == bar) {
			if (checkViewer()) viewer.removeFocusListener(this);
		}
		//get rid of hanging references.
		if (!(checkBar() || checkViewer())) {
			viewer = null;
			bar = null;
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.widgets.IBreadCrumbListener#breadCrumbSelected(org.eclipse.mylar.zest.core.widgets.BreadCrumbItem)
	 */
	public void breadCrumbSelected(BreadCrumbItem item) {
		viewer.setFocus(item.getData());
		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.widgets.IBreadCrumbListener#handleBackButtonSelected()
	 */
	public void handleBackButtonSelected() {
		viewer.goBackward();		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.widgets.IBreadCrumbListener#handleForwardButtonSelected()
	 */
	public void handleForwardButtonSelected() {
		viewer.goForward();
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.widgets.IBreadCrumbListener#handleUpButtonSelected()
	 */
	public void handleUpButtonSelected() {
		viewer.goUp();		
	}

}
