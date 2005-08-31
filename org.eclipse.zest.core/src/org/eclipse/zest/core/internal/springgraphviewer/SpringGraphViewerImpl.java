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
package org.eclipse.mylar.zest.core.internal.springgraphviewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.DebugPrint;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.gefx.ThreadedGraphicalViewer;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.springgraphviewer.parts.GraphEditPartFactory;
import org.eclipse.mylar.zest.core.internal.springgraphviewer.parts.GraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.springgraphviewer.parts.GraphRootEditPart;
import org.eclipse.mylar.zest.core.internal.viewers.IPanningListener;
import org.eclipse.mylar.zest.core.viewers.SpringGraphViewer;
import org.eclipse.mylar.zest.layouts.Stoppable;
import org.eclipse.mylar.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;


/**
 * Simple GEF viewer used to display graphical layouts.
 * 
 * @author irbull
 * @author ccallendar
 */
public class SpringGraphViewerImpl extends ThreadedGraphicalViewer implements IPanningListener, ZoomListener {
	
	private AbstractLayoutAlgorithm layoutAlgorithm = null;
	private Stoppable layoutThread = null;
	private GraphModel model = null;
	private IGraphModelFactory modelFactory = null;
	
	/** Indicates if marquee (multiple) selection of nodes is allowed. */
	private boolean allowMarqueeSelection;

	/* irbull: These were the force directed settings
	private boolean noOverlappingNodes = false;
	private boolean stabilize = false;
	private boolean enforceBounds = false;
	*/
	private boolean directedGraph = false;
	private int style = ZestStyles.NONE;
	
	/** 
	 * Indicates if panning (moving the canvas essentially) is allowed.  This
	 * can only be allowed if marquee selection is not allowed.
	 */ 
	private boolean allowPanning;
	
	/** If the layout algorithm was running before panning started. */
	private boolean wasRunningBeforePanning = false;
	
	
	/**
	 * SpringGraphViewerImpl constructor.
	 * @param composite	the composite object
	 * @param style	The style for this viewer. 
	 * 				Note marquee selection can only be enabled if panning is not enabled.
	 * @see ZestStyles#PANNING
	 * @see ZestStyles#MARQUEE_SELECTION
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#STABILIZE
	 * @see ZestStyles#ENFORCE_BOUNDS
	 * @see ZestStyles#DIRECTED_GRAPH
	 */
	public SpringGraphViewerImpl(Composite composite, int style) {
		super(composite);
		this.setStyle( style );
		this.addSelectionChangedListener( new _selectionChangedListener() );
	}	
	
	/**
	 * Sets the style on the SpringGraphViewer
	 * @param style	The style for this viewer. 
	 * 				Note marquee selection can only be enabled if panning is not enabled.
	 * @see ZestStyles#PANNING
	 * @see ZestStyles#MARQUEE_SELECTION
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#STABILIZE
	 * @see ZestStyles#ENFORCE_BOUNDS
	 * @see ZestStyles#DIRECTED_GRAPH
	 */
	public void setStyle( int style ) {
		this.style = style;
		/* irbull: These were the Force Directed Settings
		this.noOverlappingNodes = ZestStyles.checkStyle(style, ZestStyles.NO_OVERLAPPING_NODES);
		this.stabilize = ZestStyles.checkStyle(style, ZestStyles.STABILIZE);
		this.enforceBounds = ZestStyles.checkStyle(style, ZestStyles.ENFORCE_BOUNDS);
		*/
		this.allowPanning = ZestStyles.checkStyle(style, ZestStyles.PANNING);
		this.allowMarqueeSelection = !allowPanning && ZestStyles.checkStyle(style, ZestStyles.MARQUEE_SELECTION);
		
		this.directedGraph = ZestStyles.checkStyle(style, ZestStyles.DIRECTED_GRAPH);
		
		if ( model != null ) {
			// Set the styles that must be set on the model
			model.setDirectedEdges( this.directedGraph );
			model.fireAllPropertyChange(GraphModelConnection.DIRECTED_EDGE_PROP, null, null);
		}
	}
	
	
	/**
	 * Gets the style on the SpringGraphViewer
	 * @return
	 */
	public int getStyle( ) {
		return this.style;
	}
	
