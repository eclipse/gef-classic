/*******************************************************************************
 * Copyright (c) 2009-2010, 2024 Mateusz Matela and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Mateusz Matela - initial API and implementation
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Item;

import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentPoint;
import org.eclipse.zest.layouts.interfaces.ConnectionLayout;
import org.eclipse.zest.layouts.interfaces.EntityLayout;
import org.eclipse.zest.layouts.interfaces.NodeLayout;
import org.eclipse.zest.layouts.interfaces.SubgraphLayout;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

class InternalNodeLayout implements NodeLayout {

	/**
	 * This listener is added to nodes' figures as a workaround for the problem of
	 * minimized nodes leaving single on the graph pixels when zoomed out
	 */
	private final static FigureListener figureListener = source -> {
		// hide figures of minimized nodes
		GraphNode node = InternalNodeLayout.figureToNode.get(source);
		if (node.getLayout().isMinimized() && source.getSize().equals(0, 0)) {
			source.setVisible(false);
		} else {
			source.setVisible(node.isVisible());
		}
	};
	private final static Map<IFigure, GraphNode> figureToNode = new HashMap<>();

	private DisplayIndependentPoint location;
	private DisplayIndependentDimension size;
	private boolean minimized = false;
	private final GraphNode node;
	private final InternalLayoutContext ownerLayoutContext;
	private DefaultSubgraph subgraph;
	private boolean isDisposed = false;

	public InternalNodeLayout(GraphNode graphNode) {
		this.node = graphNode;
		this.ownerLayoutContext = (InternalLayoutContext) ((IContainer2) node.parent).getLayoutContext();
		graphNode.nodeFigure.addFigureListener(figureListener);
		figureToNode.put(graphNode.nodeFigure, graphNode);
	}

	@Override
	public DisplayIndependentPoint getLocation() {
		if (location == null) {
			refreshLocation();
		}
		return new DisplayIndependentPoint(location);
	}

	@Override
	public DisplayIndependentDimension getSize() {
		if (size == null) {
			refreshSize();
		}
		return new DisplayIndependentDimension(size);
	}

	@Override
	public SubgraphLayout getSubgraph() {
		return subgraph;
	}

	@Override
	public boolean isMovable() {
		return true;
	}

	@Override
	public boolean isPrunable() {
		return ownerLayoutContext.isPruningEnabled();
	}

	@Override
	public boolean isPruned() {
		return subgraph != null;
	}

	@Override
	public boolean isResizable() {
		return (((IContainer2) node.parent).getItem().getStyle() & ZestStyles.NODES_NO_LAYOUT_RESIZE) == 0;
	}

	@Override
	public void prune(SubgraphLayout subgraph) {
		if (subgraph != null && !(subgraph instanceof DefaultSubgraph)) {
			throw new RuntimeException("InternalNodeLayout can be pruned only to instance of DefaultSubgraph."); //$NON-NLS-1$
		}
		ownerLayoutContext.checkChangesAllowed();
		if (subgraph == this.subgraph) {
			return;
		}
		if (this.subgraph != null) {
			SubgraphLayout subgraph2 = this.subgraph;
			this.subgraph = null;
			subgraph2.removeNodes(new NodeLayout[] { this });
		}
		if (subgraph != null) {
			this.subgraph = (DefaultSubgraph) subgraph;
			subgraph.addNodes(new NodeLayout[] { this });
		}
	}

	@Override
	public void setLocation(double x, double y) {
		if (!ownerLayoutContext.isLayoutItemFiltered(this.getNode())) {
			ownerLayoutContext.checkChangesAllowed();
			internalSetLocation(x, y);
		}
	}

	private void internalSetLocation(double x, double y) {
		if (location != null) {
			location.x = x;
			location.y = y;
		} else {
			location = new DisplayIndependentPoint(x, y);
		}
	}

	@Override
	public void setSize(double width, double height) {
		ownerLayoutContext.checkChangesAllowed();
		internalSetSize(width, height);
	}

	private void internalSetSize(double width, double height) {
		if (size != null) {
			size.width = width;
			size.height = height;
		} else {
			size = new DisplayIndependentDimension(width, height);
		}
	}

	@Override
	public void setMinimized(boolean minimized) {
		ownerLayoutContext.checkChangesAllowed();
		getSize();
		this.minimized = minimized;
	}

	@Override
	public boolean isMinimized() {
		return minimized;
	}

	@Override
	public NodeLayout[] getPredecessingNodes() {
		ConnectionLayout[] connections = getIncomingConnections();
		NodeLayout[] result = new NodeLayout[connections.length];
		for (int i = 0; i < connections.length; i++) {
			result[i] = connections[i].getSource();
			if (result[i] == this) {
				result[i] = connections[i].getTarget();
			}
		}
		return result;
	}

	@Override
	public NodeLayout[] getSuccessingNodes() {
		ConnectionLayout[] connections = getOutgoingConnections();
		NodeLayout[] result = new NodeLayout[connections.length];
		for (int i = 0; i < connections.length; i++) {
			result[i] = connections[i].getTarget();
			if (result[i] == this) {
				result[i] = connections[i].getSource();
			}
		}
		return result;
	}

	@Override
	public EntityLayout[] getSuccessingEntities() {
		if (isPruned()) {
			return new NodeLayout[0];
		}
		List<EntityLayout> result = new ArrayList<>();
		Set<SubgraphLayout> addedSubgraphs = new HashSet<>();
		NodeLayout[] successingNodes = getSuccessingNodes();
		for (NodeLayout successingNode : successingNodes) {
			if (!successingNode.isPruned()) {
				result.add(successingNode);
			} else {
				SubgraphLayout successingSubgraph = successingNode.getSubgraph();
				if (successingSubgraph.isGraphEntity() && !addedSubgraphs.contains(successingSubgraph)) {
					result.add(successingSubgraph);
					addedSubgraphs.add(successingSubgraph);
				}
			}
		}
		return result.toArray(new EntityLayout[result.size()]);
	}

	@Override
	public EntityLayout[] getPredecessingEntities() {
		if (isPruned()) {
			return new NodeLayout[0];
		}
		List<EntityLayout> result = new ArrayList<>();
		Set<SubgraphLayout> addedSubgraphs = new HashSet<>();
		NodeLayout[] predecessingNodes = getPredecessingNodes();
		for (NodeLayout predecessingNode : predecessingNodes) {
			if (!predecessingNode.isPruned()) {
				result.add(predecessingNode);
			} else {
				SubgraphLayout predecessingSubgraph = predecessingNode.getSubgraph();
				if (predecessingSubgraph.isGraphEntity() && !addedSubgraphs.contains(predecessingSubgraph)) {
					result.add(predecessingSubgraph);
					addedSubgraphs.add(predecessingSubgraph);
				}
			}
		}
		return result.toArray(new EntityLayout[result.size()]);
	}

	@Override
	public ConnectionLayout[] getIncomingConnections() {
		List<ConnectionLayout> result = new ArrayList<>();
		for (GraphConnection connection : node.getTargetConnections()) {
			if (!ownerLayoutContext.isLayoutItemFiltered(connection)) {
				result.add(connection.getLayout());
			}
		}
		for (GraphConnection connection : node.getSourceConnections()) {
			if (!connection.isDirected() && !ownerLayoutContext.isLayoutItemFiltered(connection)) {
				result.add(connection.getLayout());
			}
		}
		return result.toArray(new ConnectionLayout[result.size()]);
	}

	@Override
	public ConnectionLayout[] getOutgoingConnections() {
		List<ConnectionLayout> result = new ArrayList<>();
		for (GraphConnection connection : node.getSourceConnections()) {
			if (!ownerLayoutContext.isLayoutItemFiltered(connection)) {
				result.add(connection.getLayout());
			}
		}
		for (GraphConnection connection : node.getTargetConnections()) {
			if (!connection.isDirected() && !ownerLayoutContext.isLayoutItemFiltered(connection)) {
				result.add(connection.getLayout());
			}
		}
		return result.toArray(new ConnectionLayout[result.size()]);
	}

	@Override
	public double getPreferredAspectRatio() {
		return 0;
	}

	GraphNode getNode() {
		return node;
	}

	@Override
	public Item[] getItems() {
		return new GraphNode[] { node };
	}

	void applyLayout() {
		if (minimized) {
			node.setSize(0, 0);
			if (location != null) {
				node.setLocation(location.x, location.y);
			}
		} else {
			node.setSize(-1, -1);
			if (location != null) {
				node.setLocation(location.x, location.y);
			}
			if (size != null) {
				Dimension currentSize = node.getSize();
				if (size.width != currentSize.width || size.height != currentSize.height) {
					node.setSize(size.width, size.height);
				}
			}
		}
	}

	InternalLayoutContext getOwnerLayoutContext() {
		return ownerLayoutContext;
	}

	void refreshSize() {
		Dimension size2 = node.getSize();
		internalSetSize(size2.width, size2.height);
	}

	void refreshLocation() {
		Point location2 = node.getLocation();
		internalSetLocation(location2.x + getSize().width / 2, location2.y + size.height / 2);
	}

	@Override
	public String toString() {
		return node.toString() + "(layout)"; //$NON-NLS-1$
	}

	void dispose() {
		isDisposed = true;
		if (subgraph != null) {
			subgraph.removeDisposedNodes();
		}
		ownerLayoutContext.fireNodeRemovedEvent(node.getLayout());
		figureToNode.remove(node.nodeFigure);
	}

	boolean isDisposed() {
		return isDisposed;
	}
}