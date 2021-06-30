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

import java.util.Stack;

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
class PopulateRanks extends org.eclipse.draw2dl.graph.GraphVisitor {

	private Stack changes = new Stack();

	/**
	 * @see org.eclipse.draw2dl.graph.GraphVisitor#visit(org.eclipse.draw2dl.graph.DirectedGraph)
	 */
	public void visit(org.eclipse.draw2dl.graph.DirectedGraph g) {
		if (g.forestRoot != null) {
			for (int i = g.forestRoot.outgoing.size() - 1; i >= 0; i--)
				g.removeEdge(g.forestRoot.outgoing.getEdge(i));
			g.removeNode(g.forestRoot);
		}
		g.ranks = new RankList();
		for (int i = 0; i < g.nodes.size(); i++) {
			org.eclipse.draw2dl.graph.Node node = g.nodes.getNode(i);
			g.ranks.getRank(node.rank).add(node);
		}
		for (int i = 0; i < g.nodes.size(); i++) {
			org.eclipse.draw2dl.graph.Node node = g.nodes.getNode(i);
			for (int j = 0; j < node.outgoing.size();) {
				Edge e = node.outgoing.getEdge(j);
				if (e.getLength() > 1)
					changes.push(new org.eclipse.draw2dl.graph.VirtualNodeCreation(e, g));
				else
					j++;
			}
		}
	}

	/**
	 * @see org.eclipse.draw2dl.graph.GraphVisitor#revisit(org.eclipse.draw2dl.graph.DirectedGraph)
	 */
	public void revisit(DirectedGraph g) {
		for (int r = 0; r < g.ranks.size(); r++) {
			Rank rank = g.ranks.getRank(r);
			Node prev = null, cur;
			for (int n = 0; n < rank.size(); n++) {
				cur = rank.getNode(n);
				cur.left = prev;
				if (prev != null) {
					prev.right = cur;
				}
				prev = cur;
			}
		}
		for (int i = 0; i < changes.size(); i++) {
			org.eclipse.draw2dl.graph.RevertableChange change = (org.eclipse.draw2dl.graph.RevertableChange) changes.get(i);
			change.revert();
		}
	}

}
