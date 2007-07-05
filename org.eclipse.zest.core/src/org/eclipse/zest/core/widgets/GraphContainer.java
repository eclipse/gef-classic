/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets;

import java.util.Iterator;
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

	private static final int ANIMATION_TIME = 1000;
	private static final int SUBLAYER_OFFSET = 3;

	private ExpandGraphLabel expandGraphLabel;
	private FreeformLayer container;
	private int expandedHeight = 150;
	private FreeformLayer edgeLayer;

	private ScrollPane scrollPane;

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
		expandGraphLabel.setExpandedState(ExpandGraphLabel.CLOSED);
		Rectangle newBounds = scrollPane.getBounds().getCopy();
		newBounds.height = 0;
		this.nodeFigure.setConstraint(scrollPane, newBounds);
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
		expandGraphLabel.setExpandedState(ExpandGraphLabel.OPEN);
		Rectangle newBounds = scrollPane.getBounds().getCopy();
		newBounds.height = expandedHeight;

		this.nodeFigure.setConstraint(scrollPane, newBounds);
		this.nodeFigure.revalidate();
		scrollPane.setSize(scrollPane.getSize().width, expandedHeight);
		setSize(expandGraphLabel.getSize().width, expandGraphLabel.getSize().height + expandedHeight - SUBLAYER_OFFSET);

		List children = this.container.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			IFigure child = (IFigure) iterator.next();
			GraphItem item = getGraph().getGraphItem(child);
			item.setVisible(true);
		}
		if (animate) {
			Animation.run(ANIMATION_TIME);
		}
		this.nodeFigure.getUpdateManager().performUpdate();
		updateFigureForModel(nodeFigure);

	}

	/**
	 * Gets the graph that this container has been added to.
	 */
	public Graph getGraph() {
		return this.graph.getGraph();
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
		containerFigure.addLayoutListener(LayoutAnimator.getDefault());

		containerFigure.setLayoutManager(new FreeformLayout());
		expandGraphLabel = new ExpandGraphLabel(this, node.getText(), node.getImage(), false);
		expandGraphLabel.setText(getText());
		expandGraphLabel.setImage(getImage());
		int labelHeight = expandGraphLabel.getPreferredSize().height;
		int labelWidth = expandGraphLabel.getPreferredSize().width;
		if (labelWidth < 150) {
			labelWidth = 150;
			expandGraphLabel.setPreferredSize(labelWidth, labelHeight);
			expandGraphLabel.setSize(labelWidth + 2 * (2 + 2 + 1), labelHeight);
		}

		this.expandedHeight = 150;
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
		(pane).setScale(0.75);
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

		containerFigure.add(scrollPane);
		containerFigure.add(expandGraphLabel);
		scrollPane.getViewport().setContents(pane);
		scrollPane.setBorder(new LineBorder());

		containerFigure.setSize(labelWidth + 5 + 2 + 2 + 1, labelHeight + 150 - 5 + 2);
		setSize(labelWidth + 5 + 2 + 2 + 1, labelHeight + 150 - 5 + 2);
		return containerFigure;
	}

	protected IFigure updateFigureForModel(IFigure currentFigure) {
		IFigure figure = currentFigure;

		expandGraphLabel.setText(getText());
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
		if (labelWidth < 150) {
			labelWidth = 150;
		}
		if (labelHeight < 30) {
			labelHeight = 30;
		}
		expandGraphLabel.setSize(labelWidth, labelHeight);
		Rectangle bounds = expandGraphLabel.getBounds().getCopy();
		Rectangle newBounds = new Rectangle(new Point(bounds.x, bounds.y + labelHeight - SUBLAYER_OFFSET), scrollPane.getSize());
		figure.setConstraint(scrollPane, newBounds);

		size.width = labelWidth;
		if (scrollPane.getSize().height > 0) {
			size.height = labelHeight + scrollPane.getSize().height - SUBLAYER_OFFSET;
		} else {
			size.height = labelHeight;
		}
		refreshLocation();
		figure.getUpdateManager().performValidation();

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
		nodeFigure.getParent().revalidate();
		int labelHeight = expandGraphLabel.getPreferredSize().height;
		int labelWidth = expandGraphLabel.getPreferredSize().width;
		if (labelWidth < 150) {
			labelWidth = 150;
		}
		if (labelHeight < 30) {
			labelHeight = 30;
		}
		expandGraphLabel.setSize(labelWidth, labelHeight);
		Rectangle exapndedGraphLabelBounds = expandGraphLabel.getBounds().getCopy();
		Rectangle newBounds = new Rectangle(new Point(exapndedGraphLabelBounds.x, exapndedGraphLabelBounds.y + labelHeight - SUBLAYER_OFFSET), scrollPane.getSize());
		nodeFigure.setConstraint(scrollPane, newBounds);
		nodeFigure.getUpdateManager().performUpdate();
	}

	void addConnectionFigure(PolylineConnection connection) {
		nodeFigure.add(connection);
	}

	void addNode(GraphNode node) {
		container.add(node.getNodeFigure());
		node.setVisible(false);
	}

	void addNode(GraphContainer container) {
		// Containers cannot be added to other containers (yet)

	}
}
