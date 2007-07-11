/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylyn.zest.core.widgets.internal.ExpandGraphLabel;
import org.eclipse.mylyn.zest.core.widgets.internal.IContainer;
import org.eclipse.mylyn.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylyn.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylyn.zest.layouts.LayoutEntity;
import org.eclipse.mylyn.zest.layouts.LayoutRelationship;
import org.eclipse.mylyn.zest.layouts.LayoutStyles;
import org.eclipse.mylyn.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.swt.graphics.Image;

/**
 * A Container than can be added to a Graph. Nodes can be added to this container.
 * The container supports collapsing and expanding and has the same properties as the
 * nodes.  Containers cannot have custom figures. 
 * 
 * @author Ian Bull
 *
 */
public class GraphContainer extends GraphNode implements IContainer {

	private static final double CONTAINER_SCALE = 0.75;
	private static final int CONTAINER_HEIGHT = 150;
	private static final int MIN_WIDTH = 250;
	private static final int ANIMATION_TIME = 100;
	private static final int SUBLAYER_OFFSET = 2;

	private ExpandGraphLabel expandGraphLabel;
	private FreeformLayer container;
	private int expandedHeight = CONTAINER_HEIGHT;
	private FreeformLayer edgeLayer;
	private List childNodes = null;

	private ScrollPane scrollPane;
	private LayoutAlgorithm layoutAlgorithm;
	private boolean isExpanded = false;

	/**
	 * Creates a new GraphContainer.  A GraphContainer may contain nodes,
	 * and has many of the same properties as a graph node.
	 * @param graph The graph that the container is being added to
	 * @param style 
	 */
	public GraphContainer(Graph graph, int style) {
		this(graph, style, "");

	}

	public GraphContainer(Graph graph, int style, String text) {
		this(graph, style, text, null);

	}

	public GraphContainer(Graph graph, int style, String text, Image image) {
		super(graph, style, text, image);
		initModel(graph, text, image);
		close(false);
		childNodes = new ArrayList();
	}

	/**
	 * Custom figures cannot be set on a GraphContainer.
	 */
	public void setCustomFigure(IFigure nodeFigure) {
		throw new RuntimeException("Operation not supported:  Containers cannot have custom figures");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.widgets.GraphItem#getItemType()
	 */
	public int getItemType() {
		return GraphItem.CONTAINER;
	}

	/**
	 * Gets the figure for this container.
	 */
	public IFigure getNodeFigure() {
		return this.nodeFigure;
	}

	/**
	 * Close this node.
	 * @param animate
	 */
	public void close(boolean animate) {
		if (animate) {
			Animation.markBegin();
		}
		isExpanded = false;

		expandGraphLabel.setExpandedState(ExpandGraphLabel.CLOSED);
		Rectangle newBounds = scrollPane.getBounds().getCopy();
		newBounds.height = 0;
		//this.nodeFigure.setConstraint(scrollPane, newBounds);
		this.nodeFigure.revalidate();
		scrollPane.setSize(scrollPane.getSize().width, 0);
		setSize(expandGraphLabel.getSize().width, expandGraphLabel.getSize().height);
		List children = this.container.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IFigure child = (IFigure) iterator.next();
			GraphItem item = getGraph().getGraphItem(child);
			item.setVisible(false);
		}
		if (animate) {
			Animation.run(ANIMATION_TIME);
		}
		this.nodeFigure.getUpdateManager().performUpdate();
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Open the container.  This opens the graph container to 
	 * show the nodes within and update the twistie
	 */
	public void open(boolean animate) {
		if (animate) {
			Animation.markBegin();
		}
		isExpanded = true;

		expandGraphLabel.setExpandedState(ExpandGraphLabel.OPEN);
		Rectangle newBounds = scrollPane.getBounds().getCopy();
		newBounds.height = expandedHeight;

		//this.nodeFigure.setConstraint(scrollPane, newBounds);
		//this.nodeFigure.revalidate();
		scrollPane.setSize(expandGraphLabel.getSize().width, expandedHeight);
		setSize(expandGraphLabel.getSize().width, expandGraphLabel.getSize().height + expandedHeight - SUBLAYER_OFFSET);

		List children = this.container.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IFigure child = (IFigure) iterator.next();
			GraphItem item = getGraph().getGraphItem(child);
			item.setVisible(true);
		}

		updateFigureForModel(nodeFigure);
		//this.nodeFigure.getUpdateManager().performUpdate();

		Rectangle containerBounds = new Rectangle(this.getLocation(), new Dimension(this.getSize().width, CONTAINER_HEIGHT + this.expandGraphLabel.getSize().height));
		moveIntersectedNodes(containerBounds, this);
		if (animate) {
			Animation.run(ANIMATION_TIME);
		}
		this.nodeFigure.getUpdateManager().performUpdate();

	}

