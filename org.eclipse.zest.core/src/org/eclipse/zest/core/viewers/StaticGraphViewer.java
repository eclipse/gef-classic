/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelEntityFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.StaticGraphViewerImpl;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * This view is used to represent a static graph.  Static graphs can be layed out, 
 * but do not continually update their layout locations.
 * 
 * @author Ian Bull
 */
public class StaticGraphViewer extends StructuredViewer {

	StaticGraphViewerImpl viewer = null;
	private IGraphModelFactory modelFactory = null;
	private GraphModel model;
	
	public StaticGraphViewer( Composite composite, int style ) {
		this.viewer = new StaticGraphViewerImpl(composite, style);
		hookControl( this.viewer.getControl() );
	}
	
	/**
	 * Sets the layout algorithm to use for this viewer.
	 * @param algorithm the algorithm to layout the nodes
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		viewer.setLayoutAlgorithm(algorithm);
	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		if (contentProvider instanceof IGraphContentProvider) {
			super.setContentProvider(contentProvider);
		} else if ( contentProvider instanceof IGraphEntityContentProvider ) {
			super.setContentProvider( contentProvider );
		} else {
			throw new IllegalArgumentException("Invalid content provider, only IGraphContentProvider and IGraphEntityContentProvider are supported.");
		}
	}
	
	protected void inputChanged(Object input, Object oldInput) {
		boolean highlightAdjacentNodes = ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.HIGHLIGHT_ADJACENT_NODES);
		if ( getContentProvider() instanceof IGraphContentProvider ) {
			modelFactory = new GraphModelFactory( this, highlightAdjacentNodes );
		}
		else if ( getContentProvider() instanceof IGraphEntityContentProvider ) {
			modelFactory = new GraphModelEntityFactory( this, highlightAdjacentNodes );
		}
		model = modelFactory.createModelFromContentProvider( input );

		// set the model contents (initializes the layout algorithm)
		model.setDirectedEdges(ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.DIRECTED_GRAPH)); 
		viewer.setContents(model, modelFactory);	
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
		// TODO Auto-generated method stub
		return new ArrayList(0);
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
		return viewer.getControl();
	}

}
