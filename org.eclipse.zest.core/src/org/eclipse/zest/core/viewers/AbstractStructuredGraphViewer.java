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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.ZestException;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphItem;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.IZestGraphDefaults;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Widget;

/**
 * Abstraction of graph viewers to implement functionality used by all of them.
 * Not intended to be implemented by clients. Use one of the provided children
 * instead.
 * 
 * @author Del Myers
 * 
 */
public abstract class AbstractStructuredGraphViewer extends AbstractZoomableViewer {
	/**
	 * Contains top-level styles for the entire graph. Set in the constructor. *
	 */
	private int graphStyle;

	/**
	 * Contains node-level styles for the graph. Set in setNodeStyle(). Defaults
	 * are used in the constructor.
	 */
	private int nodeStyle;

	/**
	 * Contains arc-level styles for the graph. Set in setConnectionStyle().
	 * Defaults are used in the constructor.
	 */
	private int connectionStyle;

	/**
	 * The main graph model
	 */
	private GraphModel model;

	/**
	 * A simple graph comparator that orders graph elements based on thier type
	 * (connection or node), and their unique object identification.
	 */
	private class SimpleGraphComparator implements Comparator {
		TreeSet storedStrings;

		/**
		 * 
		 */
		public SimpleGraphComparator() {
			this.storedStrings = new TreeSet();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1) {
			if (arg0 instanceof IGraphModelNode
					&& arg1 instanceof IGraphModelConnection) {
				return 1;
			} else if (arg0 instanceof IGraphModelConnection
					&& arg1 instanceof IGraphModelNode) {
				return -1;
			}
			if (arg0.equals(arg1))
				return 0;
			return getObjectString(arg0).compareTo(getObjectString(arg1));
		}

		private String getObjectString(Object o) {
			String s = o.getClass().getName() + "@"
					+ Integer.toHexString(o.hashCode());
			while (storedStrings.contains(s)) {
				s = s + 'X';
			}
			return s;
		}
	}

	protected AbstractStructuredGraphViewer(int graphStyle) {
		this.graphStyle = graphStyle;
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.nodeStyle = IZestGraphDefaults.NODE_STYLE;
	}

	/**
	 * Sets the default style for nodes in this graph. Note: if an input is set
	 * on the viewer, a ZestException will be thrown.
	 * 
	 * @param nodeStyle
	 *            the style for the nodes.
	 * @see #ZestStyles
	 */
	public void setNodeStyle(int nodeStyle) {
		if (getInput() != null)
			ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		this.nodeStyle = nodeStyle;
	}

	/**
	 * Sets the default style for connections in this graph. Note: if an input
	 * is set on the viewer, a ZestException will be thrown.
	 * 
	 * @param connectionStyle
	 *            the style for the connections.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int connectionStyle) {
		if (getInput() != null)
			ZestPlugin.error(ZestException.ERROR_CANNOT_SET_STYLE);
		if (!ZestStyles.validateConnectionStyle(connectionStyle))
			ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		this.connectionStyle = connectionStyle;
	}

	/**
	 * Returns the style set for the graph
	 * 
	 * @return The style set of the graph
	 */
	public int getGraphStyle() {
		return graphStyle;
	}

	/**
	 * Returns the style set for the nodes.
	 * 
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
	 * Sets the layout algorithm for this viewer. Subclasses may place
	 * restrictions on the algorithms that it accepts.
	 * 
	 * @param algorithm
	 *            the layout algorithm
	 * @param run
	 *            true if the layout algorithm should be run immediately. This
	 *            is a hint.
	 */
	public abstract void setLayoutAlgorithm(LayoutAlgorithm algorithm,
			boolean run);

	/**
	 * Gets the current layout algorithm.
	 * 
	 * @return the current layout algorithm.
	 */
	protected abstract LayoutAlgorithm getLayoutAlgorithm();

