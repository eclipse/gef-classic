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

import java.util.Iterator;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.gefx.IPanningListener;
import org.eclipse.mylar.zest.core.internal.gefx.NonThreadedGraphicalViewer;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.parts.GraphEditPartFactory;
import org.eclipse.mylar.zest.core.internal.viewers.NoOverlapLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class StaticGraphViewerImpl extends NonThreadedGraphicalViewer implements IPanningListener {
	
	private LayoutAlgorithm layoutAlgorithm = null;
	private NoOverlapLayoutAlgorithm noOverlapAlgorithm = null;
	private GraphModel model = null;
	private IGraphModelFactory modelFactory = null;
	private boolean allowMarqueeSelection = false;
	private boolean allowPanning = false;
	private boolean noOverlappingNodes = false;
	private boolean directedGraph = false;
	//private boolean enforeBounds = false;
	private boolean vScroll = false;
	private boolean hScroll = false;
	private int style = 0;

	private boolean hasLayoutRun = false;

	/**
	 * Initializes the viewer impl. 
	 * @see ZestStyles#PANNING
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#HIGHLIGHT_ADJACENT_NODES
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
		this.noOverlapAlgorithm = new NoOverlapLayoutAlgorithm();
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
	 * Sets the style for the viewer.
	 * @param style
	 */
	public void setStyle(int style) {
		this.style = style;
		this.noOverlappingNodes = ZestStyles.checkStyle(style, ZestStyles.NO_OVERLAPPING_NODES);
		this.allowPanning = ZestStyles.checkStyle(style, ZestStyles.PANNING);
		this.allowMarqueeSelection = !allowPanning && ZestStyles.checkStyle(style, ZestStyles.MARQUEE_SELECTION);
		this.directedGraph = ZestStyles.checkStyle(style, ZestStyles.DIRECTED_GRAPH);
		//this.enforeBounds = ZestStyles.checkStyle(style, ZestStyles.ENFORCE_BOUNDS);
		this.hScroll = ZestStyles.checkStyle(style, SWT.H_SCROLL);
		this.vScroll = ZestStyles.checkStyle(style, SWT.V_SCROLL);
		
		// set the scrollbar visibility
		// TODO this doesn't work...  scrollbars never show up unless set to ALWAYS
		if (hScroll || vScroll) {
			getFigureCanvas().setHorizontalScrollBarVisibility((hScroll ? FigureCanvas.AUTOMATIC : FigureCanvas.NEVER));
			getFigureCanvas().setVerticalScrollBarVisibility((vScroll ? FigureCanvas.AUTOMATIC : FigureCanvas.NEVER));
		}
		getFigureCanvas().setBorder(new LineBorder(2));
		
		if (model != null) {
			// Set the styles that must be set on the model
			model.setDirectedEdges(this.directedGraph);
			model.fireAllPropertyChange(GraphModelConnection.DIRECTED_EDGE_PROP, null, null);
		}
		
		setLayoutFromStyle(style);
	}
	
	private void setLayoutFromStyle(int style) {
		boolean grid = ZestStyles.checkStyle(ZestStyles.LAYOUT_GRID, style);
		boolean radial = ZestStyles.checkStyle(ZestStyles.LAYOUT_RADIAL, style);
		boolean tree = ZestStyles.checkStyle(ZestStyles.LAYOUT_TREE, style);
		if (grid) {
			setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
		} else if (radial) {
			setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
		} else if (tree) {
			setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), false);
		} else {
			// default to Spring layout
			SpringLayoutAlgorithm layout = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
			setLayoutAlgorithm(layout, false);
		}
	}
	
	/**
	 * Sets the model and initializes the layout algorithm.
	 * @see org.eclipse.mylar.zest.core.internal.gefx.ThreadedGraphicalViewer#setContents(java.lang.Object)
	 */
	public void setContents(GraphModel model, IGraphModelFactory modelFactory) { 
		super.setContents( model );
		this.model = model;
		this.modelFactory = modelFactory;
		
		applyLayout();
		
		this.addControlListener(new ControlListener() {
			private boolean isMinimized = true;
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				// handle minimized case
				Dimension d = StaticGraphViewerImpl.this.getCanvasSize();
				if (d.isEmpty()) {
					isMinimized = true;
				} else if (isMinimized) {
					isMinimized = false;
					applyLayout();
				}
			}
		});	
	}
	
	public void applyLayout() {
		if ((model == null) || (model.getNodes().size() == 0)) 
			return;
		
		if (layoutAlgorithm == null) {
			layoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		}
		
		// calculate the size for the layout algorithm
		Dimension d = this.getCanvasSize();
		Dimension nodeSize = findBiggestNode();
		d.width = Math.max(0, d.width - nodeSize.width);
		d.height = Math.max(0, d.height - nodeSize.width);
		
		if (d.isEmpty())
			return;
		
		// For the spring layout, I think it works a little nicer 
		// if a radial layout is run first first
		if (layoutAlgorithm instanceof SpringLayoutAlgorithm) {
			try {
				RadialLayoutAlgorithm radial = new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				radial.applyLayout(model.getNodesArray(), model.getConnectionsArray(), 0, 0, d.width, d.height, false, false);
			} catch (InvalidLayoutConfiguration e) {
				e.printStackTrace();
			}
		}

		try {
			layoutAlgorithm.applyLayout(model.getNodesArray(), model.getConnectionsArray(), 
								0, 0, d.width, d.height, false, false);
		} catch (InvalidLayoutConfiguration e) {
			e.printStackTrace();
		}
		// enforce no overlapping nodes
		if (noOverlappingNodes) {
			noOverlapAlgorithm.layout(model.getNodes());
		}
		
		hasLayoutRun = true;
	}
	
	private Dimension findBiggestNode() {
		Dimension dim = new Dimension();
		if (model != null) {
			for (Iterator iter = model.getNodes().iterator(); iter.hasNext(); ) {
				GraphModelNode node = (GraphModelNode)iter.next();
				dim.width = Math.max(dim.width, (int)node.getWidthInLayout());
				dim.height = Math.max(dim.height, (int)node.getHeightInLayout());
			}
		}
		return dim;
	}

	protected void configureGraphicalViewer() {
		GraphRootEditPart root = new GraphRootEditPart(this, allowMarqueeSelection, allowPanning);
		this.setRootEditPart(root);
		this.setEditPartFactory(new GraphEditPartFactory());
	}

	public void panningStart() {
	}

	public void panning(int dx, int dy) {
	   	((AbstractLayoutAlgorithm)layoutAlgorithm).moveAllEntities(dx, dy);
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
	 * Creates a new relationship between the source node and the destination node.
	 * If either node doesn't exist then it will be created.
	 * @param connection	The connection data object.
	 * @param srcNode		The source node data object.
	 * @param destNode		The destination node data object.
	 */
	public void addRelationship(Object connection, Object srcNode, Object destNode) {
		// create the new relationship
		GraphModelConnection newConnection = modelFactory.createRelationship(model, connection, srcNode, destNode);

		// add it to the layout algorithm
		layoutAlgorithm.addRelationship(newConnection);
		applyLayout();
	}
	
	/**
	 * Adds a new relationship given the connection.  It will use the content provider 
	 * to determine the source and destination nodes.
	 * @param connection	The connection data object.
	 */
	public void addRelationship (Object connection) {
		if (model.getInternalConnection(connection) == null) {
			// create the new relationship
			GraphModelConnection newConnection = modelFactory.createRelationship(model, connection);
			
			// add it to the layout algorithm
			layoutAlgorithm.addRelationship(newConnection);
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
		GraphModelConnection relationship = model.getInternalConnection(connection);
		if (relationship != null) {
			relationship.setWeightInLayout(weight);
			applyLayout();
		}
	}

	/**
	 * Removes the given connection object from the layout algorithm and the model.
	 * @param connection
	 */
	public void removeRelationship(Object connection) {
		GraphModelConnection relation = model.getInternalConnection(connection);
		
		if (relation != null) {
			// remove the relationship from the layout algorithm
			layoutAlgorithm.removeRelationship(relation);
			
			// remove the relationship from the model
			model.removeConnection(relation);
			applyLayout();
		}
	}

	
	/**
	 * Creates a new node and adds it to the graph.  If it already exists nothing happens.
	 * @param newNode
	 */
	public void addNode(Object element) {
		if (model.getInternalNode(element) == null ) {
			// create the new node
			GraphModelNode newNode = modelFactory.createNode(model, element);
			
			// add it to the layout algorithm
			layoutAlgorithm.addEntity(newNode);
			applyLayout();
		}
	}

	/**
	 * Removes the given element from the layout algorithm and the model.
	 * @param element	The node element to remove.
	 */
	public void removeNode(Object element) {
		GraphModelNode node = model.getInternalNode(element);
		
		if (node != null) {
			// remove the node from the layout algorithm and all the connections
			layoutAlgorithm.removeEntity(node);
			layoutAlgorithm.removeRelationships(node.getSourceConnections());
			layoutAlgorithm.removeRelationships(node.getTargetConnections());
			
			// remove the node and it's connections from the model
			model.removeNode(node);
			applyLayout();
		}
	}
	

}
