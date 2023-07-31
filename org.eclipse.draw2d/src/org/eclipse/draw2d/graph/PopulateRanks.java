/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class takes a DirectedGraph with an optimal rank assignment and a
 * spanning tree, and populates the ranks of the DirectedGraph. Virtual nodes
 * are inserted for edges that span 1 or more ranks.
 * <P>
 * Ranks are populated using a pre-order depth-first traversal of the spanning
 * tree. For each node, all edges requiring virtual nodes are added to the
 * ranks.
 *
 * @author Randy Hudson
 * @since 2.1.2
 */
class PopulateRanks extends GraphVisitor {

	private final Deque<RevertableChange> changes = new ArrayDeque<>();

	/**
	 * @see GraphVisitor#visit(DirectedGraph)
	 */
	@Override
	public void visit(DirectedGraph g) {
		if (g.forestRoot != null) {
			for (int i = g.forestRoot.outgoing.size() - 1; i >= 0; i--) {
				g.removeEdge(g.forestRoot.outgoing.getEdge(i));
			}
			g.removeNode(g.forestRoot);
		}
		g.ranks = new RankList();
		for (Node node : g.nodes) {
			g.ranks.getRank(node.rank).add(node);
		}
		for (Node node : g.nodes) {
			for (int j = 0; j < node.outgoing.size();) {
				Edge e = node.outgoing.getEdge(j);
				if (e.getLength() > 1) {
					changes.push(new VirtualNodeCreation(e, g));
				} else {
					j++;
				}
			}
		}
	}

	/**
	 * @see GraphVisitor#revisit(DirectedGraph)
	 */
	@Override
	public void revisit(DirectedGraph g) {
		for (int r = 0; r < g.ranks.size(); r++) {
			Rank rank = g.ranks.getRank(r);
			Node prev = null;
			for (Node cur : rank) {
				cur.left = prev;
				if (prev != null) {
					prev.right = cur;
				}
				prev = cur;
			}
		}
		changes.forEach(RevertableChange::revert);
	}

}