	/**
	 * Equivalent to setLayoutAlgorithm(algorithm, false).
	 * 
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		setLayoutAlgorithm(algorithm, false);
	}

	protected void handleDispose(DisposeEvent event) {
		if (model != null && !model.isDisposed()) {
			model.dispose();
		}
		super.handleDispose(event);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	protected void internalRefresh(Object element) {
		if (element == getInput())
			getFactory().refreshGraph(getModel());
		else
			getFactory().refresh(getModel(), element);
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		if (item == getModel()) {
			getFactory().update(getModel().getNodesArray());
			getFactory().update(getModel().getConnectionsArray());
		} else if (item instanceof IGraphItem) {
			getFactory().update((IGraphItem) item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	protected Widget doFindInputItem(Object element) {
		if (element == getInput())
			return getModel();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	protected Widget doFindItem(Object element) {
		Widget node = (GraphItem) getModel().getNodesMap().get(element);
		Widget connection = (GraphItem) getModel().getConnectionMap().get(
				element);
		return (node != null) ? node : connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	protected List getSelectionFromWidget() {
		List internalSelection = getWidgetSelection();
		LinkedList externalSelection = new LinkedList();
		for (Iterator i = internalSelection.iterator(); i.hasNext();) {
			// @tag zest.todo : should there be a method on IGraphItem to get
			// the external data?
			IGraphItem item = (IGraphItem) i.next();
			if (item instanceof IGraphModelNode) {
				externalSelection.add(((IGraphModelNode) item)
						.getExternalNode());
			} else if (item instanceof IGraphModelConnection) {
				externalSelection.add(((IGraphModelConnection) item)
						.getExternalConnection());
			} else if (item instanceof Widget) {
				externalSelection.add(((Widget) item).getData());
			}
		}
		return externalSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List,
	 *      boolean)
	 */
	protected void setSelectionToWidget(List l, boolean reveal) {
		EditPartViewer viewer = getEditPartViewer();
		List selection = new LinkedList();
		for (Iterator i = l.iterator(); i.hasNext();) {
			Object obj = i.next();
			IGraphModelNode node = getModel().getInternalNode(obj);
			IGraphModelConnection conn = getModel().getInternalConnection(obj);
			if (node != null) {
				selection.add(node);
			}
			if (conn != null) {
				selection.add(conn);
			}
		}
		viewer.setSelection(new StructuredSelection(selection));
	}

	/**
	 * Gets the internal model elements that are selected.
	 * 
	 * @return
	 */
	protected List getWidgetSelection() {
		List editParts = getEditPartViewer().getSelectedEditParts();
		List modelElements = new ArrayList();
		for (Iterator i = editParts.iterator(); i.hasNext();) {
			EditPart part = (EditPart) i.next();
			if (part.getModel() instanceof Widget)
				modelElements.add(part.getModel());
		}
		return modelElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object,
	 *      java.lang.Object)
	 */
	protected void inputChanged(Object input, Object oldInput) {
		IStylingGraphModelFactory factory = getFactory();
		factory.setConnectionStyle(getConnectionStyle());
		factory.setNodeStyle(getNodeStyle());
		GraphModel newModel = factory.createGraphModel();
		// get current list of nodes before they are re-created from the
		// factory & content provider
		Map oldNodesMap = (model != null ? model.getNodesMap()
				: Collections.EMPTY_MAP);

		if (model != null && !model.isDisposed()) {
			// Dispose the model
			model.dispose();
		}
		
		model = newModel;
		model.setNodeStyle(getNodeStyle());
		model.setConnectionStyle(getConnectionStyle());

		// check if any of the pre-existing nodes are still present
		// in this case we want them to keep the same location & size
		for (Iterator iter = oldNodesMap.keySet().iterator(); iter.hasNext();) {
			Object data = iter.next();
			IGraphModelNode newNode = model.getInternalNode(data);
			if (newNode != null) {
				IGraphModelNode oldNode = (IGraphModelNode) oldNodesMap
						.get(data);
				newNode.setPreferredLocation(oldNode.getXInLayout(), oldNode
						.getYInLayout());
				newNode.setSizeInLayout(oldNode.getWidthInLayout(), oldNode
						.getHeightInLayout());
			}
		}
		getEditPartViewer().setContents(model);
		applyLayout();
	}

	/**
	 * Returns the factory used to create the model. This must not be called
	 * before the content provider is set.
	 * 
	 * @return
	 */
	protected abstract IStylingGraphModelFactory getFactory();

