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

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;

/**
 * @author hudsonr
 * @since 2.1
 */
public class InitialRankSolver extends GraphVisitor {

protected DirectedGraph graph;
protected EdgeList candidates = new EdgeList();
protected NodeList members = new NodeList();

public void visit(DirectedGraph graph) {
	this.graph = graph;
	graph.edges.resetFlags();
	graph.nodes.resetFlags();
	solve();
}

protected void solve() {

	NodeList unranked = new NodeList(graph.nodes);
	NodeList rankMe = new NodeList();
	Node node;
	int i;
	do {
		rankMe.clear();
		for (i = 0; i < unranked.size();) {
			node = unranked.getNode(i);
			if (node.incoming.isCompletelyFlagged()) {
				rankMe.add(node);
				unranked.remove(i);
			} else
				i++;
		}
		for (i = 0; i < rankMe.size(); i++) {
			node = rankMe.getNode(i);
			node.rank = node.incoming.calculateRank();
			node.outgoing.setFlags(true);
		}
	} while (!unranked.isEmpty());

}

}
