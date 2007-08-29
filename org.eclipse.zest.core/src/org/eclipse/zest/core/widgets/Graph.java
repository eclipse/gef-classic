/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylyn.zest.core.widgets.internal.RevealListener;
import org.eclipse.mylyn.zest.core.widgets.internal.ZestRootLayer;
import org.eclipse.mylyn.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylyn.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.LayoutEntity;
import org.eclipse.mylyn.zest.layouts.LayoutRelationship;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.constraints.LayoutConstraint;
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
public class Graph extends FigureCanvas implements IContainer {

	// CLASS CONSTANTS
	public static final int ANIMATION_TIME = 500;
	public static final int FISHEYE_ANIMATION_TIME = 100;

	// @tag CGraph.Colors : These are the colour constants for the graph, they
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
	IFigure fisheyedFigure = null;
	private List /* SelectionListener */selectionListeners = null;

	/** This maps all visible nodes to their model element. */
	private HashMap figure2ItemMap = null;

	/** Maps user nodes to internal nodes */
	private int connectionStyle;
	private int nodeStyle;
	private List constraintAdapters;
	private List revealListeners = null;

	private ScalableFreeformLayeredPane fishEyeLayer = null;
	LayoutAlgorithm layoutAlgorithm = null;
	private Dimension preferredSize = null;
	int style = 0;

	private ScalableFreeformLayeredPane rootlayer;
	private ZestRootLayer zestRootLayer;

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
				super.dispatchMouseMoved(me);

				// If the current event is null, return
				if (getCurrentEvent() == null) {
					return;
				}

