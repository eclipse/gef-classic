package org.eclipse.draw2d.graph;

import java.util.Iterator;

import org.eclipse.draw2d.internal.graph.*;

/**
 * Performs a graph layout on a <code>CompoundDirectedGraph</code>.  The input format is
 * the same as for {@link DirectedGraphLayout}.  All nodes, including subgraphs and their
 * children, should be added to the {@link DirectedGraph#nodes} field.
 * <P>
 * The requirements for this algorithm are the same as those of
 * <code>DirectedGraphLayout</code>, with the following exceptions:
 * <UL>
 *   <LI>There is an implied edge between a subgraph and each of its member nodes. These
 *   edges form the containment graph <EM>T</EM>. Thus, the compound directed graph
 *   <EM>CG</EM> is said to be connected iff Union(<EM>G</EM>, <EM>T</EM>) is connected,
 *   where G represents the given nodes (including subgraphs) and edges.
 *   <LI>Using the same definitions as above, this layout will remove any cycles found in
 *   the input graph <em>G</em>.  However, it will not detect or remove compound cycles.
 *   If the input graph contains compound cycles, the results are undefined, and may
 *   include infinite loops.  A compound cycle is defined as: a cycle comprised of edges
 *   from <EM>G</EM>, <EM>T</EM>, and <em>T<sup>-1</sup></em>, in the form
 *   (c<sup>*</sup>e<sup>+</sup>p<sup>*</sup>e<sup>+</sup>)*, where
 *   <em>T<sup>-1</sup></em> is the backwards graph of <EM>T</EM>, c element of T, e
 *   element of G, and p element of T<sup>-1</sup>.
 * </UL>
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public final class CompoundDirectedGraphLayout extends GraphVisitor {

/**
 * @see org.eclipse.draw2d.internal.graph.GraphVisitor#visit(org.eclipse.draw2d.graph.DirectedGraph)
 */
public void visit(DirectedGraph g) {
	CompoundDirectedGraph graph = (CompoundDirectedGraph)g;
	new CompoundBreakCycles()
		.visit(graph);
	new ConvertCompoundGraph()
		.visit(graph);
	new InitialRankSolver()
		.visit(graph);
	new TightSpanningTreeSolver()
		.visit(graph);
	new RankAssigmentSolver()
		.visit(graph);
	new CompoundPopulateRanks()
		.visit(graph);
	new CompoundVerticalPlacement()
		.visit(graph);

	Iterator containment = graph.containment.iterator();
	while (containment.hasNext()) {
		Edge e = (Edge)containment.next();
		if (e.getSlack() > 0) {
			graph.removeEdge(e);
			containment.remove();
		}
	}
	for (int i = 0; i < graph.ranks.size(); i++) {
		Rank rank = graph.ranks.getRank(i);
		rank.assignIndices();
	}

	MinCross minCross = new MinCross();
	minCross.setRankSorter(new CompoundRankSorter());
	minCross.visit(graph);
	
	new LocalOptimizer()
		.visit(graph);

	new SortSubgraphs()
		.visit(graph);

	containment = graph.containment.iterator();

	while (containment.hasNext())
		graph.removeEdge((Edge)containment.next());
	graph.containment.clear();
	
	new CompoundHorizontalPlacement()
		.visit(graph);
		
	new PlaceEndpoints()
		.visit(graph);
	new InvertEdges()
		.visit(graph);
}

}