	/**
	 * Sets the model and initializes the layout algorithm.
	 * @see org.eclipse.mylar.zest.core.internal.gefx.ThreadedGraphicalViewer#setContents(java.lang.Object)
	 */
	public void setContents(GraphModel model, IGraphModelFactory modelFactory) {
		super.setContents(model);
		this.model = model;
		this.modelFactory = modelFactory;
		
		Dimension d = this.getCanvasSize();
		
		layoutAlgorithm = new SpringLayoutAlgorithm();
		/* irbull, these were the force directed settings
		((SpringLayoutAlgorithm) layoutAlgorithm).setSpringLengthRange(60, 225);
		((SpringLayoutAlgorithm) layoutAlgorithm).setStabilize(stabilize);
		((SpringLayoutAlgorithm) layoutAlgorithm).setEnforceBounds(enforceBounds);
		((SpringLayoutAlgorithm) layoutAlgorithm).setMinimumStabilizeDistance(ForceDirectedAlgorithm.DEFAULT_MIN_STABILIZE_DISTANCE);
		((SpringLayoutAlgorithm) layoutAlgorithm).setOverlappingNodesAllowed(!noOverlappingNodes);
		*/
		layoutThread = layoutAlgorithm.getLayoutThread(model.getNodesArray(), model.getConnectionsArray(),  5D, 5D, (double)d.width,(double) d.height);
		
		this.addThread(layoutThread);		
		//this.addThread(new FreqUpdater());
		this.addControlListener(new ControlListener() {
			private boolean minimized = false;
			private boolean wasRunning = false;
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				// handle minimized case
				Dimension d = SpringGraphViewerImpl.this.getCanvasSize();
				if (d.isEmpty()) {
					wasRunning = layoutAlgorithm.isRunning() && !layoutAlgorithm.isPaused();
					layoutAlgorithm.pause();
					minimized = true;
					DebugPrint.println("Viewer minimized - pausing layout algorithm");
				} else {
					layoutAlgorithm.setLayoutArea(5, 5, Math.max(10, d.width - 60), Math.max(10, d.height - 30));
					if (minimized) {
						minimized = false;
						if (wasRunning) {
							layoutAlgorithm.resume();
							DebugPrint.println("Viewer restored - resuming layout algorithm");
						}
					}
				}
			}
		});		
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		GraphRootEditPart root = new GraphRootEditPart(this, allowMarqueeSelection, allowPanning);
		//setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), this);
		
		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		ZoomManager zoomMgr = root.getZoomManager();
		zoomMgr.setZoomLevelContributions(zoomLevels);

		zoomMgr.addZoomListener(this);
		IAction zoomIn = new ZoomInAction(zoomMgr);
		IAction zoomOut = new ZoomOutAction(zoomMgr);
		
		KeyHandler handler = getKeyHandler();
		if (handler == null) {
			handler = new KeyHandler();
			this.setKeyHandler(handler);
		}
		
