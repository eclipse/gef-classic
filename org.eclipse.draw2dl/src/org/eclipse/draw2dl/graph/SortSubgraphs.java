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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Performs a topological sort from left to right of the subgraphs in a compound
 * directed graph. This ensures that subgraphs do not intertwine.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class SortSubgraphs extends org.eclipse.draw2dl.graph.GraphVisitor {

	org.eclipse.draw2dl.graph.CompoundDirectedGraph g;

	org.eclipse.draw2dl.graph.NestingTree nestingTrees[];

	Set orderingGraphEdges = new HashSet();
	Set orderingGraphNodes = new HashSet();
	org.eclipse.draw2dl.graph.NodePair pair = new org.eclipse.draw2dl.graph.NodePair();

	private void breakSubgraphCycles() {
		// The stack of nodes which have no unmarked incoming edges
		List noLefts = new ArrayList();

		int index = 1;
		// Identify all initial nodes for removal
		for (Iterator iter = orderingGraphNodes.iterator(); iter.hasNext();) {
			org.eclipse.draw2dl.graph.Node node = (org.eclipse.draw2dl.graph.Node) iter.next();
			if (node.x == 0)
				sortedInsert(noLefts, node);
		}

		org.eclipse.draw2dl.graph.Node cycleRoot;
		do {
			// Remove all leftmost nodes, updating the nodes to their right
			while (noLefts.size() > 0) {
				org.eclipse.draw2dl.graph.Node node = (org.eclipse.draw2dl.graph.Node) noLefts.remove(noLefts.size() - 1);
				node.sortValue = index++;
				orderingGraphNodes.remove(node);
				// System.out.println("removed:" + node);
				org.eclipse.draw2dl.graph.NodeList rightOf = rightOf(node);
				if (rightOf == null)
					continue;
				for (int i = 0; i < rightOf.size(); i++) {
					org.eclipse.draw2dl.graph.Node right = rightOf.getNode(i);
					right.x--;
					if (right.x == 0)
						sortedInsert(noLefts, right);
				}
			}
			cycleRoot = null;
			double min = Double.MAX_VALUE;
			for (Iterator iter = orderingGraphNodes.iterator(); iter.hasNext();) {
				org.eclipse.draw2dl.graph.Node node = (org.eclipse.draw2dl.graph.Node) iter.next();
				if (node.sortValue < min) {
					cycleRoot = node;
					min = node.sortValue;
				}
			}
			if (cycleRoot != null) {
				// break the cycle;
				sortedInsert(noLefts, cycleRoot);
				// System.out.println("breaking cycle with:" + cycleRoot);
				// Display.getCurrent().beep();
				cycleRoot.x = -1; // prevent x from ever reaching 0
			} // else if (OGmembers.size() > 0)
				//System.out.println("FAILED TO FIND CYCLE ROOT"); //$NON-NLS-1$
		} while (cycleRoot != null);
	}

	private void buildSubgraphOrderingGraph() {
		org.eclipse.draw2dl.graph.RankList ranks = g.ranks;
		nestingTrees = new org.eclipse.draw2dl.graph.NestingTree[ranks.size()];
		for (int r = 0; r < ranks.size(); r++) {
			org.eclipse.draw2dl.graph.NestingTree entry = org.eclipse.draw2dl.graph.NestingTree.buildNestingTreeForRank(ranks
					.getRank(r));
			nestingTrees[r] = entry;
			entry.calculateSortValues();
			entry.recursiveSort(false);
		}

		for (int i = 0; i < nestingTrees.length; i++) {
			org.eclipse.draw2dl.graph.NestingTree entry = nestingTrees[i];
			buildSubgraphOrderingGraph(entry);
		}
	}

	private void buildSubgraphOrderingGraph(org.eclipse.draw2dl.graph.NestingTree entry) {
		org.eclipse.draw2dl.graph.NodePair pair = new org.eclipse.draw2dl.graph.NodePair();
		if (entry.isLeaf)
			return;
		for (int i = 0; i < entry.contents.size(); i++) {
			Object right = entry.contents.get(i);
			if (right instanceof org.eclipse.draw2dl.graph.Node)
				pair.n2 = (org.eclipse.draw2dl.graph.Node) right;
			else {
				pair.n2 = ((org.eclipse.draw2dl.graph.NestingTree) right).subgraph;
				buildSubgraphOrderingGraph((org.eclipse.draw2dl.graph.NestingTree) right);
			}
			if (pair.n1 != null && !orderingGraphEdges.contains(pair)) {
				orderingGraphEdges.add(pair);
				leftToRight(pair.n1, pair.n2);
				orderingGraphNodes.add(pair.n1);
				orderingGraphNodes.add(pair.n2);
				pair.n2.x++; // Using x field to count predecessors.
				pair = new org.eclipse.draw2dl.graph.NodePair(pair.n2, null);
			} else {
				pair.n1 = pair.n2;
			}
		}
	}

	/**
	 * Calculates the average position P for each node and subgraph. The average
	 * position is stored in the sortValue for each node or subgraph.
	 * 
	 * Runs in approximately linear time with respect to the number of nodes,
	 * including virtual nodes.
	 */
	private void calculateSortValues() {
		RankList ranks = g.ranks;

		g.subgraphs.resetSortValues();
		g.subgraphs.resetIndices();

		/*
		 * For subgraphs, the sum of all positions is kept, along with the
		 * number of contributions, which is tracked in the subgraph's index
		 * field.
		 */
		for (int r = 0; r < ranks.size(); r++) {
			org.eclipse.draw2dl.graph.Rank rank = ranks.getRank(r);
			for (int j = 0; j < rank.count(); j++) {
				org.eclipse.draw2dl.graph.Node node = rank.getNode(j);
				node.sortValue = node.index;
				org.eclipse.draw2dl.graph.Subgraph parent = node.getParent();
				while (parent != null) {
					parent.sortValue += node.sortValue;
					parent.index++;
					parent = parent.getParent();
				}
			}
		}

		/*
		 * For each subgraph, divide the sum of the positions by the number of
		 * contributions, to give the average position.
		 */
		for (int i = 0; i < g.subgraphs.size(); i++) {
			org.eclipse.draw2dl.graph.Subgraph subgraph = (org.eclipse.draw2dl.graph.Subgraph) g.subgraphs.get(i);
			subgraph.sortValue /= subgraph.index;
		}
	}

	private void repopulateRanks() {
		for (int i = 0; i < nestingTrees.length; i++) {
			org.eclipse.draw2dl.graph.Rank rank = g.ranks.getRank(i);
			rank.clear();
			nestingTrees[i].repopulateRank(rank);
		}
	}

	private org.eclipse.draw2dl.graph.NodeList rightOf(org.eclipse.draw2dl.graph.Node left) {
		return (org.eclipse.draw2dl.graph.NodeList) left.workingData[0];
	}

	private void leftToRight(org.eclipse.draw2dl.graph.Node left, org.eclipse.draw2dl.graph.Node right) {
		rightOf(left).add(right);
	}

	void sortedInsert(List list, org.eclipse.draw2dl.graph.Node node) {
		int insert = 0;
		while (insert < list.size()
				&& ((org.eclipse.draw2dl.graph.Node) list.get(insert)).sortValue > node.sortValue)
			insert++;
		list.add(insert, node);
	}

	private void topologicalSort() {
		for (int i = 0; i < nestingTrees.length; i++) {
			nestingTrees[i].getSortValueFromSubgraph();
			nestingTrees[i].recursiveSort(false);
		}
	}

	void init() {
		for (int r = 0; r < g.ranks.size(); r++) {
			Rank rank = g.ranks.getRank(r);
			for (int i = 0; i < rank.count(); i++) {
				org.eclipse.draw2dl.graph.Node n = (Node) rank.get(i);
				n.workingData[0] = new org.eclipse.draw2dl.graph.NodeList();
			}
		}
		for (int i = 0; i < g.subgraphs.size(); i++) {
			org.eclipse.draw2dl.graph.Subgraph s = (Subgraph) g.subgraphs.get(i);
			s.workingData[0] = new NodeList();
		}
	}

	public void visit(DirectedGraph dg) {
		g = (CompoundDirectedGraph) dg;

		init();
		buildSubgraphOrderingGraph();
		calculateSortValues();
		breakSubgraphCycles();
		topologicalSort();
		repopulateRanks();
	}

}
