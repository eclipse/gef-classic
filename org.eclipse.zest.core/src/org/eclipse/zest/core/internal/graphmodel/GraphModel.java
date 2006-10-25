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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.swt.widgets.Canvas;


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
	/** Property ID to use when the focus (current) node has changed in the model */
	public static final String NODE_FOCUS_PROP = "LayoutDiagram.NodeFocus";
	/** Property ID to use when a proxy node is removed **/
	public static final String NODE_PROXY_REMOVED_PROP = "Proxy.NodeRemoved";
	
	
	private List nodes;
	protected List connections;
	
	//@tag zest(bug(153466-NoNestedClientSupply(fix))) : keep proxy connections and nodes inside the model for easy access.
	protected List proxyConnections;
	protected List proxyNodes;

	/** Maps user nodes to internal nodes */
	private HashMap external2InternalNodeMap; 

	/** Maps user connections to internal connections */
	private HashMap external2InternalConnectionMap;
	private int connectionStyle;
	private int nodeStyle;
	
	/**
	 * Initializes this diagram.
	 * @param canvas	The parent widget.
	 */
	public GraphModel(Canvas canvas) {
		super(canvas);
		this.nodes = new ArrayList();
		this.proxyNodes = new LinkedList();
		this.proxyConnections = new LinkedList();
		this.connectionStyle = ZestStyles.NONE;
		this.nodeStyle = ZestStyles.NONE;
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
	 * Converts the list of GraphModelNode objects into an array an returns it.
	 * @return GraphModelNode[]
	 */
	public IGraphModelNode[] getNodesArray() {
		IGraphModelNode[] nodesArray = new IGraphModelNode[nodes.size()];
		nodesArray = (IGraphModelNode[])nodes.toArray(nodesArray);
		return nodesArray;
	}
	
	/**
	 * @return the proxyConnections
	 */
	 //@tag zest(bug(153466-NoNestedClientSupply(fix))) : make proxies available from the model
	public List getProxyConnections() {
		return proxyConnections;
	}
	
	/**
	 * @return the proxyNodes
	 */
	  //@tag zest(bug(153466-NoNestedClientSupply(fix))) : make proxies available from the model
	public List getProxyNodes() {
		return proxyNodes;
	}
	
	/**
	 * Creates and reaturns a proxy node based on the given node, and adds it
	 * to the list of proxies in the model.
	 * @return the proxy node.
	 * 
	 */
	//@tag zest(bug(153466-NoNestedClientSupply(fix))) : proxies can only be made on the model. This ensures that they are properly monitored here.
	public NonNestedProxyNode createProxyNode(IGraphModelNode node) {
		NonNestedProxyNode proxy = new NonNestedProxyNode(node);
		proxyNodes.add(proxy);
		proxy.activate();
		return proxy;
	}
	/**
	 * Creates and returns a proxy connection based on the given connection, and
	 * the source and target endpoints. The created proxy is also added to the
	 * list of proxies in the model. Note, only the visual elements of the
	 * proxy connection are used for display: the given source and target nodes will
	 * be the actual source and target nodes for the returned proxy. The reason
	 * for this is that the source and target nodes may themselves be proxies
	 * for the actual source and target nodes of the original connection. Some
	 * example usages are:
	 * <pre>
	 * &#47;&#47;to make a proxy connection based exactly on the given connection
	 * graphModel.createProxyConnection(conn.getSource(), conn.getDestination(), conn);
	 * &#47;&#47;to make a proxy using a proxy node as the source, and the original node as the target:
	 * graphModel.createProxyConnection(graphModel.createProxyNode(conn.getSource()), conn.getDestination(), conn); 
	 * </pre>
	 * In general, either the original source and destination nodes, or a proxy to them, should be used.
	 * @param source the source node that this connection will be linked to. May be a proxy.
	 * @param target the target node that this connection will be linked to. May be a proxy.
	 * @param conn the connection to base this proxy on.
	 * @return the proxy connection
	 */
	 //@tag zest(bug(153466-NoNestedClientSupply(fix))) : proxies can only be made on the model. This ensures that they are properly monitored here.
	public ProxyConnection createProxyConnection(IGraphModelNode source, IGraphModelNode target, IGraphModelConnection conn) {
		ProxyConnection connection = new ProxyConnection(source, target, conn);
		proxyConnections.add(connection);
		connection.reconnect();
		return connection;
	}
	
	/**
	 * Removes the given proxy node from the model, if it exists. All connections
	 * on the node will be removed as well.
	 * @param node
	 */
	 //@tag zest(bug(153466-NoNestedClientSupply(fix))) : proxies can only be made on the model. This ensures that they are properly monitored here.
	public void removeProxyNode(NonNestedProxyNode node) {
		if (proxyNodes.contains(node)) {
			proxyNodes.remove(node);
			List connections = new LinkedList();
			connections.addAll(node.getSourceConnections());
			connections.addAll(node.getTargetConnections());
			IGraphModelConnection[] connectionsArray = 
				(IGraphModelConnection[])connections.toArray(new IGraphModelConnection[connections.size()]);
			for (int i = 0; i < connectionsArray.length; i++) {
				IGraphModelConnection conn = connectionsArray[i];
				if (conn instanceof ProxyConnection) {
					removeProxyConnection((ProxyConnection) conn);
				} else {
					removeConnection(conn);
				}
			}
			node.deactivate();
			firePropertyChange(NODE_PROXY_REMOVED_PROP, null, node);
		}
	}
	
	/**
	 * Disconnects the given connection if it exists in the model.
	 * @param connection the connection to disconnect.
	 */
	 //@tag zest(bug(153466-NoNestedClientSupply(fix))) : proxies can only be made on the model. This ensures that they are properly monitored here.
	public void removeProxyConnection(ProxyConnection connection) {
		if (proxyConnections.contains(connection)) {
			proxyConnections.remove(connection);
			connection.disconnect();
		}
	}
	
	/**
	 * Removes all proxie nodes and connections from the model.
	 *
	 */
	 //@tag zest(bug(153466-NoNestedClientSupply(fix))) : proxies can only be made on the model. This ensures that they are properly monitored here.
	public void clearProxies() {
		while (proxyNodes.size() > 0) {
			removeProxyNode((NonNestedProxyNode)proxyNodes.get(0));
		}
		while (proxyConnections.size() > 0) {
			removeProxyConnection((ProxyConnection) proxyConnections.get(0));
		}
	}
	
	/**
	 * Returns the nodes map.  The key is the node data and the value
	 * is the GraphModelNode.
	 * @return HashMap
	 */
	public HashMap getNodesMap() {
		return external2InternalNodeMap;
	}
	
	/**
	 * Returns the connection map. They key is the connection data and the value
	 * is te GraphModelConnection
	 * @return
	 */
	public HashMap getConnectionMap() {
		return external2InternalConnectionMap;
	}
	

	
	/**
	 * Sets the default connection style.
	 * @param connection style the connection style to set
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		this.connectionStyle = connectionStyle;
	}
	
	/**
	 * Gets the default connection style.
	 * @return the connection style
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	
	/**
	 * Sets the default node style.
	 * @param nodeStyle the node style to set
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		this.nodeStyle = nodeStyle;
	}
	
	/**
	 * Gets the default node style.
	 * @return the node style
	 * @see org.eclipse.mylar.zest.core.ZestStyles
	 */
	public int getNodeStyle() {
		return nodeStyle;
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
	public IGraphModelConnection[] getConnectionsArray() {
		IGraphModelConnection[] connsArray = new IGraphModelConnection[connections.size()];
		connsArray = (IGraphModelConnection[])connections.toArray(connsArray);
		return connsArray;
	}
	
	/**
	 * Adds a connection to this model
	 * @param connection
	 */
	public boolean addConnection( Object externalConnection, IGraphModelConnection connection ) {
		if ((connection != null) && connections.add(connection)) {
			external2InternalConnectionMap.put(externalConnection, connection);
			connection.reconnect();
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
	public IGraphModelConnection getInternalConnection(Object externalConnection) {
		if (external2InternalConnectionMap.containsKey(externalConnection)) {
			return (IGraphModelConnection)external2InternalConnectionMap.get(externalConnection);
		}
		return null;
	}
	
	/**
	 * Disconnects and removes the connection as well as notifying the graph listeners
	 * that a connection has been removed.
	 * @param connection
	 * @return boolean if removed
	 */
	public boolean removeConnection(IGraphModelConnection connection) {
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
		IGraphModelConnection connection = (IGraphModelConnection)external2InternalConnectionMap.get(externalConnection);
		return this.removeConnection(connection);
	}
	
	/**
	 * Adds a new node to the diagram.
	 * @param node The node to add
	 * @return boolean if successful.
	 */
	public boolean addNode(Object externalNode, IGraphModelNode node) {
		boolean added = false;
		if (node != null) {
			addNodeToList(node);
			external2InternalNodeMap.put( externalNode, node );
			firePropertyChange(NODE_ADDED_PROP, null, node);
			added = true;
		}
		return added;
	}
	
	protected void addNodeToList(IGraphModelNode node) {
		nodes.add(node);
	}
	
	protected boolean removeNodeFromList(IGraphModelNode node) {
		return nodes.remove(node);
	}
	
	/**
	 * Removes a node from this graph and disconnects all the connections.
	 * @param node a non-null LayoutNode instance.
	 * @return boolean If the node was removed.
	 */
	public boolean removeNode(IGraphModelNode node) {
		boolean removed = false;
		if (node != null) {
			external2InternalNodeMap.remove( node.getExternalNode() );
			removed = removeNodeFromList(node);
			if (removed) {
				// remove the source and target connections & notify the graph listeners
				for (Iterator iter = node.getSourceConnections().iterator(); iter.hasNext();) {
					removeConnection((IGraphModelConnection)iter.next());
				}
				for (Iterator iter = node.getTargetConnections().iterator(); iter.hasNext();) {
					removeConnection((IGraphModelConnection)iter.next());
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
		IGraphModelNode node = (IGraphModelNode) external2InternalNodeMap.get( externalNode );
		return this.removeNode(node);
	}
	
	/**
	 * Gets the internal node from the external node.
	 * @param o The user data.
	 * @return The internal node or null if none
	 */
	public IGraphModelNode getInternalNode( Object o ) {
		if ( external2InternalNodeMap.containsKey( o ) ) {
			return (IGraphModelNode) external2InternalNodeMap.get( o );
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
			((IGraphModelConnection) iter.next() ).firePropertyChange(property, oldValue, newValue ); 
		}
		
		for ( Iterator iter = this.nodes.iterator(); iter.hasNext(); ) {
			((IGraphModelNode) iter.next() ).firePropertyChange(property, oldValue, newValue );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem#getGraphModel()
	 */
	public GraphModel getGraphModel() {
		return this;
	}
	
}
