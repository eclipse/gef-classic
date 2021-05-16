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

/**
 * Places nodes into ranks for a compound directed graph. If a subgraph spans a
 * rank without any nodes which belong to that rank, a bridge node is inserted
 * to prevent nodes from violating the subgraph boundary.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class CompoundPopulateRanks extends org.eclipse.draw2dl.graph.PopulateRanks {

	public void visit(DirectedGraph g) {
		org.eclipse.draw2dl.graph.CompoundDirectedGraph graph = (org.eclipse.draw2dl.graph.CompoundDirectedGraph) g;

		/**
		 * Remove long containment edges at this point so they don't affect
		 * MinCross.
		 */
		Iterator containment = graph.containment.iterator();
		while (containment.hasNext()) {
			org.eclipse.draw2dl.graph.Edge e = (Edge) containment.next();
			if (e.getSlack() > 0) {
				graph.removeEdge(e);
				containment.remove();
			}
		}

		super.visit(g);
		NodeList subgraphs = graph.subgraphs;
		for (int i = 0; i < subgraphs.size(); i++) {
			org.eclipse.draw2dl.graph.Subgraph subgraph = (org.eclipse.draw2dl.graph.Subgraph) subgraphs.get(i);
			bridgeSubgraph(subgraph, graph);
		}
	}

	/**
	 * @param subgraph
	 */
	private void bridgeSubgraph(org.eclipse.draw2dl.graph.Subgraph subgraph, CompoundDirectedGraph g) {
		int offset = subgraph.head.rank;
		boolean occupied[] = new boolean[subgraph.tail.rank
				- subgraph.head.rank + 1];
		org.eclipse.draw2dl.graph.Node bridge[] = new org.eclipse.draw2dl.graph.Node[occupied.length];

		for (int i = 0; i < subgraph.members.size(); i++) {
			org.eclipse.draw2dl.graph.Node n = (org.eclipse.draw2dl.graph.Node) subgraph.members.get(i);
			if (n instanceof org.eclipse.draw2dl.graph.Subgraph) {
				org.eclipse.draw2dl.graph.Subgraph s = (Subgraph) n;
				for (int r = s.head.rank; r <= s.tail.rank; r++)
					occupied[r - offset] = true;
			} else
				occupied[n.rank - offset] = true;
		}

		for (int i = 0; i < bridge.length; i++) {
			if (!occupied[i]) {
				org.eclipse.draw2dl.graph.Node br = bridge[i] = new Node("bridge", subgraph); //$NON-NLS-1$
				br.rank = i + offset;
				br.height = br.width = 0;
				br.nestingIndex = subgraph.nestingIndex;
				g.ranks.getRank(br.rank).add(br);
				g.nodes.add(br);
			}
		}
	}

}
