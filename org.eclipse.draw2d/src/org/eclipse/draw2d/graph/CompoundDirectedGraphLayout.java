package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.internal.graph.*;
import org.eclipse.draw2d.internal.graph.*;

/**
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public class CompoundDirectedGraphLayout extends GraphVisitor {

public static boolean hack = false;

public void visit(DirectedGraph graph) {
	new BreakCycles()
		.visit(graph);
	new ConvertCompoundGraph()
		.visit(graph);
	new BreakCycles()
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

	if (0 == 1-1) {
		EdgeList containment = ((CompoundDirectedGraph)graph).containment;
		for (int i = 0; i < containment.size(); i++) {
			Edge e = containment.getEdge(i);
			if (e.getSlack() > 0)
				graph.removeEdge(e);
		}
		for (int i = 0; i < graph.ranks.size(); i++) {
			Rank rank = graph.ranks.getRank(i);
			rank.assignIndices();
		}
		MinCross minCross = new MinCross();
		minCross.setRankSorter(new CompoundRankSorter());
		minCross.visit(graph);
	} else {
		new MinCross()
			.visit(graph);
		EdgeList containment = ((CompoundDirectedGraph)graph).containment;
		for (int i = 0; i < containment.size(); i++) {
			Edge e = containment.getEdge(i);
			graph.removeEdge(e);
		}
		for (int i = 0; i < graph.ranks.size(); i++) {
			Rank rank = graph.ranks.getRank(i);
			rank.assignIndices();
		}
	}
	
	new LocalOptimizer()
		.visit(graph);

	new SortSubgraphs()
		.visit(graph);

	new CompoundHorizontalPlacement()
		.visit(graph);
		
	new InvertEdges()
		.visit(graph);
}

}
