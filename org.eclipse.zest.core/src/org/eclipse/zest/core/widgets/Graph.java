/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.mylar.zest.core.widgets.internal.RevealListener;
import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;

/**
 * Holds the nodes and connections for the graph.
 * 
 * @author Chris Callendar
 * @author Ian Bull
 */
public class Graph extends FigureCanvas {

	// CLASS CONSTANTS
	public static final int ANIMATION_TIME = 500;

	// @tag CGraph.Colors : These are the colour contants for the graph, they
	// are disposed on clean-up
	public Color LIGHT_BLUE = new Color(null, 216, 228, 248);
	public Color LIGHT_BLUE_CYAN = new Color(null, 213, 243, 255);
	public Color GREY_BLUE = new Color(null, 139, 150, 171);
	public Color DARK_BLUE = new Color(null, 1, 70, 122);
	public Color LIGHT_YELLOW = new Color(null, 255, 255, 206);

	public Color HIGHLIGHT_COLOR = ColorConstants.yellow;
	public Color HIGHLIGHT_ADJACENT_COLOR = ColorConstants.orange;
	public Color DEFAULT_NODE_COLOR = LIGHT_BLUE;

	/**
	 * These are all the children of this graph. These lists contains all nodes
	 * and connections that have added themselves to this graph.
	 */
	private final List nodes;
	protected List connections;
	private List selectedItems = null;
	private List /* SelectionListener */selectionListeners = null;

	/** This maps all visible nodes to their model element. */
	private HashMap figure2ItemMap = null;

	/** Maps user nodes to internal nodes */
	private int connectionStyle;
	private int nodeStyle;
	private List constraintAdapters;
	private List revealListeners = null;

	private ScalableFreeformLayeredPane nodeLayer = null;
	private ScalableFreeformLayeredPane edgeLayer = null;
	private ScalableFreeformLayeredPane edgeFeedbackLayer = null;
	private ScalableFreeformLayeredPane nodeFeedbackLayer = null;
	LayoutAlgorithm layoutAlgorithm = null;
	private Dimension preferredSize = null;
	int style = 0;

	private ScalableFreeformLayeredPane rootlayer;