				if (getMouseTarget() == null) {
					setMouseTarget(getRoot());
				}
				if ((me.stateMask & SWT.BUTTON_MASK) != 0) {
					// Sometimes getCurrentEvent() returns null
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
				if (nodes[i] != null && nodes[i].getItemType() == GraphItem.NODE) {
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

	/**
	 * Finds a figure at the location X, Y in the graph
	 * 
	 * This point should be translated to relative before calling findFigureAt
	 */
	public IFigure getFigureAt(int x, int y) {
		IFigure figureUnderMouse = this.getContents().findFigureAt(x, y, new TreeSearch() {

			public boolean accept(IFigure figure) {
				return true;
			}

			public boolean prune(IFigure figure) {
				IFigure parent = figure.getParent();
				// @tag TODO Zest : change these to from getParent to their actual layer names

				if (parent == fishEyeLayer) {
					// If it node is on the fish eye layer, don't worry about it.
					return true;
				}
				if (parent == zestRootLayer || parent == zestRootLayer.getParent() || parent == zestRootLayer.getParent().getParent()) {
					return false;
				}
				GraphItem item = (GraphItem) figure2ItemMap.get(figure);
				if (item != null && item.getItemType() == GraphItem.CONTAINER) {
					return false;
				} else if (figure instanceof FreeformLayer || parent instanceof FreeformLayer || figure instanceof ScrollPane || parent instanceof ScrollPane || parent instanceof ScalableFreeformLayeredPane || figure instanceof ScalableFreeformLayeredPane) {
					return false;
				}
				return true;
			}

		});
		return figureUnderMouse;

	}

	// /////////////////////////////////////////////////////////////////////////////////
	// PRIVATE METHODS. These are NON API
	// /////////////////////////////////////////////////////////////////////////////////
	class DragSupport implements MouseMotionListener, org.eclipse.draw2d.MouseListener {
		/**
		 * 
		 */
		Graph graph = null;
		Point lastLocation = null;
		GraphItem fisheyedItem = null;
		boolean isDragging = false;

		DragSupport(Graph graph) {
			this.graph = graph;
		}

		public void mouseDragged(org.eclipse.draw2d.MouseEvent me) {
			if (!isDragging) {
				return;
			}
			Point mousePoint = new Point(me.x, me.y);
			Point tempPoint = mousePoint.getCopy();
			if (selectedItems.size() > 0) {
				Iterator iterator = selectedItems.iterator();
				while (iterator.hasNext()) {
					GraphItem item = (GraphItem) iterator.next();
					if ((item.getItemType() == GraphItem.NODE) || (item.getItemType() == GraphItem.CONTAINER)) {
						// @tag Zest.selection Zest.move : This is where the node movement is tracked
						Point pointCopy = mousePoint.getCopy();

						Point tempLastLocation = lastLocation.getCopy();
						item.getFigure().getParent().translateToRelative(tempLastLocation);
						item.getFigure().getParent().translateFromParent(tempLastLocation);

						item.getFigure().getParent().translateToRelative(pointCopy);
						item.getFigure().getParent().translateFromParent(pointCopy);
						Point delta = new Point(pointCopy.x - tempLastLocation.x, pointCopy.y - tempLastLocation.y);
						if (item.getItemType() == GraphItem.NODE || item.getItemType() == GraphItem.CONTAINER) {
							GraphNode node = (GraphNode) item;
							node.setLocation(node.getLocation().x + delta.x, node.getLocation().y + delta.y);

						}
						/*
						else if (item.getItemType() == GraphItem.CONTAINER) {
							GraphContainer container = (GraphContainer) item;
							container.setLocation(container.getLocation().x + delta.x, container.getLocation().y + delta.y);
						}
						*/
					} else {
						// There is no movement for connection
					}
				}
				if (fisheyedFigure != null) {
					Point pointCopy = mousePoint.getCopy();

					Point tempLastLocation = lastLocation.getCopy();
					fisheyedFigure.translateToRelative(tempLastLocation);
					fisheyedFigure.translateFromParent(tempLastLocation);

					fisheyedFigure.translateToRelative(pointCopy);
					fisheyedFigure.translateFromParent(pointCopy);
					Point delta = new Point(pointCopy.x - tempLastLocation.x, pointCopy.y - tempLastLocation.y);
					Point point = new Point(fisheyedFigure.getBounds().x + delta.x, fisheyedFigure.getBounds().y + delta.y);
					fishEyeLayer.setConstraint(fisheyedFigure, new Rectangle(point, fisheyedFigure.getSize()));
					fishEyeLayer.getUpdateManager().performUpdate();
					//fisheyedFigure.setBounds(new Rectangle(point2, fisheyedFigure.getSize()));
					//fisheyedFigure.setLocation(new Point(fisheyedFigure.getBounds().x + delta.x, fisheyedFigure.getBounds().y + delta.y));
				}
			}
			lastLocation = tempPoint;
			//oldLocation = mousePoint;
		}

		public void mouseEntered(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mouseExited(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mouseHover(org.eclipse.draw2d.MouseEvent me) {

		}

		/**
		 * This tracks whenever a mouse moves. The only thing we care about is
		 * fisheye(ing) nodes.  This means whenever the mouse moves we check if
		 * we need to fisheye on a node or not.
		 */
		public void mouseMoved(org.eclipse.draw2d.MouseEvent me) {
			Point mousePoint = new Point(me.x, me.y);
			getRootLayer().translateToRelative(mousePoint);
			IFigure figureUnderMouse = getFigureAt(mousePoint.x, mousePoint.y);

			if (figureUnderMouse != null) {
				// There is a figure under this mouse
				GraphItem itemUnderMouse = (GraphItem) figure2ItemMap.get(figureUnderMouse);
				if (itemUnderMouse == fisheyedItem) {

				} else if (itemUnderMouse != null && itemUnderMouse.getItemType() == GraphItem.NODE) {
					fisheyedItem = itemUnderMouse;
					fisheyedFigure = ((GraphNode) itemUnderMouse).fishEye(true, true);
					if (fisheyedFigure == null) {
						// If there is no fisheye figure (this means that the node does not support a fish eye)
						// then remove the fisheyed item
						fisheyedItem = null;
					}
				} else if (fisheyedItem != null) {
					((GraphNode) fisheyedItem).fishEye(false, true);
					fisheyedItem = null;
					fisheyedFigure = null;
				}
			} else {
				if (fisheyedItem != null) {
					((GraphNode) fisheyedItem).fishEye(false, true);
					fisheyedItem = null;
					fisheyedFigure = null;
				}
			}
		}

		public void mouseDoubleClicked(org.eclipse.draw2d.MouseEvent me) {

		}

		public void mousePressed(org.eclipse.draw2d.MouseEvent me) {
			isDragging = true;
			Point mousePoint = new Point(me.x, me.y);
			lastLocation = mousePoint.getCopy();

			getRootLayer().translateToRelative(mousePoint);

			if (me.getState() == org.eclipse.draw2d.MouseEvent.ALT) {
				double scale = getRootLayer().getScale();
				scale *= 1.05;
				getRootLayer().setScale(scale);
				Point newMousePoint = mousePoint.getCopy().scale(1.05);
				Point delta = new Point(newMousePoint.x - mousePoint.x, newMousePoint.y - mousePoint.y);
				Point newViewLocation = getViewport().getViewLocation().getCopy().translate(delta);
				getViewport().setViewLocation(newViewLocation);
				lastLocation.scale(scale);

				clearSelection();
				return;
			} else if (me.getState() == (org.eclipse.draw2d.MouseEvent.ALT | org.eclipse.draw2d.MouseEvent.SHIFT)) {
				double scale = getRootLayer().getScale();
				scale /= 1.05;
				getRootLayer().setScale(scale);

				Point newMousePoint = mousePoint.getCopy().scale(1 / 1.05);
				Point delta = new Point(newMousePoint.x - mousePoint.x, newMousePoint.y - mousePoint.y);
				Point newViewLocation = getViewport().getViewLocation().getCopy().translate(delta);
				getViewport().setViewLocation(newViewLocation);
				clearSelection();
				return;
			} else {
				boolean hasSelection = selectedItems.size() > 0;
				IFigure figureUnderMouse = getFigureAt(mousePoint.x, mousePoint.y);
				getRootLayer().translateFromParent(mousePoint);

				if (figureUnderMouse != null) {
					figureUnderMouse.getParent().translateFromParent(mousePoint);
				}
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
					// @tag Zest.selection : This deselects when you have CTRL pressed
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

				} else if (itemUnderMouse.getItemType() == GraphItem.CONTAINER) {
					selectedItems.add(itemUnderMouse);
					((GraphContainer) itemUnderMouse).highlight();
					fireWidgetSelectedEvent(itemUnderMouse);
				}
			}

		}

		public void mouseReleased(org.eclipse.draw2d.MouseEvent me) {
			isDragging = false;

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
			zestRootLayer.highlightConnection(figure);
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
			zestRootLayer.unHighlightConnection(figure);
		}
	}

	/**
	 * Moves the node onto the node feedback layer
	 * 
	 * @param node
	 */
	void highlightNode(GraphNode node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null) {
			zestRootLayer.highlightNode(figure);
		}
	}

	/**
	 * Moves the node onto the node feedback layer
	 * 
	 * @param node
	 */
	void highlightNode(GraphContainer node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null) {
			zestRootLayer.highlightNode(figure);
		}
	}

	/**
	 * Moves the node off the node feedback layer
	 * 
	 * @param node
	 */
	void unhighlightNode(GraphContainer node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null) {
			zestRootLayer.unHighlightNode(figure);
		}
	}

