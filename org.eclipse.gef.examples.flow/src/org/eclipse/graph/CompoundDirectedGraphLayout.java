package org.eclipse.graph;


/**
 * 
 * @author hudsonr
 * Created on Jul 20, 2003
 */
public class CompoundDirectedGraphLayout extends GraphVisitor {

public void visit(DirectedGraph graph) {
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
	new MinCross()
		.visit(graph);
	new SortSubgraphs()
		.visit(graph);
	new CompoundHorizontalPlacement()
		.visit(graph);
}

}