	/**
	 * Gets a list of nodes below the given node
	 * @param node
	 * @return
	 */
	private List getNodesBelow(int y, List nodes) {
		Iterator allNodes = nodes.iterator();
		LinkedList result = new LinkedList();
		while (allNodes.hasNext()) {
			GraphNode nextNode = (GraphNode) allNodes.next();
			int top = nextNode.getLocation().y;
			if (top >= y + 2) {
				result.add(nextNode);
			}
		}
		return result;
	}

	/**
	 * Checks all the nodes in the list of nodesToCheck to see if they intersect with the bounds set
	 * @param node
	 * @param nodesToCheck
	 * @return
	 */
	private List intersectingNodes(Rectangle bounds, List nodesToCheck, GraphNode node) {
		List result = new LinkedList();
		Iterator nodes = nodesToCheck.iterator();
		while (nodes.hasNext()) {
			GraphNode nodeToCheck = (GraphNode) nodes.next();
			if (node == nodeToCheck) {
				continue;
			}
			if (bounds.intersects(nodeToCheck.getBounds())) {
				result.add(nodeToCheck);
			}
		}
		return result;
	}

	/**
	 * Gets the max distance the intersecting nodes need to be shifted to make room for the expanding node
	 * @param bounds
	 * @param nodesToMove
	 * @return
	 */
	private int getMaxMovement(Rectangle bounds, List nodesToMove) {
		Iterator iterator = nodesToMove.iterator();
		int maxMovement = 0;
		while (iterator.hasNext()) {
			GraphNode node = (GraphNode) iterator.next();
			int yValue = node.getLocation().y;
			int distanceFromBottom = (bounds.y + bounds.height) - yValue;
			maxMovement = Math.max(maxMovement, distanceFromBottom);
		}
		return maxMovement + 3;
	}

	/**
	 * Shifts a collection of nodes down.
	 * @param nodesToShift
	 * @param amount
	 */
	private void shiftNodesDown(List nodesToShift, int amount) {
		Iterator iterator = nodesToShift.iterator();
		while (iterator.hasNext()) {
			GraphNode node = (GraphNode) iterator.next();
			node.setLocation(node.getLocation().x, node.getLocation().y + amount);
		}
	}

	/**
	 * This finds the highest Y Value of a set of nodes.
	 * @param nodes
	 * @return
	 */
	private int findSmallestYValue(List nodes) {
		Iterator iterator = nodes.iterator();
		int lowestNode /*highest on the screen*/= Integer.MAX_VALUE - 100; // Subtract 100 so we don't overflow
		while (iterator.hasNext()) {
			GraphNode node = (GraphNode) iterator.next();
			int y = node.getLocation().y;
			lowestNode = Math.min(lowestNode, y);
		}
		return lowestNode;
	}

	/**
	 * Clears the nodes that the container intersects as it expands
	 * @param containerBounds
	 * @param graphContainer
	 */
	private void moveIntersectedNodes(Rectangle containerBounds, GraphNode graphContainer) {

		List nodesBelowHere = getNodesBelow(this.getLocation().y, graphContainer.getGraphModel().getNodes());
		List intersectingNodes = intersectingNodes(containerBounds, nodesBelowHere, graphContainer);
		int delta = getMaxMovement(containerBounds, intersectingNodes);
		shiftNodesDown(intersectingNodes, delta);

		int lowestNode /*ihghest on the screen*/= findSmallestYValue(intersectingNodes);
		nodesBelowHere = getNodesBelow(lowestNode, nodesBelowHere);

		while (nodesBelowHere.size() > 0) {
			Iterator intersectingNodeIterator = intersectingNodes.iterator();
			List nodesMovedInLastIteration = new LinkedList();
			while (intersectingNodeIterator.hasNext()) {
				GraphNode node = (GraphNode) intersectingNodeIterator.next();
				intersectingNodes = intersectingNodes(node.getBounds(), nodesBelowHere, node);
				delta = getMaxMovement(node.getBounds(), intersectingNodes);
				shiftNodesDown(intersectingNodes, delta);
				nodesMovedInLastIteration.addAll(intersectingNodes);
			}
			lowestNode /*highest on the screen*/= findSmallestYValue(nodesMovedInLastIteration);
			nodesBelowHere = getNodesBelow(lowestNode, nodesBelowHere);
			intersectingNodes = nodesMovedInLastIteration;
		}
	}

