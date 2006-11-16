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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Base class that can be used for model factories. Offers facilities to 
 * style the items that have been created by the factory.
 * @author Del Myers
 * 
 */
//@tag zest.bug.160367-Refreshing.fix : update the factory to use the IStylingGraphModelFactory
public abstract class AbstractStylingModelFactory implements IStylingGraphModelFactory {
	private StructuredViewer viewer;
	private int connectionStyle;
	private int nodeStyle;

	/**
	 * 
	 */
	public AbstractStylingModelFactory(StructuredViewer viewer) {
		this.viewer = viewer;
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.nodeStyle = IZestGraphDefaults.NODE_STYLE;
	}
	
	public void styleConnection(IGraphModelConnection conn) {
		//recount the source and target connections on the node.
		//this isn't a great way to do it, because it results in
		//an n^2 algorithm. But, if anyone can figure out a better way
		//go ahead and try it.
		IGraphModelNode source = conn.getSource();
		IGraphModelNode dest = conn.getDestination();
		LinkedList rightList = getConnectionList(source, dest);
		
		LinkedList leftList = null;
		
		if (dest != source) {
			leftList = getConnectionList(dest, source);
		}
		
		//adjust the arcs going from source to destination
		adjustCurves(rightList);
		//adjust the arcs going from destination to source
		if (leftList != null)
			adjustCurves(leftList);
	}
	
	/**
	 * Takes a list of IGraphModelConnections and adjusts the curve depths and the
	 * bezier curves based on the number of curves in the list.
	 * @param rightList
	 */
	protected void adjustCurves(List connections) {
		int scale = 3;
		for (int i = 0; i < connections.size(); i++) {
			IGraphModelConnection conn = (IGraphModelConnection) connections.get(i);
			if (conn.getSource() == conn.getDestination()) scale = 5;
			//even if the connection isn't curved in the style, the edit part
			//may decide that it should be curved if source and dest are equal.
			//@tag drawing(arcs) : check here if arcs are too close when being drawn. Adjust the constant.
			int lineWidth = conn.getLineWidth();
			conn.setCurveDepth((i+1)*(scale+lineWidth));

//			@tag zest(bug(152530-Bezier(fix))) : set the angles, etc based on the count.
			//limit the angle to 90 degrees.
			conn.setStartAngle(90.0 - 85.0/Math.pow(i, 1.0/9.0));
			conn.setEndAngle(85.0/Math.pow(i, 1.0/9.0) - 90.0);
			//limit the length to 1
			conn.setStartLength(.75 - .25/(Math.sqrt(i)));
			conn.setEndLength(.75 - .25/(Math.sqrt(i)));
		}
	}

	
	/**
	 * @param source
	 * @param dest
	 * @return
	 */
	private LinkedList getConnectionList(IGraphModelNode source, IGraphModelNode dest) {
		LinkedList list = new LinkedList();
		Iterator i = source.getSourceConnections().iterator();
		while (i.hasNext()) {
			IGraphModelConnection c = (IGraphModelConnection) i.next();
			if (c.getDestination() == dest) {
				list.add(c);
			}
		}
		return list;
	}

	public void styleItem(IGraphItem item) {
		GraphItemStyler.styleItem(item, getLabelProvider());
		if (item instanceof IGraphModelConnection) styleConnection((IGraphModelConnection) item);
	}

