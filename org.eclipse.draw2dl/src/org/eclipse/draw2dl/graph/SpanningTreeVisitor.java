/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.graph;

/**
 * A base class for visitors which operate on the graphs spanning tree used to
 * induce rank assignments.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
abstract class SpanningTreeVisitor extends org.eclipse.draw2dl.graph.GraphVisitor {

	org.eclipse.draw2dl.graph.Edge getParentEdge(org.eclipse.draw2dl.graph.Node node) {
		return (org.eclipse.draw2dl.graph.Edge) node.workingData[1];
	}

	org.eclipse.draw2dl.graph.EdgeList getSpanningTreeChildren(org.eclipse.draw2dl.graph.Node node) {
		return (EdgeList) node.workingData[0];
	}

	protected org.eclipse.draw2dl.graph.Node getTreeHead(org.eclipse.draw2dl.graph.Edge edge) {
		if (getParentEdge(edge.source) == edge)
			return edge.target;
		return edge.source;
	}

	org.eclipse.draw2dl.graph.Node getTreeParent(org.eclipse.draw2dl.graph.Node node) {
		org.eclipse.draw2dl.graph.Edge e = getParentEdge(node);
		if (e == null)
			return null;
		return e.opposite(node);
	}

	protected org.eclipse.draw2dl.graph.Node getTreeTail(org.eclipse.draw2dl.graph.Edge edge) {
		if (getParentEdge(edge.source) == edge)
			return edge.source;
		return edge.target;
	}

	void setParentEdge(Node node, Edge edge) {
		node.workingData[1] = edge;
	}

}
