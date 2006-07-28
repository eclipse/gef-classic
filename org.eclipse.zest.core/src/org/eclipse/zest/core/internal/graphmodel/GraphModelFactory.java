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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphContentProvider;
import org.eclipse.swt.widgets.Canvas;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


/**
 * This factory helps make models (nodes & connections).
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class GraphModelFactory implements IGraphModelFactory {
	/**
	 * 
	 * Class to keep track of the number of connections there are between two nodes.
	 * @author Del Myers
	 *
	 */
	//@tag bug(114452-MultipleArcs) : resolution
	class ConnectionCounter {
		Object source;
		Object dest;
		public ConnectionCounter(Object source, Object dest) {
			this.source = source;
			this.dest = dest;
		}
		public boolean equals(Object that) {
			return (
				this.source.equals(((ConnectionCounter)that).source) &&
				this.dest.equals(((ConnectionCounter)that).dest)
			);
		}
		public int hashCode() {
			return 0;
		}
	}

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
		return new GraphModel((Canvas)viewer.getControl());
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
	public GraphModel createModelFromContentProvider( Object inputElement, int nodeStyle, int connectionStyle) {
		//@tag bug(152045-UnconnectedNodes) : This does not take care of non-connected nodes. FIXED
		GraphModel model = createModel();
		model.setConnectionStyle(connectionStyle);
		model.setNodeStyle(nodeStyle);
		//make the model have the same styles as the viewer
		Object rels[] = getContentProvider().getElements(inputElement);
		for ( int i = 0; i < rels.length; i++ ) {
			createRelationship(model, rels[i], getContentProvider().getSource(rels[i]), getContentProvider().getDestination(rels[i]));
		}
		//@tag bug(114452-MultipleArcs) : count the number of arcs between nodes, and set the curve accordingly
		Iterator ci = model.getConnections().iterator();
		Hashtable counters = new Hashtable();
		while (ci.hasNext()) {
			GraphModelConnection conn = (GraphModelConnection) ci.next();
			ConnectionCounter key = new ConnectionCounter(
				conn.getSource().getExternalNode(),
				conn.getDestination().getExternalNode()
			);
			Integer count = (Integer) counters.get(key);
			if (count == null) {
				count = new Integer(1);
				counters.put(key, count);
			} else {
				count = new Integer(count.intValue() + 1);
				counters.put(key, count);
			}
			int scale = 3;
			if (conn.getSource() == conn.getDestination()) {
				scale = 5;
			}
			//even if the connection isn't curved in the style, the edit part
			//may decide that it should be curved if source and dest are equal.
			//@tag drawing(arcs) : check here if arcs are too close when being drawn. Adjust the constant.
			conn.setCurveDepth(count.intValue()*(scale+conn.getLineWidth()));
				
			
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
		//@tag bug(152045-UnconnectedNodes) : FIX
		if (source == null && dest == null) {
			//no information to create on.
			return null;
		}
		
		GraphModelNode sourceNode = null;
		GraphModelNode destNode = null;
		if (source != null) {
			sourceNode = model.getInternalNode( source );
			if ( sourceNode == null ) {
				sourceNode = createNode(model, source );
			}
		}
		if (dest != null) { 
			destNode = model.getInternalNode( dest );
			if ( destNode == null ) {
				destNode = createNode(model, dest);
			}	
		}
		
		if (sourceNode == null || destNode == null) {
			//no connection to create
			return null;
		}
			
		
		GraphModelConnection connection;
		/*
		 * Allow potentially infinite number of connections between two nodes.
		for (Iterator iterator =  sourceNode.getTargetConnections().iterator(); iterator.hasNext(); ) {
			//TODO: get connections won't work for directed graphs!
			connection = (GraphModelConnection) iterator.next();
			if ( connection.getSource().getExternalNode().equals( dest ) ) {
				// We already have a node that goes from source to dest!
				// @tag bug(114452)
				return null;
			}
		}
		*/
		
		connection = new GraphModelConnection(model, data, sourceNode, destNode, false, getContentProvider().getWeight(data));
		connection.setText(getLabelProvider().getText(data));
		connection.setImage(getLabelProvider().getImage(data));
		model.addConnection(connection.getExternalConnection(), connection);
		GraphItemStyler.styleItem(connection, getLabelProvider());
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
			GraphItemStyler.styleItem(node,labelProvider);
			model.addNode(data, node);
			return node;
		} else {
			return model.getInternalNode( data );
		}
	}

}