	protected void filterVisuals() {
		if (getModel() == null)
			return;
		Object[] filtered = getFilteredChildren(getInput());
		SimpleGraphComparator comparator = new SimpleGraphComparator();
		TreeSet filteredElements = new TreeSet(comparator);
		TreeSet unfilteredElements = new TreeSet(comparator);
		List connections = getModel().getConnections();
		List nodes = getModel().getNodes();
		if (filtered.length == 0) {
			// set everything to invisible.
			// @tag zest.bug.156528-Filters.check : should we only filter out
			// the nodes?
			for (Iterator i = connections.iterator(); i.hasNext();) {
				IGraphModelConnection c = (IGraphModelConnection) i.next();
				c.setVisible(false);
			}
			for (Iterator i = nodes.iterator(); i.hasNext();) {
				IGraphModelNode n = (IGraphModelNode) i.next();
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
			IGraphModelNode n = (IGraphModelNode) i.next();
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
		// set all the elements that did not pass the filters to invisible, and
		// all the elements that passed to visible.
		while (unfilteredElements.size() > 0) {
			IGraphItem i = (IGraphItem) unfilteredElements.first();
			i.setVisible(false);
			unfilteredElements.remove(i);
		}
		while (filteredElements.size() > 0) {
			IGraphItem i = (IGraphItem) filteredElements.first();
			i.setVisible(true);
			filteredElements.remove(i);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getRawChildren(java.lang.Object)
	 */
	protected Object[] getRawChildren(Object parent) {
		if (parent == getInput()) {
			// get the children from the model.
			LinkedList children = new LinkedList();
			if (getModel() != null) {
				List connections = getModel().getConnections();
				List nodes = getModel().getNodes();
				for (Iterator i = connections.iterator(); i.hasNext();) {
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
	 * 
	 * @return the internal graph model.
	 */
	protected final GraphModel getModel() {
		if (model == null)
			createModel();
		return model;
	}

	protected final GraphModel createModel() {
		this.model = getFactory().createGraphModel();
		return model;
	}

	/**
	 * Returns the underlying editpart viewer for this viewer.
	 * 
	 * @return the underlying editpart viewer for this viewer.
	 */
	protected abstract EditPartViewer getEditPartViewer();

	/**
	 * Removes the given connection object from the layout algorithm and the
	 * model.
	 * 
	 * @param connection
	 */
	public void removeRelationship(Object connection) {
		IGraphModelConnection relation = model
				.getInternalConnection(connection);

		if (relation != null) {
			// remove the relationship from the layout algorithm
			if (getLayoutAlgorithm() != null)
				getLayoutAlgorithm().removeRelationship(relation);
			// remove the relationship from the model
			getModel().removeConnection(relation);
			applyLayout();
		}
	}

	/**
	 * Creates a new node and adds it to the graph. If it already exists nothing
	 * happens.
	 * 
	 * @param newNode
	 */
	public void addNode(Object element) {
		if (model.getInternalNode(element) == null) {
			// create the new node
			IGraphModelNode newNode = getFactory().createNode(model, element);

			// add it to the layout algorithm
			if (getLayoutAlgorithm() != null)
				getLayoutAlgorithm().addEntity(newNode);
			applyLayout();
		}
	}

	/**
	 * Removes the given element from the layout algorithm and the model.
	 * 
	 * @param element
	 *            The node element to remove.
	 */
	public void removeNode(Object element) {
		IGraphModelNode node = model.getInternalNode(element);

		if (node != null) {
			// remove the node from the layout algorithm and all the connections
			if (getLayoutAlgorithm() != null) {
				getLayoutAlgorithm().removeEntity(node);
				getLayoutAlgorithm().removeRelationships(
						node.getSourceConnections());
				getLayoutAlgorithm().removeRelationships(
						node.getTargetConnections());
			}

			// remove the node and it's connections from the model
			getModel().removeNode(node);
			applyLayout();
		}
	}

	/**
	 * Creates a new relationship between the source node and the destination
	 * node. If either node doesn't exist then it will be created.
	 * 
	 * @param connection
	 *            The connection data object.
	 * @param srcNode
	 *            The source node data object.
	 * @param destNode
	 *            The destination node data object.
	 */
	public void addRelationship(Object connection, Object srcNode,
			Object destNode) {
		// create the new relationship
		IStylingGraphModelFactory modelFactory = getFactory();
		IGraphModelConnection newConnection = modelFactory.createConnection(
				model, connection, srcNode, destNode);

		// add it to the layout algorithm
		if (getLayoutAlgorithm() != null)
			getLayoutAlgorithm().addRelationship(newConnection);
		applyLayout();
	}

	/**
	 * Adds a new relationship given the connection. It will use the content
	 * provider to determine the source and destination nodes.
	 * 
	 * @param connection
	 *            The connection data object.
	 */
	public void addRelationship(Object connection) {
		IStylingGraphModelFactory modelFactory = getFactory();
		if (model.getInternalConnection(connection) == null) {
			if (modelFactory.getContentProvider() instanceof IGraphContentProvider) {
				IGraphContentProvider content = ((IGraphContentProvider) modelFactory
						.getContentProvider());
				Object source = content.getSource(connection);
				Object dest = content.getDestination(connection);
				// create the new relationship
				IGraphModelConnection newConnection = modelFactory
						.createConnection(model, connection, source, dest);
				// add it to the layout algorithm
				if (getLayoutAlgorithm() != null)
					getLayoutAlgorithm().addRelationship(newConnection);
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

}