	/**
	 * Gets the graph that this container has been added to.
	 */
	public Graph getGraph() {
		return this.graph.getGraph();
	}

	/**
	 * 
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean applyLayout) {
		this.layoutAlgorithm = algorithm;
		if (applyLayout) {
			applyLayout();
		}

	}

	public void applyLayout() {
		if ((this.getNodes().size() == 0)) {
			return;
		}

		int layoutStyle = 0;

		if (checkStyle(ZestStyles.NODES_NO_LAYOUT_RESIZE)) {
			layoutStyle = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		}

		if (layoutAlgorithm == null) {
			layoutAlgorithm = new TreeLayoutAlgorithm(layoutStyle);
		}

		layoutAlgorithm.setStyle(layoutAlgorithm.getStyle() | layoutStyle);

		// calculate the size for the layout algorithm
		Dimension d = this.container.getSize();
		d.width = d.width - 10;
		d.height = d.height - 10;
		//if (d.height <= 0) {
		d.height = (int) (CONTAINER_HEIGHT * (1 / CONTAINER_SCALE));
		//}

		if (d.isEmpty()) {
			return;
		}
		LayoutRelationship[] connectionsToLayout = getGraph().getConnectionsToLayout(getNodes());
		LayoutEntity[] nodesToLayout = getGraph().getNodesToLayout(getNodes());

		try {
			//Animation.markBegin();
			layoutAlgorithm.applyLayout(nodesToLayout, connectionsToLayout, 25, 25, d.width - 50, d.height - 50, false, false);
			//Animation.run(ANIMATION_TIME);
			//container.getUpdateManager().performUpdate();

		} catch (InvalidLayoutConfiguration e) {
			e.printStackTrace();
		}

	}

	/***************************************************************************
	 * NON API MEMBERS
	 **************************************************************************/
	protected IFigure initFigure() {
		IFigure nodeFigure = createContainerFigure();
		return nodeFigure;
	}

	private IFigure createContainerFigure() {
		GraphContainer node = this;
		IFigure containerFigure = new Figure();
		containerFigure.setOpaque(true);

		containerFigure.addLayoutListener(LayoutAnimator.getDefault());

		containerFigure.setLayoutManager(new FreeformLayout());
		expandGraphLabel = new ExpandGraphLabel(this, node.getText(), node.getImage(), false);
		expandGraphLabel.setText(getText());
		expandGraphLabel.setImage(getImage());
		int labelHeight = expandGraphLabel.getPreferredSize().height;
		int labelWidth = expandGraphLabel.getPreferredSize().width;
		if (labelWidth < MIN_WIDTH) {
			labelWidth = MIN_WIDTH;
			expandGraphLabel.setPreferredSize(labelWidth, labelHeight);
		}
		if (labelHeight < 30) {
			labelHeight = 30;
		}
		this.expandedHeight = CONTAINER_HEIGHT;
		scrollPane = new ScrollPane();
		scrollPane.addLayoutListener(LayoutAnimator.getDefault());
		FreeformViewport viewport = new FreeformViewport();
		/*
		 * This is the code that helps remove the scroll bars moving when the nodes
		 * are dragged.  
		 *
		viewport.setHorizontalRangeModel(new DefaultRangeModel() {
			public void setAll(int min, int ext, int max) {
				System.out.println("Max: " + max + " : current Max:  " + getMaximum());
				if (max < getMaximum()) {
					max = getMaximum();
				}
				super.setAll(min, ext, max);
			}

			public void setMaximum(int maximum) {
				// TODO Auto-generated method stub
				System.out.println("Max: " + maximum + " : current Max:  " + getMaximum());
				if (maximum < getMaximum()) {
					return;
				}
				super.setMaximum(maximum);
			}
		});
		*/

		scrollPane.setViewport(viewport);
		scrollPane.setScrollBarVisibility(ScrollPane.AUTOMATIC);

		ScalableFreeformLayeredPane pane = new ScalableFreeformLayeredPane();
		pane.addLayoutListener(LayoutAnimator.getDefault());
		(pane).setScale(CONTAINER_SCALE);
		container = new FreeformLayer();
		edgeLayer = new FreeformLayer();
		pane.add(edgeLayer);
		pane.add(container);

		container.setLayoutManager(new FreeformLayout());
		scrollPane.setSize(labelWidth, expandedHeight);
		scrollPane.setLocation(new Point(0, labelHeight - SUBLAYER_OFFSET));
		scrollPane.setForegroundColor(ColorConstants.gray);

		expandGraphLabel.setBackgroundColor(getBackgroundColor());
		expandGraphLabel.setForegroundColor(getForegroundColor());
		expandGraphLabel.setLocation(new Point(0, 0));

		containerFigure.add(scrollPane);
		containerFigure.add(expandGraphLabel);

		scrollPane.getViewport().setContents(pane);
		scrollPane.setBorder(new LineBorder());

		return containerFigure;
	}

