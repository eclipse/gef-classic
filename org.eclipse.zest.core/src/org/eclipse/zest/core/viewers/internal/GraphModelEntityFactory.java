/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria,
 *                      BC, Canada and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.viewers.internal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.zest.core.viewers.AbstractStructuredGraphViewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 *
 * @author Ian Bull
 */
@SuppressWarnings("removal")
public class GraphModelEntityFactory extends AbstractStylingModelFactory {

	AbstractStructuredGraphViewer viewer = null;

	public GraphModelEntityFactory(AbstractStructuredGraphViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#
	 * createGraphModel()
	 */
	@Override
	public Graph createGraphModel(Graph model) {
		doBuildGraph(model);
		return model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory
	 * #doBuildGraph(org.eclipse.zest.core.internal.graphmodel.GraphModel)
	 */
	@Override
	protected void doBuildGraph(Graph model) {
		super.doBuildGraph(model);
		Object inputElement = getViewer().getInput();
		Object entities[] = getContentProvider().getElements(inputElement);
		if (entities == null) {
			return;
		}
		for (Object data : entities) {
			IFigureProvider figureProvider = null;
			if (getLabelProvider() instanceof IFigureProvider) {
				figureProvider = (IFigureProvider) getLabelProvider();
			}
			if (!filterElement(inputElement, data)) {
				if (figureProvider != null) {
					createNode(model, data, figureProvider.getFigure(data));
				} else {
					createNode(model, data);
				}
			}
		}

		// We may have other entities (such as children of containers)
		Set<Object> keySet = ((AbstractStructuredGraphViewer) getViewer()).getNodesMap().keySet();
		entities = keySet.toArray();

		for (Object data : entities) {
			// If this element is filtered, continue to the next one.
			if (filterElement(inputElement, data)) {
				continue;
			}
			Object[] related = ((IGraphEntityContentProvider) getContentProvider()).getConnectedTo(data);

			if (related != null) {
				for (Object element : related) {
					// if the node this node is connected to is filtered,
					// don't display this edge
					if (filterElement(inputElement, element)) {
						continue;
					}
					EntityConnectionData connectionData = new EntityConnectionData(data, element);
					if (filterElement(inputElement, connectionData)) {
						continue;
					}
					createConnection(model, connectionData, data, element);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
	 * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	@Override
	public void refresh(Graph graph, Object element, boolean refreshLabels) {
		if (element == null) {
			return;
		}
		GraphNode node = viewer.getGraphModelNode(element);
		if (node == null) {
			// check to make sure that the user didn't send us an edge.
			GraphConnection conn = viewer.getGraphModelConnection(element);
			if (conn != null) {
				// refresh on the connected nodes.
				refresh(graph, conn.getSource().getData(), refreshLabels);
				refresh(graph, conn.getDestination().getData(), refreshLabels);
				return;
			}
		}
		// can only refresh on nodes in this kind of factory.
		if (node == null) {
			// do nothing
			return;
		}
		reconnect(graph, element, refreshLabels);

		if (refreshLabels) {
			update(node);
			for (GraphItem item : node.getSourceConnections()) {
				update(item);
			}
			for (GraphItem item : node.getTargetConnections()) {
				update(item);
			}
		}
	}

	/**
	 * @param graph
	 * @param element
	 * @param refreshLabels
	 */
	private void reconnect(Graph graph, Object element, boolean refreshLabels) {
		GraphNode node = viewer.getGraphModelNode(element);
		Object[] related = ((IGraphEntityContentProvider) getContentProvider()).getConnectedTo(element);
		List<? extends GraphConnection> connections = node.getSourceConnections();
		List<Object> toAdd = new LinkedList<>();
		List<Object> toDelete = new LinkedList<>();
		List<Object> toKeep = new LinkedList<>();
		Set<Object> oldExternalConnections = new HashSet<>();
		Set<Object> newExternalConnections = new HashSet<>();
		for (GraphConnection connection : connections) {
			oldExternalConnections.add(connection.getExternalConnection());
		}
		for (Object element2 : related) {
			newExternalConnections.add(new EntityConnectionData(element, element2));
		}
		for (Object next : oldExternalConnections) {
			if (!newExternalConnections.contains(next)) {
				toDelete.add(next);
			} else {
				toKeep.add(next);
			}
		}
		for (Object next : newExternalConnections) {
			if (!oldExternalConnections.contains(next)) {
				toAdd.add(next);
			}
		}
		for (Object element2 : toDelete) {
			viewer.removeGraphModelConnection(element2);
		}
		toDelete.clear();
		List<Object> newNodeList = new LinkedList<>();
		for (Object element2 : toAdd) {
			EntityConnectionData data = (EntityConnectionData) element2;
			GraphNode dest = viewer.getGraphModelNode(data.dest);
			if (dest == null) {
				newNodeList.add(data.dest);
			}
			createConnection(graph, data, data.source, data.dest);
		}
		toAdd.clear();
		if (refreshLabels) {
			for (Object element2 : toKeep) {
				styleItem(viewer.getGraphModelConnection(element2));
			}
		}
		for (Object element2 : newNodeList) {
			// refresh the new nodes so that we get a fully-up-to-date graph.
			refresh(graph, element2);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh
	 * (org.eclipse.zest.core.internal.graphmodel.GraphModel, java.lang.Object,
	 * boolean)
	 */
	@Override
	public void refresh(Graph graph, Object element) {
		refresh(graph, element, false);
	}

}
