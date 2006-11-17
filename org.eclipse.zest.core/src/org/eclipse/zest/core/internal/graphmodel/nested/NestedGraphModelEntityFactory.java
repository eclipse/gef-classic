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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.messages.ZestUIMessages;
import org.eclipse.mylar.zest.core.viewers.EntityConnectionData;
import org.eclipse.mylar.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.mylar.zest.core.viewers.INestedGraphEntityContentProvider;
import org.eclipse.swt.widgets.Canvas;


/**
 * A factory for creating nodes and relationships for nested graphs.
 * 
 * @author Chris Callendar
 */
//@tag bug(151327-Styles(todo)) : Use GraphItemStyler to style the graphs that are created.
//@tag bug.160367-Refreshing.fix : Updated to use the new AbstractStylingModelFactory
public class NestedGraphModelEntityFactory extends AbstractStylingModelFactory {

		
	public NestedGraphModelEntityFactory(StructuredViewer viewer) {
		super(viewer);
	}
	
	
	private INestedGraphEntityContentProvider getCastedContent() {
		return (INestedGraphEntityContentProvider)getContentProvider();
	}
		
	//@tag bug(153348-NestedStyle(fix)) : add nodestyle and connectionstyle so that the model can be updated.
//	@tag bug(154412-ClearStatic(fix)) : renamed to allow the parent to do some processing before the model is created.
	public GraphModel createGraphModel() {
		NestedGraphModel model = new NestedGraphModel((Canvas)getViewer().getControl());
		doBuildGraph(model);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#doBuildGraph(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel)
	 */
	protected void doBuildGraph(GraphModel graph) {
		NestedGraphModel model = (NestedGraphModel) graph;
		clearGraph(model);
		model.setConnectionStyle(getConnectionStyle());
		model.setNodeStyle(getNodeStyle());
		
		// add the root element
		// TODO this maybe should be optional...
		NestedGraphModelNode rootNode = new NestedGraphModelNode(model, null);
		rootNode.setData(rootNode);
		rootNode.setHighlightAdjacentNodes(true);
		rootNode.setText(ZestUIMessages.VIEW_NESTED_TOP_NODE);
		model.addNode(rootNode.getData(), rootNode);
		
		rootNode.setSizeInLayout(-1, -1);
		
		model.setRootNode(rootNode);
		
		Object entities[] = getContentProvider().getElements(getViewer().getInput());
		entities = filter(getViewer().getInput(), entities);
		// add all root the entities and recursively add their children
		if (entities != null) {
			for ( int i = 0; i < entities.length; i++ ) {
				Object data = entities[i];
				NestedGraphModelNode node = (NestedGraphModelNode) createNode(model,data);
				node.setParent(rootNode);
				rootNode.addChild(node);
				node.setSizeInLayout(100, 100);
				addChildNodes(model, data);
			}
			NestedGraphModelNode node = model.getRootNode();
			//relationships have to be created only after all nodes have been
			//otherwise, we can't connect nested nodes.
			recursiveCreateRelationships(node);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#createNode(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public IGraphModelNode createNode(GraphModel graph, Object element) {
		NestedGraphModelNode node = getNode((NestedGraphModel)graph, element);
		if (node == null) {
			node = new NestedGraphModelNode((NestedGraphModel)graph,element);
			// add the parent
			Object parent = getCastedContent().getParent(element);
			if (parent != null) {
				NestedGraphModelNode parentNode = getNode((NestedGraphModel)graph, parent);
				if (parentNode != null) {
					node.setParent(parentNode);
					parentNode.addChild(node);
				}
			}
			// add it to the model (must be done after adding the parent!)
			graph.addNode(element, node);
		}
		styleItem(node);
		return node;
	}
	
	/**
	 * @param node
	 */
	private void recursiveCreateRelationships(NestedGraphModelNode node) {
		//add the sibling, children and parent relationships
		Object data = node.getData();
		NestedGraphModel model = node.getNestedGraphModel();
		Object[] related = getCastedContent().getConnectedTo(data);
		if ( related != null ) {
			for ( int i = 0; i < related.length; i++ ) {
				createConnection(model, new EntityConnectionData(data, related[i]), data, related[i]);
			}
		}
		for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
			recursiveCreateRelationships((NestedGraphModelNode) it.next());
		}
	}

	/**
	 * Recusively adds the children nodes for the given data object.  The children
	 * are gotten from the content provider.
	 * @param model
	 * @param data
	 */
	private void addChildNodes(NestedGraphModel model, Object data) {
		Object[] childrenData = getCastedContent().getChildren(data);
		childrenData = filter(data, childrenData);
		if (childrenData != null) {
			for (int j = 0; j < childrenData.length; j++) {
				Object childData = childrenData[j];
				NestedGraphModelNode node = (NestedGraphModelNode) createNode(model, childData);
				node.setSizeInLayout(100, 100);
//				@tag bug(153348-NestedStyle(fix))
				addChildNodes(model, childData);
			}
		}
	}


	
	private NestedGraphModelNode getNode(NestedGraphModel model, Object data) {
		NestedGraphModelNode node = (NestedGraphModelNode)model.getInternalNode(data);
		return node;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, boolean)
	 */
	public void refresh(GraphModel graph, Object element, boolean updateLabels) {
		NestedGraphModel model = (NestedGraphModel) graph;
		NestedGraphModelNode node = (NestedGraphModelNode) model.getInternalNode(element);
		if (node == null) {
			IGraphModelConnection conn = model.getInternalConnection(element);
			if (conn != null) {
				refresh(graph, conn.getSource().getExternalNode(), updateLabels);
				refresh(graph, conn.getDestination().getExternalNode(), updateLabels);
			}
			return;
		}
		
		
		Map nodesMap = graph.getNodesMap();
//		remove all of the child nodes.
		clearChildren(node);
		addChildNodes(model, element);
		reconnect(node, updateLabels);
		//update the positions on the new nodes to match the old ones.
		List nodes = getAllChildren(node);
		
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			IGraphModelNode next = (IGraphModelNode) i.next();
			IGraphModelNode oldNode = (IGraphModelNode) nodesMap.get(next.getExternalNode());
			if (oldNode != null) {
				next.setPreferredLocation(oldNode.getXInLayout(), oldNode.getYInLayout());
			}
			reconnect(next, updateLabels);
		}
		
	}
	