		// so many zoom choices, so little time!
		getKeyHandler().put(KeyStroke.getPressed('+', 43, SWT.CTRL), zoomIn);
		getKeyHandler().put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, SWT.CTRL), zoomIn);
		getKeyHandler().put(KeyStroke.getPressed('=', 61, SWT.CTRL), zoomIn);
		getKeyHandler().put(KeyStroke.getPressed('-', 45, SWT.CTRL), zoomOut);
		getKeyHandler().put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, SWT.CTRL), zoomOut);
		this.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), MouseWheelZoomHandler.SINGLETON);
		
		this.setRootEditPart(root);
		this.setEditPartFactory(new GraphEditPartFactory());
	}

	/**
	 * Gets an array of the selected model elements (Widget objects).
	 * @return Widget[]
	 */
	public Widget[] getSelectedModelElements() {
		Widget[] items = new Widget[ getSelectedEditParts().size() ];
		int i = 0;
		for ( Iterator iterator = getSelectedEditParts().iterator(); iterator.hasNext(); ) {
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
	
    public void setSelection(ISelection selection) {
    	if (!(selection instanceof IStructuredSelection))
    		return;
    	List orderedSelection = ((IStructuredSelection)selection).toList();
    	ArrayList editPartSelection = new ArrayList( orderedSelection.size() );
    	for( int i = 0; i < orderedSelection.size(); i++ ) {
    		editPartSelection.add( i, orderedSelection.get(i));
    	}
    	super.setSelection( new StructuredSelection( editPartSelection )  );
    }
    
    /**
     * @see SpringGraphViewerImpl#centerNode(GraphModelNode)
     */
    public void setCenterSelection( GraphModelNode nodeToCenter, int x, int y ) {
    	//TODO: Really make this the center, note this is harder when we are in a SASHForm
    	nodeToCenter.setLocationInLayout(x,y);
    	StructuredSelection selection = new StructuredSelection( new Object[]{nodeToCenter} );
    	setSelection( selection );
    }
	
    /**
     * This is an alternate way of centering the given node.  It 
     * pans the canvas to make the given node be at the center of the viewer.
     * @param nodeToCenter	The node to center (assumed not null).
     */
    public void centerNodeInCanvas(GraphModelNode nodeToCenter) {
    	Dimension dim = getTranslatedCanvasSize();
    	int cx = (dim.width / 2);
    	int cy = (dim.height / 2);
		int dx = cx - (int)nodeToCenter.getXInLayout();
		int dy = cy - (int)nodeToCenter.getYInLayout();
		System.out.println(cx + "," + cy + "   " + nodeToCenter.getXInLayout() + "," + nodeToCenter.getYInLayout());
		panningStart();
		panning(dx, dy);
		panningEnd();    	
    }
    
    /**
     * Indicates that panning has started.  Pauses the layout algorithm if it is running.  
     * @see org.eclipse.mylar.zest.core.internal.springgraphviewer.IPanningListener#panningStart()
     */
    public void panningStart() {
    	wasRunningBeforePanning = layoutAlgorithm.isRunning() && !layoutAlgorithm.isPaused();
    	if (wasRunningBeforePanning) {
    		layoutAlgorithm.pause();
    	}
    }
    
    /**
     * Handles a change in position due to panning.  Tells the {@link SpringGraphViewer}
     * to move all the nodes.
     * @param dx	the change in x position
     * @param dy	the change in y position
     * @see org.eclipse.mylar.zest.core.internal.springgraphviewer.IPanningListener#panning(int, int)
     */
    public void panning(int dx, int dy) {
    	layoutAlgorithm.moveAllEntities(dx, dy);
    }
    
    /**
     * Indicates that panning has ceased.  Resumes the layout algorithm if it was running
     * before panning started.
     * @see org.eclipse.mylar.zest.core.internal.springgraphviewer.IPanningListener#panningEnd()
     */
    public void panningEnd() {
    	if (wasRunningBeforePanning) {
    		layoutAlgorithm.resume();
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
		}
	}

	/**
	 * Pauses layout algorithm if it is running not paused.
	 */
	public void pauseLayoutAlgorithm() {
		if ((layoutAlgorithm != null) && layoutAlgorithm.isRunning()) {
			if (!layoutAlgorithm.isPaused()) {
				layoutAlgorithm.pause();
			}
		}
	}

	/**
	 * Resumes the layout algorithm if it is running and paused.
	 */
	public void resumeLayoutAlgorithm() {
		if ((layoutAlgorithm != null) && layoutAlgorithm.isRunning()) {
			if (layoutAlgorithm.isPaused()) {
				layoutAlgorithm.resume();
			}
		}
	}
	
	/**
	 * Stops the layout algorithm if it is running.
	 */
	public void stopLayoutAlgorithm() {
		if ((layoutAlgorithm != null) && layoutAlgorithm.isRunning()) {
			layoutAlgorithm.stop();
		}
	}
	
	/**
	 * Restarts the layout algorithm.  This will do nothing 
	 * if the algorithm is already running.
	 */
	public void restartLayoutAlgorithm() {
		if ((layoutAlgorithm != null) && !layoutAlgorithm.isRunning()) {
			this.removeThread(layoutThread);
			this.addThread(layoutThread);
		}
	}
	
	/**	
	 * Returns if the layout algorithm is running.
	 * @return boolean if the layout algorithm is running
	 */
	public boolean isLayoutAlgorithmRunning() {
		return ((layoutAlgorithm != null) && layoutAlgorithm.isRunning());
	}
	
	/**	
	 * Returns if the layout algorithm is paused.
	 * @return boolean if the layout algorithm is paused
	 */
	public boolean isLayoutAlgorithmPaused() {
		return ((layoutAlgorithm != null) && layoutAlgorithm.isPaused());
	}
	
	
	public void zoomIn() {
		((GraphRootEditPart)this.getRootEditPart()).getZoomManager().zoomIn();
	}
	
	public void zoomOut() {
		((GraphRootEditPart)this.getRootEditPart()).getZoomManager().zoomOut();		
	}
	
	/**
	 * Ensures that the selected node is visible (centered).
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double zoom) {
		
		Dimension dim = getCanvasSize();
		if ((zoom < 1D) && (zoom > 0D)) {
			layoutAlgorithm.setLayoutArea(10, 10, Math.max(0, (dim.width / zoom) - 10), Math.max(0, (dim.height / zoom) - 10));
		} else {
			layoutAlgorithm.setLayoutArea(10, 10, dim.width, dim.height);
		}
		
		List selection = getSelectedEditParts();
		if (selection.size() > 0) {
			Object obj = selection.get(0);
			if (obj instanceof GraphNodeEditPart) {
				//GraphNodeEditPart editPart = (GraphNodeEditPart)obj;
				//centerNodeInCanvas(editPart.getCastedModel());
			}
		}
	}
		
	class _selectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			//DebugPrint.println("Impl selection changed called: ");
			Event e = new Event();
			ArrayList selectedModelElements = new ArrayList( getSelectedEditParts().size() );
			for ( Iterator iterator = getSelectedEditParts().iterator(); iterator.hasNext(); ) {
				Object modelElement = ((EditPart) iterator.next()).getModel();
				//DebugPrint.println(modelElement);
				
				selectedModelElements.add( modelElement );
			}
			e.data = new String("test");
			
			getControl().notifyListeners(SWT.Selection, null);
		}
	}
	
}
