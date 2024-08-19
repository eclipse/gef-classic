/*******************************************************************************
 * Copyright 2005-2006, 2024 CHISEL Group, University of Victoria, Victoria,
 *                           BC, Canada and others.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.zest.core.viewers.AbstractStructuredGraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityRelationshipContentProvider;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;

/**
 * A factory for the IGraphEntityRelationshipContentProvider.
 *
 * @author Del Myers
 */
// @tag bug.154580-Content.fix
// @tag bug.160367-Refreshing.fix : updated to use new
// AbstractStylingModelFactory
public class GraphModelEntityRelationshipFactory extends AbstractStylingModelFactory {

	public GraphModelEntityRelationshipFactory(AbstractStructuredGraphViewer viewer) {
		super(viewer);
		if (!(viewer.getContentProvider() instanceof IGraphEntityRelationshipContentProvider)) {
			throw new IllegalArgumentException("Expected IGraphEntityRelationshipContentProvider");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.zest.core.internal.graphmodel.AbstractStylingModelFactory
	 * #createGraphModel()
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
		Object[] nodes = getContentProvider().getElements(getViewer().getInput());
		nodes = filter(getViewer().getInput(), nodes);
		createModelNodes(model, nodes);
		createModelRelationships(model);
	}

	/**
	 * Creates all the model relationships. Assumes that all of the model nodes have
	 * been created in the graph model already. Runtime O(n^2) + O(r).
	 *
	 * @param model the model to create the relationship on.
	 */
	private void createModelRelationships(Graph model) {
		GraphNode[] modelNodes = getNodesArray(model);
		List<GraphNode> listOfNodes = new ArrayList<>();
		Collections.addAll(listOfNodes, modelNodes);

		for (int i = 0; i < listOfNodes.size(); i++) {
			GraphNode node = listOfNodes.get(i);
			if (node instanceof GraphContainer container) {
				List<GraphNode> childNodes = container.getNodes();
				listOfNodes.addAll(childNodes);
			}
		}
		modelNodes = listOfNodes.toArray(new GraphNode[listOfNodes.size()]);

		IGraphEntityRelationshipContentProvider content = getCastedContent();
		for (GraphNode modelNode : modelNodes) {
			for (GraphNode modelNode2 : modelNodes) {
				Object[] rels = content.getRelationships(modelNode.getData(), modelNode2.getData());
				if (rels != null) {
					rels = filter(getViewer().getInput(), rels);
					for (Object rel : rels) {
						createConnection(model, rel, modelNode.getData(), modelNode2.getData());
					}
				}
			}
		}
	}

	/**
	 * Creates the model nodes for the given external nodes.
	 *
	 * @param model the graph model.
	 * @param nodes the external nodes.
	 */
	private void createModelNodes(Graph model, Object[] nodes) {
		for (Object node : nodes) {
			createNode(model, node);
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
	public void refresh(Graph graph, Object element) {
		refresh(graph, element, false);
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
	public void refresh(Graph graph, Object element, boolean updateLabels) {
		// with this kind of graph, it is just as easy and cost-effective to
		// rebuild the whole thing.
		refreshGraph(graph);
	}

	private IGraphEntityRelationshipContentProvider getCastedContent() {
		return (IGraphEntityRelationshipContentProvider) getContentProvider();
	}

}