	/**
	 * @param graph
	 * @param element
	 * @param refreshLabels
	 */
	private void reconnect(IGraphModelNode node, boolean refreshLabels) {
		GraphModel graph = node.getGraphModel();
		Object element = node.getExternalNode();
		Object[] related = ((IGraphEntityContentProvider)getContentProvider()).getConnectedTo(element);
		List connections = node.getSourceConnections();
		LinkedList toAdd = new LinkedList();
		LinkedList toDelete = new LinkedList();
		LinkedList toKeep = new LinkedList();
		HashSet oldExternalConnections = new HashSet();
		HashSet newExternalConnections = new HashSet();
		for (Iterator it = connections.iterator(); it.hasNext();) {
			oldExternalConnections.add(((IGraphModelConnection)it.next()).getExternalConnection());
		}
		for (int i = 0; i < related.length; i++) {
			newExternalConnections.add(new EntityConnectionData(element, related[i]));
		}
		for (Iterator it = oldExternalConnections.iterator(); it.hasNext();) {
			Object next = it.next();
			if (!newExternalConnections.contains(next)) {
				toDelete.add(next);
			} else {
				toKeep.add(next);
			}
		}
		for (Iterator it = newExternalConnections.iterator(); it.hasNext();) {
			Object next = it.next();
			if (!oldExternalConnections.contains(next)) {
				toAdd.add(next);
			}
		}
		for (Iterator it = toDelete.iterator(); it.hasNext();) {
			graph.removeConnection(it.next());
		}
		toDelete.clear();
		LinkedList newNodeList = new LinkedList();
		for (Iterator it = toAdd.iterator(); it.hasNext();) {
			EntityConnectionData data = (EntityConnectionData) it.next();
			IGraphModelNode dest = graph.getInternalNode(data.dest);
			if (dest == null) {
				newNodeList.add(data.dest);
			}
			createConnection(graph, data, data.source, data.dest);
		}
		toAdd.clear();
		if (refreshLabels) {
			for (Iterator i = toKeep.iterator(); i.hasNext();) {
				styleItem(graph.getInternalConnection(i.next()));
			}
		}
		for (Iterator it = newNodeList.iterator(); it.hasNext();) {
			//refresh the new nodes so that we get a fully-up-to-date graph.
			refresh(graph, it.next());
		}
	}



	/**
	 * @param node
	 * @return
	 */
	private List getAllChildren(NestedGraphModelNode node) {
		LinkedList nodes = new LinkedList();
		nodes.addAll(node.getChildren());
		for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
			nodes.addAll(getAllChildren((NestedGraphModelNode) it.next()));
		}
		return nodes;
	}


	/**
	 * @param node
	 */
	private void clearChildren(NestedGraphModelNode node) {
		List children = node.getChildren();
		for (Iterator it = children.iterator(); it.hasNext();) {
			NestedGraphModelNode child = (NestedGraphModelNode) it.next();
			clearChildren(child);
		}
		node.setParent(null);
		node.getParent().getChildren().remove(node);
		node.getGraphModel().removeNode(node);
	}
		
}