	/**
	 * Moves the node off the node feedback layer
	 * 
	 * @param node
	 */
	void unhighlightNode(GraphNode node) {
		IFigure figure = node.getNodeFigure();
		if (figure != null) {
			zestRootLayer.unHighlightNode(figure);
		}
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

	LayoutRelationship[] getConnectionsToLayout(List nodesToLayout) {
		// @tag zest.bug.156528-Filters.follows : make sure not to layout
		// filtered connections, if the style says so.
		LayoutRelationship[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList connectionList = new LinkedList();
			for (Iterator i = this.getConnections().iterator(); i.hasNext();) {
				GraphConnection next = (GraphConnection) i.next();
				if (next.isVisible() && nodesToLayout.contains(next.getSource()) && nodesToLayout.contains(next.getDestination())) {
					connectionList.add(next.getLayoutRelationship());
				}
			}
			entities = (LayoutRelationship[]) connectionList.toArray(new LayoutRelationship[] {});
		} else {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = this.getConnections().iterator(); i.hasNext();) {
				GraphConnection next = (GraphConnection) i.next();
				if (nodesToLayout.contains(next.getSource()) && nodesToLayout.contains(next.getDestination())) {
					nodeList.add(next.getLayoutRelationship());
				}
			}
			entities = (LayoutRelationship[]) nodeList.toArray(new LayoutRelationship[] {});
		}
		return entities;
	}

