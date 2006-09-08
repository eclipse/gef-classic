/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphEntityRelationshipContentProvider;
import org.eclipse.swt.widgets.Canvas;

/**
 * A factory for the IGraphEntityRelationshipContentProvider.
 * @author Del Myers
 *
 */
//@tag bug.154580-Content.fix
public class GraphModelEntityRelationshipFactory extends AbstractStylingModelFactory implements IGraphModelFactory {

	
	private StructuredViewer viewer;
	private boolean highlight;

	public GraphModelEntityRelationshipFactory(StructuredViewer viewer, boolean highlightAdjacent) {
		this.viewer = viewer;
		this.highlight = highlightAdjacent;
		if (!(viewer.getContentProvider() instanceof IGraphEntityRelationshipContentProvider)) {
			throw new IllegalArgumentException("Expected IGraphEntityRelationshipContentProvider");
		}
	}
	
	protected IGraphEntityRelationshipContentProvider getContentProvider() {
		return (IGraphEntityRelationshipContentProvider) viewer.getContentProvider();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#doCreateModelFromContentProvider(java.lang.Object, int, int)
	 */
	protected GraphModel doCreateModelFromContentProvider(Object input, int nodeStyle, int connectionStyle) {
		GraphModel model = createModel();
		model.setConnectionStyle(connectionStyle);
		model.setNodeStyle(nodeStyle);
		Object[] nodes = getContentProvider().getElements(input);
		createModelNodes(model, nodes);
		createModelRelationships(model);
		return model;
	}

	/**
	 * Creates all the model relationships. Assumes that all of the model nodes
	 * have been created in the graph model already. Runtime O(n^2) + O(r).
	 * @param model the model to create the relationship on.
	 */
	private void createModelRelationships(GraphModel model) {
		IGraphModelNode[] modelNodes = model.getNodesArray();
		IGraphEntityRelationshipContentProvider content = getContentProvider();
		for (int i = 0; i < modelNodes.length; i++) {
			for (int j = 0; j < modelNodes.length; j++) {
				Object[] rels = content.getRelationships(
					modelNodes[i].getExternalNode(), 
					modelNodes[j].getExternalNode()
				);
				for (int r = 0; r < rels.length; r++) {
					IGraphModelConnection conn = 
						new GraphModelConnection(
							model,
							rels[r],
							modelNodes[i],
							modelNodes[j],
							false,
							content.getWeight(rels[r])
						);
					conn.setText(getLabelProvider().getText(rels[r]));
					conn.setImage(getLabelProvider().getImage(rels[r]));
					model.addConnection(rels[r], conn);
					styleItem(conn);
				}
			}
		}
	}

	/**
	 * Creates the model nodes for the given external nodes.
	 * @param model the graph model.
	 * @param nodes the external nodes.
	 */
	private void createModelNodes(GraphModel model, Object[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			IGraphModelNode node = createNode(model, nodes[i]);
			model.addNode(nodes[i], node);
			node.setHighlightAdjacentNodes(highlight);
			styleItem(node);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#getLabelProvider()
	 */
	protected ILabelProvider getLabelProvider() {
		return (ILabelProvider)viewer.getLabelProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory#createModel()
	 */
	public GraphModel createModel() {
		return new GraphModel((Canvas)viewer.getControl());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory#createNode(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public IGraphModelNode createNode(GraphModel model, Object data) {
		ILabelProvider labelProvider = getLabelProvider();
		GraphModelNode node = new GraphModelNode(
				model, 
				labelProvider.getText(data), 
				labelProvider.getImage(data), 
				data
			);
		return node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory#createRelationship(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public IGraphModelConnection createRelationship(GraphModel model, Object data) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory#createRelationship(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public IGraphModelConnection createRelationship(GraphModel model, Object data, Object source, Object dest) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

}
