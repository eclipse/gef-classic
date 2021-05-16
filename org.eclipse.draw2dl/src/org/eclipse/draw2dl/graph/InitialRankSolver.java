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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Assigns a valid rank assignment to all nodes based on their edges. The
 * assignment is not optimal in that it does not provide the minimum global
 * length of edge lengths.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class InitialRankSolver extends org.eclipse.draw2dl.graph.GraphVisitor {

	protected org.eclipse.draw2dl.graph.DirectedGraph graph;
	protected org.eclipse.draw2dl.graph.EdgeList candidates = new EdgeList();
	protected org.eclipse.draw2dl.graph.NodeList members = new org.eclipse.draw2dl.graph.NodeList();

	public void visit(DirectedGraph graph) {
		this.graph = graph;
		graph.edges.resetFlags(false);
		graph.nodes.resetFlags();
		solve();
	}

	protected void solve() {
		if (graph.nodes.size() == 0)
			return;
		org.eclipse.draw2dl.graph.NodeList unranked = new org.eclipse.draw2dl.graph.NodeList(graph.nodes);
		org.eclipse.draw2dl.graph.NodeList rankMe = new org.eclipse.draw2dl.graph.NodeList();
		org.eclipse.draw2dl.graph.Node node;
		int i;
		while (!unranked.isEmpty()) {
			rankMe.clear();
			for (i = 0; i < unranked.size();) {
				node = unranked.getNode(i);
				if (node.incoming.isCompletelyFlagged()) {
					rankMe.add(node);
					unranked.remove(i);
				} else
					i++;
			}
			if (rankMe.size() == 0)
				throw new RuntimeException("Cycle detected in graph"); //$NON-NLS-1$
			for (i = 0; i < rankMe.size(); i++) {
				node = rankMe.getNode(i);
				assignMinimumRank(node);
				node.outgoing.setFlags(true);
			}
		}

		connectForest();
	}

	private void connectForest() {
		List forest = new ArrayList();
		Stack stack = new Stack();
		org.eclipse.draw2dl.graph.NodeList tree;
		graph.nodes.resetFlags();
		for (int i = 0; i < graph.nodes.size(); i++) {
			org.eclipse.draw2dl.graph.Node neighbor, n = graph.nodes.getNode(i);
			if (n.flag)
				continue;
			tree = new org.eclipse.draw2dl.graph.NodeList();
			stack.push(n);
			while (!stack.isEmpty()) {
				n = (org.eclipse.draw2dl.graph.Node) stack.pop();
				n.flag = true;
				tree.add(n);
				for (int s = 0; s < n.incoming.size(); s++) {
					neighbor = n.incoming.getEdge(s).source;
					if (!neighbor.flag)
						stack.push(neighbor);
				}
				for (int s = 0; s < n.outgoing.size(); s++) {
					neighbor = n.outgoing.getEdge(s).target;
					if (!neighbor.flag)
						stack.push(neighbor);
				}
			}
			forest.add(tree);
		}

		if (forest.size() > 1) {
			// connect the forest
			graph.forestRoot = new org.eclipse.draw2dl.graph.Node("the forest root"); //$NON-NLS-1$
			graph.nodes.add(graph.forestRoot);
			for (int i = 0; i < forest.size(); i++) {
				tree = (NodeList) forest.get(i);
				graph.edges.add(new org.eclipse.draw2dl.graph.Edge(graph.forestRoot, tree.getNode(0), 0,
						0));
			}
		}
	}

	private void assignMinimumRank(Node node) {
		int rank = 0;
		Edge e;
		for (int i1 = 0; i1 < node.incoming.size(); i1++) {
			e = node.incoming.getEdge(i1);
			rank = Math.max(rank, e.delta + e.source.rank);
		}
		node.rank = rank;
	}

}
