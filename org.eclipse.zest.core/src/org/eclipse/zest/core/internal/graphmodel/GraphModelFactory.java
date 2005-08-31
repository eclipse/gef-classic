/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.util.Iterator;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.DebugPrint;
import org.eclipse.mylar.zest.core.internal.viewers.Graph;
import org.eclipse.mylar.zest.core.viewers.IGraphContentProvider;


/**
 * This factory helps make models (nodes & connections).
 * 
 * @author irbull
 * @author ccallendar
 */
public class GraphModelFactory implements IGraphModelFactory {
	
	private StructuredViewer viewer = null;
	private boolean highlightAdjacentNodes = false;
		
	public GraphModelFactory(StructuredViewer viewer, boolean highlightAdjacentNodes) {
		this.viewer = viewer;
		this.highlightAdjacentNodes = highlightAdjacentNodes;
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createModel()
	 */
	public GraphModel createModel() {
		return new GraphModel((Graph)viewer.getControl());
	}
	
	private IGraphContentProvider getContentProvider() {
		return (IGraphContentProvider)viewer.getContentProvider();
	}
	
	private ILabelProvider getLabelProvider() {
		return (ILabelProvider)viewer.getLabelProvider();
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createModelFromContentProvider(java.lang.Object)
	 */
	public GraphModel createModelFromContentProvider( Object inputElement ) {
		//TODO: This does not take care of non-connected nodes
		GraphModel model = createModel();
		Object rels[] = getContentProvider().getRelationships();
		for ( int i = 0; i < rels.length; i++ ) {
			createRelationship(model, rels[i], getContentProvider().getSource(rels[i]), getContentProvider().getDestination(rels[i]));
		}
		return model;
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createRelationship(ca.uvic.cs.zest.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public GraphModelConnection createRelationship(GraphModel model, Object data) {
		return createRelationship(model, data, getContentProvider().getSource(data), getContentProvider().getDestination(data));
	}	
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createRelationship(ca.uvic.cs.zest.internal.graphmodel.GraphModel, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public GraphModelConnection createRelationship( GraphModel model, Object data, Object source, Object dest  ) {
		GraphModelNode sourceNode = model.getInternalNode( source );
		GraphModelNode destNode = model.getInternalNode( dest );
		
		if ( sourceNode == null ) {
			sourceNode = createNode(model, source );
		}
		if ( destNode == null ) {
			destNode = createNode(model, dest);
		}		
		
		GraphModelConnection connection;
		for (Iterator iterator =  sourceNode.getTargetConnections().iterator(); iterator.hasNext(); ) {
			//TODO: get connections won't work for directed graphs!
			connection = (GraphModelConnection) iterator.next();
			if ( connection.getSource().getExternalNode().equals( dest ) ) {
				// We already have a node that goes from source to dest!
				DebugPrint.println("Connection already exists: " + connection);
				return null;
			}
		}
		
		connection = new GraphModelConnection(model, data, sourceNode, destNode, false, getContentProvider().getWeight(data));
		connection.setText(getLabelProvider().getText(data));
		connection.setImage(getLabelProvider().getImage(data));
		model.addConnection(connection.getExternalConnection(), connection);
		return connection;
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createNode(ca.uvic.cs.zest.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public GraphModelNode createNode( GraphModel model, Object data ) {
		if ( model.getInternalNode( data ) == null ) {
			ILabelProvider labelProvider = getLabelProvider();
			GraphModelNode node = new GraphModelNode(model, getLabelProvider().getText(data), labelProvider.getImage(data), data);
			node.setHighlightAdjacentNodes(highlightAdjacentNodes);
			if (labelProvider instanceof IColorProvider) {
				IColorProvider colorProvider = (IColorProvider) labelProvider;
				node.setForegroundColor(colorProvider.getForeground(data));
				node.setBackgroundColor(colorProvider.getBackground(data));
			}
			if (labelProvider instanceof IFontProvider) {
				IFontProvider fontProvider = (IFontProvider) labelProvider;
				node.setFont(fontProvider.getFont(data));
			}
			model.addNode(data, node);
			return node;
		} else {
			return model.getInternalNode( data );
		}
	}

}
