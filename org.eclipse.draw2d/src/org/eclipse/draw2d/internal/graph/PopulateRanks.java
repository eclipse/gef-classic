/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import org.eclipse.draw2d.graph.*;

/**
 * This class takes a DirectedGraph with an optimal rank assigment and a spanning tree,
 * and populates the ranks of the DirectedGraph. Virtual nodes are inserted for edges that
 * span 1 or more ranks.
 * <P>
 * Ranks are populated using a pre-order depth-first traversal of the spanning tree. For
 * each node, all edges requiring virtual nodes are added to the ranks.
 * @author hudsonr
 * @since 2.1
 */
public class PopulateRanks extends GraphVisitor {

private DirectedGraph g;

/**
 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	this.g = g;
	g.ranks = new RankList();
	for (int i = 0; i < g.nodes.size(); i++) {
		Node node = g.nodes.getNode(i);
		g.ranks.getRank(node.rank).add(node);
	}
	for (int i = 0; i < g.nodes.size(); i++) {
		Node node = g.nodes.getNode(i);
		for (int j = 0; j < node.outgoing.size(); j++) {
			Edge e = node.outgoing.getEdge(j);
			if (e.getLength() > 1)
				addVirtualNodes(e);
		}
	}
}

void addVirtualNodes(Edge e) {
	int start = e.source.rank + 1;
	int end = e.target.rank;
	VirtualNode previous = null;
	VirtualNode current = null;
	e.vNodes = new NodeList();
	for (int i = start; i < end; i++) {
		current = createVirtualNode(e, i);
		if (previous == null)
			current.prev = e.source;
		else {
			previous.next = current;
			current.prev = previous;
		}
		g.ranks.getRank(i).add(current);
		e.vNodes.add(current);
		previous = current;
	}
	current.next = e.target;
}

VirtualNode createVirtualNode(Edge e, int i) {
	return new VirtualNode(e, i);
}

//protected void recursivePopulate(Node n) {
//	g.ranks.getRank(n.rank).add(n);
//	Edge e;
//	for (int i = 0; i < n.incoming.size(); i++) {
//		e = n.incoming.getEdge(i);
//		if (!e.flag && e.span() > 1) {
//			addVirtualNodes(e);
//			e.flag = true;
//		}
//	}
//	for (int i = 0; i < n.outgoing.size(); i++) {
//		e = n.outgoing.getEdge(i);
//		if (!e.flag && e.span() > 1) {
//			addVirtualNodes(e);
//			e.flag = true;
//		}
//	}
//	for (int i = 0; i < n.spanTreeChildren.size(); i++) {
//		recursivePopulate(n.spanTreeChildren.getEdge(i).tail());
//	}
//}

}
