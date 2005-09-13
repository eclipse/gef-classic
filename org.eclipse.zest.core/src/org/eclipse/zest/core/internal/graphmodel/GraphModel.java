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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylar.zest.core.DebugPrint;
import org.eclipse.mylar.zest.core.internal.viewers.Graph;


/**
 * Holds the nodes and connections for the graph.
 * 
 * @author Chris Callendar
 */
public class GraphModel extends GraphItem {

	/** Property ID to use when a child is added to this diagram. */
	public static final String NODE_ADDED_PROP = "LayoutDiagram.NodeAdded";
	/** Property ID to use when a child is removed from this diagram. */
	public static final String NODE_REMOVED_PROP = "LayoutDiagram.NodeRemoved";
	
	private List nodes;
	protected List connections;
	
	private boolean directedEdges = false;
	

	/** Maps user nodes to internal nodes */
	private HashMap external2InternalNodeMap; 

	/** Maps user connections to internal connections */
	private HashMap external2InternalConnectionMap; 
	
	/**
	 * Initializes this diagram.
	 * @param canvas	The parent widget.
	 */
	public GraphModel(Graph canvas) {
		super(canvas);
		this.nodes = new ArrayList();
		this.connections  = new ArrayList();
		this.external2InternalNodeMap = new HashMap();
		this.external2InternalConnectionMap = new HashMap();
	}

	/**
	 * Gets a list of the GraphModelNode children objects under the root node in this diagram.
	 * If the root node is null then all the top level nodes are returned.
	 * @return List of GraphModelNode objects
	 */
	public List getNodes() {
		return nodes;		
	}
	
	/**
	 * Sets whether or not the edges should be directed or not
	 * @param directedEdges
	 */
	public void setDirectedEdges( boolean directedEdges ) {
		this.directedEdges = directedEdges;
	}
	
	/**
	 * Gets whether or not the edges should be directed
	 * Note: Individual edges may have their own property in the future, but
	 * for now this is a global property.  
	 * @return
	 */
	public boolean getDirectedEdges( ) {
		return this.directedEdges;
	}
	
	/**
	 * Converts the list of GraphModelNode objects into an array an returns it.
	 * @return GraphModelNode[]
	 */
	public GraphModelNode[] getNodesArray() {
		GraphModelNode[] nodesArray = new GraphModelNode[nodes.size()];
		nodesArray = (GraphModelNode[])nodes.toArray(nodesArray);
		return nodesArray;
	}
	
	/**
	 * Gets the list of GraphModelConnection objects.
	 * @return list of GraphModelConnection objects
	 */
	public List getConnections() {
		return this.connections;
	}
	
	/**
	 * Converts the list of GraphModelConnection objects into an array and returns it.
	 * @return GraphModelConnection[]
	 */
	public GraphModelConnection[] getConnectionsArray() {
		GraphModelConnection[] connsArray = new GraphModelConnection[connections.size()];
		connsArray = (GraphModelConnection[])connections.toArray(connsArray);
		return connsArray;
	}
	
	/**
	 * Adds a connection to this model
	 * @param connection
	 */
	public boolean addConnection( Object externalConnection, GraphModelConnection connection ) {
		if ((connection != null) && connections.add(connection)) {
			external2InternalConnectionMap.put(externalConnection, connection);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the internal GraphModelConnection object associated with the 
	 * given external connection object.
	 * @param externalConnection
	 * @return GraphModelConnection
	 */
	public GraphModelConnection getInternalConnection(Object externalConnection) {
		if (external2InternalConnectionMap.containsKey(externalConnection)) {
			return (GraphModelConnection)external2InternalConnectionMap.get(externalConnection);
		}
		return null;
	}
	
	/**
	 * Disconnects and removes the connection as well as notifying the graph listeners
	 * that a connection has been removed.
	 * @param connection
	 * @return boolean if removed
	 */
	public boolean removeConnection(GraphModelConnection connection) {
		boolean removed = false;
		if (connection != null) {
			connection.disconnect();
			external2InternalConnectionMap.remove(connection.getExternalConnection());
			removed = connections.remove(connection);
		}
		return removed;
	}
	
	/**
	 * Removes the connection associated with the external connection object.
	 * @param externalConnection
	 * @return boolean
	 */
	public boolean removeConnection(Object externalConnection) {
		GraphModelConnection connection = (GraphModelConnection)external2InternalConnectionMap.get(externalConnection);
		return this.removeConnection(connection);
	}
	
	/**
	 * Adds a new node to the diagram.
	 * @param node The node to add
	 * @return boolean if successful.
	 */
	public boolean addNode(Object externalNode, GraphModelNode node) {
		boolean added = false;
		if (node != null) {
			addNodeToList(node);
			external2InternalNodeMap.put( externalNode, node );
			firePropertyChange(NODE_ADDED_PROP, null, node);
			added = true;
		}
		return added;
	}
	
	protected void addNodeToList(GraphModelNode node) {
		nodes.add(node);
	}
	
	protected boolean removeNodeFromList(GraphModelNode node) {
		return nodes.remove(node);
	}
	
	/**
	 * Removes a node from this graph and disconnects all the connections.
	 * @param node a non-null LayoutNode instance.
	 * @return boolean If the node was removed.
	 */
	public boolean removeNode(GraphModelNode node) {
		boolean removed = false;
		if (node != null) {
			DebugPrint.println("Deleting node '" + node.getText() + "'");
			external2InternalNodeMap.remove( node.getExternalNode() );
			removed = removeNodeFromList(node);

			if (removed) {
				// remove the source and target connections & notify the graph listeners
				for (Iterator iter = node.getSourceConnections().iterator(); iter.hasNext();) {
					removeConnection((GraphModelConnection)iter.next());
				}
				for (Iterator iter = node.getTargetConnections().iterator(); iter.hasNext();) {
					removeConnection((GraphModelConnection)iter.next());
				}
				firePropertyChange(NODE_REMOVED_PROP, null, node);
			}
		}
		return removed;
	}
	
	/**
	 * Removes the internal node from the external node
	 * @param externalNode The external node representation of this node
	 * @return true if successful
	 */
	public boolean removeNode( Object externalNode ) {
		GraphModelNode node = (GraphModelNode) external2InternalNodeMap.get( externalNode );
		return this.removeNode(node);
	}
	
	/**
	 * Gets the internal node from the external node.
	 * @param o The user data.
	 * @return The internal node or null if none
	 */
	public GraphModelNode getInternalNode( Object o ) {
		if ( external2InternalNodeMap.containsKey( o ) ) {
			return (GraphModelNode) external2InternalNodeMap.get( o );
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#toString()
	 */
	public String toString() {
		return "GraphModel {" + nodes.size() + " nodes, " +
			connections.size() + " connections}";
	}
	
	
	/**
	 * Fires changes to all the model elements
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	public void fireAllPropertyChange(String property, Object oldValue, Object newValue) {
		for ( Iterator iter = this.connections.iterator(); iter.hasNext(); ) {
			((GraphModelConnection) iter.next() ).firePropertyChange(property, oldValue, newValue ); 
		}
		
		for ( Iterator iter = this.nodes.iterator(); iter.hasNext(); ) {
			((GraphModelNode) iter.next() ).firePropertyChange(property, oldValue, newValue );
		}
	}
	
}
