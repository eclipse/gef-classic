/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

/**
 * Finds a tight spanning tree from the graphs edges which induce a valid rank assignment.
 * This process requires that the nodes be initially given a feasible ranking.
 * @author Randy Hudson
 * @since 2.1.2
 */
public class TightSpanningTreeSolver extends SpanningTreeVisitor {

protected DirectedGraph graph;
protected EdgeList candidates = new EdgeList();
protected NodeList members = new NodeList();

public void visit(DirectedGraph graph) {
	this.graph = graph;
	init();
	solve();
}

Node addEdge(Edge edge) {
	int delta = edge.getSlack();
	edge.tree = true;
	Node node;
	if (edge.target.flag) {
		delta = -delta;
		node = edge.source;
		setParentEdge(node, edge);
		getSpanningTreeChildren(edge.target).add(edge);
	} else {
		node = edge.target;
		setParentEdge(node, edge);
		getSpanningTreeChildren(edge.source).add(edge);
	}
	members.adjustRank(delta);
	addNode(node);
	return node;
}

void addNode(Node node) {
	node.flag = true;
	EdgeList list = node.incoming;
	Edge e;
	for (int i = 0; i < list.size(); i++) {
		e = list.getEdge(i);
		if (!e.source.flag) {
			if (!candidates.contains(e))
				candidates.add(e);
		} else
			candidates.remove(e);
	}

	list = node.outgoing;
	for (int i = 0; i < list.size(); i++) {
		e = list.getEdge(i);
		if (!e.target.flag) {
			if (!candidates.contains(e))
				candidates.add(e);
		} else
			candidates.remove(e);
	}
	members.add(node);
}

void init() {
	graph.edges.resetFlags();
	graph.nodes.resetFlags();
	for (int i = 0; i < graph.nodes.size(); i++) {
		Node node = (Node)graph.nodes.get(i);
		node.workingData[0] = new EdgeList();
	}
}

protected void solve() {
	Node root = graph.nodes.getNode(0);
	setParentEdge(root, null);
	addNode(root);
	List nonMembers = new ArrayList(graph.nodes);
	while (members.size() < graph.nodes.size()) {
		if (candidates.size() == 0)
			throw new RuntimeException("graph is not fully connected");//$NON-NLS-1$
		int minSlack = Integer.MAX_VALUE, slack;
		Edge minEdge = null, edge;
		for (int i = 0; i < candidates.size() && minSlack > 0; i++) {
			edge = candidates.getEdge(i);
			slack = edge.getSlack();
			if (slack < minSlack) {
				minSlack = slack;
				minEdge = edge;
			}
		}
		Node added = addEdge(minEdge);
		nonMembers.remove(added);
	}
	graph.nodes.normalizeRanks();
}

}
