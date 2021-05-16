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
 * Finds a tight spanning tree from the graphs edges which induce a valid rank
 * assignment. This process requires that the nodes be initially given a
 * feasible ranking.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class TightSpanningTreeSolver extends org.eclipse.draw2dl.graph.SpanningTreeVisitor {

	protected org.eclipse.draw2dl.graph.DirectedGraph graph;
	protected CandidateList candidates = new CandidateList();

	static final class CandidateList {
		private org.eclipse.draw2dl.graph.Edge edges[] = new org.eclipse.draw2dl.graph.Edge[10];
		private int size;

		public void add(org.eclipse.draw2dl.graph.Edge edge) {
			if (size == edges.length - 1) {
				org.eclipse.draw2dl.graph.Edge newEdges[] = new org.eclipse.draw2dl.graph.Edge[edges.length * 2];
				System.arraycopy(edges, 0, newEdges, 0, edges.length);
				edges = newEdges;
			}
			edges[size++] = edge;
		}

		public org.eclipse.draw2dl.graph.Edge getEdge(int index) {
			return edges[index];
		}

		public void remove(org.eclipse.draw2dl.graph.Edge edge) {
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

	protected org.eclipse.draw2dl.graph.NodeList members = new NodeList();

	public void visit(DirectedGraph graph) {
		this.graph = graph;
		init();
		solve();
	}

	org.eclipse.draw2dl.graph.Node addEdge(org.eclipse.draw2dl.graph.Edge edge) {
		int delta = edge.getSlack();
		edge.tree = true;
		org.eclipse.draw2dl.graph.Node node;
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

	private boolean isNodeReachable(org.eclipse.draw2dl.graph.Node node) {
		return node.flag;
	}

	private void setNodeReachable(org.eclipse.draw2dl.graph.Node node) {
		node.flag = true;
	}

	private boolean isCandidate(org.eclipse.draw2dl.graph.Edge e) {
		return e.flag;
	}

	private void setCandidate(org.eclipse.draw2dl.graph.Edge e) {
		e.flag = true;
	}

	void addNode(org.eclipse.draw2dl.graph.Node node) {
		setNodeReachable(node);
		org.eclipse.draw2dl.graph.EdgeList list = node.incoming;
		org.eclipse.draw2dl.graph.Edge e;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (!isNodeReachable(e.source)) {
				if (!isCandidate(e)) {
					setCandidate(e);
					candidates.add(e);
				}
			} else
				candidates.remove(e);
		}

		list = node.outgoing;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (!isNodeReachable(e.target)) {
				if (!isCandidate(e)) {
					setCandidate(e);
					candidates.add(e);
				}
			} else
				candidates.remove(e);
		}
		members.add(node);
	}

	void init() {
		graph.edges.resetFlags(true);
		graph.nodes.resetFlags();
		for (int i = 0; i < graph.nodes.size(); i++) {
			org.eclipse.draw2dl.graph.Node node = (org.eclipse.draw2dl.graph.Node) graph.nodes.get(i);
			node.workingData[0] = new EdgeList();
		}
	}

	protected void solve() {
		Node root = graph.nodes.getNode(0);
		setParentEdge(root, null);
		addNode(root);
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
			addEdge(minEdge);
		}
		graph.nodes.normalizeRanks();
	}

}
