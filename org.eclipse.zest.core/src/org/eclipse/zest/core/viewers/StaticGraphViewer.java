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

import java.util.List;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.StaticGraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelEntityFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelEntityRelationshipFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.StaticGraphViewerImpl;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * This view is used to represent a static graph. Static graphs can be layed
 * out, but do not continually update their layout locations.
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class StaticGraphViewer extends AbstractStructuredGraphViewer {

	StaticGraphViewerImpl viewer = null;
	private IStylingGraphModelFactory modelFactory = null;


	/**
	 * Initializes the viewer.
	 * 
	 * @param composite
	 * @param style
	 *            the style for the viewer and for the layout algorithm
	 * @see ZestStyles#LAYOUT_GRID
	 * @see ZestStyles#LAYOUT_TREE
	 * @see ZestStyles#LAYOUT_RADIAL
	 * @see ZestStyles#LAYOUT_SPRING
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#NODES_HIGHLIGHT_ADJACENT
	 * @see SWT#V_SCROLL
	 * @see SWT#H_SCROLL
	 */
	public StaticGraphViewer(Composite composite, int style) {
		super(style);
		this.viewer = new StaticGraphViewerImpl(composite, style);
		hookControl(this.viewer.getControl());
	}

	/**
	 * Gets the styles for this structuredViewer
	 * 
	 * @return
	 */
	public int getStyle() {
		return this.viewer.getStyle();
	}

	/**
	 * Sets the horizontal scale
	 * 
	 * @param scale
	 */
	public void setHorizontalScale(double scale) {
		this.viewer.setScale(scale, this.viewer.getHeightScale());
	}

	/**
	 * Sets the horizontal scale
	 * 
	 * @param scale
	 */
	public void setVerticalScale(double scale) {
		this.viewer.setScale(this.viewer.getWidthScale(), scale);
	}

	/**
	 * Sets the scale in both the X and Y
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		this.viewer.setScale(scale, scale);
	}

	/**
	 * Sets the style on this structuredViewer
	 * 
	 * @param style
	 * @return
	 */
	public void setStyle(int style) {
		this.viewer.setStyle(style);
	}

	/**
	 * Sets the layout algorithm to use for this viewer.
	 * 
	 * @param algorithm
	 *            the algorithm to layout the nodes
	 * @param runLayout
	 *            if the layout should be run
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean runLayout) {
		viewer.setLayoutAlgorithm(algorithm, runLayout);
	}

	public void setContentProvider(IContentProvider contentProvider) {
		if (contentProvider instanceof IGraphContentProvider) {
			super.setContentProvider(contentProvider);
		} else if (contentProvider instanceof IGraphEntityContentProvider) {
			super.setContentProvider(contentProvider);
		} else if (contentProvider instanceof IGraphEntityRelationshipContentProvider){
			super.setContentProvider(contentProvider);
		} else {
			throw new IllegalArgumentException(
					"Invalid content provider, only IGraphContentProvider, IGraphEntityContentProvider, or IGraphEntityRelationshipContentProvider are supported.");
		}
	}
	
	
	/**
	 * Finds the graph widget item for a given user model item.  
	 * 
	 * Note:  This method returns an internal interface (GraphItem).  You should be able to cast this 
	 * to either a IGraphModelNode or IGraphModelConnection (which are also internal). These are internal
	 * because this API is not stable.  If use this method (to access internal nodes and edges), your code may not
	 * compile between versions.  
	 * 
	 * @param The user model node.
	 * @return  An IGraphItem.  This should be either a IGraphModelNode or IGraphModelConnection
	 */
	public IGraphItem findGraphItem(Object element) {
		Widget[] result = findItems(element);
		return (result.length == 0 && result[0] instanceof IGraphItem)? null : (IGraphItem)result[0];
	}
	

	/**
	 * Applys the current layout to the viewer
	 */
	public void applyLayout() {
		if (viewer.hasLayoutRun()) {
			viewer.applyLayout();
		}
	}



	protected void setSelectionToWidget(List l, boolean reveal) {
		viewer.setSelection(l);
	}

	public Control getControl() {
		return viewer.getControl();
	}
	
	/**
	 * Gets the internal static graph.  Becareful when using this because the static graph viewer impl may change.
	 * @return
	 */
	public StaticGraphViewerImpl getStaticGraph() {
		return this.viewer;
	}

	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.removeSelectionChangedListener(listener);
	}
	
	//@tag zest.bug.156286-Zooming.fix.experimental : expose the zoom manager for new actions.
	protected ZoomManager getZoomManager() {
		return ((StaticGraphRootEditPart)viewer.getRootEditPart()).getZoomManager();
	}



	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractStructuredGraphViewer#getEditPartViewer()
	 */
	protected EditPartViewer getEditPartViewer() {
		return viewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractStructuredGraphViewer#getFactory()
	 */
	protected IStylingGraphModelFactory getFactory() {
		if (modelFactory == null) {
			if (getContentProvider() instanceof IGraphContentProvider) {
				modelFactory = new GraphModelFactory(this);
			} else if (getContentProvider() instanceof IGraphEntityContentProvider) {
				modelFactory = new GraphModelEntityFactory(this);
			} else if (getContentProvider() instanceof IGraphEntityRelationshipContentProvider) {
				modelFactory = new GraphModelEntityRelationshipFactory(this);
			}
		}
		return modelFactory;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractStructuredGraphViewer#getLayoutAlgorithm()
	 */
	protected LayoutAlgorithm getLayoutAlgorithm() {
		return viewer.getLayoutAlgorithm();
	}
	
	

}