	public StructuredViewer getViewer() {
		return viewer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#getLabelProvider()
	 */
	public IBaseLabelProvider getLabelProvider() {
		return viewer.getLabelProvider();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#getContentProvider()
	 */
	public IStructuredContentProvider getContentProvider() {
		return (IStructuredContentProvider) viewer.getContentProvider();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#createConnection(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public IGraphModelConnection createConnection(GraphModel graph, Object element, Object source, Object dest) {
		if (source == null || dest == null) return null;
		IGraphModelConnection oldConnection = graph.getInternalConnection(element);
		IGraphModelNode sn = graph.getInternalNode(source);
		IGraphModelNode dn = graph.getInternalNode(dest);
		if (oldConnection != null) {
			if (sn != oldConnection.getSource() || dn != oldConnection.getDestination()) {
				graph.removeConnection(oldConnection);
			} else {
				styleItem(oldConnection);
				return oldConnection;
			}
		}
		if (sn == null) {
			sn = createNode(graph, element);
		}
		if (dn == null) {
			dn = createNode(graph, element);
		}
		GraphModelConnection c = new GraphModelConnection(graph, element, sn, dn);
		styleItem(c);
		graph.addConnection(element, c);
		return c;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#createNode(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public IGraphModelNode createNode(GraphModel graph, Object element) {
		IGraphModelNode node = graph.getInternalNode(element);
		if (node == null) {
			node = new GraphModelNode(graph, element);
			styleItem(node);
			graph.addNode(element, node);
		} else {
			styleItem(node);
		}
		return node;
	}
	
	public void setConnectionStyle(int style) {
		this.connectionStyle = style;
	}
	
	/**
	 * @return the connectionStyle
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	
	public void setNodeStyle(int style) {
		this.nodeStyle = style;
	}
	
	/**
	 * @return the nodeStyle
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}
	
	/**
	 * Default implementation simply restyles the item, regardless of the properties.
	 */
	public void update(IGraphItem item) {
		styleItem(item);		
	}
	
	/**
	 * Default implementation simply restyles the items, regardless of the properties.
	 */
	public void update(IGraphItem[] items) {
		for (int i = 0; i < items.length; i++) {
			styleItem(items[i]);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refreshGraph(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel)
	 */
	public void refreshGraph(GraphModel graph) {
		//with this kind of graph, it is just as easy and cost-effective to
		//rebuild the whole thing.
		
		Map oldMap = graph.getNodesMap();
		HashMap nodesMap = new HashMap();
		//have to copy the Map data accross so that it doesn't get overwritten
		for (Iterator keys = oldMap.keySet().iterator(); keys.hasNext();) {
			Object key = keys.next();
			nodesMap.put(key, oldMap.get(key));
		}
		clearGraph(graph);
		doBuildGraph(graph);
		//update the positions on the new nodes to match the old ones.
		IGraphModelNode[] nodes = graph.getNodesArray();
		//save a little time, go with the smallest list as the primary list
		if (nodes.length < nodesMap.keySet().size()) {
			for (int i = 0; i < nodes.length; i++) {
				IGraphModelNode oldNode = (IGraphModelNode) nodesMap.get(nodes[i].getExternalNode());
				if (oldNode != null) {
					nodes[i].setPreferredLocation(oldNode.getXInLayout(), oldNode.getYInLayout());
				}
			}
		} else {
			for (Iterator i = nodesMap.keySet().iterator(); i.hasNext();) {
				Object key = i.next();
				IGraphModelNode node = graph.getInternalNode(key);
				if (node != null) {
					IGraphModelNode oldNode = (IGraphModelNode) nodesMap.get(key);
					node.setPreferredLocation(oldNode.getXInLayout(), oldNode.getYInLayout());
				}
			}
		}
	}
	
	
	
	/**
	 * Convenience method for clearing all the elements in the graph.
	 * @param graph
	 */
	public void clearGraph(GraphModel graph) {
		graph.clearProxies();
		IGraphModelNode[] nodes = graph.getNodesArray();
		for (int i = 0; i < nodes.length; i++) {
			graph.removeNode(nodes[i]);
		}
	}
	
	/**
	 * Builds the graph model from the viewer's content provider. There is no guarantee that the
	 * model will be cleared before this method is called.
	 * @param graph
	 */
	protected abstract void doBuildGraph(GraphModel graph);
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#isFiltered(java.lang.Object)
	 */
	public Object[] filter(Object parent, Object[] elements) {
		Object[] result = elements;
		ViewerFilter[] filters = getViewer().getFilters();
		for (int i = 0; i < filters.length; i++) {
			result = filters[i].filter(viewer, parent, result);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public void refresh(GraphModel graph, Object element) {
		refresh(graph, element, false);
	}
}