	LayoutEntity[] getNodesToLayout(List nodes) {
		// @tag zest.bug.156528-Filters.follows : make sure not to layout
		// filtered nodes, if the style says so.
		LayoutEntity[] entities;
		if (ZestStyles.checkStyle(style, ZestStyles.IGNORE_INVISIBLE_LAYOUT)) {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = nodes.iterator(); i.hasNext();) {
				GraphNode next = (GraphNode) i.next();
				if (next.isVisible()) {
					nodeList.add(next.getLayoutEntity());
				}
			}
			entities = (LayoutEntity[]) nodeList.toArray(new LayoutEntity[] {});
		} else {
			LinkedList nodeList = new LinkedList();
			for (Iterator i = nodes.iterator(); i.hasNext();) {
				GraphNode next = (GraphNode) i.next();
				nodeList.add(next.getLayoutEntity());
			}
			entities = (LayoutEntity[]) nodeList.toArray(new LayoutEntity[] {});
		}
		return entities;
	}

	void addConnection(GraphConnection connection, boolean addToEdgeLayer) {
		this.getConnections().add(connection);
		if (addToEdgeLayer) {
			zestRootLayer.addConnection(connection.getFigure());
		}
	}

	void removeConnection(GraphConnection connection) {
		IFigure figure = connection.getConnectionFigure();
		zestRootLayer.removeConnection(figure);
		this.getConnections().remove(connection);
		figure2ItemMap.remove(figure);
	}

	void removeNode(GraphNode node) {
		IFigure figure = node.getNodeFigure();
		zestRootLayer.removeNode(figure);
		this.getNodes().remove(node);
		figure2ItemMap.remove(figure);
	}

	void addNode(GraphNode node) {
		this.getNodes().add(node);
		IFigure figure = node.getFigure();
		zestRootLayer.addNode(figure);
	}

	void addNode(GraphContainer graphContainer) {
		this.getNodes().add(graphContainer);
		IFigure figure = graphContainer.getFigure();
		zestRootLayer.addNode(figure);
	}

	void registerItem(GraphItem item) {
		if (item.getItemType() == GraphItem.NODE) {
			IFigure figure = item.getFigure();
			figure2ItemMap.put(figure, item);
		} else if (item.getItemType() == GraphItem.CONNECTION) {
			IFigure figure = item.getFigure();
			figure2ItemMap.put(figure, item);
		} else if (item.getItemType() == GraphItem.CONTAINER) {
			IFigure figure = item.getFigure();
			figure2ItemMap.put(figure, item);
		} else {
			throw new RuntimeException("Unknown item type: " + item.getItemType());
		}
	}

	/*


	/**
	 * Changes the figure for a particular node
	 */
	void changeNodeFigure(IFigure oldValue, IFigure newFigure, GraphNode graphItem) {
		if (zestRootLayer.getChildren().contains(oldValue)) {
			zestRootLayer.remove(oldValue);
			figure2ItemMap.remove(oldValue);
		}
		figure2ItemMap.put(newFigure, graphItem);
		zestRootLayer.add(newFigure);
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
		LayoutRelationship[] connectionsToLayout = getConnectionsToLayout(nodes);
		LayoutEntity[] nodesToLayout = getNodesToLayout(getNodes());

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
		zestRootLayer = new ZestRootLayer();

		zestRootLayer.setLayoutManager(new FreeformLayout());

		fishEyeLayer = new ScalableFreeformLayeredPane();
		fishEyeLayer.setLayoutManager(new FreeformLayout());

		rootlayer.add(zestRootLayer);
		rootlayer.add(fishEyeLayer);

		zestRootLayer.addLayoutListener(LayoutAnimator.getDefault());
		fishEyeLayer.addLayoutListener(LayoutAnimator.getDefault());
		return rootlayer;
	}

	/**
	 * This removes the fisheye from the graph. It uses an animation to make the fisheye
	 * shrink, and then it finally clears the fisheye layer.  This assumes that there
	 * is ever only 1 node on the fisheye layer at any time.  
	 * 
	 * @param fishEyeFigure The fisheye figure
	 * @param regularFigure The regular figure (i.e. the non fisheye version)
	 */
	void removeFishEye(final IFigure fishEyeFigure, final IFigure regularFigure, boolean animate) {

		if (!fishEyeLayer.getChildren().contains(fishEyeFigure)) {
			return;
		}
		if (animate) {
			Animation.markBegin();
		}

		Rectangle bounds = regularFigure.getBounds().getCopy();
		regularFigure.translateToAbsolute(bounds);

		double scale = rootlayer.getScale();
		fishEyeLayer.setScale(1 / scale);
		fishEyeLayer.translateToRelative(bounds);
		fishEyeLayer.translateFromParent(bounds);

		fishEyeLayer.setConstraint(fishEyeFigure, bounds);

		if (animate) {
			Animation.run(FISHEYE_ANIMATION_TIME * 2);
		}
		this.getRootLayer().getUpdateManager().performUpdate();
		fishEyeLayer.removeAll();
		this.fisheyedFigure = null;

	}

	/**
	 * Replaces the old fisheye figure with a new one.
	 * @param oldFigure
	 * @param newFigure
	 */
	boolean replaceFishFigure(IFigure oldFigure, IFigure newFigure) {
		if (this.fishEyeLayer.getChildren().contains(oldFigure)) {
			Rectangle bounds = oldFigure.getBounds();
			newFigure.setBounds(bounds);
			//this.fishEyeLayer.getChildren().remove(oldFigure);
			this.fishEyeLayer.remove(oldFigure);
			this.fishEyeLayer.add(newFigure);
			//this.fishEyeLayer.getChildren().add(newFigure);
			//this.fishEyeLayer.invalidate();
			//this.fishEyeLayer.repaint();
			this.fisheyedFigure = newFigure;
			return true;
		}
		return false;
	}

	/**
	 * Add a fisheye version of the node.  This works by animating the change from the original node
	 * to the fisheyed one, and then placing the fisheye node on the fisheye layer.
	 * @param startFigure The original node
	 * @param endFigure The fisheye figure
	 * @param newBounds The final size of the fisheyed figure
	 */
	void fishEye(IFigure startFigure, IFigure endFigure, Rectangle newBounds, boolean animate) {

		fishEyeLayer.removeAll();
		fisheyedFigure = null;
		if (animate) {
			Animation.markBegin();
		}

		double scale = rootlayer.getScale();
		fishEyeLayer.setScale(1 / scale);

		fishEyeLayer.translateToRelative(newBounds);
		fishEyeLayer.translateFromParent(newBounds);

		Rectangle bounds = startFigure.getBounds().getCopy();
		startFigure.translateToAbsolute(bounds);
		//startFigure.translateToRelative(bounds);
		fishEyeLayer.translateToRelative(bounds);
		fishEyeLayer.translateFromParent(bounds);

		endFigure.setLocation(bounds.getLocation());
		endFigure.setSize(bounds.getSize());
		fishEyeLayer.add(endFigure);
		fishEyeLayer.setConstraint(endFigure, newBounds);

		if (animate) {
			Animation.run(FISHEYE_ANIMATION_TIME);
		}
		this.getRootLayer().getUpdateManager().performUpdate();
	}

	public Graph getGraph() {
		// @tag refactor : Is this method really needed
		return this.getGraphModel();
	}

	public int getItemType() {
		return GraphItem.GRAPH;
	}

	GraphItem getGraphItem(IFigure figure) {
		return (GraphItem) figure2ItemMap.get(figure);
	}

}
