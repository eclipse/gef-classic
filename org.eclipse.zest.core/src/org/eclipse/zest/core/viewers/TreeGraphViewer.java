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
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.internal.treegraphviewer.TreeGraphViewerImpl;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * @author Ian Bull
 */
public class TreeGraphViewer extends StructuredViewer {

	TreeGraphViewerImpl viewer = null;

	public TreeGraphViewer(Composite composite, int style) {
		this.viewer = new TreeGraphViewerImpl(composite, style);
	}
	
	
	protected void inputChanged(Object input, Object oldInput) {
		ITreeContentProvider contentProvider = (ITreeContentProvider) getContentProvider();
		ILabelProvider labelProvider = (ILabelProvider)getLabelProvider();
		this.viewer.setContents(input, contentProvider, labelProvider);
	}
	
	protected Widget doFindInputItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Widget doFindItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		// TODO Auto-generated method stub

	}

	protected List getSelectionFromWidget() {
		return Collections.EMPTY_LIST;
	}

	protected void internalRefresh(Object element) {
		// TODO Auto-generated method stub

	}

	public void reveal(Object element) {
		// TODO Auto-generated method stub

	}

	protected void setSelectionToWidget(List l, boolean reveal) {
		// TODO Auto-generated method stub

	}

	public Control getControl() {
		// TODO Auto-generated method stub
		return viewer;
	}

}