	/**
	 * Constructor for a Graph. This widget represents the root of the graph,
	 * and can contain graph items such as graph nodes and graph connections.
	 * 
	 * @param parent
	 * @param style
	 */
	public Graph(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		this.style = style;
		this.setBackground(ColorConstants.white);

		LIGHT_BLUE = new Color(Display.getDefault(), 216, 228, 248);
		LIGHT_BLUE_CYAN = new Color(Display.getDefault(), 213, 243, 255);
		GREY_BLUE = new Color(Display.getDefault(), 139, 150, 171);
		DARK_BLUE = new Color(Display.getDefault(), 1, 70, 122);
		LIGHT_YELLOW = new Color(Display.getDefault(), 255, 255, 206);

		this.setViewport(new FreeformViewport());

		this.getVerticalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Graph.this.redraw();
			}

		});
		this.getHorizontalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Graph.this.redraw();
			}
		});

		// @tag CGraph.workaround : this allows me to handle mouse events
		// outside of the canvas
		this.getLightweightSystem().setEventDispatcher(new SWTEventDispatcher() {
			public void dispatchMouseMoved(org.eclipse.swt.events.MouseEvent me) {
				if (getCurrentEvent() == null) {
					return;
				}
				super.dispatchMouseMoved(me);

				if (getMouseTarget() == null) {
					setMouseTarget(getRoot());
				}
				if ((me.stateMask & SWT.BUTTON_MASK) != 0) {
					getMouseTarget().handleMouseDragged(getCurrentEvent());
				} else {
					getMouseTarget().handleMouseMoved(getCurrentEvent());
				}
			}
		});

		this.setContents(createLayers());
		DragSupport dragSupport = new DragSupport(this);
		this.getLightweightSystem().getRootFigure().addMouseListener(dragSupport);
		this.getLightweightSystem().getRootFigure().addMouseMotionListener(dragSupport);

		this.nodes = new ArrayList();
		this.preferredSize = new Dimension(-1, -1);
		this.connectionStyle = ZestStyles.NONE;
		this.nodeStyle = ZestStyles.NONE;
		this.connections = new ArrayList();
		this.constraintAdapters = new ArrayList();
		this.selectedItems = new ArrayList();
		this.selectionListeners = new ArrayList();
		this.figure2ItemMap = new HashMap();

		revealListeners = new ArrayList(1);
		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (!revealListeners.isEmpty()) {
					// Go through the reveal list and let everyone know that the
					// view is now available. Remove the listeners so they are
					// only
					// called once!
					Iterator iterator = revealListeners.iterator();
					while (iterator.hasNext()) {
						RevealListener reveallisetner = (RevealListener) iterator.next();
						reveallisetner.revealed(Graph.this);
						iterator.remove();
					}
				}
			}
		});

	}

	/**
	 * This adds a listener to the set of listeners that will be called when a
	 * selection event occurs.
	 * 
	 * @param selectionListener
	 */
	public void addSelectionListener(SelectionListener selectionListener) {
		if (!selectionListeners.contains(selectionListener)) {
			selectionListeners.add(selectionListener);
		}
	}

	public void removeSelectionListener(SelectionListener selectionListener) {
		if (selectionListeners.contains(selectionListener)) {
			selectionListeners.remove(selectionListener);
		}
	}

	/**
	 * Gets a list of the GraphModelNode children objects under the root node in
	 * this diagram. If the root node is null then all the top level nodes are
	 * returned.
	 * 
	 * @return List of GraphModelNode objects
	 */
	public List getNodes() {
		return nodes;
	}

	/**
	 * Sets the constraint adapters on this model
	 * 
	 * @param constraintAdapters
	 */
	public void setConstraintAdapters(List /* ConstraintAdapters */constraintAdapters) {
		this.constraintAdapters = constraintAdapters;
	}

	/**
	 * Gets the root layer for this graph
	 * 
	 * @return
	 */
	public ScalableFigure getRootLayer() {
		return rootlayer;
	}

	/**
	 * Sets the default connection style.
	 * 
	 * @param connection
	 *            style the connection style to set
	 * @see org.eclipse.mylar.zest.core.widgets.ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		this.connectionStyle = connectionStyle;
	}

	/**
	 * Gets the default connection style.
	 * 
	 * @return the connection style
	 * @see org.eclipse.mylar.zest.core.widgets.ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}

	/**
	 * Sets the default node style.
	 * 
	 * @param nodeStyle
	 *            the node style to set
	 * @see org.eclipse.mylar.zest.core.widgets.ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		this.nodeStyle = nodeStyle;
	}

	/**
	 * Gets the default node style.
	 * 
	 * @return the node style
	 * @see org.eclipse.mylar.zest.core.widgets.ZestStyles
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}

	/**
	 * Gets the list of GraphModelConnection objects.
	 * 
	 * @return list of GraphModelConnection objects
	 */
	public List getConnections() {
		return this.connections;
	}

	/**
	 * Changes the selection to the list of items
	 * 
	 * @param l
	 */
	public void setSelection(GraphItem[] nodes) {
		clearSelection();
		if (nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i].getItemType() == GraphItem.NODE) {
					selectedItems.add(nodes[i]);
					((GraphNode) nodes[i]).highlight();
				}
			}
		}
	}

	public void selectAll() {
		clearSelection();
		for (int i = 0; i < nodes.size(); i++) {
			selectedItems.add(nodes.get(i));
			((GraphNode) nodes.get(i)).highlight();
		}
	}

	/**
	 * Gets the list of currently selected GraphNodes
	 * 
	 * @return Currently selected graph node
	 */
	public List getSelection() {
		return selectedItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#toString()
	 */
	public String toString() {
		return "GraphModel {" + nodes.size() + " nodes, " + connections.size() + " connections}";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem#getGraphModel()
	 */
	public Graph getGraphModel() {
		return this;
	}

	/**
	 * Dispose of the nodes and edges when the graph is disposed.
	 */
	public void dispose() {
		while (nodes.size() > 0) {
			GraphNode node = (GraphNode) nodes.get(0);
			if (node != null && !node.isDisposed()) {
				node.dispose();
			}
		}
		while (connections.size() > 0) {
			GraphConnection connection = (GraphConnection) connections.get(0);
			if (connection != null && !connection.isDisposed()) {
				connection.dispose();
			}
		}
		super.dispose();

		LIGHT_BLUE.dispose();
		LIGHT_BLUE_CYAN.dispose();
		GREY_BLUE.dispose();
		DARK_BLUE.dispose();
		LIGHT_YELLOW.dispose();
	}

	/**
	 * Runs the layout on this graph. It uses the reveal listner to run the
	 * layout only if the view is visible. Otherwise it will be deferred until
	 * after the view is available.
	 */
	public void applyLayout() {
		this.addRevealListener(new RevealListener() {
			public void revealed(Control c) {
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						applyLayoutInternal();
					}
				});
			}
		});
	}

	/**
	 * Sets the preferred size of the layout area. Size of ( -1, -1) uses the
	 * current canvas size.
	 * 
	 * @param width
	 * @param height
	 */
	public void setPreferredSize(int width, int height) {
		this.preferredSize = new Dimension(width, height);
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

	public LayoutAlgorithm getLayoutAlgorithm() {
		return this.layoutAlgorithm;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS. These are NON API
	// /////////////////////////////////////////////////////////////////////////////////

	class DragSupport implements MouseMotionListener, org.eclipse.draw2d.MouseListener {
		Graph graph = null;
		Point oldLocation = null;
		boolean mouseUp = true;

		DragSupport(Graph graph) {
			this.graph = graph;
		}

		public void mouseDragged(org.eclipse.draw2d.MouseEvent me) {
			if (mouseUp) {
				return;
			}
			Point mousePoint = new Point(me.x, me.y);
			getRootLayer().translateToRelative(mousePoint);
			getRootLayer().translateFromParent(mousePoint);
			if (selectedItems.size() > 0) {
				Iterator iterator = selectedItems.iterator();
				while (iterator.hasNext()) {
					GraphItem item = (GraphItem) iterator.next();
					if (item.getItemType() == GraphItem.NODE) {
						// @tag Zest.selection Zest.move : This is where the node movement is tracked
						GraphNode node = (GraphNode) item;
						Point delta = new Point(mousePoint.x - oldLocation.x, mousePoint.y - oldLocation.y);
						node.setLocation(node.getLocation().x + delta.x, node.getLocation().y + delta.y);
					} else {
						// There is no movement for connection
					}
				}
			}
			oldLocation = mousePoint;
		}

		public void mouseEntered(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mouseExited(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mouseHover(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mouseMoved(org.eclipse.draw2d.MouseEvent me) {
		}

		public void mouseDoubleClicked(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mousePressed(org.eclipse.draw2d.MouseEvent me) {
			mouseUp = false;
			Point mousePoint = new Point(me.x, me.y);
			getRootLayer().translateToRelative(mousePoint);

			if (me.getState() == org.eclipse.draw2d.MouseEvent.ALT) {
				double scale = getRootLayer().getScale();
				scale *= 2;
				getRootLayer().setScale(scale);
			} else if (me.getState() == (org.eclipse.draw2d.MouseEvent.ALT | org.eclipse.draw2d.MouseEvent.SHIFT)) {
				double scale = getRootLayer().getScale();
				scale /= 2;
				getRootLayer().setScale(scale);
			} else {
				boolean hasSelection = selectedItems.size() > 0;
				IFigure figureUnderMouse = graph.getContents().findFigureAt(mousePoint.x, mousePoint.y, new TreeSearch() {

					public boolean accept(IFigure figure) {
						return true;
					}

					public boolean prune(IFigure figure) {
						IFigure parent = figure.getParent();
						// @tag TODO Zest : change these to from getParent to their actual layer names
						if (parent == nodeLayer || parent == nodeFeedbackLayer || parent == nodeLayer.getParent() || parent == nodeLayer.getParent().getParent() || parent == edgeFeedbackLayer || parent == edgeLayer) {
							return false;
						}
						return true;
					}

				});
				oldLocation = mousePoint;
				getRootLayer().translateFromParent(oldLocation);
				// If the figure under the mouse is the canvas, and CTRL is not being held down, then select
				// nothing
				if (figureUnderMouse == null || figureUnderMouse == graph) {
					if (me.getState() != org.eclipse.draw2d.MouseEvent.CONTROL) {
						clearSelection();
						if (hasSelection) {
							fireWidgetSelectedEvent(null);
							hasSelection = false;
						}
					}
					return;
				}

				GraphItem itemUnderMouse = (GraphItem) figure2ItemMap.get(figureUnderMouse);
				if (itemUnderMouse == null) {
					if (me.getState() != org.eclipse.draw2d.MouseEvent.CONTROL) {
						clearSelection();
						if (hasSelection) {
							fireWidgetSelectedEvent(null);
							hasSelection = false;
						}
					}
					return;
				}
				if (selectedItems.contains(itemUnderMouse)) {
					// We have already selected this node, and CTRL is being held down, remove this selection
					// @tag Zest.selection : This de-selects when you have CTRL pressed
					if (me.getState() == org.eclipse.draw2d.MouseEvent.CONTROL) {
						selectedItems.remove(itemUnderMouse);
						(itemUnderMouse).unhighlight();
						fireWidgetSelectedEvent(itemUnderMouse);
					}
					return;
				}

				if (me.getState() != org.eclipse.draw2d.MouseEvent.CONTROL) {
					clearSelection();
				}

				if (itemUnderMouse.getItemType() == GraphItem.NODE) {
					// @tag Zest.selection : This is where the nodes are selected
					selectedItems.add(itemUnderMouse);
					((GraphNode) itemUnderMouse).highlight();
					fireWidgetSelectedEvent(itemUnderMouse);
				} else if (itemUnderMouse.getItemType() == GraphItem.CONNECTION) {
					selectedItems.add(itemUnderMouse);
					((GraphConnection) itemUnderMouse).highlight();
					fireWidgetSelectedEvent(itemUnderMouse);

				}
			}

		}

		public void mouseReleased(org.eclipse.draw2d.MouseEvent me) {
			mouseUp = true;
		}
	}

	private void clearSelection() {
		if (selectedItems.size() > 0) {
			Iterator iterator = selectedItems.iterator();
			while (iterator.hasNext()) {
				GraphItem item = (GraphItem) iterator.next();
				item.unhighlight();
				iterator.remove();
			}
		}
	}

	private void fireWidgetSelectedEvent(Item item) {
		Iterator iterator = selectionListeners.iterator();
		while (iterator.hasNext()) {
			SelectionListener selectionListener = (SelectionListener) iterator.next();
			Event swtEvent = new Event();
			swtEvent.item = item;
			swtEvent.widget = this;
			SelectionEvent event = new SelectionEvent(swtEvent);
			selectionListener.widgetSelected(event);
		}

	}

	/**
	 * Moves the edge to the highlight layer. This moves the edge above the
	 * nodes
	 * 
	 * @param connection
	 */
	void highlightEdge(GraphConnection connection) {
		IFigure figure = connection.getConnectionFigure();
		if (figure != null) {
			edgeLayer.remove(figure);
			edgeFeedbackLayer.add(figure);
		}
	}

	/**
	 * Moves the edge from the edge feedback layer back to the edge layer
	 * 
	 * @param graphConnection
	 */
	void unhighlightEdge(GraphConnection connection) {
		IFigure figure = connection.getConnectionFigure();
		if (figure != null) {
			edgeFeedbackLayer.remove(figure);
			edgeLayer.add(figure);
		}
	}

	/**
	 * Moves the node onto the node feedback layer
	 * 
	 * @param node
	 */
	void highlightNode(GraphNode node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null && nodeLayer.getChildren().contains(figure)) {
			nodeLayer.remove(figure);
			//figure.setBounds(node.getBounds());
			nodeFeedbackLayer.add(figure);
			nodeFeedbackLayer.setConstraint(figure, node.getBounds());
		}
		nodeFeedbackLayer.getUpdateManager().performUpdate();
	}

	/**
	 * Moves the node off the node feedback layer
	 * 
	 * @param node
	 */
	void unhighlightNode(GraphNode node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null && nodeFeedbackLayer.getChildren().contains(figure)) {
			nodeFeedbackLayer.remove(figure);
			nodeLayer.add(figure);
			nodeLayer.setConstraint(figure, node.getBounds());
			//figure.setBounds(node.getBounds());

		}
		nodeFeedbackLayer.getUpdateManager().performUpdate();
	}

	/**
	 * Converts the list of GraphModelConnection objects into an array and
	 * returns it.
	 * 
	 * @return GraphModelConnection[]
	 */
	GraphConnection[] getConnectionsArray() {
		GraphConnection[] connsArray = new GraphConnection[connections.size()];
		connsArray = (GraphConnection[]) connections.toArray(connsArray);
		return connsArray;
	}

	LayoutRelationship[] getConnectionsToLayout() {
		// @tag zest.bug.156528-Filters.follows : make sure not to layout
		// filtered connections, if the style says so.
		LayoutRelationship[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = this.getConnections().iterator(); i.hasNext();) {
				GraphConnection next = (GraphConnection) i.next();
				if (next.isVisible()) {
					nodeList.add(next.getLayoutRelationship());
				}
			}
			entities = (LayoutRelationship[]) nodeList.toArray(new LayoutRelationship[] {});
		} else {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = this.getConnections().iterator(); i.hasNext();) {
				GraphConnection next = (GraphConnection) i.next();
				nodeList.add(next.getLayoutRelationship());
			}
			entities = (LayoutRelationship[]) nodeList.toArray(new LayoutRelationship[] {});
		}
		return entities;
	}

	LayoutEntity[] getNodesToLayout() {
		// @tag zest.bug.156528-Filters.follows : make sure not to layout
		// filtered nodes, if the style says so.
		LayoutEntity[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = this.getNodes().iterator(); i.hasNext();) {
				GraphNode next = (GraphNode) i.next();
				if (next.isVisible()) {
					nodeList.add(next.getLayoutEntity());
				}
			}
			entities = (LayoutEntity[]) nodeList.toArray(new LayoutEntity[] {});
		} else {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = this.getNodes().iterator(); i.hasNext();) {
				GraphNode next = (GraphNode) i.next();
				nodeList.add(next.getLayoutEntity());
			}
			entities = (LayoutEntity[]) nodeList.toArray(new LayoutEntity[] {});
		}
		return entities;
	}

	void addConnection(GraphConnection connection) {
		this.getConnections().add(connection);
		IFigure figure = connection.getConnectionFigure();
		edgeLayer.add(figure);
		figure2ItemMap.put(figure, connection);
	}

	void removeConnection(GraphConnection connection) {
		setItemVisible(connection, false);
		this.getConnections().remove(connection);
	}

	void removeNode(GraphNode node) {
		setItemVisible(node, false);
		this.getNodes().remove(node);
	}

	void addNode(GraphNode node) {
		this.getNodes().add(node);
		IFigure figure = node.getNodeFigure();
		nodeLayer.add(figure);
		figure2ItemMap.put(figure, node);
	}

	void setItemVisible(GraphItem item, boolean visible) {
		if (visible) {
			if (item.getItemType() == GraphItem.NODE && (!nodeLayer.getChildren().contains(item) || !nodeFeedbackLayer.getChildren().contains(item))) {
				GraphNode graphNode = (GraphNode) item;
				IFigure figure = graphNode.getNodeFigure();
				if (graphNode.isHighlighted()) {
					nodeFeedbackLayer.add(figure);
				} else {
					nodeLayer.add(figure);
				}
				figure2ItemMap.put(figure, item);
			} else if (item.getItemType() == GraphItem.CONNECTION && (!edgeLayer.getChildren().contains(item) || !edgeFeedbackLayer.getChildren().contains(item))) {
				GraphConnection graphConnection = (GraphConnection) item;
				IFigure figure = graphConnection.getConnectionFigure();
				if (graphConnection.isHighlighted()) {
					edgeFeedbackLayer.add(figure);
				} else {
					edgeLayer.add(figure);
				}
				figure2ItemMap.put(figure, item);
			}
		} else {
			if (item.getItemType() == GraphItem.NODE) {
				GraphNode graphNode = (GraphNode) item;
				IFigure figure = graphNode.getNodeFigure();
				if (graphNode.isHighlighted()) {
					nodeFeedbackLayer.remove(figure);
				} else {
					nodeLayer.remove(figure);
				}
				figure2ItemMap.remove(figure);
			} else if (item.getItemType() == GraphItem.CONNECTION) {
				GraphConnection graphConnection = (GraphConnection) item;
				IFigure figure = graphConnection.getConnectionFigure();
				if (graphConnection.isHighlighted()) {
					edgeFeedbackLayer.remove(figure);
				} else {
					edgeLayer.remove(figure);
				}
				figure2ItemMap.remove(figure);
			}
		}
	}

	void changeFigure(IFigure oldValue, IFigure newFigure, GraphNode graphItem) {
		if (nodeLayer.getChildren().contains(oldValue)) {
			nodeLayer.remove(oldValue);
			figure2ItemMap.remove(oldValue);
		}
		figure2ItemMap.put(newFigure, graphItem);
		nodeLayer.add(newFigure);
	}

	/**
	 * Invoke all the constraint adapaters for this constraints
	 * 
	 * @param object
	 * @param constraint
	 */
	void invokeConstraintAdapters(Object object, LayoutConstraint constraint) {
		if (constraintAdapters == null) {
			return;
		}
		Iterator iterator = this.constraintAdapters.iterator();
		while (iterator.hasNext()) {
			ConstraintAdapter constraintAdapter = (ConstraintAdapter) iterator.next();
			constraintAdapter.populateConstraint(object, constraint);
		}
	}

	private void applyLayoutInternal() {

		if ((this.getNodes().size() == 0)) {
			return;
		}

		int layoutStyle = 0;

		if ((nodeStyle & ZestStyles.NODES_NO_LAYOUT_RESIZE) > 0) {
			layoutStyle = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		}

		if (layoutAlgorithm == null) {
			layoutAlgorithm = new TreeLayoutAlgorithm(layoutStyle);
		}

		layoutAlgorithm.setStyle(layoutAlgorithm.getStyle() | layoutStyle);

		// calculate the size for the layout algorithm
		Dimension d = this.getViewport().getSize();
		d.width = d.width - 10;
		d.height = d.height - 10;

		if (this.preferredSize.width >= 0) {
			d.width = preferredSize.width;
		}
		if (this.preferredSize.height >= 0) {
			d.height = preferredSize.height;
		}

		if (d.isEmpty()) {
			return;
		}
		LayoutRelationship[] connectionsToLayout = getConnectionsToLayout();
		LayoutEntity[] nodesToLayout = getNodesToLayout();

		try {
			Animation.markBegin();
			layoutAlgorithm.applyLayout(nodesToLayout, connectionsToLayout, 0, 0, d.width, d.height, false, false);
			Animation.run(ANIMATION_TIME);
			getLightweightSystem().getUpdateManager().performUpdate();

		} catch (InvalidLayoutConfiguration e) {
			e.printStackTrace();
		}

	}

	interface MyRunnable extends Runnable {
		public boolean isVisible();
	}

	/**
	 * Adds a reveal listener to the view. Note: A reveal listener will only
	 * every be called ONCE!!! even if a view comes and goes. There is no remove
	 * reveal listener. This is used to defer some events until after the view
	 * is revealed.
	 * 
	 * @param revealListener
	 */
	private void addRevealListener(final RevealListener revealListener) {

		MyRunnable myRunnable = new MyRunnable() {
			boolean isVisible;

			public boolean isVisible() {
				return this.isVisible;
			}

			public void run() {
				isVisible = Graph.this.isVisible();
			}

		};
		Display.getDefault().syncExec(myRunnable);

		if (myRunnable.isVisible()) {
			revealListener.revealed(this);
		} else {
			revealListeners.add(revealListener);
		}
	}

	private ScalableFigure createLayers() {
		rootlayer = new ScalableFreeformLayeredPane();
		rootlayer.setLayoutManager(new FreeformLayout());
		nodeLayer = new ScalableFreeformLayeredPane();
		edgeLayer = new ScalableFreeformLayeredPane();
		edgeFeedbackLayer = new ScalableFreeformLayeredPane();
		nodeFeedbackLayer = new ScalableFreeformLayeredPane();
		nodeLayer.setLayoutManager(new FreeformLayout());
		edgeLayer.setLayoutManager(new FreeformLayout());
		edgeFeedbackLayer.setLayoutManager(new FreeformLayout());
		nodeFeedbackLayer.setLayoutManager(new FreeformLayout());
		rootlayer.add(edgeLayer);
		rootlayer.add(nodeLayer);
		rootlayer.add(edgeFeedbackLayer);
		rootlayer.add(nodeFeedbackLayer);

		nodeLayer.addLayoutListener(LayoutAnimator.getDefault());
		nodeFeedbackLayer.addLayoutListener(LayoutAnimator.getDefault());
		return rootlayer;
	}

}
