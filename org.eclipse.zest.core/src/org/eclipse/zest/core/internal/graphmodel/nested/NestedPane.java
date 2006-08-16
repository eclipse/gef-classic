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
package org.eclipse.mylar.zest.core.internal.graphmodel.nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.NonNestedProxyNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.ProxyConnection;


/**
 * @author Ian bull
 */
//@tag bug(152613-Client-Supplier(fix))
public class NestedPane {

	public static final int SUPPLIER_PANE = 0;
	public static final int MAIN_PANE = 1;
	public static final int CLIENT_PANE = 2;
	private boolean closedState = false;
	
	private	NestedGraphModel nestedGraphModel = null;
	private int paneType = 0;
	//@tag bug(153466-NoNestedClientSupply(fix)) : must cache the children in order to avoid making extra proxies.
	private List children = null;
	public NestedPane( int paneType ) {
		this.paneType = paneType;
		children = new ArrayList();
		
	}
	
	public void setModel( NestedGraphModel model ) {
//		@tag bug(152613-Client-Supplier(fix)) : set the initial closed state based on the model.
		this.nestedGraphModel = model;
		if (model == null) return;
		switch(paneType) {
		case CLIENT_PANE:
			closedState = model.isClientClosed();
			this.children = createProxies(nestedGraphModel.getCurrentNode().getConnectionsTo(), true);
			break;
		case SUPPLIER_PANE:
			closedState = model.isSupplierClosed();
			this.children = createProxies(nestedGraphModel.getCurrentNode().getConnectionsFrom(), false);
			break;
		case MAIN_PANE:
			this.children = Arrays.asList(new Object[] {nestedGraphModel.getCurrentNode()});
			break;
		default:
			this.children = Collections.EMPTY_LIST;
			closedState = false;
		}
	}
	
	public NestedGraphModel getModel() {
		return this.nestedGraphModel;
	}
	
	public int getPaneType() {
		return this.paneType;
	}
	
	/**
	 * Refreshes the children from the proxies listed in the model.
	 *
	 */
	public void refreshChildren() {
		List proxies = nestedGraphModel.getProxyConnections();
		NestedGraphModelNode topNode = nestedGraphModel.getCurrentNode();
		List connections;
		boolean to = false;
		switch (getPaneType()) {
		case CLIENT_PANE : 
			connections = topNode.getConnectionsTo();
			to = true;
			break;
		case SUPPLIER_PANE:
			connections = topNode.getConnectionsFrom();
			break;
		case MAIN_PANE:
			this.children = Arrays.asList(new Object[] {nestedGraphModel.getCurrentNode()});
		default: return;
		}
		this.children = new ArrayList();
		Iterator i = proxies.iterator();
		while (i.hasNext()) {
			ProxyConnection conn = (ProxyConnection) i.next();
			GraphModelConnection referenced = conn.getProxy();
			Object end = (to) ? conn.getDestination() : conn.getSource();
			if (end instanceof NonNestedProxyNode) {
				if (connections.contains(referenced) && ! this.children.contains(end)) {
					this.children.add(end);
				} 
			}
		}
	}

	public List getChildren() {
		return children;
	}
	
	/**
	 * Creates proxy nodes for the given list of connections. Visible proxy connections
	 * are also created and added to the nodes.
	 * @param connections the list of connections.
	 * @return The list of proxy nodes and connections
	 */
	private List createProxies(List connections, boolean to) {
		LinkedList proxies = new LinkedList();
		HashMap modelNodes = new HashMap();
		for (Iterator i = connections.iterator(); i.hasNext();) {
			GraphModelConnection conn = (GraphModelConnection) i.next();
			NestedGraphModelNode node =	null;
			if (to) {
				node = (NestedGraphModelNode) conn.getDestination();
			} else {
				node = (NestedGraphModelNode) conn.getSource();
			}
			NonNestedProxyNode proxy = (NonNestedProxyNode) modelNodes.get(node);
			if (proxy == null) {
				proxy = node.getGraphModel().createProxyNode(node);
				modelNodes.put(node,proxy);
				proxies.add(proxy);
			}
			if (!to) {
				node.getGraphModel().createProxyConnection(proxy, conn.getDestination(), conn);	
			} else {
				node.getGraphModel().createProxyConnection(conn.getSource(), proxy, conn);
			}
		}
		return proxies;
	}

	/**
     * Removes a proxy node from the list of children.
	 */
	public void removeProxy(NonNestedProxyNode proxy) {
		this.children.remove(proxy);
	}
	
	/**
     * @return true iff the state of this panel is closed.
     */
	public boolean isClosed() {
		return closedState;
	}
}
