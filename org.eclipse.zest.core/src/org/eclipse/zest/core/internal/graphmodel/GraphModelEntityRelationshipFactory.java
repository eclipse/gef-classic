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

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphEntityRelationshipContentProvider;
import org.eclipse.swt.widgets.Canvas;

/**
 * A factory for the IGraphEntityRelationshipContentProvider.
 * @author Del Myers
 *
 */
//@tag bug.154580-Content.fix
//@tag bug.160367-Refreshing.fix : updated to use new AbstractStylingModelFactory
public class GraphModelEntityRelationshipFactory extends AbstractStylingModelFactory {

	public GraphModelEntityRelationshipFactory(StructuredViewer viewer) {
		super(viewer);
		if (!(viewer.getContentProvider() instanceof IGraphEntityRelationshipContentProvider)) {
			throw new IllegalArgumentException("Expected IGraphEntityRelationshipContentProvider");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#createGraphModel()
	 */
	public GraphModel createGraphModel() {
		GraphModel model = new GraphModel((Canvas) getViewer().getControl());
		doBuildGraph(model);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#doBuildGraph(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel)
	 */
	protected void doBuildGraph(GraphModel model) {
		super.doBuildGraph(model);
		Object[] nodes = getContentProvider().getElements(getViewer().getInput());
		nodes = filter(getViewer().getInput(), nodes);
		createModelNodes(model, nodes);
		createModelRelationships(model);
	}

	/**
	 * Creates all the model relationships. Assumes that all of the model nodes
	 * have been created in the graph model already. Runtime O(n^2) + O(r).
	 * @param model the model to create the relationship on.
	 */
	private void createModelRelationships(GraphModel model) {
		IGraphModelNode[] modelNodes = model.getNodesArray();
		IGraphEntityRelationshipContentProvider content = getCastedContent();
		for (int i = 0; i < modelNodes.length; i++) {
			for (int j = 0; j < modelNodes.length; j++) {
				Object[] rels = content.getRelationships(
					modelNodes[i].getExternalNode(), 
					modelNodes[j].getExternalNode()
				);
				if (rels != null) {
					rels = filter(getViewer().getInput(), rels);
					for (int r = 0; r < rels.length; r++) {
						createConnection(model, rels[r], modelNodes[i].getExternalNode(), modelNodes[j].getExternalNode());
					}
				}
			}
		}
	}

	/**
	 * Creates the model nodes for the given external nodes.
	 * 
	 * @param model
	 *            the graph model.
	 * @param nodes
	 *            the external nodes.
	 */
	private void createModelNodes(GraphModel model, Object[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			createNode(model, nodes[i]);
		}
	}

	

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public void refresh(GraphModel graph, Object element) {
		refresh(graph, element, false);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, boolean)
	 */
	public void refresh(GraphModel graph, Object element, boolean updateLabels) {
		//with this kind of graph, it is just as easy and cost-effective to
		//rebuild the whole thing.
		refreshGraph(graph);
	}
	
	private IGraphEntityRelationshipContentProvider getCastedContent() {
		return (IGraphEntityRelationshipContentProvider) getContentProvider();
	}




}
