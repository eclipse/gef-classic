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
package org.eclipse.mylar.zest.core.viewers;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.mylar.zest.core.ZestException;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IZestGraphDefaults;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;

/**
 * Abstraction of graph viewers to implement functionality used by all of them.
 * Not intended to be implemented by clients. Use one of the provided children
 * instead.
 * @author Del Myers
 *
 */
public abstract class AbstractStructuredGraphViewer extends AbstractZoomableViewer {
	/**
	 * A simple graph comparator that orders graph elements based on 
	 * thier type (connection or node), and their unique object identification.
	 */
	private class SimpleGraphComparator implements Comparator {
		TreeSet storedStrings;
		/**
		 * 
		 */
		public SimpleGraphComparator() {
			this.storedStrings = new TreeSet();
		}
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1) {
			if (arg0 instanceof IGraphModelNode && arg1 instanceof IGraphModelConnection) {
				return 1;
			} else if (arg0 instanceof IGraphModelConnection && arg1 instanceof IGraphModelNode) {
				return -1;
			}
			if (arg0.equals(arg1)) return 0;
			return getObjectString(arg0).compareTo(getObjectString(arg1));
		}
		
		private String getObjectString(Object o) {
			String s = o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
			while (storedStrings.contains(s)) {
				s = s + 'X';
			}
			return s;
		}
	}
	/**
	 * Contains top-level styles for the entire graph. Set in the constructor.	 * 
	 */
	private int graphStyle;
	
	/**
	 * Contains node-level styles for the graph. Set in setNodeStyle(). Defaults
	 * are used in the constructor.
	 */
	private int nodeStyle;
	
	/**
	 * Contains arc-level styles for the graph. Set in setConnectionStyle(). Defaults
	 * are used in the constructor.
	 */
	private int connectionStyle;
	
	
	AbstractStructuredGraphViewer(int graphStyle) {
		this.graphStyle = graphStyle;
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.nodeStyle = IZestGraphDefaults.NODE_STYLE;
	}
	
	/**
	 * Sets the default style for nodes in this graph. Note: if an input
	 * is set on the viewer, a ZestException will be thrown.
	 * @param nodeStyle the style for the nodes.
	 * @see #ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		if (getInput() != null) ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		this.nodeStyle = nodeStyle;
	}
	
	/**
	 * Sets the default style for connections in this graph. Note: if an input
	 * is set on the viewer, a ZestException will be thrown.
	 * @param connectionStyle the style for the connections.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		if (getInput() != null) ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		if (!ZestStyles.validateConnectionStyle(connectionStyle)) ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		this.connectionStyle = connectionStyle;
	}
	
	
	/**
	 * Returns the style set for the graph
	 * @return The style set of the graph
	 */ 
	public int getGraphStyle() {
		return graphStyle;
	}
	
	/**
	 * Returns the style set for the nodes.
	 * @return the style set for the nodes.
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}
	
	/**
	 * @return the connection style.
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	
	/**
	 * Sets the layout algorithm for this viewer. Subclasses may place restrictions on the
	 * algorithms that it accepts.
	 * @param algorithm the layout algorithm
	 * @param run true if the layout algorithm should be run immediately. This is a hint.
	 */
	public abstract void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean run);
	
	/**
	 * Equivalent to setLayoutAlgorithm(algorithm, false).
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		setLayoutAlgorithm(algorithm, false);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	protected void internalRefresh(Object element) {
		filterVisuals();
	}
	
	protected void filterVisuals() {
		if (getModel() == null) return;
		Object[] filtered = getFilteredChildren(getInput());
		SimpleGraphComparator comparator = new SimpleGraphComparator();
		TreeSet filteredElements = new TreeSet(comparator);
		TreeSet unfilteredElements = new TreeSet(comparator);
		List connections = getModel().getConnections();
		List nodes = getModel().getNodes();
		if (filtered.length == 0) {
			//set everything to invisible.
			//@tag bug.156528-Filters.check : should we only filter out the nodes?
			for (Iterator i = connections.iterator(); i.hasNext();) {
				IGraphModelConnection c = (IGraphModelConnection) i.next();
				c.setVisible(false);
			}
			for (Iterator i = nodes.iterator(); i.hasNext();) {
				IGraphModelNode n = (IGraphModelNode)i.next();
				n.setVisible(false);
			}
			return;
		}
		for (Iterator i = connections.iterator(); i.hasNext();) {
			IGraphModelConnection c = (IGraphModelConnection) i.next();
			if (c.getExternalConnection() != null)
				unfilteredElements.add(c);	
		}
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			IGraphModelNode n = (IGraphModelNode)i.next();
			if (n.getExternalNode() != null)
				unfilteredElements.add(n);
		}
		for (int i = 0; i < filtered.length; i++) {
			Object modelElement = getModel().getInternalConnection(filtered[i]);
			if (modelElement == null) {
				modelElement = getModel().getInternalNode(filtered[i]);
			}
			if (modelElement != null) {
				filteredElements.add(modelElement);
			}
		}
		unfilteredElements.removeAll(filteredElements);
		//set all the elements that did not pass the filters to invisible, and
		//all the elements that passed to visible.
		while (unfilteredElements.size() > 0) {
			IGraphItem i = (IGraphItem) unfilteredElements.first();
			i.setVisible(false);
			unfilteredElements.remove(i);
		}
		while (filteredElements.size() > 0) {
			IGraphItem i = (IGraphItem)filteredElements.first();
			i.setVisible(true);
			filteredElements.remove(i);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRawChildren(java.lang.Object)
	 */
	protected Object[] getRawChildren(Object parent) {
		if (parent == getInput()) {
			//get the children from the model.
			LinkedList children = new LinkedList();
			if (getModel() != null) {
				List connections = getModel().getConnections();
				List nodes = getModel().getNodes();
				for (Iterator i = connections.iterator(); i.hasNext();){
					IGraphModelConnection c = (IGraphModelConnection) i.next();
					if (c.getExternalConnection() != null) {
						children.add(c.getExternalConnection());
					}
				}
				for (Iterator i = nodes.iterator(); i.hasNext();) {
					IGraphModelNode n = (IGraphModelNode) i.next();
					if (n.getExternalNode() != null) {
						children.add(n.getExternalNode());
					}
				}
				return children.toArray();
			}
		}
		return super.getRawChildren(parent);
	}
	
	/**
	 * Applies the viewers layouts.
	 *
	 */
	public abstract void applyLayout();
	
	/**
	 * Returns the internal graph model.
	 * @return the internal graph model.
	 */
	protected abstract GraphModel getModel();
	

}
