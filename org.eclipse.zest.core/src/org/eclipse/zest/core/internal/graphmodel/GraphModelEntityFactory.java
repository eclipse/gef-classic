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

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphEntityContentProvider;


/**
 * 
 * @author Ian Bull
 */
public class GraphModelEntityFactory extends AbstractStylingModelFactory implements IGraphModelFactory {

	private StructuredViewer viewer = null;
	private boolean highlightAdjacentNodes = false;
	
	public GraphModelEntityFactory(StructuredViewer viewer, boolean highlightAdjacentNodes) {
		this.viewer = viewer;
		this.highlightAdjacentNodes = highlightAdjacentNodes;
	}
	
	/**
	 * Creates a new graph model
	 * @return
	 */
	public GraphModel createModel() {
		return new GraphModel((FigureCanvas)viewer.getControl());
	}
	
	private IGraphEntityContentProvider getContentProvider() {
		return (IGraphEntityContentProvider)viewer.getContentProvider();
	}
	
	protected ILabelProvider getLabelProvider() {
		return (ILabelProvider)viewer.getLabelProvider();
	}
	
	
	//	@tag bug(154412-ClearStatic(fix)) : renamed to allow the parent to do some processing before the model is created.
	public GraphModel doCreateModelFromContentProvider( Object inputElement, int nodeStyle, int connectionStyle ) {
		GraphModel model = createModel();
		model.setNodeStyle(nodeStyle);
		model.setConnectionStyle(connectionStyle);
		
		Object entities[] = getContentProvider().getElements( inputElement );
		if ( entities == null ) return model;
		for ( int i = 0; i < entities.length; i++ ) {
			Object data = entities[ i ];
			GraphModelNode node = new GraphModelNode(model, getLabelProvider().getText(data), getLabelProvider().getImage(data), data);
			node.setHighlightAdjacentNodes(highlightAdjacentNodes);
			styleItem(node);
			model.addNode( data, node );
		}
		
		for ( int i=0; i < entities.length; i++ ) {
			Object data = entities[ i ];
			Object[] related = getContentProvider().getConnectedTo( data );
			if ( related != null )
				for ( int j = 0; j < related.length; j++ ) {
					createRelationship(model, null, data, related[j]);
				}
		}	
		return model;
	}

	
	public GraphModelNode createNode(GraphModel model, Object data) {
		GraphModelNode node = new GraphModelNode(model, getLabelProvider().getText( data ), getLabelProvider().getImage(data), data);
		node.setHighlightAdjacentNodes(highlightAdjacentNodes);
		styleItem(node);
		Object[] related = getContentProvider().getConnectedTo( data );
		for ( int i = 0; i < related.length; i++ ) {
			createRelationship(model, null, data, related);
		}
		return node;
	}

	public GraphModelConnection createRelationship(GraphModel model, Object data, Object source, Object dest) {
		GraphModelNode sourceNode = getNode(model, source );
		GraphModelNode destNode = getNode( model, dest );

		if ( sourceNode == null || destNode == null ) return null;
		
		// Check if connection already exists
		GraphModelConnection connection;
		for (Iterator iterator =  sourceNode.getTargetConnections().iterator(); iterator.hasNext(); ) {
			//TODO: get connections won't work for directed graphs!
			connection = (GraphModelConnection) iterator.next();
			if ((dest != null) && dest.equals(connection.getDestination().getExternalNode())) {
				// We already have a node that goes from source to dest!
				//@tag bug(114452)
				return null;
			}
		}
		// Create the connection
		double weight = getContentProvider().getWeight( source, dest );
		connection = new GraphModelConnection(model, data, sourceNode, destNode, false, weight);
		styleItem(connection);
		model.addConnection(connection.getExternalConnection(), connection);
		return connection;
	}

	public GraphModelConnection createRelationship(GraphModel model, Object data) {
		throw new UnsupportedOperationException("Use createRelationship(model, object, object, object)");
	}
	
	private GraphModelNode getNode( GraphModel model, Object data ) {
		GraphModelNode node = model.getInternalNode( data );
		return node;
	}
		
}
