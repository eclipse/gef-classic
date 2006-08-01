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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.mylar.zest.core.IZestColorConstants;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.NonThreadedGraphicalViewer;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NodeChildrenComparator;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphEditPartFactory;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphRootEditPart;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;


/**
 * The GEF GraphicalViewerImpl extension for nested graphs.
 * 
 * @author Chris Callendar 
 */
public class NestedGraphViewerImpl extends NonThreadedGraphicalViewer  {
	
	private NestedGraphModel model = null;
	private NestedGraphEditPartFactory editPartFactory = null;
	private LayoutAlgorithm layoutAlgorithm;
	
	// styles
	private int style = ZestStyles.NONE;
	private boolean allowOverlap = false;
	private boolean enforceBounds = false;
	//private boolean directedGraph = false;
	
	/**
	 * Initializes the viewer with the given styles (NO_SCROLLBARS, ZOOM_REAL, ZOOM_FAKE, ZOOM_EXPAND).  
	 * @see ZestStyles
	 * @param parent
	 * @param style the styles for the viewer
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#NODES_HIGHLIGHT_ADJACENT
	 * @see ZestStyles#ENFORCE_BOUNDS
	 * @see ZestStyles#ZOOM_EXPAND
	 * @see ZestStyles#ZOOM_FAKE
	 * @see ZestStyles#ZOOM_REAL
	 * @see ZestStyles#DIRECTED_GRAPH
	 */
	public NestedGraphViewerImpl(Composite parent, int style) {
		super(parent);
		setStyle(style);
		this.getFigureCanvas().addControlListener(new ControlListener() {

			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void controlResized(ControlEvent e) {
				resize(getFigureCanvas().getSize().x, getFigureCanvas().getSize().y);
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	/**
	 * Sets the style on the NestedGraphViewer
	 * @param style the style
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#NODES_HIGHLIGHT_ADJACENT
	 * @see ZestStyles#ENFORCE_BOUNDS
	 * @see ZestStyles#ZOOM_EXPAND
	 * @see ZestStyles#ZOOM_FAKE
	 * @see ZestStyles#ZOOM_REAL
	 * @see ZestStyles#DIRECTED_GRAPH
	 */
	public void setStyle(int style) {
		this.style = style;
		this.allowOverlap = !ZestStyles.checkStyle(style, ZestStyles.NO_OVERLAPPING_NODES);
		this.enforceBounds = ZestStyles.checkStyle(style, ZestStyles.ENFORCE_BOUNDS);
		//this.directedGraph = ZestStyles.checkStyle(style, ZestStyles.DIRECTED_GRAPH);
	}
	
	/**
	 * Returns the style for this viewer.
	 * @return int the style for the viewer
	 */
	public int getStyle() {
		return style;
	}
    
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.gefx.ThreadedGraphicalViewer#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		NestedGraphRootEditPart root = new NestedGraphRootEditPart();
		if (editPartFactory == null) {
			editPartFactory = new NestedGraphEditPartFactory(root, allowOverlap, enforceBounds);
		}
		
		this.setRootEditPart(root);
		this.setEditPartFactory(editPartFactory);
	}
	

	/**
	 * Sets the model.
	 */
	public void setContents(NestedGraphModel model) {
		if ((this.model != null) && (this.model != model)) {
			// release the old model
			this.model.dispose();
		}
		this.model = model;
		fireModelUpdate();
	}
	
	/**
	 * Updates the graph based on the model.  This method
	 * will be called when the model changes.
	 * Some form of zooming might be performed.
	 */
	public void fireModelUpdate() {
		// remove all the connections (they will be recreated)
		
		
		NestedGraphModelNode previousNode = model.getPreviousRootNode();
		NestedGraphModelNode nodeToMoveTo = model.getCurrentNode();
		NestedGraphModelNode nodeToSelect = nodeToMoveTo;	// node to select in the TreeViewer
		
		
		//@tag bug(152393-TopSelection(fix)) : set-up the colors for selected nodes.
		//@tag bug(151327-Styles(todo)) : this set-up should be done by the GraphItemStyler, not hard-coded.
		if (previousNode != null) {
			//reset the color
			previousNode.setBackgroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.LIGHT_BLUE));
			previousNode.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.BLACK));
		}
		if (nodeToMoveTo != null) {
			//set the color to dark-blue
			nodeToMoveTo.setBackgroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.BLUE));
			nodeToMoveTo.setForegroundColor(ZestPlugin.getDefault().getColor(IZestColorConstants.GRAY));
		}

		if (this.getRootEditPart() instanceof NestedGraphRootEditPart) {
			NestedGraphRootEditPart rootEditPart = (NestedGraphRootEditPart)getRootEditPart();
			if ( previousNode == null ) {
				rootEditPart.zoomInOnNode( (NestedGraphNodeEditPart) nodeToMoveTo.getEditPart() );
				//removeConnections();
				super.setContents(model);
			}
			else if ( nodeToMoveTo.getRelationshipBetweenNodes( previousNode ) == NestedGraphModelNode.DESCENDANT ) {
				//removeConnections();
				
				rootEditPart.zoomInOnNode( (NestedGraphNodeEditPart) nodeToMoveTo.getEditPart() );
				
				super.setContents(model);
				
			}
			else if ( nodeToMoveTo.getRelationshipBetweenNodes( previousNode ) == NestedGraphModelNode.ANCESTOR) {
				checkScaling(previousNode);
				super.setContents(model);
				getLightweightSystem().getUpdateManager().performValidation();
				nodeToSelect = previousNode;  // select the previous node
				
				
				if (getEditPartRegistry().containsKey(nodeToSelect)) {
					//nodeToSelect.setSelected(true);
					//setSelection(new StructuredSelection(getEditPartRegistry().get(nodeToSelect)));
					//this.setFocus(this.getRootEditPart());
				}
				
				// now do the zoom (sizes and locations should be set)
				rootEditPart = (NestedGraphRootEditPart)getRootEditPart();
				rootEditPart.zoomOutOnNode((NestedGraphNodeEditPart)previousNode.getEditPart());
			}
			else {
				super.setContents(model);
			}
		}
		else {
			super.setContents(model);
		}
		
		
		
		
		// layout the children in a grid layout
		// only happens the first time a node is the current node
		doLayout(nodeToMoveTo,500, 500);
	}


	
	/**
	 * Applies a grid layout to the children of the given node ONLY if it hasn't 
	 * been already been done.  To force the layout to run again you must call 
	 * nodeToLayout.setData("GridLayout", null);
	 * @see Widget#setData(java.lang.String, java.lang.Object)
	 * @param nodeToLayout The node whose children are going to be displayed in a grid
	 * @param width		the total available width
	 * @param height	the total available height
	 */
	public void doLayout(NestedGraphModelNode nodeToLayout, double width, double height) {
		// apply the current layout on any node that hasn't aleady been layed out
		
		// Check that we have a width, height, and the total number of nodes to layout is not 0.
		// Also make sure that the layout is not currently running
		if ((width > 0) && 
			(height > 0) && 
			(nodeToLayout.getChildren().size() > 0 ) &&
			!("true".equals(nodeToLayout.getData("LayoutCompleted")))) {
			
			List children = nodeToLayout.getChildren();
			LayoutEntity[] entities = new LayoutEntity[children.size()];
			entities = (LayoutEntity[])children.toArray(entities);

			// put the nodes who have no children last
			Arrays.sort(entities, new NodeChildrenComparator());
			
			if (layoutAlgorithm == null) {
				layoutAlgorithm = new GridLayoutAlgorithm(LayoutStyles.NONE);
				((GridLayoutAlgorithm)layoutAlgorithm).setRowPadding(20);
			}
			layoutAlgorithm.setEntityAspectRatio(width / height);
			try {
				Animation.markBegin();
				layoutAlgorithm.applyLayout(entities, new LayoutRelationship[0], 0, 0, width - 20, height - 40, false, false);
				Animation.run(1000);
				
				//animator.animateNodes(animateableNodes);
				// set this attribute to signal the a grid layout has occured
				// this way the grid layout is only done once.
				nodeToLayout.setData("LayoutCompleted", "true");
				
				// now resize any node that has no children
				for (Iterator iter = children.iterator(); iter.hasNext(); ) {
					NestedGraphModelNode node = (NestedGraphModelNode)iter.next();
					if (!node.hasChildren()) {
						node.setSizeInLayout(node.getWidthInLayout(), node.getMinimizedSize().height);
					}				
				}
				
			} catch (InvalidLayoutConfiguration ilc) {
				throw new RuntimeException(ilc.getMessage());
			}
		}
	}
	
	/**
	 * Checks the size of the given node with the size of its children.
	 * If the children (unscaled) are bigger than the node then the node is scaled
	 * to make the children all visible.
	 * @param rootNode
	 */
	public void checkScaling(NestedGraphModelNode rootNode) {
		if (rootNode != null) {
			double width = rootNode.getWidthInLayout();
			double height = rootNode.getHeightInLayout() - rootNode.calculateMinimumLabelSize().height;
			
			// the minimum size without scaling
			Dimension minSize = rootNode.calculateMinimumSize();
			double minWidth = minSize.width;
			double minHeight = minSize.height;
			
			
			double scale = 1;
			double xscale = 1.0;
			double yscale = 1.0;

			xscale = width / minWidth;
			yscale = height / minHeight;
			scale = Math.min(xscale, yscale);

			scale = (double) ((int) (100 * scale)) / 100D; // chop to 2 decimal
															// places

			if (rootNode.getCastedParent() != null) {
				xscale *= rootNode.getCastedParent().getWidthScale();
				yscale *= rootNode.getCastedParent().getHeightScale();
			}

			rootNode.setScale(xscale, yscale);

		}
	}
	
	

	
	
	
	
	
	public void setCurrentNode(NestedGraphModelNode modelNode) {
		NestedGraphModelNode lastNode = model.getCurrentNode(); 
		if ((modelNode != null) && !modelNode.equals(lastNode)) {
			model.setCurrentNode(modelNode);
			fireModelUpdate();	
		}
	}
	
	public void goBack() {
		model.goBack();
		fireModelUpdate();
	}
	
	public void goForward() {
		model.goForward();
		fireModelUpdate();
	}
	
	public void goUp() {
		model.goUp();
		fireModelUpdate();
	}

	
	/**
	 * Gets an array of the selected model elements (Widget objects).
	 * @return Widget[]
	 */
	public Widget[] getSelectedModelElements() {
		Widget[] items = new Widget[ getSelectedEditParts().size() ];
		int i = 0;
		for (Iterator iterator = getSelectedEditParts().iterator(); iterator.hasNext(); ) {
			AbstractGraphicalEditPart editPart =(AbstractGraphicalEditPart) iterator.next(); 
			Object modelElement = (Object)editPart.getModel();
			if ( modelElement.equals(LayerManager.ID)) {
				items[i++] = getControl();
			} else {
				items[i++] = (Widget)modelElement;
			}
		}
		return items;
	}

    /**
     * Resizes the NestedFreeformLayer figure.
     * @param widthHint
     * @param heightHint
     */
    public void resize(int widthHint, int heightHint) {
		if (getEditPartRegistry().containsKey(model)) {
			//System.out.println(getRootEditPart());
			((NestedGraphRootEditPart)getRootEditPart()).resize(widthHint, heightHint);
			//NestedGraphEditPart editPart = (NestedGraphEditPart)getEditPartRegistry().get(model);
			//NestedFreeformLayer layer = (NestedFreeformLayer)editPart.getFigure();
			
			/*
			Rectangle mainArea = layer.resize(widthHint, heightHint);
			getFigureCanvas().layout(true, true);
			Rectangle oldArea = model.getMainArea();
			
			model.setMainArea(mainArea);
			if (oldArea.isEmpty()) {
				doLayout(model.getCurrentNode(), mainArea.width, mainArea.height);
			}
			*/
			
			
		}
    }

	/**
	 * Sets the layout algorithm to use and re-runs the layout.
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		this.layoutAlgorithm = algorithm;
		NestedGraphModelNode node = model.getCurrentNode();
		if (node != null) {
			node.setData("LayoutCompleted", "false");
			doLayout(node, model.getMainArea().width, model.getMainArea().height);
		}
	}
        
}
