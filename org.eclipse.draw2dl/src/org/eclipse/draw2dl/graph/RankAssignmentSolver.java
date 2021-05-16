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

import java.util.Iterator;
import java.util.Stack;

/**
 * Assigns the final rank assignment for a DirectedGraph with an initial
 * feasible spanning tree.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class RankAssignmentSolver extends org.eclipse.draw2dl.graph.SpanningTreeVisitor {

	org.eclipse.draw2dl.graph.DirectedGraph graph;
	org.eclipse.draw2dl.graph.EdgeList spanningTree;
	boolean searchDirection;

	int depthFirstCutValue(org.eclipse.draw2dl.graph.Edge edge, int count) {
		org.eclipse.draw2dl.graph.Node n = getTreeTail(edge);
		setTreeMin(n, count);
		int cutvalue = 0;
		int multiplier = (edge.target == n) ? 1 : -1;
		org.eclipse.draw2dl.graph.EdgeList list;

		list = n.outgoing;
		org.eclipse.draw2dl.graph.Edge e;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (e.tree && e != edge) {
				count = depthFirstCutValue(e, count);
				cutvalue += (e.cut - e.weight) * multiplier;
			} else {
				cutvalue -= e.weight * multiplier;
			}
		}
		list = n.incoming;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (e.tree && e != edge) {
				count = depthFirstCutValue(e, count);
				cutvalue -= (e.cut - e.weight) * multiplier;
			} else {
				cutvalue += e.weight * multiplier;
			}
		}

		edge.cut = cutvalue;
		if (cutvalue < 0)
			spanningTree.add(edge);
		setTreeMax(n, count);
		return count + 1;
	}

	/**
	 * returns the Edge which should be entered.
	 * 
	 * @param branch
	 * @return Edge
	 */
	org.eclipse.draw2dl.graph.Edge enter(org.eclipse.draw2dl.graph.Node branch) {
		org.eclipse.draw2dl.graph.Node n;
		org.eclipse.draw2dl.graph.Edge result = null;
		int minSlack = Integer.MAX_VALUE;
		boolean incoming = getParentEdge(branch).target != branch;
		// searchDirection = !searchDirection;
		for (int i = 0; i < graph.nodes.size(); i++) {
			if (searchDirection)
				n = graph.nodes.getNode(i);
			else
				n = graph.nodes.getNode(graph.nodes.size() - 1 - i);
			if (subtreeContains(branch, n)) {
				org.eclipse.draw2dl.graph.EdgeList edges;
				if (incoming)
					edges = n.incoming;
				else
					edges = n.outgoing;
				for (int j = 0; j < edges.size(); j++) {
					org.eclipse.draw2dl.graph.Edge e = edges.getEdge(j);
					if (!subtreeContains(branch, e.opposite(n)) && !e.tree
							&& e.getSlack() < minSlack) {
						result = e;
						minSlack = e.getSlack();
					}
				}
			}
		}
		return result;
	}

	int getTreeMax(org.eclipse.draw2dl.graph.Node n) {
		return n.workingInts[1];
	}

	int getTreeMin(org.eclipse.draw2dl.graph.Node n) {
		return n.workingInts[0];
	}

	void initCutValues() {
		org.eclipse.draw2dl.graph.Node root = graph.nodes.getNode(0);
		spanningTree = new org.eclipse.draw2dl.graph.EdgeList();
		org.eclipse.draw2dl.graph.Edge e;
		setTreeMin(root, 1);
		setTreeMax(root, 1);

		for (int i = 0; i < root.outgoing.size(); i++) {
			e = root.outgoing.getEdge(i);
			if (!getSpanningTreeChildren(root).contains(e))
				continue;
			setTreeMax(root, depthFirstCutValue(e, getTreeMax(root)));
		}
		for (int i = 0; i < root.incoming.size(); i++) {
			e = root.incoming.getEdge(i);
			if (!getSpanningTreeChildren(root).contains(e))
				continue;
			setTreeMax(root, depthFirstCutValue(e, getTreeMax(root)));
		}
	}

	org.eclipse.draw2dl.graph.Edge leave() {
		org.eclipse.draw2dl.graph.Edge result = null;
		org.eclipse.draw2dl.graph.Edge e;
		int minCut = 0;
		int weight = -1;
		for (int i = 0; i < spanningTree.size(); i++) {
			e = spanningTree.getEdge(i);
			if (e.cut < minCut) {
				result = e;
				minCut = result.cut;
				weight = result.weight;
			} else if (e.cut == minCut && e.weight > weight) {
				result = e;
				weight = result.weight;
			}
		}
		return result;
	}

	void networkSimplexLoop() {
		org.eclipse.draw2dl.graph.Edge leave, enter;
		int count = 0;
		while ((leave = leave()) != null && count < 900) {

			count++;

			org.eclipse.draw2dl.graph.Node leaveTail = getTreeTail(leave);
			org.eclipse.draw2dl.graph.Node leaveHead = getTreeHead(leave);

			enter = enter(leaveTail);
			if (enter == null)
				break;

			// Break the "leave" edge from the spanning tree
			getSpanningTreeChildren(leaveHead).remove(leave);
			setParentEdge(leaveTail, null);
			leave.tree = false;
			spanningTree.remove(leave);

			org.eclipse.draw2dl.graph.Node enterTail = enter.source;
			if (!subtreeContains(leaveTail, enterTail))
				// Oops, wrong end of the edge
				enterTail = enter.target;
			org.eclipse.draw2dl.graph.Node enterHead = enter.opposite(enterTail);

			// Prepare enterTail by making it the root of its sub-tree
			updateSubgraph(enterTail);

			// Add "enter" edge to the spanning tree
			getSpanningTreeChildren(enterHead).add(enter);
			setParentEdge(enterTail, enter);
			enter.tree = true;

			repairCutValues(enter);

			org.eclipse.draw2dl.graph.Node commonAncestor = enterHead;

			while (!subtreeContains(commonAncestor, leaveHead)) {
				repairCutValues(getParentEdge(commonAncestor));
				commonAncestor = getTreeParent(commonAncestor);
			}
			while (leaveHead != commonAncestor) {
				repairCutValues(getParentEdge(leaveHead));
				leaveHead = getTreeParent(leaveHead);
			}
			updateMinMax(commonAncestor, getTreeMin(commonAncestor));
			tightenEdge(enter);
		}
	}

	void repairCutValues(org.eclipse.draw2dl.graph.Edge edge) {
		spanningTree.remove(edge);
		org.eclipse.draw2dl.graph.Node n = getTreeTail(edge);
		int cutvalue = 0;
		int multiplier = (edge.target == n) ? 1 : -1;
		org.eclipse.draw2dl.graph.EdgeList list;

		list = n.outgoing;
		org.eclipse.draw2dl.graph.Edge e;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (e.tree && e != edge)
				cutvalue += (e.cut - e.weight) * multiplier;
			else
				cutvalue -= e.weight * multiplier;
		}
		list = n.incoming;
		for (int i = 0; i < list.size(); i++) {
			e = list.getEdge(i);
			if (e.tree && e != edge)
				cutvalue -= (e.cut - e.weight) * multiplier;
			else
				cutvalue += e.weight * multiplier;
		}

		edge.cut = cutvalue;
		if (cutvalue < 0)
			spanningTree.add(edge);
	}

	void setTreeMax(org.eclipse.draw2dl.graph.Node n, int value) {
		n.workingInts[1] = value;
	}

	void setTreeMin(org.eclipse.draw2dl.graph.Node n, int value) {
		n.workingInts[0] = value;
	}

	boolean subtreeContains(org.eclipse.draw2dl.graph.Node parent, org.eclipse.draw2dl.graph.Node child) {
		return parent.workingInts[0] <= child.workingInts[1]
				&& child.workingInts[1] <= parent.workingInts[1];
	}

	void tightenEdge(org.eclipse.draw2dl.graph.Edge edge) {
		org.eclipse.draw2dl.graph.Node tail = getTreeTail(edge);
		int delta = edge.getSlack();
		if (tail == edge.target)
			delta = -delta;
		org.eclipse.draw2dl.graph.Node n;
		for (int i = 0; i < graph.nodes.size(); i++) {
			n = graph.nodes.getNode(i);
			if (subtreeContains(tail, n))
				n.rank += delta;
		}
	}

	int updateMinMax(org.eclipse.draw2dl.graph.Node root, int count) {
		setTreeMin(root, count);
		org.eclipse.draw2dl.graph.EdgeList edges = getSpanningTreeChildren(root);
		for (int i = 0; i < edges.size(); i++)
			count = updateMinMax(getTreeTail(edges.getEdge(i)), count);
		setTreeMax(root, count);
		return count + 1;
	}

	void updateSubgraph(org.eclipse.draw2dl.graph.Node root) {
		Edge flip = getParentEdge(root);
		if (flip != null) {
			org.eclipse.draw2dl.graph.Node rootParent = getTreeParent(root);
			getSpanningTreeChildren(rootParent).remove(flip);
			updateSubgraph(rootParent);
			setParentEdge(root, null);
			setParentEdge(rootParent, flip);
			repairCutValues(flip);
			getSpanningTreeChildren(root).add(flip);
		}
	}

	public void visit(DirectedGraph graph) {
		this.graph = graph;
		initCutValues();
		networkSimplexLoop();
		if (graph.forestRoot == null)
			graph.nodes.normalizeRanks();
		else
			normalizeForest();
	}

	private void normalizeForest() {
		org.eclipse.draw2dl.graph.NodeList tree = new NodeList();
		graph.nodes.resetFlags();
		graph.forestRoot.flag = true;
		EdgeList rootEdges = graph.forestRoot.outgoing;
		Stack stack = new Stack();
		for (int i = 0; i < rootEdges.size(); i++) {
			org.eclipse.draw2dl.graph.Node node = rootEdges.getEdge(i).target;
			node.flag = true;
			stack.push(node);
			while (!stack.isEmpty()) {
				node = (org.eclipse.draw2dl.graph.Node) stack.pop();
				tree.add(node);
				Iterator neighbors = node.iteratorNeighbors();
				while (neighbors.hasNext()) {
					org.eclipse.draw2dl.graph.Node neighbor = (Node) neighbors.next();
					if (!neighbor.flag) {
						neighbor.flag = true;
						stack.push(neighbor);
					}
				}
			}
			tree.normalizeRanks();
			tree.clear();
		}
	}

}
