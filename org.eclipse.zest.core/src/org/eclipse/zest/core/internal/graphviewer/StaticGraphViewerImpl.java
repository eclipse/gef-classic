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
package org.eclipse.mylar.zest.core.internal.graphviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.IPanningListener;
import org.eclipse.mylar.zest.core.internal.gefx.NonThreadedGraphicalViewer;
import org.eclipse.mylar.zest.core.internal.gefx.RevealListener;
import org.eclipse.mylar.zest.core.internal.gefx.StaticGraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IZestGraphDefaults;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPartFactory;
import org.eclipse.mylar.zest.core.internal.viewers.NoOverlapLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class StaticGraphViewerImpl extends NonThreadedGraphicalViewer implements IPanningListener {
	
	private static final int ANIMATION_TIME = 500;
	private LayoutAlgorithm layoutAlgorithm = null;
	private NoOverlapLayoutAlgorithm noOverlapAlgorithm = null;
	private GraphModel model = null;
	private boolean allowMarqueeSelection = false;
	private boolean allowPanning = false;
	private boolean noOverlappingNodes = false;
	private int style = 0;
	private int nodeStyle;
	private int connectionStyle;

	private boolean hasLayoutRun = false;

	
	
	/**
	 * Initializes the viewer impl. 
	 * @see ZestStyles#PANNING
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#NODES_HIGHLIGHT_ADJACENT
	 * @see ZestStyles#DIRECTED_GRAPH
	 * @see ZestStyles#LAYOUT_GRID
	 * @see ZestStyles#LAYOUT_TREE
	 * @see ZestStyles#LAYOUT_RADIAL
	 * @see ZestStyles#LAYOUT_SPRING
	 * @see SWT#V_SCROLL
	 * @see SWT#H_SCROLL
	 */
	public StaticGraphViewerImpl(Composite composite, int style) {
		super(composite);
		this.setStyle(style);
		setConnectionStyle(IZestGraphDefaults.CONNECTION_STYLE);
		setNodeStyle(IZestGraphDefaults.NODE_STYLE);
		this.noOverlapAlgorithm = new NoOverlapLayoutAlgorithm();
		this.setSelectionManager(new ZestSelectionManager());
	}
	
	public boolean hasLayoutRun() {
		return hasLayoutRun;
	}
	
	/**
	 * Gets the style on the SpringGraphViewer
	 * @return int
	 */
	public int getStyle() {
		return this.style;
	}
	
	/**
	 * Sets the scale for the main canvas
	 * @param x The scale in the X direction
	 * @param y The scale in the y direction
	 */
	public void setScale( double x, double y ) {
		StaticGraphRootEditPart root = (StaticGraphRootEditPart)getRootEditPart();
		root.setScale(x, y);
		
	}
	
	/**
	 * Gets the scale in the y Direction
	 */
	public double getHeightScale() {
		StaticGraphRootEditPart root = (StaticGraphRootEditPart)getRootEditPart();
		return root.getYScale();
	}
	
	/**
	 * Gets the scale in the X Direction
	 */
	public double getWidthScale() {
		StaticGraphRootEditPart root = (StaticGraphRootEditPart)getRootEditPart();
		return root.getXScale();
	}


	/**
	 * Sets the style for the viewer.
	 * @param style
	 */
	public void setStyle(int style) {
		this.style = style;
		this.noOverlappingNodes = ZestStyles.checkStyle(style, ZestStyles.NO_OVERLAPPING_NODES);
		this.allowPanning = ZestStyles.checkStyle(style, ZestStyles.PANNING);
		this.allowMarqueeSelection = !allowPanning && ZestStyles.checkStyle(style, ZestStyles.MARQUEE_SELECTION);				
		(getFigureCanvas()).setScrollBarVisibility(FigureCanvas.AUTOMATIC);
		setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NONE), false);
	}
	
	/**
	 * Sets the default connection style.
	 * @param connection style the connection style to set
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		this.connectionStyle = connectionStyle;
		if (model != null) {
			model.setConnectionStyle(connectionStyle);
		}
	}
	
	/**
	 * Gets the default connection style.
	 * @return the connection style
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	
	/**
	 * Sets the default node style.
	 * @param nodeStyle the node style to set
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		this.nodeStyle = nodeStyle;
		if (model != null) {
			model.setNodeStyle(nodeStyle);
		}
	}
	
	/**
	 * Gets the default node style.
	 * @return the node style
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}
	

	/**
	 * Sets the model and initializes the layout algorithm.
	 * @see org.eclipse.mylar.zest.core.internal.gefx.ThreadedGraphicalViewer#setContents(java.lang.Object)
	 */
	//@tag zest.bug.160367-Refreshing.fix : uses the IStylingGraphModelFactory now
	public void setContents(Object model) { 
		super.setContents( model );
		this.model = (GraphModel)model;
		applyLayout();
	}
	

	/**
	 * Runs the layout on this graph.
	 * It uses the reveal listner to run the layout only if the view
	 * is visible.  Otherwise it will be deferred until after the view
	 * is available.
	 */
	public void applyLayout() {
		this.addRevealListener(new RevealListener() {
			public void revealed(Control c) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

					public void run() {
						applyLayoutInternal();
					}
				});
			}
		});
	}
	
	private void applyLayoutInternal() {		
		
		if ((model == null) || (model.getNodes().size() == 0)) 
			return;
		
		if (layoutAlgorithm == null) {
			layoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		}
		
		// calculate the size for the layout algorithm
		Dimension d = this.getCanvasSize();
		Dimension nodeSize = findBiggestNode();
		d.width = Math.max(0, d.width - nodeSize.width);
		//@tag zest.bug.159645 : should be d.height - nodeSize.height
		d.height = Math.max(0, d.height - nodeSize.height);
		
		if (d.isEmpty())
			return;
		IGraphModelConnection[] connectionsToLayout = getConnectionsToLayout();
		IGraphModelNode[] nodesToLayout = getNodesToLayout();
		// For the spring layout, I think it works a little nicer 
		// if a radial layout is run first first
		if (layoutAlgorithm instanceof SpringLayoutAlgorithm) {
			
			try {
				RadialLayoutAlgorithm radial = new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				radial.applyLayout(nodesToLayout, connectionsToLayout, 0, 0, d.width, d.height, false, false);
			} catch (InvalidLayoutConfiguration e) {
				e.printStackTrace();
			}
			
		}

		try {
			Animation.markBegin();
			layoutAlgorithm.applyLayout(nodesToLayout, connectionsToLayout, 0, 0, d.width, d.height, false, false);
			Animation.run(ANIMATION_TIME);
			getLightweightSystem().getUpdateManager().performUpdate();
			
		} catch (InvalidLayoutConfiguration e) {
			e.printStackTrace();
		}

		// enforce no overlapping nodes
		if (noOverlappingNodes) {
			noOverlapAlgorithm.layout(model.getNodes());
		}
		
		hasLayoutRun = true;
	}
	
	IGraphModelConnection[] getConnectionsToLayout() {
//		@tag zest.bug.156528-Filters.follows : make sure not to layout filtered connections, if the style says so.
		IGraphModelConnection[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = model.getConnections().iterator(); i.hasNext();) {
				IGraphItem next = (IGraphItem) i.next();
				if (next.isVisible())
					nodeList.add(next);
			}
			entities = (IGraphModelConnection[]) nodeList.toArray(new IGraphModelConnection[]{});
		} else {
			entities = model.getConnectionsArray();
		}
		return entities;
	}
	
	IGraphModelNode[] getNodesToLayout(){
//		@tag zest.bug.156528-Filters.follows : make sure not to layout filtered nodes, if the style says so.
		IGraphModelNode[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = model.getNodes().iterator(); i.hasNext();) {
				IGraphItem next = (IGraphItem) i.next();
				if (next.isVisible())
					nodeList.add(next);
			}
			entities = (IGraphModelNode[]) nodeList.toArray(new IGraphModelNode[]{});
		} else {
			entities = model.getNodesArray();
		}
		return entities;
	}
	
	private Dimension findBiggestNode() {
		Dimension dim = new Dimension();
		if (model != null) {
			for (Iterator iter = model.getNodes().iterator(); iter.hasNext(); ) {
				IGraphModelNode node = (IGraphModelNode)iter.next();
				dim.width = Math.max(dim.width, (int)node.getWidthInLayout());
				dim.height = Math.max(dim.height, (int)node.getHeightInLayout());
			}
		}
		return dim;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#createDefaultRoot()
	 */
	//@tag zest.bug.156617ClearViewer.fix : create the correct default root.
	protected void createDefaultRoot() {
		StaticGraphRootEditPart root = new StaticGraphRootEditPart();
		this.setRootEditPart(root);
	}
	
//	@tag zest.bug.156617ClearViewer.fix : convenience.
	private StaticGraphRootEditPart getCastedRoot() {
		return (StaticGraphRootEditPart)getRootEditPart();
	}
	
	protected void configureGraphicalViewer() {
		//@tag zest.bug.156617ClearViewer.fix : just clear the children, don't create a new root.
		getCastedRoot().clear();
		getCastedRoot().configure(this, allowMarqueeSelection, allowPanning);
		this.setEditPartFactory(new GraphEditPartFactory(getCastedRoot()));
	}

	public void panningStart() {
	}

	public void panning(int dx, int dy) {
			// @tag zest(bug(153356)) : Revist panning support for static graph
	   	//((AbstractLayoutAlgorithm)layoutAlgorithm).moveAllEntities(dx, dy);
	}

	public void panningEnd() {
	}

	/**
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean applyLayout) {
		this.layoutAlgorithm = algorithm;
		if (applyLayout) {
			applyLayout();
		}
	}


	
	/**
	 * Updates the connection with the given weight.  
	 * The weight should be in {-1, [0-1]}.  
	 * A weight of -1 means that there is no force/tension between the nodes.
	 * A weight of 0 results in the maximum spring length being used (farthest apart).
	 * A weight of 1 results in the minimum spring length being used (closest together).
	 * @param connection	The connection object.
	 * @param weight		The new weight for the connection.
	 */
	public void updateRelationshipWeight(Object connection, double weight) {
		IGraphModelConnection relationship = model.getInternalConnection(connection);
		if (relationship != null) {
			relationship.setWeightInLayout(weight);
			applyLayout();
		}
	}


	
	public void setSelection(List selection) {
		if (model == null) return;
		Iterator iterator = selection.iterator();
		HashMap nodeMap = model.getNodesMap();
		HashMap connectionMap = model.getConnectionMap();
		List editPartList = new ArrayList(1);
		while (iterator.hasNext()) {
			Object current = iterator.next();
			Object currentNode = nodeMap.get(current);
			if ( current != null ) {
				//@tag zest(bug(153466-NoNestedClientSupply(fix))) : use the edit part registry to avoid having back-links in the model.
				EditPart part = (EditPart) getEditPartRegistry().get(currentNode);
				if (part != null)
					editPartList.add(part);//((GraphModelNode)currentNode).getEditPart() );
			}
			else {
				Object currentConnection = connectionMap.get(current);
				if ( currentConnection != null ) {
					// Currenly we cannot select edges
				}
			}
		}
		setSelection(new StructuredSelection(editPartList));
	}
	
	public LayoutAlgorithm getLayoutAlgorithm() {
		return layoutAlgorithm;
	}
	

}
