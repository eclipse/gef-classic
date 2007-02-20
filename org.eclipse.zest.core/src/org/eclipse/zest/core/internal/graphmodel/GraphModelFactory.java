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
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.viewers.IGraphContentProvider;
import org.eclipse.swt.widgets.Canvas;



/**
 * This factory helps make models (nodes & connections).
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class GraphModelFactory extends AbstractStylingModelFactory {


		
	public GraphModelFactory(StructuredViewer viewer) {
		super(viewer);
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.IGraphModelFactory#createModel()
	 */
	public GraphModel createGraphModel() {
		GraphModel model = new GraphModel((Canvas)getViewer().getControl());
		doBuildGraph(model);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.AbstractStylingModelFactory#doBuildGraph(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel)
	 */
	protected void doBuildGraph(GraphModel model) {
		super.doBuildGraph(model);
		//make the model have the same styles as the viewer
		Object rels[] = getContentProvider().getElements(getViewer().getInput());
		if ( rels != null ) {
			// If rels returns null then just continue
			// @tag zest(bug(134928(fix))) : An empty graph causes an NPE
			for ( int i = 0; i < rels.length; i++ ) {
				// Check the filter on the source
				Object source = getCastedContent().getSource(rels[i]);
				source = filterElement(getViewer().getInput(),source) ? null : source;
				
				// Check hte filter on the dest
				Object dest = getCastedContent().getDestination(rels[i]);
				dest = filterElement(getViewer().getInput(),dest) ? null : dest;
				if (source == null) {
					//just create the node for the destination
					if (dest != null) createNode(model, dest);
					continue;
				} else if (dest == null) {
					//just create the node for the source
					if (source != null) createNode(model, source);
					continue;
				}
				// If any of the source, dest is null or the edge is filtered, don't create the graph.
				if ( source != null && dest != null && !filterElement(getViewer().getInput(), rels[i])) {
					createConnection(model, rels[i], getCastedContent().getSource(rels[i]), getCastedContent().getDestination(rels[i]));
				}
			}
		}
		
		
	}

	private IGraphContentProvider getCastedContent() {
		return (IGraphContentProvider)getContentProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object)
	 */
	public void refresh(GraphModel graph, Object element) {
		refresh(graph, element, false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IStylingGraphModelFactory#refresh(org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel, java.lang.Object, boolean)
	 */
	public void refresh(GraphModel graph, Object element, boolean updateLabels) {
		IGraphModelConnection conn = graph.getInternalConnection(element);
		if (conn == null) {
			//did the user send us a node? Check all of the connections on the node.
			IGraphModelNode node = graph.getInternalNode(element);
			if (node != null) {
				List connections = node.getSourceConnections();
				for (Iterator it = connections.iterator(); it.hasNext();) {
					IGraphModelConnection c = (IGraphModelConnection) it.next();
					refresh(graph, c.getExternalConnection(), updateLabels);
				}
				connections = node.getTargetConnections();
				for (Iterator it = connections.iterator(); it.hasNext();) {
					IGraphModelConnection c = (IGraphModelConnection) it.next();
					refresh(graph, c.getExternalConnection(), updateLabels);
				}
			}
			return;
		}
		Object oldSource = conn.getSource().getExternalNode();
		Object oldDest = conn.getDestination().getExternalNode();
		Object newSource = getCastedContent().getSource(element);
		Object newDest = getCastedContent().getDestination(element);
		if (!(oldSource.equals(newSource) && oldDest.equals(newDest))) {
			IGraphModelNode internalSource = graph.getInternalNode(newSource);
			IGraphModelNode internalDest = graph.getInternalNode(newDest);
			if (internalSource == null) {
				internalSource = createNode(graph, newSource);
			} else if (updateLabels) {
				styleItem(internalSource);	
			}
			if (internalDest == null) {
				internalDest = createNode(graph, newDest);
			} else if (updateLabels) {
				styleItem(internalDest);
			}
			
			conn.disconnect();
			conn.reconnect(internalSource, internalDest);
			if (updateLabels) {
				styleItem(conn);
			}
		}
		
	}



}
