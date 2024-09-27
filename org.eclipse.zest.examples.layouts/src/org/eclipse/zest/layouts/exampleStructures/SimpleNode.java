/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleStructures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Item;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.interfaces.NodeLayout;
import org.eclipse.zest.layouts.interfaces.SubgraphLayout;

/**
 * Rerpresents a simple node that can be used in the layout algorithms.
 *
 * @author Ian Bull
 * @author Casey Best (Version 1 by Rob Lintern)
 * @version 2
 */
public class SimpleNode implements NodeLayout {
	private static Object NODE_NORMAL_COLOR;
	private static Object NODE_SELECTED_COLOR;
	private static Object NODE_ADJACENT_COLOR;
	private static Object BORDER_NORMAL_COLOR;
	private static Object BORDER_SELECTED_COLOR;
	private static Object BORDER_ADJACENT_COLOR;

	private static final int BORDER_NORMAL_STROKE = 1;
	private static final int BORDER_STROKE_SELECTED = 2;

	protected DisplayIndependentPoint location;
	protected DisplayIndependentDimension size;
	protected Object realObject;
	private boolean ignoreInLayout = false;

	private Object colour = null;
	private Object borderColor = null;
	private int borderWidth;

	private final SimpleGraph graph;
	private boolean minimized;

	/**
	 * Constructs a new SimpleNode.
	 */
	public SimpleNode(Object realObject, SimpleGraph graph) {
		this.realObject = realObject;
		this.graph = graph;
		location = new DisplayIndependentPoint(-1, -1);
		size = new DisplayIndependentDimension(110, 110);
		this.borderWidth = BORDER_NORMAL_STROKE;
		this.colour = NODE_NORMAL_COLOR;
		this.borderColor = BORDER_NORMAL_COLOR;
	}

	public static void setNodeColors(Object nodeNormalColor, Object borderNormalColor, Object nodeSelectedColor,
			Object nodeAdjacentColor, Object borderSelectedColor, Object borderAdjacentColor) {
		NODE_NORMAL_COLOR = nodeNormalColor;
		BORDER_NORMAL_COLOR = borderNormalColor;
		NODE_SELECTED_COLOR = nodeSelectedColor;
		NODE_ADJACENT_COLOR = nodeAdjacentColor;
		BORDER_SELECTED_COLOR = borderSelectedColor;
		BORDER_ADJACENT_COLOR = borderAdjacentColor;
	}

	public List<SimpleNode> getRelatedEntities() {
		List<SimpleNode> listOfRelatedEntities = new LinkedList<>();
		for (SimpleRelationship rel : graph.getConnections()) {
			if (rel.getSource() != this) {
				listOfRelatedEntities.add(rel.getSource());
			}
			if (rel.getTarget() != this) {
				listOfRelatedEntities.add(rel.getTarget());
			}
		}
		return listOfRelatedEntities;
	}

	/**
	 * Ignores this entity in the layout
	 *
	 * @param ignore Should this entity be ignored
	 */
	public void ignoreInLayout(boolean ignore) {
		this.ignoreInLayout = ignore;
	}

	public Object getRealObject() {
		return realObject;
	}

	/**
	 * Gets the x position of this SimpleNode.
	 */
	public double getX() {
		return getLocation().x;
	}

	/**
	 * Gets the y position of this SimpleNode.
	 */
	public double getY() {
		return getLocation().y;
	}

	/**
	 * Get the size of this node
	 */
	public double getWidth() {
		return getSize().width;
	}

	/**
	 * Get the size of this node
	 */
	public double getHeight() {
		return getSize().height;
	}

	@Override
	public DisplayIndependentDimension getSize() {
		return size;
	}

	@Override
	public void setSize(double width, double height) {
		if (!ignoreInLayout) {
			this.size.width = width;
			this.size.height = height;
		}
	}

	@Override
	public DisplayIndependentPoint getLocation() {
		return location;
	}

	@Override
	public void setLocation(double x, double y) {
		if (!ignoreInLayout) {
			this.location.x = x;
			this.location.y = y;
		}
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object instanceof SimpleNode node) {
			result = realObject.equals(node.getRealObject());
		}
		return result;
	}

	@Override
	public int hashCode() {
		return realObject.hashCode();
	}

	@Override
	public String toString() {
		return realObject.toString();
	}

	public void setSelected() {
		this.colour = NODE_SELECTED_COLOR;
		this.borderColor = BORDER_SELECTED_COLOR;
		this.borderWidth = BORDER_STROKE_SELECTED;
	}

	public void setUnSelected() {
		this.colour = NODE_NORMAL_COLOR;
		this.borderColor = BORDER_NORMAL_COLOR;
		this.borderWidth = BORDER_NORMAL_STROKE;
	}

	public void setAdjacent() {
		this.colour = NODE_ADJACENT_COLOR;
		this.borderColor = BORDER_ADJACENT_COLOR;
		this.borderWidth = BORDER_STROKE_SELECTED;
	}

	public Object getColor() {
		return this.colour;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public Object getBorderColor() {
		return borderColor;
	}

	@Override
	public double getPreferredAspectRatio() {
		return 0;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	public SimpleNode[] getSuccessingEntities() {
		return getSuccessingNodes();
	}

	@Override
	public SimpleNode[] getPredecessingEntities() {
		return getPredecessingNodes();
	}

	@Override
	public Item[] getItems() {
		return null;
	}

	@Override
	public boolean isPrunable() {
		return false;
	}

	@Override
	public boolean isPruned() {
		return false;
	}

	@Override
	public SubgraphLayout getSubgraph() {
		return null;
	}

	@Override
	public void prune(SubgraphLayout subgraph) {
	}

	@Override
	public SimpleNode[] getSuccessingNodes() {
		List<SimpleNode> successors = new ArrayList<>();
		for (SimpleRelationship relationship : graph.getConnections()) {
			if (relationship.getSource() == this) {
				successors.add(relationship.getTarget());
			}
		}
		return successors.toArray(SimpleNode[]::new);
	}

	@Override
	public SimpleNode[] getPredecessingNodes() {
		List<SimpleNode> predecessors = new ArrayList<>();
		for (SimpleRelationship relationship : graph.getConnections()) {
			if (relationship.getTarget() == this) {
				predecessors.add(relationship.getSource());
			}
		}
		return predecessors.toArray(SimpleNode[]::new);
	}

	@Override
	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
	}

	@Override
	public boolean isMinimized() {
		return minimized;
	}

	@Override
	public SimpleRelationship[] getIncomingConnections() {
		List<SimpleRelationship> relationships = new ArrayList<>();
		for (SimpleRelationship relationship : graph.getConnections()) {
			if (relationship.getTarget() == this) {
				relationships.add(relationship);
			}
		}
		return relationships.toArray(SimpleRelationship[]::new);
	}

	@Override
	public SimpleRelationship[] getOutgoingConnections() {
		List<SimpleRelationship> relationships = new ArrayList<>();
		for (SimpleRelationship relationship : graph.getConnections()) {
			if (relationship.getSource() == this) {
				relationships.add(relationship);
			}
		}
		return relationships.toArray(SimpleRelationship[]::new);
	}
}
