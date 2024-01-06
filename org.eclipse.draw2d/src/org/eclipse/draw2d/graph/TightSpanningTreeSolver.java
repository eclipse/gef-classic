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

/**
 * Finds a tight spanning tree from the graphs edges which induce a valid rank
 * assignment. This process requires that the nodes be initially given a
 * feasible ranking.
 *
 * @author Randy Hudson
 * @since 2.1.2
 */
class TightSpanningTreeSolver extends SpanningTreeVisitor {

	protected DirectedGraph graph;
	protected CandidateList candidates = new CandidateList();

	static final class CandidateList {
		private Edge[] edges = new Edge[10];
		private int size;

		public void add(Edge edge) {
			if (size == edges.length - 1) {
				Edge[] newEdges = new Edge[edges.length * 2];
				System.arraycopy(edges, 0, newEdges, 0, edges.length);
				edges = newEdges;
			}
			edges[size++] = edge;
		}

		public Edge getEdge(int index) {
			return edges[index];
		}

		public void remove(Edge edge) {
			for (int i = 0; i < size; i++) {
				if (edges[i] == edge) {
					edges[i] = edges[size - 1];
					size--;
					return;
				}
			}
			throw new RuntimeException("Remove called on invalid Edge"); //$NON-NLS-1$
		}

		public int size() {
			return size;
		}
	}

	protected NodeList members = new NodeList();

	@Override
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

	private static boolean isNodeReachable(Node node) {
		return node.flag;
	}

	private static void setNodeReachable(Node node) {
		node.flag = true;
	}

	private static boolean isCandidate(Edge e) {
		return e.flag;
	}

	private static void setCandidate(Edge e) {
		e.flag = true;
	}

	void addNode(Node node) {
		setNodeReachable(node);

		for (Edge e : node.incoming) {
			if (!isNodeReachable(e.source)) {
				if (!isCandidate(e)) {
					setCandidate(e);
					candidates.add(e);
				}
			} else {
				candidates.remove(e);
			}
		}

		for (Edge e : node.outgoing) {
			if (!isNodeReachable(e.target)) {
				if (!isCandidate(e)) {
					setCandidate(e);
					candidates.add(e);
				}
			} else {
				candidates.remove(e);
			}
		}
		members.add(node);
	}

	void init() {
		graph.edges.resetFlags(true);
		graph.nodes.resetFlags();
		for (Node node : graph.nodes) {
			node.workingData[0] = new EdgeList();
		}
	}

	protected void solve() {
		Node root = graph.nodes.get(0);
		setParentEdge(root, null);
		addNode(root);
		while (members.size() < graph.nodes.size()) {
			if (candidates.size() == 0) {
				throw new RuntimeException("graph is not fully connected");//$NON-NLS-1$
			}
			int minSlack = Integer.MAX_VALUE;
			Edge minEdge = null;
			for (int i = 0; i < candidates.size() && minSlack > 0; i++) {
				Edge edge = candidates.getEdge(i);
				int slack = edge.getSlack();
				if (slack < minSlack) {
					minSlack = slack;
					minEdge = edge;
				}
			}
			addEdge(minEdge);
		}
		graph.nodes.normalizeRanks();
	}

}
