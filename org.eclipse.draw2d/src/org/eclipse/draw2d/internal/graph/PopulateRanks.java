/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal.graph;

import java.util.Stack;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.RankList;

/**
 * This class takes a DirectedGraph with an optimal rank assignment and a spanning tree,
 * and populates the ranks of the DirectedGraph. Virtual nodes are inserted for edges that
 * span 1 or more ranks.
 * <P>
 * Ranks are populated using a pre-order depth-first traversal of the spanning tree. For
 * each node, all edges requiring virtual nodes are added to the ranks.
 * @author Randy Hudson
 * @since 2.1.2
 */
public class PopulateRanks extends GraphVisitor {

private Stack changes = new Stack();

/**
 * @see GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	g.ranks = new RankList();
	for (int i = 0; i < g.nodes.size(); i++) {
		Node node = g.nodes.getNode(i);
		g.ranks.getRank(node.rank).add(node);
	}
	for (int i = 0; i < g.nodes.size(); i++) {
		Node node = g.nodes.getNode(i);
		for (int j = 0; j < node.outgoing.size();) {
			Edge e = node.outgoing.getEdge(j);
			if (e.getLength() > 1)
				changes.push(new VirtualNodeCreation(e, g));
			else
				j++;
		}
	}
}

/**
 * @see GraphVisitor#revisit(DirectedGraph)
 */
public void revisit(DirectedGraph g) {
	for (int i = 0; i < changes.size(); i++) {
		RevertableChange change = (RevertableChange)changes.get(i);
		change.revert();
	}
}

}
