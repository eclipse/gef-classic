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
package org.eclipse.mylar.zest.core.viewers;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.internal.treegraphviewer.DagViewerImpl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * @author Ian Bull
 *
 */
public class DAGViewer extends StructuredViewer {

	DagViewerImpl viewer = null;

	public DAGViewer(Composite composite, int style) {
		this.viewer = new DagViewerImpl(composite, style);
	}
	
	
	protected void inputChanged(Object input, Object oldInput) {
		IDAGContentProvider contentProvider = (IDAGContentProvider) getContentProvider();
		ILabelProvider labelProvider = (ILabelProvider)getLabelProvider();
		this.viewer.setContents(input, contentProvider, labelProvider);
	}
	
	protected Widget doFindInputItem(Object element) {
		return null;
	}

	protected Widget doFindItem(Object element) {
		return null;
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {

	}

	protected List getSelectionFromWidget() {
		return Collections.EMPTY_LIST;
	}

	protected void internalRefresh(Object element) {

	}

	public void reveal(Object element) {

	}

	protected void setSelectionToWidget(List l, boolean reveal) {

	}

	public Control getControl() {
		return viewer;
	}

}
