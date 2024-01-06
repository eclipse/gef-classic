/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Assigns a valid rank assignment to all nodes based on their edges. The
 * assignment is not optimal in that it does not provide the minimum global
 * length of edge lengths.
 *
 * @author Randy Hudson
 * @since 2.1.2
 */
class InitialRankSolver extends GraphVisitor {

	protected DirectedGraph graph;
	protected EdgeList candidates = new EdgeList();
	protected NodeList members = new NodeList();

	@Override
	public void visit(DirectedGraph graph) {
		this.graph = graph;
		graph.edges.resetFlags(false);
		graph.nodes.resetFlags();
		solve();
	}

	protected void solve() {
		if (graph.nodes.isEmpty()) {
			return;
		}
		NodeList unranked = new NodeList(graph.nodes);
		NodeList rankMe = new NodeList();
		while (!unranked.isEmpty()) {
			rankMe.clear();
			for (int i = 0; i < unranked.size();) {
				Node node = unranked.get(i);
				if (node.incoming.isCompletelyFlagged()) {
					rankMe.add(node);
					unranked.remove(i);
				} else {
					i++;
				}
			}
			if (rankMe.isEmpty()) {
				throw new RuntimeException("Cycle detected in graph"); //$NON-NLS-1$
			}
			for (Node node : rankMe) {
				assignMinimumRank(node);
				node.outgoing.setFlags(true);
			}
		}

		connectForest();
	}

	private void connectForest() {
		List<NodeList> forest = new ArrayList<>();
		Deque<Node> stack = new ArrayDeque<>();
		graph.nodes.resetFlags();
		for (Node n : graph.nodes) {
			if (n.flag) {
				continue;
			}
			NodeList tree = new NodeList();
			stack.push(n);
			while (!stack.isEmpty()) {
				n = stack.pop();
				n.flag = true;
				tree.add(n);
				for (Edge e : n.incoming) {
					Node neighbor = e.source;
					if (!neighbor.flag) {
						stack.push(neighbor);
					}
				}
				for (Edge e : n.outgoing) {
					Node neighbor = e.target;
					if (!neighbor.flag) {
						stack.push(neighbor);
					}
				}
			}
			forest.add(tree);
		}

		if (forest.size() > 1) {
			// connect the forest
			graph.forestRoot = new Node("the forest root"); //$NON-NLS-1$
			graph.nodes.add(graph.forestRoot);
			for (NodeList tree : forest) {
				graph.edges.add(new Edge(graph.forestRoot, tree.get(0), 0, 0));
			}
		}
	}

	private static void assignMinimumRank(Node node) {
		int rank = 0;
		for (Edge e : node.incoming) {
			rank = Math.max(rank, e.getDelta() + e.source.rank);
		}
		node.rank = rank;
	}

}