	protected IFigure updateFigureForModel(IFigure currentFigure) {
		IFigure figure = currentFigure;

		expandGraphLabel.setTextT(getText());
		expandGraphLabel.setImage(getImage());

		if (highlighted == HIGHLIGHT_ON) {
			expandGraphLabel.setForegroundColor(getForegroundColor());
			expandGraphLabel.setBackgroundColor(getHighlightColor());
		} else if (highlighted == HIGHLIGHT_ADJACENT) {
			expandGraphLabel.setForegroundColor(getForegroundColor());
			expandGraphLabel.setBackgroundColor(getHighlightAdjacentColor());
		} else {
			expandGraphLabel.setForegroundColor(getForegroundColor());
			expandGraphLabel.setBackgroundColor(getBackgroundColor());
		}
		int labelHeight = expandGraphLabel.getPreferredSize().height;
		int labelWidth = expandGraphLabel.getPreferredSize().width;
		if (labelWidth < MIN_WIDTH) {
			labelWidth = MIN_WIDTH;
			expandGraphLabel.setPreferredSize(labelWidth, labelHeight);
		}
		if (labelHeight < 30) {
			labelHeight = 30;
		}

		expandGraphLabel.setSize(labelWidth, labelHeight);
		if (isExpanded) {
			setSize(expandGraphLabel.getSize().width, expandGraphLabel.getSize().height + expandedHeight - SUBLAYER_OFFSET);
		} else {
			setSize(labelWidth, labelHeight);
		}
		scrollPane.setLocation(new Point(expandGraphLabel.getLocation().x, expandGraphLabel.getLocation().y + labelHeight - SUBLAYER_OFFSET));
		//scrollPane.setLocation(new Point(0, labelHeight - SUBLAYER_OFFSET));
		//Rectangle bounds = expandGraphLabel.getBounds().getCopy();
		//Rectangle newBounds = new Rectangle(new Point(bounds.x, bounds.y + labelHeight - SUBLAYER_OFFSET), scrollPane.getSize());
		//figure.setConstraint(scrollPane, newBounds);
		/*
		size.width = labelWidth;
		if (scrollPane.getSize().height > 0) {
			size.height = labelHeight + scrollPane.getSize().height - SUBLAYER_OFFSET;
		} else {
			size.height = labelHeight;
		}
		refreshLocation();
		figure.getUpdateManager().performValidation();
		*/

		return figure;
	}

	protected void refreshLocation() {
		if (nodeFigure == null || nodeFigure.getParent() == null) {
			return; // node figure has not been created yet
		}
		GraphNode node = this;
		Point loc = node.getLocation();
		Dimension size = node.size;
		Rectangle bounds = new Rectangle(loc, size);
		nodeFigure.getParent().setConstraint(nodeFigure, bounds);
		//nodeFigure.getParent().revalidate();
		int labelHeight = expandGraphLabel.getPreferredSize().height;
		int labelWidth = expandGraphLabel.getPreferredSize().width;
		if (labelWidth < MIN_WIDTH) {
			labelWidth = MIN_WIDTH;
		}
		if (labelHeight < 30) {
			labelHeight = 30;
		}

		//expandGraphLabel.setSize(labelWidth, labelHeight);
		//Rectangle exapndedGraphLabelBounds = expandGraphLabel.getBounds().getCopy();
		//Rectangle newBounds = new Rectangle(new Point(exapndedGraphLabelBounds.x, exapndedGraphLabelBounds.y + labelHeight - SUBLAYER_OFFSET), scrollPane.getSize());
		//nodeFigure.setConstraint(scrollPane, newBounds);
		//nodeFigure.getUpdateManager().performUpdate();
	}

	void addConnectionFigure(PolylineConnection connection) {
		nodeFigure.add(connection);
	}

	void addNode(GraphNode node) {
		container.add(node.getNodeFigure());
		node.setVisible(false);
		this.childNodes.add(node);
	}

	void addNode(GraphContainer container) {
		// Containers cannot be added to other containers (yet)
	}

	private List getNodes() {
		return this.childNodes;
	}

}
