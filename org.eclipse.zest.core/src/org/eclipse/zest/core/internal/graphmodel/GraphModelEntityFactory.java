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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.swt.widgets.Canvas;


/**
 * 
 * @author Ian Bull
 */
public class GraphModelEntityFactory extends AbstractStylingModelFactory {

	
	public GraphModelEntityFactory(StructuredViewer viewer) {
		super(viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#createGraphModel()
	 */
	public GraphModel createGraphModel() {
		GraphModel model = new GraphModel((Canvas) getViewer().getControl());
		doBuildGraph(model);
		return model;
	}
	
	
	public void doBuildGraph(GraphModel model) {
		clearGraph(model);
		model.setConnectionStyle(getConnectionStyle());
		model.setNodeStyle(getNodeStyle());
		Object inputElement = getViewer().getInput();
		Object entities[] = getContentProvider().getElements( inputElement );
		if ( entities == null ) return;
		entities = filter(inputElement, entities);
		for ( int i = 0; i < entities.length; i++ ) {
			Object data = entities[ i ];
			createNode(model, data);
		}
		for ( int i=0; i < entities.length; i++ ) {
			Object data = entities[ i ];
			Object[] related = ((IGraphEntityContentProvider)getContentProvider()).getConnectedTo( data );
			if ( related != null )
				for ( int j = 0; j < related.length; j++ ) {
					createConnection(model, new EntityConnectionData(data, related[j]), data, related[j]);
				}
		}	
	}


	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public void refresh(GraphModel graph, Object element, boolean refreshLabels) {
		if (element == null) return;
		IGraphModelNode node = graph.getInternalNode(element);
		if (node == null) {
			//check to make sure that the user didn't send us an edge.
			IGraphModelConnection conn = graph.getInternalConnection(element);
			if (conn != null) {
				//refresh on the connected nodes.
				refresh(graph, conn.getSource().getExternalNode(), refreshLabels);
				refresh(graph, conn.getDestination().getExternalNode(), refreshLabels);
				return;
			}
		}
		//can only refresh on nodes in this kind of factory.
		if (node == null) {
			//do nothing
			return;
		}
		reconnect(graph, element, refreshLabels);
		
		if (refreshLabels) {
			update(node);
			for (Iterator it = node.getSourceConnections().iterator(); it.hasNext();) {
				update((IGraphItem) it.next());
			}
			for (Iterator it = node.getTargetConnections().iterator(); it.hasNext();) {
				update((IGraphItem) it.next());
			}
		}
	}


	/**
	 * @param graph
	 * @param element
	 * @param refreshLabels
	 */
	private void reconnect(GraphModel graph, Object element, boolean refreshLabels) {
		IGraphModelNode node = graph.getInternalNode(element);
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

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, boolean)
	 */
	public void refresh(GraphModel graph, Object element) {
		refresh(graph, element, false);		
	}
	
}
